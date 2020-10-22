package net.dodian.discord.listeners.guild;

import net.dodian.discord.JDAProvider;
import net.dodian.discord.listeners.ListenerAdapterBase;
import net.dodian.discord.validators.IValidator;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class GuildMemberListener extends ListenerAdapterBase {

    private IValidator<String> usernameValidator;

    @Autowired
    public GuildMemberListener(JDAProvider jdaProvider, @Qualifier("usernameValidator") IValidator<String> usernameValidator) {
        super(jdaProvider);
        this.usernameValidator = usernameValidator;
    }

    @Override
    public void onGuildMemberRoleAdd(@Nonnull GuildMemberRoleAddEvent event) {
    }

    @Override
    public void onGuildMemberRoleRemove(@Nonnull GuildMemberRoleRemoveEvent event) {
    }

    @Override
    public void onGuildMemberUpdateNickname(@Nonnull GuildMemberUpdateNicknameEvent event) {
    }
}
