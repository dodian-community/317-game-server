package net.dodian.commands.impl;

import net.dodian.commands.Command;
import net.dodian.commands.annotations.MaintenanceCommand;
import net.dodian.commands.annotations.Name;
import net.dodian.old.world.model.*;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

import java.util.List;

@Name("make")
@MaintenanceCommand
@Component
public class MakeOption extends Command {

    @Override
    public boolean execute(Player player, List<String> parts) {
        if (!super.execute(player, parts)) {
            return false;
        }
        player.getPacketSender().sendMakeItemOption(new Item[]{new Item(4151), new Item(1040), new Item(1048)});
        player.getPacketSender().sendMessage("testing opening option interface...");
        return true;
    }
}
