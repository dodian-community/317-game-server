package net.dodian.extend.events.player;

import net.dodian.extend.events.EventListener;
import net.dodian.old.net.PlayerSession;
import net.dodian.old.net.login.LoginDetailsMessage;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

@Component
public interface PlayerSessionEventListener extends EventListener {
    void onInitialize(PlayerSession playerSession, Object msg);
    void onLogin(LoginDetailsMessage loginDetailsMessage, PlayerSession session);
    void onLoginSuccessful(Player player);
    void onLogout(Player player);
    void onLogoutSuccessful(Player player);
}
