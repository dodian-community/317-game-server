package net.dodian.discord.listeners.commands;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.InitializingBean;
import picocli.CommandLine.*;

public abstract class CommandBase implements InitializingBean {
    @Getter @Setter boolean picoCliCommand;
    @Getter private String name;
    @Getter private String description;
    @Getter @Setter private MessageReceivedEvent event;

    public abstract void execute(String[] arguments);

    @Override
    public void afterPropertiesSet() {
        if(this.getClass().isAnnotationPresent(Command.class)) {
            this.name = this.getClass().getAnnotation(Command.class).name();
            this.description = this.getClass().getAnnotation(Command.class).description()[0];
        }
    }

    public void reply(String message) {
        reply(message, false);
    }

    public void reply(String message, boolean mention) {
        if(mention) {
            message = this.event.getAuthor().getAsMention() + ": " + message;
        }

        this.event.getChannel().sendMessage(message).queue();
    }
}
