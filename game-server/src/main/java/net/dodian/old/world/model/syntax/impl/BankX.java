package net.dodian.old.world.model.syntax.impl;

import lombok.Data;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.container.impl.Bank;
import net.dodian.old.world.model.syntax.EnterSyntax;

@Data
public class BankX implements EnterSyntax {

	private int itemId;
	private int slot;

	public BankX(int itemId, int slot) {
		this.itemId = itemId;
		this.slot = slot;
	}

	@Override
	public void handleSyntax(Player player, String input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleSyntax(Player player, int input) {
		if(itemId <= 0 || slot < 0 || input <= 0) {
			return;
		}
		Bank.deposit(player, itemId, slot, input);
	}
}
