package net.dodian.old.world.model;

import net.dodian.Server;
import net.dodian.orm.models.definitions.ItemDefinition;


/**
 * Represents an item which is owned by a player.
 * 
 * @author relex lawl
 */

public class Item {

	/**
	 * An Item object constructor.
	 * @param id		Item id.
	 * @param amount	Item amount.
	 */
	public Item(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	/**
	 * An Item object constructor.
	 * @param id		Item id.
	 */
	public Item(int id) {
		this(id, 1);
	}

	/**
	 * The item id.
	 */
	private int id;

	/**
	 * Gets the item's id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the item's id.
	 * @param id	New item id.
	 */
	public Item setId(int id) {
		this.id = id;
		return this;
	}

	/**
	 * Amount of the item.
	 */
	private int amount;

	/**
	 * Gets the amount of the item.
	 */
	public int getAmount() {
		return amount;
	}
	
	/**	
	 * The item's slot in a container.
	 */
	private int slot;
	
	public int getSlot() {
		return this.slot;
	}
	
	public void setSlot(int slot) {
		this.slot = slot;
	}

	/**
	 * Sets the amount of the item.
	 */
	public Item setAmount(int amount) {
		this.amount = amount;
		return this;
	}
	
	/**
	 * Checks if this item is valid or not.	
	 * @return
	 */
	public boolean isValid() {
		return id > 0 && amount >= 1;
	}

	/**
	 * Copying the item by making a new item with same values.
	 */
	public Item copy() {
		Item item = new Item(id, amount);
		item.setSlot(slot);
		return item;
	}

	/**
	 * Increment the amount by 1.
	 */
	public void incrementAmount() {
		if ((amount + 1) > Integer.MAX_VALUE) {
			return;
		}
		amount++;
	}
	
	/**
	 * Decrement the amount by 1.
	 */
	public void decrementAmount() {
		if ((amount - 1) < 0) {
			return;
		}
		amount--;
	}

	/**
	 * Increment the amount by the specified amount.
	 */
	public void incrementAmountBy(int amount) {
		if ((this.amount + amount) > Integer.MAX_VALUE) {
			this.amount = Integer.MAX_VALUE;
		} else {
			this.amount += amount;
		}
	}

	/**
	 * Decrement the amount by the specified amount.
	 */
	public void decrementAmountBy(int amount) {
		if ((this.amount - amount) < 1) {
			this.amount = 0;
		} else {
			this.amount -= amount;
		}
	}
	
	/**
	 * Gets item's definition.
	 */
	public ItemDefinition getDefinition() {
		return Server.getDefinitionsHandler().getItemDefinitionById(id);
	}
	
	public static int getNoted(int id) {
		int noteId = Server.getDefinitionsHandler().getItemDefinitionById(id).getNoteId();
		return noteId > 0 ? noteId : id;
	}
	
	public static int getUnNoted(int id) {
		ItemDefinition unNoted = Server.getDefinitionsHandler().getItemDefinitionById(id - 1);
		int unNotedId = id;
		if(unNoted.getName().equals(Server.getDefinitionsHandler().getItemDefinitionById(id).getName())) {
			unNotedId = unNoted.getId();
		}
		if(id == 11284 || id == 11285) {
			unNotedId = 11283;
		}
		return unNotedId;
	}
}