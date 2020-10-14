package net.dodian.plugins.impl;

import net.dodian.game.events.EventHandler;
import net.dodian.game.events.EventListener;
import net.dodian.game.events.impl.player.interact.PlayerButtonClickEvent;
import net.dodian.game.events.impl.player.interact.item.container.*;
import net.dodian.game.events.impl.player.interact.npc.PlayerNpcSecondClickEvent;
import net.dodian.game.events.impl.player.interact.object.PlayerObjectSecondClickEvent;
import net.dodian.old.world.model.PlayerStatus;
import net.dodian.old.world.model.container.impl.Bank;
import net.dodian.old.world.model.syntax.impl.BankX;
import net.dodian.old.world.model.syntax.impl.SearchBank;
import net.dodian.old.world.model.syntax.impl.WithdrawBankX;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static net.dodian.old.world.model.container.impl.Bank.exitSearch;

@Component
public class Banking implements EventListener {

    @EventHandler
    public void onButtonClick(PlayerButtonClickEvent event) {
        if(event.getButton() == 32503) {
            event.getPlayer().getPacketSender().sendInterfaceRemoval();
        }

        if(event.getButton() == 32512) {
            event.getPlayer().getBank(event.getPlayer().getCurrentBankTab()).open();
        }

        if(!event.getPlayer().getStatus().equals(PlayerStatus.BANKING)) {
            return;
        }

        // Exit interface
        if(event.getButton() == 50001 || event.getButton() == 5384) {
            event.getPlayer().getPacketSender().sendInterfaceRemoval();
            return;
        }

        // Deposit Equipment
        if(event.getButton() == 50007) {
            event.getPlayer().getEquipment().moveItems(event.getPlayer().getBank(event.getPlayer().getCurrentBankTab()), true, true);
            event.getPlayer().restart(true);
            return;
        }

        // Deposit Inventory
        if(event.getButton() == 50004) {
            event.getPlayer().getInventory().moveItems(event.getPlayer().getBank(event.getPlayer().getCurrentBankTab()), true, true);
            return;
        }

        if(event.getButton() == 5386) {
            event.getPlayer().setNoteWithdrawal(true);
            return;
        }

        if(event.getButton() == 5387) {
            event.getPlayer().setNoteWithdrawal(false);
            return;
        }

        if(event.getButton() == 8130) {
            event.getPlayer().setInsertMode(false);
            return;
        }

        if(event.getButton() == 8131) {
            event.getPlayer().setInsertMode(true);
            return;
        }

        if(event.getButton() == 50010) {

            if(event.getPlayer().isSearchingBank()) {
                exitSearch(event.getPlayer(), true);
                return;
            }

            event.getPlayer().setEnterSyntax(new SearchBank());
            event.getPlayer().getPacketSender().sendEnterInputPrompt("What do you wish to search for?");
        }

        if(event.getButton() == 50013) {
            //Show menu
            event.getPlayer().getPacketSender().sendInterfaceRemoval();
            event.getPlayer().getPacketSender().sendInterface(32500);
        }

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
        if(event.getInterfaceId() >= Bank.CONTAINER_START && event.getInterfaceId() < Bank.CONTAINER_START + Bank.TOTAL_BANK_TABS) {
            event.getPlayer().setEnterSyntax(new WithdrawBankX(event.getItem().getId(), event.getSlot(), event.getPlayer().getCurrentBankTab()));
            event.getPlayer().getPacketSender().sendEnterInputPrompt("How many do you wish to withdraw?");
        } else if(event.getInterfaceId() == Bank.INVENTORY_INTERFACE_ID) {
            event.getPlayer().setEnterSyntax(new BankX(event.getItem().getId(), event.getSlot()));
            event.getPlayer().getPacketSender().sendEnterInputPrompt("How many do you wish to deposit?");
        }
    }
}
