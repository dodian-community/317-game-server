package net.dodian.old.engine.task.impl;

import net.dodian.old.engine.task.Task;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.container.impl.Shop;

public class ShopRestockTask extends Task {

	public ShopRestockTask(Shop shop) {
		super(4);
		this.shop = shop;
	}

	private final Shop shop;

	@Override
	protected void execute() {
		if(shop.fullyRestocked()) {
			stop();
			return;
		}
		
		if(shop.getDefinition().getId() == Shop.GENERAL_SHOP) {
			
			//General shop, simply delete items from the stock.
			for(Item item : shop.getValidItems()) {
				shop.delete(item.getId(), getDeleteRatio(item.getAmount()), false);
			}
			
		} else {
			
			//Other shops, add or delete items.
			for(int shopItemIndex = 0; shopItemIndex < shop.getDefinition().getOriginalStock().length; shopItemIndex++) {
				int originalStockAmount = shop.getDefinition().getOriginalStock()[shopItemIndex].getAmount();
				int currentStockAmount = shop.getItems()[shopItemIndex].getAmount();
				
				//Check if we have too many items in the shop.
				//If that's the case, delete them step by step.
				if(currentStockAmount > originalStockAmount) {
					int toDelete = getDeleteRatio(shop.getItems()[shopItemIndex].getAmount());
					shop.delete(shop.getItems()[shopItemIndex].getId(), toDelete, false);
				} 
				//Check if we have too few items in the shop.
				//If that's the case, restock them step by step.
				else if(currentStockAmount < originalStockAmount) {
					int toRestock = getRestockAmount(originalStockAmount, currentStockAmount);
					shop.add(new Item(shop.getItems()[shopItemIndex].getId(), toRestock), false);
				}
			}
		}

		shop.refreshItems();
	}

	@Override
	public void stop() {
		setEventRunning(false);
		shop.setRestockingItems(false);
	}

	private static int getRestockAmount(int originalStockAmount, int currentStockAmount) {
		int calc = originalStockAmount / currentStockAmount;
		if(calc < 1) {
			calc = 1;
		} else if(calc > originalStockAmount) {
			calc = originalStockAmount;
		}
		return calc;
	}

	private static int getDeleteRatio(int currentStock) {
		int calc = currentStock / 10;
		if(calc < 1) {
			calc = 1;
		}
		return calc;
	}
}
