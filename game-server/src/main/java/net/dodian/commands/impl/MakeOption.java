package net.dodian.commands.impl;

import net.dodian.commands.Command;
import net.dodian.commands.annotations.MaintenanceCommand;
import net.dodian.commands.annotations.Name;
import net.dodian.old.net.packet.PacketSender;
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
        //player.getPacketSender().sendMakeItemOption(4151);
        //System.out.println("test: " + PacketSender.itemOptions.get(0));
        //player.getPacketSender().sendMakeItemOption("What would you like to make?", new int[]{4151, 1048});
        player.getPacketSender().sendMakeItemOption(new int[]{4151, 1040, 1048});
        player.getPacketSender().sendMessage("testing opening option interface...");
        return true;
    }
}
