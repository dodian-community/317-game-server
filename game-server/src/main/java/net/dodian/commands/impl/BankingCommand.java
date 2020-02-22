package net.dodian.commands.impl;

import net.dodian.commands.Command;
import net.dodian.commands.annotations.MaintenanceCommand;
import net.dodian.commands.annotations.Name;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

import java.util.List;

@Name("Bank")
@MaintenanceCommand
@Component
public class BankingCommand extends Command {

    @Override
    public boolean execute(Player player, List<String> parts) {
        if(!super.execute(player, parts)) {
            return false;
        }

        player.getBank(player.getCurrentBankTab()).open();

        return true;
    }
}
