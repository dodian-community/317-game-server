package net.dodian.discord.validators;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component("usernameValidator")
public class UsernameValidator implements IValidator<String> {

    @Override
    public boolean isValid(String username) {
        if(username == null) {
            return false;
        }

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        return !pattern.matcher(username).find();
    }
}
