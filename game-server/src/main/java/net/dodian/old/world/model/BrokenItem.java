package net.dodian.old.world.model;

import net.dodian.GameConstants;
import net.dodian.Server;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.dialogue.DialogueManager;

import java.util.HashMap;
import java.util.Map;

public enum BrokenItem {
	
	
	DRAGON_DEFENDER_BROKEN(12954, 20463),
	FIRE_CAPE_BROKEN(6570, 20445),
	FIGHTER_TORSO_BROKEN(10551, 20513),
	
	VOID_KNIGHT_TOP(8839, 20465),
	VOID_KNIGHT_ROBE(8840, 20469),
	VOID_KNIGHT_GLOVES(8842, 20475),
	VOID_KNIGHT_MAGE_HELM(11663, 20477),
	VOID_KNIGHT_RANGER_HELM(11664, 20479),
	VOID_KNIGHT_MELEE_HELM(11665, 20481),
	;

	BrokenItem(int originalItem, int brokenItem) {
		this.originalItem = originalItem;
		this.brokenItem = brokenItem;
	}

	public int getOriginalItem() {
		return originalItem;
	}

	public int getBrokenItem() {
		return brokenItem;
	}

	private final int originalItem;
	private final int brokenItem;

	//Original item value * this multiplier is the repair cost of all items.
	//Currently 3%
	private static final double REPAIR_COST_MULTIPLIER = 0.03;
	
	/**
	 * Gets the total cost of repairing a player's stuff.
	 * @param player
	 * @return
	 */
	public static int getRepairCost(Player player) {
		int cost = 0;
		for(BrokenItem b : BrokenItem.values()) {
			final int amt = player.getInventory().getAmount(b.getBrokenItem());
			if(amt > 0) {
				cost += ((int)(Server.getDefinitionsHandler().getItemDefinitionById(b.getOriginalItem()).getValue() * REPAIR_COST_MULTIPLIER) * amt);
			}
		}
		return cost;
	}

	/**
	 * Repairs all broken stuff for a player.
	 * @param player
	 */
	public static void repair(Player player) {
		boolean fixed = false;
		
		for(BrokenItem b : BrokenItem.values()) {
			final int amt = player.getInventory().getAmount(b.getBrokenItem());
			if(amt > 0) {
				final int cost = ((int)(Server.getDefinitionsHandler().getItemDefinitionById(b.getOriginalItem()).getValue() * REPAIR_COST_MULTIPLIER) * amt);
				if(player.getInventory().getAmount(GameConstants.BLOOD_MONEY) >= cost) {
					player.getInventory().delete(GameConstants.BLOOD_MONEY, cost);
					player.getInventory().delete(b.getBrokenItem(), amt);
					player.getInventory().add(b.getOriginalItem(), amt);
					fixed = true;
				} else {
					player.getPacketSender().sendMessage("You could not afford fixing all your items.");
					break;
				}
			}
		}
		
		if(fixed) {
			DialogueManager.start(player, 21);
		} else {
			player.getPacketSender().sendInterfaceRemoval();
		}
	}

	private static Map<Integer, BrokenItem> brokenItems = new HashMap<Integer, BrokenItem>();

	public static BrokenItem get(int originalId) {
		return brokenItems.get(originalId);
	}

	static {
		for(BrokenItem brokenItem : BrokenItem.values()) {
			brokenItems.put(brokenItem.getOriginalItem(), brokenItem);
		}
	}
}
