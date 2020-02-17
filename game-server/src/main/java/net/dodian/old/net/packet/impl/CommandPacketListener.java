package net.dodian.old.net.packet.impl;

import net.dodian.commands.Command;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.content.clan.ClanChatManager;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


/**
 * This packet listener manages commands a player uses by using the
 * command console prompted by using the "`" char.
 * 
 * @author Gabriel Hannason
 */
@Component
public class CommandPacketListener implements PacketListener {

	private final List<Command> commands;

	@Autowired
	public CommandPacketListener(List<Command> commands) {
		this.commands = commands;
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		String command = packet.readString();
		List<String> parts = new LinkedList<>(Arrays.asList(command.split(" ")));

		if(command.contains("\r") || command.contains("\n")) {
			return;
		}

		if(player == null || player.getHitpoints() <= 0) {
			return;
		}

		if(command.startsWith("/")) {
			ClanChatManager.sendMessage(player, command.substring(1));
		}

		Optional<Command> cmd =
			commands.stream()
			.filter(
				c -> c.getName().equalsIgnoreCase(parts.get(0).toLowerCase())
			)
			.findFirst();

		cmd.ifPresent(c -> {
			parts.remove(0);
			c.execute(player, parts, packet);
		});
	}

	public static final int OP_CODE = 103;
}
