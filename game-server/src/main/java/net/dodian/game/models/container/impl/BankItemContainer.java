package net.dodian.game.models.container.impl;

import net.dodian.game.models.container.ItemContainer;
import net.dodian.game.models.container.StackType;

public class BankItemContainer extends ItemContainer {

    @Override
    public int capacity() {
        return 352;
    }

    @Override
    public StackType stackType() {
        return StackType.STACKS;
    }

    @Override
    public BankItemContainer refreshItems() {
        return this;
    }

    @Override
    public BankItemContainer full() {
        if(this.player != null) {
            this.player.getPacketSender().sendMessage("Not enough space in bank.");
        }
        return this;
    }

    public BankItemContainer withdraw(int slot) {
        return this;
    }

    public BankItemContainer deposit(int slot) {
        return this;
    }
}
