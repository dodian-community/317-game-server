package net.dodian.old.engine.task.impl;

import net.dodian.GameConstants;
import net.dodian.Server;
import net.dodian.managers.DefinitionsManager;
import net.dodian.old.engine.task.Task;
import net.dodian.old.world.content.ItemsKeptOnDeath;
import net.dodian.old.world.content.PrayerHandler;
import net.dodian.old.world.content.Presetables;
import net.dodian.old.world.entity.combat.CombatFactory;
import net.dodian.old.world.entity.combat.bountyhunter.BountyHunter;
import net.dodian.old.world.entity.combat.bountyhunter.Emblem;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.grounditems.GroundItemManager;
import net.dodian.old.world.model.*;
import net.dodian.old.world.model.movement.MovementStatus;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player's death task, through which the process of dying is handled,
 * the animation, dropping items, etc.
 * 
 * @author relex lawl, redone by Gabbe.
 */

public class PlayerDeathTask extends Task implements BeanFactoryAware {

	private DefinitionsManager definitionsManager;

	/**
	 * The PlayerDeathTask constructor.
	 * @param player	The player setting off the task.
	 */
	public PlayerDeathTask(Player player) {
		super(1, player, false);
		this.player = player;
		this.killer = player.getCombat().getKiller(true);
		this.oldPosition = player.getPosition().copy();
		this.loc = player.getLocation();
		this.dropItems = player.getLocation() == Locations.Location.WILDERNESS;
	}

	private final Player player, killer;
	private final Position oldPosition;
	private final Locations.Location loc;
	private boolean dropItems;
	private ArrayList<Item> itemsToKeep = null;

	private int ticks = 5;

	@Override
	public void execute() {
		if(player == null) {
			stop();
			return;
		}
		try {
			switch (ticks) {
			case 5:
				player.getCombat().reset();
				player.setUntargetable(true);
				player.getPacketSender().sendInterfaceRemoval();
				player.getMovementQueue().setMovementStatus(MovementStatus.DISABLED).reset();
				break;
			case 3:
				player.performAnimation(new Animation(836, Priority.HIGH));

				if(PrayerHandler.isActivated(player, PrayerHandler.RETRIBUTION)) {
					if(killer != null) {
						CombatFactory.handleRetribution(player, killer);
					}
				}

				player.getPacketSender().sendMessage("Oh dear, you are dead!");
				break;
			case 1:
				if(dropItems) {
					//Get items to keep
					itemsToKeep = ItemsKeptOnDeath.getItemsToKeep(player);

					//Fetch player's items
					final List<Item> playerItems = new ArrayList<Item>();
					playerItems.addAll(player.getInventory().getValidItems());
					playerItems.addAll(player.getEquipment().getValidItems());

					//The position the items will be dropped at
					final Position position = player.getPosition();

					//Go through player items, drop them to killer
					boolean dropped = false;

					for (Item item : playerItems) {

						//Keep tradeable items
						if(!item.getDefinition().isTradeable() || itemsToKeep.contains(item)) {
							if(!itemsToKeep.contains(item)) {
								itemsToKeep.add(item);
							}
							continue;
						}

						//Dont drop spawnable items
						if(item.getDefinition().getValue() == 0) {
							continue;
						}

						//Don't drop items if we're owner or dev
						if(player.getRights().equals(PlayerRights.OWNER) || player.getRights().equals(PlayerRights.DEVELOPER)) {
							break;
						}

						//Drop emblems
						if(item.getId() == Emblem.MYSTERIOUS_EMBLEM_1.id
								|| item.getId() == Emblem.MYSTERIOUS_EMBLEM_2.id ||
								item.getId() == Emblem.MYSTERIOUS_EMBLEM_3.id ||
								item.getId() == Emblem.MYSTERIOUS_EMBLEM_4.id ||
								item.getId() == Emblem.MYSTERIOUS_EMBLEM_5.id ||
								item.getId() == Emblem.MYSTERIOUS_EMBLEM_6.id ||
								item.getId() == Emblem.MYSTERIOUS_EMBLEM_7.id ||
								item.getId() == Emblem.MYSTERIOUS_EMBLEM_8.id ||
								item.getId() == Emblem.MYSTERIOUS_EMBLEM_9.id ||
								item.getId() == Emblem.MYSTERIOUS_EMBLEM_10.id) {

							//Tier 1 shouldnt be dropped cause it cant be downgraded
							if(item.getId() == Emblem.MYSTERIOUS_EMBLEM_1.id) {
								continue;
							}

							if(killer != null) {
								final int lowerEmblem = item.getId() == Emblem.MYSTERIOUS_EMBLEM_2.id ? item.getId() - 2 : item.getId() - 1;
								GroundItemManager.spawnGroundItem((killer != null ? killer : player), new GroundItem(new Item(lowerEmblem), position, killer != null ? killer.getUsername() : player.getUsername(), player.getHostAddress(), false, 150, true, 150));
								killer.getPacketSender().sendMessage("@red@"+player.getUsername()+" dropped a "+ definitionsManager.getItemDefinitionById(lowerEmblem).getName()+"!");
								dropped = true;
							}

							continue;
						}

						//Drop item
						GroundItemManager.spawnGroundItem((killer != null ? killer : player), new GroundItem(item, position, killer != null ? killer.getUsername() : player.getUsername(), player.getHostAddress(), false, 150, true, 150));
						dropped = true;
					}


					//Give killer rewards
					if(killer != null) {
						if(!dropped) {
							killer.getPacketSender().sendMessage(""+player.getUsername()+" had no valuable items to be dropped.");
						}
						BountyHunter.onDeath(killer, player);
					}

					//Reset items
					player.getInventory().resetItems().refreshItems();
					player.getEquipment().resetItems().refreshItems();
				}
				break;
			case 0:

				if(dropItems) {
					if(itemsToKeep != null) {
						for(Item it : itemsToKeep) {
							int id = it.getId();

							BrokenItem brokenItem = BrokenItem.get(id);
							if(brokenItem != null) {
								id = brokenItem.getBrokenItem();
								player.getPacketSender().sendMessage("Your "+ Server.getDefinitionsHandler().getItemDefinitionById(it.getId()).getName()+" has been broken. You can fix it by talking to Perdu.");
							}

							player.getInventory().add(new Item(id, it.getAmount()));
						}
						itemsToKeep.clear();
					}
				} else {
					if(player.getDueling().inDuel()) {
						player.getDueling().duelLost();
					}
				}

				player.restart(true);

				loc.onDeath(player);

				player.moveTo(GameConstants.DEFAULT_POSITION.copy());

				if(player.isOpenPresetsOnDeath()) {
					Presetables.open(player);
				}

				player.setUntargetable(false);
				stop();
				break;
			}
			ticks--;
		} catch(Exception e) {
			setEventRunning(false);
			e.printStackTrace();
			if(player != null) {
				player.moveTo(GameConstants.DEFAULT_POSITION.copy());
				player.setHitpoints(player.getSkillManager().getMaxLevel(Skill.HITPOINTS));
			}
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.definitionsManager = beanFactory.getBean(DefinitionsManager.class);
	}
}
