package net.dodian.commands.impl;


import net.dodian.commands.Command;
import net.dodian.commands.annotations.MaintenanceCommand;
import net.dodian.commands.annotations.Name;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Item;
import org.springframework.stereotype.Component;

import java.util.List;

@Name("Item")
@MaintenanceCommand
@Component
public class ItemCommand extends Command {

    @Override
    public boolean execute(Player player, List<String> parts) {

        if(!super.execute(player, parts)) {
            return false;
        }

        int itemId = Integer.parseInt(parts.get(0));
        int amount = Integer.parseInt(parts.get(1));

        player.getInventory().add(new Item(itemId, amount));

        return true;
    }
}
