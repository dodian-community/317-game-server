package net.dodian.commands;

import net.dodian.commands.impl.MigrateToSQL;

import java.util.HashMap;

public class CommandManager {
    public static HashMap<String, Command> commands = new HashMap<>();

    public static void register(Command command) {
        commands.put(command.getName().toLowerCase(), command);
    }

    public static void initialize() {
    }
}
