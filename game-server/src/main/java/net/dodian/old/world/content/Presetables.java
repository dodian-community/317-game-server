package net.dodian.old.world.content;

import net.dodian.old.util.Misc;
import net.dodian.old.world.entity.combat.CombatFactory;
import net.dodian.old.world.entity.combat.bountyhunter.BountyHunter;
import net.dodian.old.world.entity.combat.magic.Autocasting;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.Locations;
import net.dodian.old.world.model.Presetable;
import net.dodian.old.world.model.Skill;
import net.dodian.old.world.model.container.impl.Bank;
import net.dodian.old.world.model.dialogue.DialogueManager;
import net.dodian.old.world.model.dialogue.DialogueOptions;
import net.dodian.old.world.model.syntax.impl.ChangePresetName;
import net.dodian.old.world.model.syntax.impl.CreatePreset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A class for handling {@code Presetable}
 * sets. 
 * 
 * Holy, this became messy quickly.
 * Sorry about that.
 * 
 * @author Professor Oak
 */
public class Presetables {

	/**
	 * The max amount of premade/custom presets.
	 */
	public static final int MAX_PRESETS = 10;

	/**
	 * The presets interface id.
	 */
	private static final int INTERFACE_ID = 45000;

	/**
	 * Pre-made sets by the server which everyone can use.
	 */
	public static final Presetable[] GLOBAL_PRESETS = new Presetable[MAX_PRESETS];

	/**
	 * Opens the presets interface
	 * for a player.
	 * 
	 * @param player
	 */
	public static void open(Player player) {
		open(player, player.getCurrentPreset());
	}

	/**
	 * Opens the specified preset for a player.
	 * @param player
	 * @param preset
	 */
	public static void open(Player player, Presetable preset) {
		if(preset != null) {

			//Send name
			player.getPacketSender().sendString(45002, "Presets - " + preset.getName());

			//Send stats
			player.getPacketSender().sendString(45007, "@yel@" + preset.getStats()[3]); //Hitpoints
			player.getPacketSender().sendString(45008, "@yel@" + preset.getStats()[0]); //Attack
			player.getPacketSender().sendString(45009, "@yel@" + preset.getStats()[2]); //Strength
			player.getPacketSender().sendString(45010, "@yel@" + preset.getStats()[1]); //Defence
			player.getPacketSender().sendString(45011, "@yel@" + preset.getStats()[4]); //Ranged
			player.getPacketSender().sendString(45012, "@yel@" + preset.getStats()[5]); //Prayer
			player.getPacketSender().sendString(45013, "@yel@" + preset.getStats()[6]); //Magic

			//Send spellbook
			player.getPacketSender().sendString(45014, "@yel@" + Misc.formatText(preset.getSpellbook().name().toLowerCase()));
		} else {

			//Reset name
			player.getPacketSender().sendString(45002, "Presets");

			//Reset stats
			for(int i = 0; i <= 6; i++) {
				player.getPacketSender().sendString(45007 + i, "");
			}

			//Reset spellbook
			player.getPacketSender().sendString(45014, "");
		}

		//Send inventory		
		for(int i = 0; i < 28; i++) {

			//Get item..
			Item item = null;
			if(preset != null) {
				if(i < preset.getInventory().length) {
					item = preset.getInventory()[i];
				}
			}

			//If it isn't null, send it. Otherwise, send empty slot.
			if(item != null) {
				player.getPacketSender().sendItemOnInterface(45015 + i, item.getId(), item.getAmount());
			} else {
				player.getPacketSender().sendItemOnInterface(45015 + i, -1, 1);
			}
		}

		//Send equipment
		for(int i = 0; i < 14; i++) {
			player.getPacketSender().sendItemOnInterface(45044 + i, -1, 1);
		}
		if(preset != null) {
			Arrays.stream(preset.getEquipment()).filter(t -> !Objects.isNull(t) && t.isValid()).forEach(t -> player.getPacketSender().sendItemOnInterface(45044 + t.getDefinition().getEquipmentType().getSlot(), t.getId(), t.getAmount()));
		}

		//Send all available global presets
		for(int i = 0; i < MAX_PRESETS; i++) {
			player.getPacketSender().sendString(45070 + i, GLOBAL_PRESETS[i] == null ? "Empty" : GLOBAL_PRESETS[i].getName());
		}

		//Send all available player presets
		for(int i = 0; i < MAX_PRESETS; i++) {
			player.getPacketSender().sendString(45082 + i, player.getPresets()[i] == null ? "Empty" : player.getPresets()[i].getName());
		}

		//Send on death toggle
		player.getPacketSender().sendConfig(987, player.isOpenPresetsOnDeath() ? 0 : 1);

		//Send interface
		player.getPacketSender().sendInterface(INTERFACE_ID);

		//Update current preset
		player.setCurrentPreset(preset);
	}

	/**
	 * Edits a preset.
	 * @param player	The player.
	 * @param index		The preset(to edit)'s index
	 */
	private static void edit(Player player, final int index) {
		//Check if we can edit..
		if(player.getLocation() == Locations.Location.WILDERNESS) {
			player.getPacketSender().sendMessage("You can't edit a preset in the wilderness!");
			return;
		}
		if(player.getDueling().inDuel()) {
			player.getPacketSender().sendMessage("You can't edit a preset during a duel!");
			return;
		}
		if(CombatFactory.inCombat(player)) {
			player.getPacketSender().sendMessage("You cannot do that right now.");
			return;
		}
		if(player.getPresets()[index] == null) {
			player.getPacketSender().sendMessage("This preset cannot be edited.");
			return;
		}

		DialogueManager.start(player, 11);
		player.setDialogueOptions(new DialogueOptions() {
			@Override
			public void handleOption(Player player, int option) {
				switch(option) {
				case 1: //Change name

					player.setEnterSyntax(new ChangePresetName(index));
					player.getPacketSender().sendEnterInputPrompt("Enter a new name for your preset below.");

					break;
				case 2: //Update items

					//Update items
					Item[] inventory = player.getInventory().copyValidItemsArray();
					Item[] equipment = player.getEquipment().copyValidItemsArray();
					for(Item t : Misc.concat(inventory, equipment)) {
						if(t.getDefinition().isNoted()) {
							player.getPacketSender().sendMessage("You cannot create presets which contain noted items.");
							return;
						}
					}
					
					player.getPresets()[index].setInventory(inventory);
					player.getPresets()[index].setEquipment(equipment);

					player.getPacketSender().sendMessage("The preset's items have been updated to match your current setup.");
					open(player);
					break;
				case 3: //Update stats

					//Fetch stats
					int[] stats = new int[7];
					for(int i = 0; i < stats.length; i++) {
						stats[i] = player.getSkillManager().getMaxLevel(Skill.values()[i]);
					}

					//Update stats
					player.getPresets()[index].setStats(stats);
					
					//Update spellbook
					player.getPresets()[index].setSpellbook(player.getSpellbook());
					
					player.getPacketSender().sendMessage("The preset's stats have been updated to match your current setup.");
					open(player);
					break;
				case 4: //Delete preset

					//Delete
					player.getPresets()[index] = null;
					player.setCurrentPreset(null);

					player.getPacketSender().sendMessage("The preset has been deleted.");
					open(player);
					break;
				case 5: //Cancel
					player.getPacketSender().sendInterfaceRemoval();
					break;
				}
			}
		});
	}

	/**
	 * Loads a preset.
	 * @param player		The player.
	 * @param preset		The preset to load.
	 */
	private static void load(Player player, final Presetable preset) {
		final int oldCbLevel = player.getSkillManager().getCombatLevel();
		
		//Close!
		player.getPacketSender().sendInterfaceRemoval();

		//Check if we can load..
		if(player.getLocation() == Locations.Location.WILDERNESS) {
			player.getPacketSender().sendMessage("You can't load a preset in the wilderness!");
			return;
		}
		if(player.getDueling().inDuel()) {
			player.getPacketSender().sendMessage("You can't load a preset during a duel!");
			return;
		}

		//Send valuable items in inventory/equipment to bank
		boolean sent = false;
		for(Item item : Misc.concat(player.getInventory().getCopiedItems(), player.getEquipment().getCopiedItems())) {
			if(item == null) {
				continue;
			}
			if(item.getDefinition().getValue() <= 0) {
				continue;
			}
			player.getBank(Bank.getTabForItem(player, item.getId())).add(item, false);
			sent = true;
		}
		if(sent) {
			player.getPacketSender().sendMessage("Your valuable items have been sent to your bank.");
		}
		
		player.getInventory().resetItems().refreshItems();
		player.getEquipment().resetItems().refreshItems();

		//Check for the preset's valuable items and see if the player has them.
		if(!preset.isGlobal()) {
			List<Item> valuableItems = new ArrayList<Item>();

			//Get all the valuable items in this preset and check if player has them..
			for(Item item : Misc.concat(preset.getInventory(), preset.getEquipment())) {
				if(item == null)
					continue;
				if(item.getDefinition().getValue() > 0) {
					valuableItems.add(item);

					int inventoryAmt = player.getInventory().getAmount(item.getId());
					int equipmentAmt = player.getEquipment().getAmount(item.getId());
					int bankAmt = player.getBank(Bank.getTabForItem(player, item.getId())).getAmount(item.getId());
					int totalAmt = inventoryAmt + equipmentAmt + bankAmt;

					int preset_amt = preset.getAmount(item.getId());

					if(totalAmt < preset_amt) {
						player.getPacketSender().sendMessage("You don't have valuable item "+item.getDefinition().getName()+" in your inventory, equipment or bank.");
						return;
					}
				}
			}

			//Delete valuable items from the proper place
			//Not from inventory/equipment, they will be reset anyway.
			for(Item item : valuableItems) {
				if(player.getInventory().contains(item)) {
					player.getInventory().delete(item);
				} else if(player.getEquipment().contains(item)) {
					player.getEquipment().delete(item);
				} else {
					player.getBank(Bank.getTabForItem(player, item.getId())).delete(item);
				}
			}
		}

		//Add inventory
		Arrays.stream(preset.getInventory()).filter(t -> !Objects.isNull(t) && t.isValid()).forEach(t -> player.getInventory().add(t));

		//Set equipment
		Arrays.stream(preset.getEquipment()).filter(t -> !Objects.isNull(t) && t.isValid()).forEach(t -> player.getEquipment().setItem(t.getDefinition().getEquipmentType().getSlot(), t.copy()));

		//Set magic spellbook
		player.setSpellbook(preset.getSpellbook());
		Autocasting.setAutocast(player, null);

		//Set levels
		for(int i = 0; i < preset.getStats().length; i++) {
			Skill skill = Skill.values()[i];
			int level = preset.getStats()[i];
			player.getSkillManager().setCurrentLevel(skill, level).
			setMaxLevel(skill, level).
			setExperience(skill, SkillManager.getExperienceForLevel(level));
		}

		//Update prayer tab with prayer info
		player.getPacketSender().sendString(687, player.getSkillManager().getCurrentLevel(Skill.PRAYER) + "/" + 
				player.getSkillManager().getMaxLevel(Skill.PRAYER));

		//Send total level
		player.getPacketSender().sendString(31200, ""+player.getSkillManager().getTotalLevel());

		//Send combat level
		final int newCbLevel = player.getSkillManager().getCombatLevel();
		final String combatLevel = "Combat level: " + newCbLevel;
		player.getPacketSender().sendString(19000,combatLevel).sendString(5858, combatLevel);
		
		if(newCbLevel != oldCbLevel) {
			BountyHunter.unassign(player);
		}
		
		//Send new spellbook
		player.getPacketSender().sendTabInterface(6, player.getSpellbook().getInterfaceId());
		
		player.getPacketSender().sendConfig(709, PrayerHandler.canUse(player, PrayerHandler.PrayerData.PRESERVE, false) ? 1 : 0);
		player.getPacketSender().sendConfig(711, PrayerHandler.canUse(player, PrayerHandler.PrayerData.RIGOUR, false) ? 1 : 0);
		player.getPacketSender().sendConfig(713, PrayerHandler.canUse(player, PrayerHandler.PrayerData.AUGURY, false) ? 1 : 0);
		
		player.restart(false);
		player.getPacketSender().sendMessage("Preset loaded!");
	}

	/**
	 * Handles a clicked button on the interface.
	 * @param player
	 * @param button
	 * @return
	 */
	public static boolean handleButton(Player player, int button) {
		if(player.getInterfaceId() != INTERFACE_ID) {
			return false;
		}
		switch(button) {
		case 45060: //Toggle on death show
			player.setOpenPresetsOnDeath(!player.isOpenPresetsOnDeath());
			player.getPacketSender().sendConfig(987, player.isOpenPresetsOnDeath() ? 0 : 1);
			return true;
		case 45061: //Edit preset
			if(player.getCurrentPreset() == null) {
				player.getPacketSender().sendMessage("You haven't selected any preset yet.");
				return true;
			}
			if(player.getCurrentPreset().isGlobal()) {
				player.getPacketSender().sendMessage("You can only edit your own presets.");
				return true;
			}
			edit(player, player.getCurrentPreset().getIndex());
			return true;
		case 45064: //Load preset
			if(player.getCurrentPreset() == null) {
				player.getPacketSender().sendMessage("You haven't selected any preset yet.");
				return true;
			}
			load(player, player.getCurrentPreset());
			return true;
		}

		//Global presets selection
		if(button >= 45070 && button <= 45079) {
			final int index = button - 45070;
			if(GLOBAL_PRESETS[index] == null) {
				player.getPacketSender().sendMessage("That preset is currently unavailable.");
				return true;
			}

			//Check if already in set, no need to re-open
			if(player.getCurrentPreset() != null && player.getCurrentPreset() == GLOBAL_PRESETS[index]) {
				return true;
			}

			open(player, GLOBAL_PRESETS[index]);
			return true;
		}

		//Custom presets selection
		if(button >= 45082 && button <= 45091) {
			final int index = button - 45082;

			if(player.getPresets()[index] == null) {
				DialogueManager.start(player, 10);
				player.setDialogueOptions(new DialogueOptions() {
					@Override
					public void handleOption(Player player, int option) {
						if(option == 1) {
							player.setEnterSyntax(new CreatePreset(index));
							player.getPacketSender().sendEnterInputPrompt("Enter a name for your preset below.");
						} else {
							player.getPacketSender().sendInterfaceRemoval();
						}
					}
				});
				return true;
			}

			//Check if already in set, no need to re-open
			if(player.getCurrentPreset() != null && player.getCurrentPreset() == player.getPresets()[index]) {
				return true;
			}

			open(player, player.getPresets()[index]);
		}
		return false;
	}
}
