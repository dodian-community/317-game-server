package net.dodian.old.world.model.syntax.impl;

import lombok.Data;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.container.impl.Bank;
import net.dodian.old.world.model.syntax.EnterSyntax;
import org.springframework.stereotype.Component;

@Component
@Data
public class BankX implements EnterSyntax {
	private final Bank bank;

	private int itemId;
	private int slotId;

	public BankX(Bank bank) {
		this.bank = bank;
	}

	@Override
	public void handleSyntax(Player player, String input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleSyntax(Player player, int input) {
		if(itemId < 0 || slotId < 0 || input <= 0) {
			return;
		}
		bank.deposit(player, itemId, slotId, input);
	}

}
