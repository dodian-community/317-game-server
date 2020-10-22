package net.dodian.discord.validators;

public interface IValidator<T> {
    boolean isValid(T input);
}
