package net.dodian.discord.listeners.commands.impl;

import lombok.SneakyThrows;
import net.dodian.discord.listeners.commands.CommandBase;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

import java.net.InetAddress;

@Command(name = "host", description = "Check who's hosting the bot instance.")
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class CheckHostCommand extends CommandBase {

    @SneakyThrows
    @Override
    public void execute(String[] arguments) {
        if(getEvent().getMessage().getMember().getRoles().contains(getEvent().getGuild().getRoleById("503700324473896960"))) {
            InetAddress addr = InetAddress.getLocalHost();
            getEvent().getMessage().getChannel().sendMessage("Host: " + addr.getHostName()).queue();
        } else {
            getEvent().getMessage().getChannel().sendMessage("Permission denied!").queue();
        }
    }
}