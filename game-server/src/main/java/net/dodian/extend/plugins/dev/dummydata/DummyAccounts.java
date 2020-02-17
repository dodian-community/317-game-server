package net.dodian.extend.plugins.dev.dummydata;

import net.dodian.extend.events.system.ServerEventListener;
import net.dodian.orm.models.Account;
import net.dodian.orm.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
public class DummyAccounts implements ServerEventListener {

    private final AccountRepository accountRepository;

    @Autowired
    public DummyAccounts(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void onStartedUp() {
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

    @Override
    public void onStartup() {

    }

    @Override
    public void onShutdown() {

    }
}
