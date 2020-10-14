package net.dodian.old.world.model.syntax.impl;

import lombok.Data;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.container.impl.Bank;
import net.dodian.old.world.model.syntax.EnterSyntax;

@Data
public class WithdrawBankX implements EnterSyntax {

	private int itemId;
	private int slot;
	private int fromTab;

	public WithdrawBankX(int itemId, int slot, int fromTab) {
		this.itemId = itemId;
		this.slot = slot;
		this.fromTab = fromTab;
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
		Bank.withdraw(player, itemId, slot, input, fromTab);
	}

}
