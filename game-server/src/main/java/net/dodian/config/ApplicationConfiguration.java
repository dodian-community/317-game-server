package net.dodian.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "discord.bot")
@Component
public class ApplicationConfiguration {

    @Value("${discord.bot.token}")
    @Getter private String botToken;

    @Value("${discord.bot.prefix}")
    @Getter private String commandPrefix;

    @Value("${discord.bot.guildId}")
    @Getter private String guildId;
}
