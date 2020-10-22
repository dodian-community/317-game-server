package net.dodian.discord.listeners;

import net.dodian.discord.JDAProvider;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.InitializingBean;

public class ListenerAdapterBase extends ListenerAdapter implements InitializingBean {

    protected JDAProvider jdaProvider;

    public ListenerAdapterBase(JDAProvider jdaProvider) {
        this.jdaProvider = jdaProvider;
    }

    @Override
    public void afterPropertiesSet() {
        this.jdaProvider.getInstance().addEventListener(this);
    }
}
