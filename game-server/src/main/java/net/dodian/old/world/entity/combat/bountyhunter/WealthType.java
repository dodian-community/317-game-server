package net.dodian.old.world.entity.combat.bountyhunter;

import net.dodian.old.util.Misc;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Item;

public enum WealthType {
	NO_TARGET("N/A", 876),
	VERY_LOW("V. Low", 877),
	LOW("Low", 878),
	MEDIUM("Medium", 879),
	HIGH("High", 880),
	VERY_HIGH("V. HIGH", 881);

	;

	WealthType(String tooltip, int configId) {
		this.tooltip = tooltip;
		this.configId = configId;
	}

	public String tooltip;		
	public int configId;

	/**
	 * Gets a player's wealth type depending on the value of
	 * their items.
	 * @return
	 */
	public static WealthType getWealth(Player player) {
		int wealth = 0;

		for(Item item : Misc.concat(player.getInventory().getItems(), player.getEquipment().getItems())) {
			if(item == null || item.getId() <= 0 || item.getAmount() <= 0 || !item.getDefinition().isDropable() || !item.getDefinition().isTradeable()) {
				continue;
			}
			wealth += item.getDefinition().getValue();
		}
		WealthType type = WealthType.VERY_LOW;
		if(wealth >= Emblem.MYSTERIOUS_EMBLEM_1.value) {
			type = WealthType.LOW;
		} 
		if(wealth >= Emblem.MYSTERIOUS_EMBLEM_3.value) {
			type = WealthType.MEDIUM;
		}
		if(wealth >= Emblem.MYSTERIOUS_EMBLEM_6.value) {
			type = WealthType.HIGH;
		}
		if(wealth >= Emblem.MYSTERIOUS_EMBLEM_9.value) {
			type = WealthType.VERY_HIGH;
		}
		return type;
	}
}
