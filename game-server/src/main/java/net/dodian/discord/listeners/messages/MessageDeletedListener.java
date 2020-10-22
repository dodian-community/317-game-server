package net.dodian.discord.listeners.messages;

import net.dodian.discord.JDAProvider;
import net.dodian.discord.listeners.ListenerAdapterBase;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class MessageDeletedListener extends ListenerAdapterBase {

    @Autowired
    public MessageDeletedListener(JDAProvider jdaProvider) {
        super(jdaProvider);
    }

    @Override
    public void onMessageDelete(@Nonnull MessageDeleteEvent event) {

    }
}
