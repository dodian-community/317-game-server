package net.dodian.discord.validators;

public class EmailValidator implements IValidator<String> {

    @Override
    public boolean isValid(String email) {
        if(email == null) {
            return false;
        }

        return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email);
    }
}
