package net.dodian.extend.plugins.dev.dummymessages;

import net.dodian.extend.events.player.PlayerSessionEventListener;
import net.dodian.old.net.PlayerSession;
import net.dodian.old.net.login.LoginDetailsMessage;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class TestPlayerSessionEventListener implements PlayerSessionEventListener {

    @Override
    public void onInitialize(PlayerSession playerSession, Object msg) {
        System.out.println("Testing player session initialization...");
    }

    @Override
    public void onLogin(LoginDetailsMessage loginDetailsMessage, PlayerSession session) {
        System.out.println("Testing player on login...");
    }

    @Override
    public void onLoginSuccessful(Player player) {
        System.out.println("Testing player successful login event...");
    }

    @Override
    public void onLogout(Player player) {
        System.out.println("Testing player logout event...");
    }

    @Override
    public void onLogoutSuccessful(Player player) {
        System.out.println("Testing player logout successful event...");
    }
}
