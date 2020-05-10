package net.dodian.plugins.impl.dummydata;

import net.dodian.game.events.EventHandler;
import net.dodian.game.events.EventListener;
import net.dodian.game.events.impl.server.ServerStartedUpEvent;
import net.dodian.game.events.impl.server.ServerStartingUpEvent;
import net.dodian.orm.models.Account;
import net.dodian.orm.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
public class DummyAccountsPlugin implements EventListener {

    private final AccountRepository accountRepository;

    @Autowired
    public DummyAccountsPlugin(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @EventHandler
    public void onServerStarting(ServerStartingUpEvent event) {
        List<Account> accounts = new ArrayList<>();
        Account playerAccount = new Account(1, "player", 1);
        Account premiumAccount = new Account(2, "premium", 2);
        Account moderatorAccount = new Account(3, "moderator", 3);
        Account developerAccount = new Account(4, "developer", 4);
        Account adminAccount = new Account(5, "admin", 5);

        accounts.add(playerAccount);
        accounts.add(premiumAccount);
        accounts.add(moderatorAccount);
        accounts.add(developerAccount);
        accounts.add(adminAccount);

        this.accountRepository.saveAll(accounts);
    }

    @EventHandler
    public void onServerStarted(ServerStartedUpEvent event) {
        accountRepository.findAll().forEach(account -> System.out.println("Account: " + account.getUsername()));
    }
}
