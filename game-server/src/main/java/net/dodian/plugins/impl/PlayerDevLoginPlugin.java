package net.dodian.plugins.impl;

import net.dodian.events.EventHandler;
import net.dodian.events.EventListener;
import net.dodian.events.impl.player.character.CharacterLoadingEvent;
import net.dodian.events.impl.player.session.PlayerConnectEvent;
import net.dodian.managers.AccountManager;
import net.dodian.managers.GroupsManager;
import net.dodian.old.net.PlayerSession;
import net.dodian.old.net.login.LoginDetailsMessage;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.PlayerLoading;
import net.dodian.old.world.model.MagicSpellbook;
import net.dodian.orm.models.Account;
import net.dodian.orm.models.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static net.dodian.old.net.login.LoginResponses.*;

@Component
@Profile("dev")
@Scope("prototype")
public class PlayerDevLoginPlugin implements EventListener {

    private final AccountManager accountManager;
    private final PlayerLoading playerLoading;
    private final GroupsManager groupsManager;

    @Autowired
    public PlayerDevLoginPlugin(AccountManager accountManager, PlayerLoading playerLoading, GroupsManager groupsManager) {
        this.accountManager = accountManager;
        this.playerLoading = playerLoading;
        this.groupsManager = groupsManager;
    }

    @EventHandler
    public void onPlayerConnect(PlayerConnectEvent event) {
        if(event.getMessage() instanceof LoginDetailsMessage) {
            this.onLogin((LoginDetailsMessage) event.getMessage(), event.getPlayerSession());
        } else if(event.getMessage() instanceof Packet) {
            event.getPlayerSession().queuePacket((Packet) event.getMessage());
        }
    }

    public void onLogin(LoginDetailsMessage message, PlayerSession playerSession) {
        playerSession.login(message);
    }

    @EventHandler
    public Integer onCharacterLoading(CharacterLoadingEvent event) {
        /*
         * We're using the password to get the account for dev mode.
         * This is because in {@link DummyAccounts} there are some dummy accounts with username
         * respective to their access role.
         */
        Optional<Account> account = accountManager.getAccountByUsername(event.getPlayer().getPassword());
        if(account.isPresent()) {
            account.ifPresent(acc -> playerLoading.getGroups(acc, event.getPlayer()));
        } else {
            event.getPlayer().setPrimaryGroup(new Group(999, "Default"));
            groupsManager.getGroupById(1).ifPresent(event.getPlayer()::setPrimaryGroup);
        }

        Optional<Integer> loadingResponse = playerLoading.load(event.getPlayer());

        if(loadingResponse.isPresent() && loadingResponse.get().equals(NEW_ACCOUNT)) {
            event.getPlayer().setSpellbook(MagicSpellbook.ANCIENT);
        }

        if(loadingResponse.isPresent() && (loadingResponse.get() == LOGIN_SUCCESSFUL || loadingResponse.get() == NEW_ACCOUNT)) {
            return LOGIN_SUCCESSFUL;
        }

        return LOGIN_REJECT_SESSION;
    }
}
