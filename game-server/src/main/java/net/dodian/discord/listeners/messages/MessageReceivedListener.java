package net.dodian.discord.listeners.messages;

import net.dodian.config.ApplicationConfiguration;
import net.dodian.discord.JDAProvider;
import net.dodian.discord.listeners.ListenerAdapterBase;
import net.dodian.discord.listeners.commands.CommandProvider;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class MessageReceivedListener extends ListenerAdapterBase {

    private final ApplicationContext context;
    private final ApplicationConfiguration configuration;

    @Autowired
    public MessageReceivedListener(ApplicationContext context, ApplicationConfiguration configuration, JDAProvider provider) {
        super(provider);
        this.context = context;
        this.configuration = configuration;
    }


    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if(event.getMessage().getContentRaw().startsWith(configuration.getCommandPrefix())) {
            String output = context.getBean(CommandProvider.class).parseCommand(event);
            if (output != null && !output.isEmpty()) {
                event.getTextChannel().sendMessage(output).queue();
            }
        }
    }
}
