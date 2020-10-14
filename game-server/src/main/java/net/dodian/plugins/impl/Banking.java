package net.dodian.plugins.impl;

import net.dodian.game.events.EventHandler;
import net.dodian.game.events.EventListener;
import net.dodian.game.events.impl.player.interact.PlayerButtonClickEvent;
import net.dodian.game.events.impl.player.interact.item.container.*;
import net.dodian.game.events.impl.player.interact.npc.PlayerNpcSecondClickEvent;
import net.dodian.game.events.impl.player.interact.object.PlayerObjectSecondClickEvent;
import net.dodian.old.world.model.container.impl.Bank;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Banking implements EventListener {

    @EventHandler
    public void onButtonClick(PlayerButtonClickEvent event) {
        if(event.getButton() != 50001) {
            return;
        }

        event.getPlayer().getPacketSender().sendInterfaceRemoval();
    }

    @EventHandler
    public void onNpcSecondClick(PlayerNpcSecondClickEvent event) {
        int[] bankerIds = new int[]{394};

        if(Arrays.stream(bankerIds).noneMatch(id -> id == event.getNpc().getId())) {
            return;
        }

        event.getPlayer().getBank(event.getPlayer().getCurrentBankTab()).open();
    }

    @EventHandler
    public void onObjectSecondClick(PlayerObjectSecondClickEvent event) {
        Integer[] bankIds = new Integer[]{25808, 6943};

        if(!Arrays.asList(bankIds).contains(event.getObject().getId())) {
            return;
        }

        event.getPlayer().getBank(event.getPlayer().getCurrentBankTab()).open();
    }

    private void moveItem(PlayerItemContainerEvent event, int amount) {
        if(event.getInterfaceId() >= Bank.CONTAINER_START && event.getInterfaceId() < Bank.CONTAINER_START + Bank.TOTAL_BANK_TABS) {
            // Moving item from bank to inventory.
            Bank.withdraw(event.getPlayer(), event.getItem().getId(), event.getSlot(), amount, event.getPlayer().getCurrentBankTab());
        } else if(event.getInterfaceId() == Bank.INVENTORY_INTERFACE_ID) {
            // Moving item from inventory to bank.
            Bank.deposit(event.getPlayer(), event.getItem().getId(), event.getSlot(), amount);
        }
    }

    @EventHandler
    public void moveOneItem(PlayerItemContainerFirstClickEvent event) {
        moveItem(event, 1);
    }

    @EventHandler
    public void moveFiveItems(PlayerItemContainerSecondClickEvent event) {
        moveItem(event, 5);
    }

    @EventHandler
    public void moveTenItems(PlayerItemContainerThirdClickEvent event) {
        moveItem(event, 10);
    }

    @EventHandler
    public void moveAllItems(PlayerItemContainerFourthClickEvent event) {
        moveItem(event, Integer.MAX_VALUE);
    }

    @EventHandler
    public void moveXItems(PlayerItemContainerFifthClickEvent event) {

    }
}
