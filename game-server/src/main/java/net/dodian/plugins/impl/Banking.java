package net.dodian.plugins.impl;

import net.dodian.game.events.EventHandler;
import net.dodian.game.events.EventListener;
import net.dodian.game.events.impl.player.interact.PlayerButtonClickEvent;
import net.dodian.game.events.impl.player.interact.item.container.PlayerItemContainerFirstClickEvent;
import net.dodian.game.events.impl.player.interact.npc.PlayerNpcSecondClickEvent;
import net.dodian.game.events.impl.player.interact.object.PlayerObjectSecondClickEvent;
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

    @EventHandler
    public void onItemContainerFirstClick(PlayerItemContainerFirstClickEvent event) {


        /*if(event.getInterfaceId() >= Bank.CONTAINER_START && event.getInterfaceId() < Bank.CONTAINER_START + Bank.TOTAL_BANK_TABS) {
            Bank.withdraw(event.getPlayer(), event.getItem().getId(), event.getSlot(), 1, event.getInterfaceId() - Bank.CONTAINER_START);
            return;
        }

        if(event.getInterfaceId() == Bank.INVENTORY_INTERFACE_ID) {
            Bank.deposit(event.getPlayer(), event.getItem().getId(), event.getSlot(), 1);
            return;
        }*/
    }
}
