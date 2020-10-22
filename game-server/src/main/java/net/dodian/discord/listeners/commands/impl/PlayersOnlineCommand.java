package net.dodian.discord.listeners.commands.impl;

import net.dodian.discord.listeners.commands.CommandBase;
import net.dodian.old.world.World;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@CommandLine.Command(name = "players", description = "Check how many players are online.")
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class PlayersOnlineCommand extends CommandBase {

    @Override
    public void execute(String[] arguments) {
        getEvent().getMessage().getChannel().sendMessage("There are currently " + World.getPlayers().size() + " player(s) on the server.").queue();
    }
}