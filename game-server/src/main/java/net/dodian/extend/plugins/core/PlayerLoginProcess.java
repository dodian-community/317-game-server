package net.dodian.extend.plugins.core;

import com.google.gson.JsonObject;
import net.dodian.extend.events.player.PlayerSessionAndCharacterLoadEventListener;
import net.dodian.http.HttpClient;
import net.dodian.managers.AccountManager;
import net.dodian.old.net.PlayerSession;
import net.dodian.old.net.login.LoginDetailsMessage;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.entity.impl.player.PlayerLoading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static net.dodian.old.net.login.LoginResponses.*;

@Component
@Profile("prod")
@Scope("prototype")
public class PlayerLoginProcess implements PlayerSessionAndCharacterLoadEventListener {

    private final AccountManager accountManager;
    private final PlayerLoading playerLoading;
    private final HttpClient httpClient;

    @Autowired
    public PlayerLoginProcess(AccountManager accountManager, PlayerLoading playerLoading, HttpClient httpClient) {
        this.accountManager = accountManager;
        this.playerLoading = playerLoading;
        this.httpClient = httpClient;
    }

    @Override
    public void onInitialize(PlayerSession session, Object msg) {
        if(msg instanceof LoginDetailsMessage) {
            this.onLogin((LoginDetailsMessage) msg, session);
        } else if(msg instanceof Packet) {
            session.queuePacket((Packet) msg);
        }
    }

    @Override
    public void onLogin(LoginDetailsMessage loginDetailsMessage, PlayerSession session) {
        session.login(loginDetailsMessage, this);
    }

    @Override
    public Optional<Integer> onCharacterLoad(Player player) {
        // TODO: Load this URL from a config
        httpClient.setServer("https://api.dodian.net");

        JsonObject userDetails = new JsonObject();
        userDetails.addProperty("username", player.getUsername());
        userDetails.addProperty("password", player.getPassword());

        ResponseEntity<String> response = httpClient.post("/login", userDetails.toString(), String.class);

        if(response.getStatusCode().is4xxClientError()) {
            return Optional.of(LOGIN_INVALID_CREDENTIALS);
        }

        accountManager.getAccountByUsername(player.getUsername())
                .ifPresent(account -> playerLoading.getGroups(account, player));

        Optional<Integer> loadingResponse = playerLoading.load(player);

        if(loadingResponse.isPresent() && (loadingResponse.get() == LOGIN_SUCCESSFUL || loadingResponse.get() == NEW_ACCOUNT)) {
            return Optional.of(LOGIN_SUCCESSFUL);
        }

        return Optional.empty();
    }

    @Override
    public void onLoginSuccessful(Player player) {

    }

    @Override
    public void onLogout(Player player) {

    }

    @Override
    public void onLogoutSuccessful(Player player) {

    }
}
