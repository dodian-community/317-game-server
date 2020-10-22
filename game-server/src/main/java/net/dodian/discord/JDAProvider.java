package net.dodian.discord;

import net.dodian.config.ApplicationConfiguration;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JDAProvider implements InitializingBean {
    private final JDA instance;

    @Autowired
    public JDAProvider(ApplicationConfiguration configuration) throws Exception {
        JDABuilder jdaBuilder = JDABuilder.createDefault(configuration.getBotToken());
        instance = jdaBuilder.build().awaitReady();
    }

    public JDA getInstance() {
        return instance;
    }

    @Override
    public void afterPropertiesSet() {

    }
}
