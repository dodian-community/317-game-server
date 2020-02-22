package net.dodian.old.world.model.syntax.impl;

import lombok.Data;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.container.impl.Bank;
import net.dodian.old.world.model.syntax.EnterSyntax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class WithdrawBankX implements EnterSyntax {

	private int itemId;
	private int slot;
	private int fromTab;

	private final Bank bank;

	@Autowired
	public WithdrawBankX(Bank bank) {
		this.bank = bank;
	}

	@Override
	public void handleSyntax(Player player, String input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleSyntax(Player player, int input) {
		if(itemId < 0 || slot < 0 || input <= 0) {
			return;
		}
		bank.withdraw(player, itemId, slot, input, fromTab);
	}

}
