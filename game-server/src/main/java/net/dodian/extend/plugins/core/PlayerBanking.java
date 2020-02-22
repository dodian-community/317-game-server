package net.dodian.extend.plugins.core;

import com.google.gson.JsonElement;
import net.dodian.config.GameConfiguration;
import net.dodian.extend.events.controllers.ButtonClickEventListener;
import net.dodian.extend.events.items.ItemContainerActionEventListener;
import net.dodian.extend.events.object.ObjectActionEventListener;
import net.dodian.old.engine.task.impl.WalkToTask;
import net.dodian.old.world.entity.impl.object.GameObject;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.container.impl.Bank;
import net.dodian.old.world.model.syntax.impl.BankX;
import net.dodian.old.world.model.syntax.impl.WithdrawBankX;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.dodian.old.world.model.container.impl.Bank.*;

@Component
public class PlayerBanking implements ItemContainerActionEventListener, ButtonClickEventListener, ObjectActionEventListener {

    private final Bank bank;
    private final WithdrawBankX withdrawBankX;
    private final BankX bankX;
    private final GameConfiguration gameConfiguration;

    public PlayerBanking(
        Bank bank,
        WithdrawBankX withdrawBankX,
        BankX bankX,
        GameConfiguration gameConfiguration
    ) {
        this.bank = bank;
        this.withdrawBankX = withdrawBankX;
        this.bankX = bankX;
        this.gameConfiguration = gameConfiguration;
    }

    @Override
    public void onClick(Player player, int button) {
        bank.handleButton(player, button, 0);
    }

    @Override
    public void onFirstAction(Player player, int interfaceId, int slot, int itemId) {
        if(interfaceId >= CONTAINER_START && interfaceId < CONTAINER_START + TOTAL_BANK_TABS) {
            bank.withdraw(player, itemId, slot, 1, interfaceId - CONTAINER_START);
        }

        if(interfaceId == INVENTORY_INTERFACE_ID) {
            bank.deposit(player, itemId, slot, 1);
        }
    }

    @Override
    public void onSecondAction(Player player, int interfaceId, int slot, int itemId) {
        if(interfaceId >= CONTAINER_START && interfaceId < CONTAINER_START + TOTAL_BANK_TABS) {
            bank.withdraw(player, itemId, slot, 5, interfaceId - CONTAINER_START);
        }

        if(interfaceId == INVENTORY_INTERFACE_ID) {
            bank.deposit(player, itemId, slot, 5);
        }
    }

    @Override
    public void onThirdAction(Player player, int interfaceId, int slot, int itemId) {
        if(interfaceId >= CONTAINER_START && interfaceId < CONTAINER_START + TOTAL_BANK_TABS) {
            bank.withdraw(player, itemId, slot, 10, interfaceId - CONTAINER_START);
        }

        if(interfaceId == INVENTORY_INTERFACE_ID) {
            bank.deposit(player, itemId, slot, 10);
        }
    }

    @Override
    public void onFourthAction(Player player, int interfaceId, int slot, int itemId) {
        if(interfaceId >= CONTAINER_START && interfaceId < CONTAINER_START + TOTAL_BANK_TABS) {
            bank.withdraw(player, itemId, slot, -1, interfaceId - CONTAINER_START);
        }

        if(interfaceId == INVENTORY_INTERFACE_ID) {
            bank.deposit(player, itemId, slot, -1);
        }
    }

    @Override
    public void onFifthAction(Player player, int interfaceId, int slot, int itemId) {
        if(interfaceId >= CONTAINER_START && interfaceId < CONTAINER_START + TOTAL_BANK_TABS) {
            withdrawBankX.setItemId(itemId);
            withdrawBankX.setSlot(slot);
            withdrawBankX.setFromTab(interfaceId - CONTAINER_START);

            player.setEnterSyntax(withdrawBankX);
            player.getPacketSender().sendEnterAmountPrompt("How many would you like to withdraw?");
        }

        if(interfaceId == INVENTORY_INTERFACE_ID) {
            bankX.setItemId(itemId);
            bankX.setSlotId(slot);

            player.setEnterSyntax(bankX);
            player.getPacketSender().sendEnterAmountPrompt("How many would you like to bank?");
        }
    }

    @Override
    public void onFirstClick(Player player, GameObject gameObject) {
        List<Integer> bankBooths = new ArrayList<>();

        Optional<JsonElement> bankBoothIdsJson = gameConfiguration.get("bank-booths");
        bankBoothIdsJson.ifPresent(ids -> {
            ids.getAsJsonArray().forEach(id -> bankBooths.add(id.getAsInt()));
        });

        if(bankBooths.contains(gameObject.getId())) {
            player.setWalkToTask(new WalkToTask(player, gameObject.getPosition(), gameObject.getSize(), () -> {
                player.getBank(player.getCurrentBankTab()).open();
            }));
        }
    }

    @Override
    public void onSecondClick(Player player, GameObject gameObject) {
        onFirstClick(player, gameObject);
    }

    @Override
    public void onThirdClick(Player player, GameObject gameObject) {

    }

    @Override
    public void onFourthClick(Player player, GameObject gameObject) {

    }

    @Override
    public void onFifthClick(Player player, GameObject gameObject) {

    }
}
