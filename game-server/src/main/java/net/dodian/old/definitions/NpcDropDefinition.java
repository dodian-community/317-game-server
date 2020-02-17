package net.dodian.old.definitions;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.dodian.GameConstants;
import net.dodian.old.util.JsonLoader;
import net.dodian.old.util.Misc;
import net.dodian.old.world.content.BossPets;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.grounditems.GroundItemManager;
import net.dodian.old.world.model.GroundItem;
import net.dodian.old.world.model.Item;

import java.util.*;

/**
 * Handles drop definitions.
 * @author Professor Oak
 */
public class NpcDropDefinition {

	/**
	 * The npc id.
	 */
	private final int npcId;

	/**
	 * The possible drop tables.
	 */
	private final Item[] alwaysDroptable;
	private final Item[] commonDroptable;
	private final Item[] uncommonDroptable;
	private final Item[] rareDroptable;
	private final Item[] veryRareDroptable;
	private final Item[] legendaryDroptable;

	/**
	 * Constructs a new drop definition.
	 * @param npcId
	 * @param always_droptable
	 * @param common_droptable
	 * @param uncommon_droptable
	 * @param rare_droptable
	 * @param very_rare_droptable
	 */
	public NpcDropDefinition(int npcId, Item[] always_droptable, Item[] common_droptable, Item[] uncommon_droptable, 
			Item[] rare_droptable, Item[] very_rare_droptable, Item[] legendaryDroptable) {
		this.npcId = npcId;
		this.alwaysDroptable = always_droptable;
		this.commonDroptable = common_droptable;
		this.uncommonDroptable = uncommon_droptable;
		this.rareDroptable = rare_droptable;
		this.veryRareDroptable = very_rare_droptable;
		this.legendaryDroptable = legendaryDroptable;
	}

	public int getNpcId() {
		return npcId;
	}

	public Item[] getAlwaysDroptable() {
		return alwaysDroptable;
	}

	public Item[] getCommonDroptable() {
		return commonDroptable;
	}

	public Item[] getUncommonDroptable() {
		return uncommonDroptable;
	}

	public Item[] getRareDroptable() {
		return rareDroptable;
	}

	public Item[] getVeryRareDroptable() {
		return veryRareDroptable;
	}

	public Item[] getLegendaryDroptable() {
		return legendaryDroptable;
	}

	/**
	 * The list containing all our npc drops.
	 */
	private static final Map<Integer, NpcDropDefinition> DROPS_LIST = new HashMap<Integer, NpcDropDefinition>();

	/**
	 * Loads all our npc drops.
	 * @return
	 */
	public static JsonLoader parse() {
		return new JsonLoader() {
			@Override
			public void load(JsonObject reader, Gson builder) {

				//Load the npc ids
				int npcId = reader.get("npcId").getAsInt();

				//Load the drop tables..
				Item[] always_droptable = builder.fromJson(reader.get("always_droptable"), Item[].class);
				Item[] common_droptable = builder.fromJson(reader.get("common_droptable"), Item[].class);
				Item[] uncommon_droptable = builder.fromJson(reader.get("uncommon_droptable"), Item[].class);
				Item[] rare_droptable = builder.fromJson(reader.get("rare_droptable"), Item[].class);
				Item[] very_rare_droptable = builder.fromJson(reader.get("very_rare_droptable"), Item[].class);
				Item[] legendary_droptable = builder.fromJson(reader.get("legendary_droptable"), Item[].class);

				DROPS_LIST.put(npcId, new NpcDropDefinition(npcId, always_droptable, common_droptable, uncommon_droptable, rare_droptable, very_rare_droptable,
						legendary_droptable));
			}
			@Override
			public String filePath() {
				return GameConstants.DEFINITIONS_DIRECTORY + "npc_drops.json";
			}
		};
	}

	/**
	 * Gets the drop definition for the specified npc id.
	 * @param npcId
	 * @return
	 */
	public static Optional<NpcDropDefinition> get(int npcId) {
		NpcDropDefinition drop = DROPS_LIST.get(npcId);
		if(drop != null) {
			return Optional.of(drop);
		}
		return Optional.empty();
	}

	/**
	 * Handles the drops for a player, rewarding them for
	 * killing the specified npc.
	 * 
	 * @param player
	 * @param npc
	 */
	public static void handleDrop(Player player, NPC npc) {
		//Get the drop definition for this npc..
		Optional<NpcDropDefinition> drop = get(npc.getId());

		//Handle the drop if the npc's definition was found..
		if(drop.isPresent()) {

			//The amount of items the player will receive.
			int rolls = 1 + Misc.getRandom(3);

			//Drop "always" items.
			for(Item item : drop.get().getAlwaysDroptable()) {
				if(item == null) {
					continue;
				}
				GroundItemManager.spawnGroundItem(player, new GroundItem(item, npc.getPosition(), player.getUsername(), false, 150, true, 120));
			}

			//Get a secondary drop table..
			//And handle the drops..
			final List<DropTableDefinition> parsedTables = new ArrayList<DropTableDefinition>();
			for(int i = 0; i < rolls; i++) {

				//Get a random percent..
				double randomPercent = Misc.getRandomDouble(100);

				//Fetch the drop table for our random percent..
				Optional<DropTableDefinition> chance = DropTableDefinition.forPercent(randomPercent);

				//Drop an item from the drop table if it's present..
				if(chance.isPresent()) {

					//Make sure we haven't already received a drop from this
					//drop table..
					if(parsedTables.contains(chance.get())) {
						continue;
					}

					//Fetch a random item from the drop table..
					Optional<Item> toDrop = getDrop(drop.get(), chance.get());

					//Drop our random item if it is present..
					if(toDrop.isPresent()) {
						
						//Drop the item we received as long as it isn't a pet..
						//Otherwise spawn it for the player.
						if(!BossPets.spawnFor(player, toDrop.get().getId(), true)) {
							GroundItemManager.spawnGroundItem(player, new GroundItem(toDrop.get(), npc.getPosition(), player.getUsername(), false, 150, true, 120));
						}
						
						//Add the drop table chance..
						parsedTables.add(chance.get());
					}
				}
			}
		}
	}


	/**
	 * Drops a random item.
	 * @param drop
	 * @param chance
	 */
	public static Optional<Item> getDrop(NpcDropDefinition drop, DropTableDefinition chance) {
		Optional<Item[]> items = Optional.empty();

		switch(chance) {
		case COMMON:
			items = Optional.of(drop.getCommonDroptable());
			break;
		case UNCOMMON:
			items = Optional.of(drop.getUncommonDroptable());
			break;
		case RARE:
			items = Optional.of(drop.getRareDroptable());
			break;
		case VERY_RARE:
			items = Optional.of(drop.getVeryRareDroptable());
			break;
		case LEGENDARY:
			items = Optional.of(drop.getLegendaryDroptable());
			break;
		}

		//Make sure that the items fetched are valid.
		if(items.isPresent() && items.get().length > 0) {
			//Get a random item to drop from the table..
			Item item = items.get()[Misc.getRandom(items.get().length)];

			//Get a random amount for the item..
			int dropAmount = item.getAmount();
			if(dropAmount > 1) {
				dropAmount = 1 + Misc.getRandom(dropAmount);
				if(dropAmount > item.getAmount()) {
					dropAmount = item.getAmount();
				}
			}

			//Create the item to drop..
			return Optional.of(new Item(item.getId(), dropAmount));
		}
		return Optional.empty();
	}

	/**
	 * Droptable definitions.
	 */
	public static enum DropTableDefinition {

		COMMON(90),
		UNCOMMON(40),
		RARE(8),
		VERY_RARE(0.6),
		LEGENDARY(0.1);

		DropTableDefinition(double percent) {
			this.percent = percent;
		}

		public double getPercent() {
			return percent;
		}

		private final double percent;

		/**
		 * Gets the {@link DropTableDefinition} for the specified percent.
		 * @param percent
		 * @return
		 */
		public static Optional<DropTableDefinition> forPercent(double percent) {
			Optional<DropTableDefinition> d_ = Optional.empty();
			for(DropTableDefinition d : DropTableDefinition.values()) {
				if(percent <= d.getPercent()) {
					d_ = Optional.of(d);
				}
			}
			return d_;
		}
	}
}
