package net.dodian.game.models.container;

import lombok.Data;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Item;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Data
public abstract class ItemContainer {
    public abstract int capacity();
    public abstract StackType stackType();
    public abstract ItemContainer refreshItems();
    public abstract ItemContainer full();

    protected Player player;
    protected List<Item> items = new ArrayList<>();

    public ItemContainer() {
        this.resetItems();
    }

    public boolean isSlotOccupied(int slot) {
        return items.get(slot) != null && items.get(0).getAmount() > 0 && items.get(0).getAmount() > 0;
    }

    public ItemContainer setItem(int slot, Item item) {
        items.add(slot, item);
        item.setSlot(slot);
        return this;
    }

    public ItemContainer swap(int fromSlot, int toSlot) {
        Item fromItem = items.get(fromSlot);
        Item toItem = items.get(toSlot);
        if(fromItem == null || fromItem.getAmount() <= 0) {
            return this;
        }

        setItem(fromSlot, toItem);
        setItem(toSlot, fromItem);
        return this;
    }

    public int getFreeSlots() {
        return (int) items.stream()
                .filter(item -> item.getId() > 0)
                .count();
    }

    public boolean contains(int id) {
        return items.stream().anyMatch(item -> item.getId() == id);
    }

    public boolean contains(Item item) {
        return contains(item.getId());
    }

    public boolean containsAmountOf(int id, int amount) {
        return containsAmountOf(new Item(id, amount));
    }

    public boolean containsAmountOf(Item item) {
        return false;
    }

    public int getSlot(int id) {
        Item item = items.stream()
                .filter(invItem -> invItem.getId() == id)
                .findFirst()
                .orElse(new Item(-1, 0));

        if(item.getId() > 0) {
            return item.getSlot();
        }

        return -1;
    }

    public int getEmptySlot() {
        for(int i = 0; i < capacity(); i++) {
            if(items.get(i).getId() <= 0 || items.get(i).getAmount() <= 0) {
                return i;
            }
        }

        return -1;
    }

    public boolean isFull() {
        return getEmptySlot() == -1;
    }

    public int getAmount(int id) {
        final Item requestedItem = new Item(id, 0);
        List<Item> requestedItems = items.stream()
                .filter(item -> item.getId() == id)
                .collect(Collectors.toList());

        requestedItems.forEach(item -> requestedItem.setAmount(requestedItem.getAmount() + item.getAmount()));

        return requestedItem.getAmount();
    }

    public ItemContainer resetItems() {
        this.items = new ArrayList<>();
        for(int i = 0; i < capacity(); i++) {
            items.add(new Item(-1, 0));
        }
        return this;
    }

    public Item getItem(int slot) {
        return items.get(slot);
    }

    public ItemContainer moveItem(ItemContainer toContainer, Item item, boolean sort, boolean refresh) {
        return this;
    }

    public ItemContainer removeItem(int id, int amount) {
        return this;
    }
}
