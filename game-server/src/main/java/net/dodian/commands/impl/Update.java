package net.dodian.commands.impl;


import net.dodian.Server;
import net.dodian.commands.Command;
import net.dodian.commands.annotations.MaintenanceCommand;
import net.dodian.commands.annotations.Name;
import net.dodian.old.engine.task.Task;
import net.dodian.old.engine.task.TaskManager;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

import java.util.List;

@Name("Update")
@MaintenanceCommand
@Component
public class Update extends Command {

    @Override
    public boolean execute(Player player, List<String> parts) {
        if(super.execute(player, parts)) {
            return false;
        }

        int time = 60;
        if(parts.size() > 1) {
            time = Integer.parseInt(parts.get(0));
        }

        if(time > 0) {
            Server.setUpdating(true);
            for (Player players : World.getPlayers()) {
                if (players == null)
                    continue;
                players.getPacketSender().sendSystemUpdate(time);
            }
            TaskManager.submit(new Task(time) {
                @Override
                protected void execute() {
                    for (Player player : World.getPlayers()) {
                        if (player != null) {
                            player.logout();
                        }
                    }
                    Server.getLogger().info("Update task finished!");
                    stop();
                }
            });
        }

        return true;
    }
}