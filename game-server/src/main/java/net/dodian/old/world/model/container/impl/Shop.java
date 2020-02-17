package net.dodian.old.world.model.container.impl;

import net.dodian.Server;
import net.dodian.old.definitions.ShopDefinition;
import net.dodian.old.engine.task.TaskManager;
import net.dodian.old.engine.task.impl.ShopRestockTask;
import net.dodian.old.util.Misc;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.PlayerStatus;
import net.dodian.old.world.model.container.ItemContainer;
import net.dodian.old.world.model.container.StackType;
import net.dodian.orm.models.definitions.ItemDefinition;

/**
 * Improved dupe-free Shop System
 * @author Gabriel Hannason
 */

public class Shop extends ItemContainer {

	private ShopDefinition definition;
	private boolean restockingItems;

	public Shop(Player player, ShopDefinition definition) {
		super(player);
		this.definition = definition;

		if(definition.getOriginalStock().length >= capacity()) {
			System.out.println("Too many items in shop id: "+definition.getId()+".  "
					+ "Item amount: "+definition.getOriginalStock().length+", max: "+capacity());
		}

		for(Item item : definition.getOriginalStock()) {
			if(getFreeSlots() == 0) {
				break;
			}
			add(item, false);
		}
	}

	//The shop's definition
	public ShopDefinition getDefinition() {
		return definition;
	}

	//Is the shop currently restocking items?
	public boolean restockingItems() {
		return restockingItems;
	}

	public void setRestockingItems(boolean restockingItems) {
		this.restockingItems = restockingItems;
	}

	//Opens shop for a player.
	public static void open(Player player, Shop shop) {
		player.getPacketSender().sendInterfaceRemoval();

		//Update player's shop and player status
		player.setShop(ShopDefinition.getShops().get(shop.getDefinition().getId()));
		player.setInterfaceId(INTERFACE_ID);
		player.setStatus(PlayerStatus.NPC_OWNED_SHOPPING);

		//Refresh items
		shop.refreshItems();

		//Set up the scroll bar
		player.getPacketSender().sendInterfaceScrollReset(SCROLL_BAR_INTERFACE_ID);
		if(shop.getDefinition().getOriginalStock().length < 37) {
			player.getPacketSender().sendScrollbarHeight(SCROLL_BAR_INTERFACE_ID, 0);
		} else {
			int rows = (shop.getDefinition().getOriginalStock().length % 9 == 0 ? (shop.getDefinition().getOriginalStock().length / 9) : ((shop.getDefinition().getOriginalStock().length / 9) + 1));
			player.getPacketSender().sendScrollbarHeight(SCROLL_BAR_INTERFACE_ID, rows * 56);
		}
		
		player.getPacketSender().sendMessage("").sendMessage("Warning! When selling an item to the shop, it loses 20% of its original value.");
	}

	public static void open(Player player, int shop) {
		open(player, ShopDefinition.getShops().get(shop));
	}

	//Checks if a player is viewing a certian shop or not.
	public static boolean viewingShop(Player player, int shopId) {
		return player.getInterfaceId() == Shop.INTERFACE_ID 
				&& player.getStatus() == PlayerStatus.NPC_OWNED_SHOPPING 
				&& player.getShop() != null && player.getShop().getDefinition().getId() == shopId;
	}

	//Checks a value in a shop
	public static void checkValue(Player player, int interface_id, int slot_id, int item_id, boolean selling_item) {

		boolean flag = false;

		if(player.getShop() == null || player.getStatus() != PlayerStatus.NPC_OWNED_SHOPPING) {
			flag = true;
		} else if(!selling_item && player.getShop().getItems()[slot_id].getId() != item_id) {
			flag = true;
		} else if(selling_item && player.getInventory().getItems()[slot_id].getId() != item_id) {
			flag = true;
		}

		if(flag) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}

		//Check if this shop buys items
		if(selling_item && !player.getShop().getDefinition().buysItems()) {
			player.getPacketSender().sendMessage("You cannot sell items to this shop.");
			return;
		}

		ItemDefinition item_def = Server.getDefinitionsHandler().getItemDefinitionById(item_id);
		int item_value = item_def.getValue();

		if(selling_item) {

			//Check if this item is even sellable
			if(!item_def.isSellable()) {
				player.getPacketSender().sendMessage("This item cannot be sold to a shop.");
				return;
			}

			//Check if shop buys this particular item
			if(!shopBuysItem(player.getShop().getDefinition().getId(), item_id)) {
				player.getPacketSender().sendMessage("You cannot sell this item to this store.");
				return;
			}

			//Reduce price by 15% when selling an item to shop.
			item_value = (int) (item_value * 0.8);
		}
		
		if(item_value == 0) {
			player.getPacketSender().sendMessage("Item has no value.");
			return;
		}

		String currencyName = player.getShop().getDefinition().getCurrency().getDefinition().getName().toLowerCase();


		if(item_value > 1) {
			if(!currencyName.endsWith("money") && !currencyName.endsWith("coins")) {
				if(!currencyName.endsWith("s")) {
					currencyName += "s";
				}
			}
		}

		String message = item_def.getName() + (selling_item ? ": shop will buy for " : " currently costs ") + ""+ Misc.insertCommasToNumber(""+(int) item_value+"")+" "+currencyName+".";

		if(item_value > 0) {
			player.getPacketSender().sendMessage(message);
			return;
		}
	}

	//Buying item
	public static void buyItem(Player player, int interface_id, int item_id, int slot, int amount) {
		boolean flag = false;

		if(player.getShop() == null || player.getStatus() != PlayerStatus.NPC_OWNED_SHOPPING) {
			flag = true;
		} else if(player.getShop().getItems()[slot].getId() != item_id) {
			flag = true;
		}

		if(player.getInterfaceId() != Shop.INTERFACE_ID || interface_id != Shop.ITEM_CHILD_ID) {
			flag = true;
		}

		if(flag) {
			return;
		}

		if(player.getShop().getDefinition().getId() != GENERAL_SHOP) {
			if(player.getShop().getItems()[slot].getAmount() <= 1) {
				player.getPacketSender().sendMessage("The shop has run out of stock for this item.");
				return;
			}
		}

		if(amount <= 0) {
			return;
		} else if(amount > 5000) {
			player.getPacketSender().sendMessage("You can only buy 5000 "+ Server.getDefinitionsHandler().getItemDefinitionById(item_id).getName()+"s at a time.");
			return;
		}

		player.getShop().setPlayer(player).switchItem(player.getInventory(), new Item(item_id, amount), slot, false, true);
	}

	@Override
	public Shop switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		final Player player = getPlayer();

		//The shop's currency id
		int currency_id = getDefinition().getCurrency().getId();

		//Is the shop a custom shop?
		boolean custom_shop = currency_id == -1;

		//The player's amount of the currency
		int currency_amount = 0;

		//The currency's name
		String currencyName = "";

		//The item's value
		int item_value = item.getDefinition().getValue();

		//The amount the player is buying
		int amount_buying = item.getAmount();

		//Check if shop isn't a custom shop..
		if(!custom_shop) {

			currency_amount = player.getInventory().getAmount(currency_id);
			currencyName = Server.getDefinitionsHandler().getItemDefinitionById(currency_id).getName().toLowerCase();

		} else {

			//Handle custom shop..

		}

		//Make sure the item has a value
		if(item_value <= 0) {
			player.getPacketSender().sendMessage("Item has no value.");
			return this;
		}

		//Check if player has enough inventory space to make the buy
		if(!hasInventorySpace(player, item, currency_id, item_value)) {
			player.getPacketSender().sendMessage("You do not have any free inventory slots.");
			return this;
		}

		//Check if player has enough of the currency to make the buy
		if (currency_amount <= 0 || currency_amount < item_value) {
			player.getPacketSender().sendMessage("You do not have enough " + ((currencyName.endsWith("s") || currencyName.endsWith("money") || currencyName.endsWith("coins") ? (currencyName) : (currencyName + "s"))) + " to purchase this item.");
			return this;
		}

		//Start buying the item.
		for(int i = amount_buying; i > 0; i--) {

			//Make sure player can keep buying the item..
			if (!contains(item.getId())) {
				break;
			}
			
			if(player.getShop().getDefinition().getId() != GENERAL_SHOP) {
				if(getItems()[slot].getAmount() <= 1) {
					player.getPacketSender().sendMessage("The shop has run out of stock for this item.");
					break;
				}
			}

			if(!item.getDefinition().isStackable()) {
				if(currency_amount >= item_value && hasInventorySpace(player, item, currency_id, item_value)) {

					if(!custom_shop) {
						player.getInventory().delete(currency_id, item_value, false);
					} else {
						//Custom shops
					}

					super.switchItem(to, new Item(item.getId(), 1), slot, false, false);
					currency_amount -= item_value;
				} else {
					break;
				}
			} else {
				if(currency_amount >= item_value && hasInventorySpace(player, item, currency_id, item_value)) {

					int canBeBought = currency_amount / (item_value);
					if(canBeBought >= amount_buying) {
						canBeBought = amount_buying;
					}
					if(canBeBought == 0)
						break;

					if(!custom_shop) {

						player.getInventory().delete(currency_id, item_value * canBeBought, false);

					} else {

						//Custom shops
					}

					super.switchItem(to, new Item(item.getId(), canBeBought), slot, false, false);
					currency_amount -= item_value;
					break;
				} else {
					break;
				}
			}
			amount_buying--;
		}
		player.getInventory().refreshItems();
		fireRestockTask();
		refreshItems();
		return this;
	}

	//Selling an item to a shop
	public static void sellItem(Player player, int interface_id, int item_id, int slot, int amount) {
		boolean flag = false;

		if(player.getShop() == null || player.getStatus() != PlayerStatus.NPC_OWNED_SHOPPING) {
			flag = true;
		} else if(player.getInventory().getItems()[slot].getId() != item_id) {
			flag = true;
		}

		if(player.getInterfaceId() != Shop.INTERFACE_ID || interface_id != Shop.INVENTORY_INTERFACE_ID) {
			flag = true;
		}

		if(flag) {
			return;
		}

		//Shop is full
		if(player.getShop().full(item_id)) {
			return;
		}

		//Shop doesn't buy items..
		if(!shopBuysItem(player.getShop().getDefinition().getId(), item_id)) {
			player.getPacketSender().sendMessage("You cannot sell this item to this store.");
			return;
		}

		//Check if this item can be sold via their definition
		ItemDefinition item_def = Server.getDefinitionsHandler().getItemDefinitionById(item_id);
		if(!item_def.isSellable()) {
			player.getPacketSender().sendMessage("This item cannot be sold.");
			return;
		}

		//Check if player has the correct amount of the item 
		//they're trying to sell
		if(player.getInventory().getAmount(item_id) < amount)
			amount = player.getInventory().getAmount(item_id);
		if(amount == 0)
			return;

		//Update the shop's player holder
		player.getShop().setPlayer(player);

		final Shop shop = player.getShop();

		//The shop's currency
		int currency_id = shop.getDefinition().getCurrency().getId();

		//Is the shop a custom one?
		boolean custom_shop = currency_id == -1;

		//Inventory space
		boolean inventory_space = custom_shop ? true : false;

		//Item value
		int item_value = 0;

		//Amount of items selling
		int amount_selling = amount;

		//Fetching inventory space
		if(!custom_shop) {
			if(!item_def.isStackable()) {
				if(!player.getInventory().contains(currency_id)) {
					inventory_space = true;
				}
			}
			if(player.getInventory().getFreeSlots() <= 0 && player.getInventory().getAmount(currency_id) > 0) {
				inventory_space = true;
			}
			if(player.getInventory().getFreeSlots() > 0 || player.getInventory().getAmount(currency_id) > 0) {
				inventory_space = true;
			}

			item_value = Server.getDefinitionsHandler().getItemDefinitionById(item_id).getValue();
		} else {
			//Get item values for custom shops
		}


		if(item_value <= 0) {
			player.getPacketSender().sendMessage("Item has no value.");
			return;
		}

		//Reduce price by 15% when selling an item to shop.
		item_value = (int) (item_value * 0.80);

		if(item_value <= 0) {
			item_value = 1;
		}

		//Sell item
		for (int i = amount_selling; i > 0; i--) {
			if(shop.full(item_id) || !player.getInventory().contains(item_id)) {
				break;
			}
			if(!item_def.isStackable()) {
				if(inventory_space) {
					shop.switchItem(player.getInventory(), shop, item_id, -1);
					if(!custom_shop) {
						player.getInventory().add(new Item(currency_id, item_value), false);
					} else {
						//Return points here

					}
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			} else {
				if(inventory_space) {
					shop.switchItem(player.getInventory(), shop, item_id, amount_selling);
					if(!custom_shop) {
						player.getInventory().add(new Item(currency_id, item_value * amount_selling), false);
					} else {
						//Return points here

					}
					break;
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			}
			amount_selling--;
		}
		player.getInventory().refreshItems();
		shop.fireRestockTask();
		shop.refreshItems();
	}

	/**
	 * Checks if a player has enough inventory space to buy an item
	 * @param item	The item which the player is buying
	 * @return		true or false if the player has enough space to buy the item
	 */
	public static boolean hasInventorySpace(Player player, Item item, int currency, int pricePerItem) {
		if(player.getInventory().getFreeSlots() >= 1) {
			return true;
		}
		if(item.getDefinition().isStackable()) {
			if(player.getInventory().contains(item.getId())) {
				return true;
			}
		}
		if(currency != -1) {
			if(player.getInventory().getFreeSlots() == 0 && player.getInventory().getAmount(currency) == pricePerItem) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int capacity() {
		return 1000;
	}

	@Override
	public StackType stackType() {
		return StackType.STACKS;
	}

	@Override
	public Shop refreshItems() {
		for (Player player : World.getPlayers()) {

			//Only check for players who are actually
			//viewing this shop.
			if (player == null || !viewingShop(player, getDefinition().getId())) {
				continue;
			}
			
			//Update the shop for the player that's viewing it
			player.setShop(this);
			
			//Sending shop items
			player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_INTERFACE_ID);
			player.getPacketSender().sendItemContainer(ShopDefinition.getShops().get(getDefinition().getId()), ITEM_CHILD_ID);

			//Sending shop name
			player.getPacketSender().sendString(NAME_INTERFACE_CHILD_ID, getDefinition().getName());

			//Sending interface set
			if(player.getEnterSyntax() == null) {
				player.getPacketSender().sendInterfaceSet(INTERFACE_ID, INVENTORY_INTERFACE_ID - 1);
			}
		}

		return this;
	}

	@Override
	public Shop full() {
		getPlayer().getPacketSender().sendMessage("The shop is currently full. Please come back later.");
		return this;
	}

	public static String shopPriceEx(int shopPrice) {
		String ShopAdd = "";
		if (shopPrice >= 1000 && shopPrice < 1000000) {
			ShopAdd = " (" + (shopPrice / 1000) + "K)";
		} else if (shopPrice >= 1000000) {
			ShopAdd = " (" + (shopPrice / 1000000) + " million)";
		}
		return ShopAdd;
	}

	public void fireRestockTask() {
		if(restockingItems() || fullyRestocked())
			return;
		setRestockingItems(true);
		TaskManager.submit(new ShopRestockTask(this));
	}

	public boolean fullyRestocked() {
		
		//General store is only fully restocked if it's empty.
		if(definition.getId() == GENERAL_SHOP) {
			return isEmpty();
		}
		
		//Compare to check if any items in the shop aren't their original amount.
		if(getDefinition().getOriginalStock() != null) {
			for(int shopItemIndex = 0; shopItemIndex < getDefinition().getOriginalStock().length; shopItemIndex++) {
				if(getItems()[shopItemIndex].getAmount() != getDefinition().getOriginalStock()[shopItemIndex].getAmount()) {
					return false;
				}
			}
		}		
		return true;
	}

	public static boolean shopBuysItem(int shopId, int item) {
		if(shopId == GENERAL_SHOP) {
			return true;
		}
		Shop shop = ShopDefinition.getShops().get(shopId);
		if(shop != null && shop.getDefinition().getOriginalStock() != null) {
			for(Item it : shop.getDefinition().getOriginalStock()) {
				if(it != null && it.getId() == item) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * The shop interface id.
	 */
	public static final int INTERFACE_ID = 3824;

	/**
	 * The starting interface child id of items.
	 */
	public static final int ITEM_CHILD_ID = 3900;

	/**
	 * The interface child id of the shop's name.
	 */
	public static final int NAME_INTERFACE_CHILD_ID = 3901;

	/**
	 * The inventory interface id, used to set the items right click values
	 * to 'sell'.
	 */
	public static final int INVENTORY_INTERFACE_ID = 3823;

	/**
	 * The scrollbar interface id
	 */
	public static final int SCROLL_BAR_INTERFACE_ID = 29995;

	/**
	 * If a shop has this amount of an item, 
	 * it's infinite.
	 */
	public static final int INFINITE_ITEM_AMOUNT = 2000000000;

	/*
	 * Declared shops
	 */
	public static final int PVP_SHOP = 0;
	public static final int GAMBLING_SHOP = 1;
	public static final int GENERAL_SHOP = 2;
}
