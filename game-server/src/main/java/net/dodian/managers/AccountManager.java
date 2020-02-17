package net.dodian.managers;

import lombok.Getter;
import net.dodian.orm.models.Account;
import net.dodian.orm.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Scope("singleton")
@Component
@Getter
public class AccountManager {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountManager(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}
