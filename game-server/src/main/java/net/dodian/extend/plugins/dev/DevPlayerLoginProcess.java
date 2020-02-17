package net.dodian.extend.plugins.dev;

import net.dodian.extend.events.player.PlayerSessionAndCharacterLoadEventListener;
import net.dodian.extend.plugins.dev.dummydata.DummyAccounts;
import net.dodian.managers.AccountManager;
import net.dodian.old.net.PlayerSession;
import net.dodian.old.net.login.LoginDetailsMessage;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.entity.impl.player.PlayerLoading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static net.dodian.old.net.login.LoginResponses.LOGIN_SUCCESSFUL;

@Component
@Profile("dev")
@Scope("prototype")
public class DevPlayerLoginProcess implements PlayerSessionAndCharacterLoadEventListener {

    private final AccountManager accountManager;
    private final PlayerLoading playerLoading;

    @Autowired
    public DevPlayerLoginProcess(AccountManager accountManager, PlayerLoading playerLoading) {
        this.accountManager = accountManager;
        this.playerLoading = playerLoading;
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
        /**
         * We're using the password to get the account for dev mode.
         * This is because in {@link DummyAccounts} there are some dummy accounts with usernames
         * respective to their access role.
         */
        accountManager.getAccountByUsername(player.getPassword())
                .ifPresent(account -> playerLoading.getGroups(account, player));

        Optional<Integer> loadingResponse = playerLoading.load(player);

        if(loadingResponse.isPresent() && loadingResponse.get() == LOGIN_SUCCESSFUL) {
            return Optional.of(LOGIN_SUCCESSFUL);
        }

        return Optional.empty();
    }

    @Override
    public void onLoginSuccessful(Player player) {
        player.getPacketSender().sendMessage("You logged in as: " + player.getPrimaryGroup().getTitle());
        player.getPacketSender().sendMessage("This was possible because you're running the server in debug mode.");
    }

    @Override
    public void onLogout(Player player) {

    }

    @Override
    public void onLogoutSuccessful(Player player) {

    }
}
