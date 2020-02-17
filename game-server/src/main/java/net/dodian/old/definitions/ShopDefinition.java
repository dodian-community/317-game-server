package net.dodian.old.definitions;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.dodian.GameConstants;
import net.dodian.old.util.JsonLoader;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.container.impl.Shop;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents Shop Definitions.
 * @author Professor Oak
 */
public class ShopDefinition {

	private static Map<Integer, Shop> shops = new HashMap<>();

	public static Map<Integer, Shop> getShops() {
		return shops;
	}

	public static JsonLoader parse() {
		return new JsonLoader() {
			@Override
			public void load(JsonObject reader, Gson builder) {
				int id = reader.get("id").getAsInt();
				boolean buysItems = reader.get("buys-items").getAsBoolean();
				String name =  reader.get("name").getAsString();
				Item[] items = builder.fromJson(reader.get("items").getAsJsonArray(), Item[].class);
				Item currency = new Item(reader.get("currency").getAsInt());
				shops.put(id, new Shop(null, new ShopDefinition(id, name, currency, items, buysItems)));
			}

			@Override
			public String filePath() {
				return GameConstants.DEFINITIONS_DIRECTORY + "shops.json";
			}
		};
	}
	
	public ShopDefinition(int id, String name, Item currency, Item[] stock, boolean buysItems) {
		this.id = id;
		this.name = name;
		this.currency = currency;
		this.originalStock = stock;
		this.buysItems = buysItems;
	}
	
	private final int id;

	private final String name;

	private final Item currency;

	private final Item[] originalStock;
	
	private final boolean buysItems;
	
	/** ------------------------------------------- **/
	
	public Item[] getOriginalStock() {
		return this.originalStock;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public Item getCurrency() {
		return currency;
	}
	
	public boolean buysItems() {
		return buysItems;
	}
}
