package net.dodian.commands.impl;

import net.dodian.commands.Command;
import net.dodian.commands.annotations.MaintenanceCommand;
import net.dodian.commands.annotations.Name;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Position;
import net.dodian.old.world.model.teleportation.TeleportHandler;
import net.dodian.old.world.model.teleportation.TeleportType;
import org.springframework.stereotype.Component;

import java.util.List;

@Name("tele")
@MaintenanceCommand
@Component
public class TeleportCommand extends Command {

    @Override
    public boolean execute(Player player, List<String> parts) {
        if(!super.execute(player, parts)) {
            return false;
        }

        if(parts.size() < 2) {
            return false;
        }

        int x = Integer.parseInt(parts.get(0));
        int y = Integer.parseInt(parts.get(1));
        int z = parts.size() > 2 ? Integer.parseInt(parts.get(2)) : 0;

        TeleportHandler.teleport(player, new Position(x, y, z), TeleportType.PURO_PURO);

        return true;
    }
}
