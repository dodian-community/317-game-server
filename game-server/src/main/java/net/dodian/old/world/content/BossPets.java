package net.dodian.old.world.content;

import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.grounditems.GroundItemManager;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.BossPet;
import net.dodian.old.world.model.GroundItem;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.container.impl.Bank;

public class BossPets {

	private static final Animation ANIM = new Animation(827);

	public static boolean spawnFor(Player player, int id, boolean bossDrop) {
		BossPet pet = BossPet.getForId(id);
		if(pet != null) {
			
			if(player.getCurrentPet() == null) {

				//Spawn the pet..
				NPC npc = new NPC(pet.getPetId(), player.getPosition().copy().add(0, 1));
				npc.setOwner(player);
				npc.getMovementQueue().setFollowCharacter(player);
				npc.setEntityInteraction(player);
				World.getNpcAddQueue().add(npc);

				//Set the player's current pet to this one.
				player.setCurrentPet(npc);

				if(bossDrop) {
					player.getPacketSender().sendMessage("@red@You get a weird feeling, as if someone is following you.");
				} else {
					player.getInventory().delete(pet.getItemId(), 1);
					player.getPacketSender().sendMessage("You drop your pet..");
					player.performAnimation(ANIM);
					player.setPositionToFace(npc.getPosition());
				}

			} else {
				if(bossDrop) {

					if(!player.getInventory().isFull()) {
						player.getInventory().add(pet.getItemId(), 1);
					} else {
						GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(pet.getItemId(), 1), player.getPosition(), player.getUsername(), player.getHostAddress(), false, 150, false, -1));
					}

					player.getPacketSender().sendMessage("@red@You've received a pet drop!");
				} else {
					player.getPacketSender().sendMessage("You already have a pet following you.");
				}
			}

			return true;
		}
		return false;
	}

	public static boolean pickup(Player player, NPC npc) {
		if(npc == null || player.getCurrentPet() == null) {
			return false;
		}
		if(player.getCurrentPet().equals(npc)) {
			
			//Remove the npc from the world
			World.getNpcRemoveQueue().add(player.getCurrentPet());
			
			//Fetch pet definitions
			final BossPet pet = BossPet.getForId(player.getCurrentPet().getId());
			
			//Add pet to inventory or bank
			if(!player.getInventory().isFull()) {
				player.getInventory().add(pet.getItemId(), 1);
			} else {
				player.getBank(Bank.getTabForItem(player, pet.getItemId())).add(pet.getItemId(), 1);
			}
			
			//Send message
			player.getPacketSender().sendMessage("You pick up your pet..");
			
			//Reset pet
			player.setCurrentPet(null);
			
			return true;
		}
		return false;
	}
}
