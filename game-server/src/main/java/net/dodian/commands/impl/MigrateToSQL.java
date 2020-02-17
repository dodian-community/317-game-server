package net.dodian.commands.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.dodian.commands.Command;
import net.dodian.commands.annotations.MaintenanceCommand;
import net.dodian.commands.annotations.Name;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.tools.JsonToSQLDefinitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Name("migrate-to-sql")
@MaintenanceCommand
@Component
public class MigrateToSQL extends Command {

    private final JsonToSQLDefinitions jsonToSQLDefinitions;

    @Autowired
    public MigrateToSQL(JsonToSQLDefinitions jsonToSQLDefinitions) {
        super();

        this.jsonToSQLDefinitions = jsonToSQLDefinitions;
    }

    @Override
    public boolean execute(Player player, List<String> parts) {
        if (!super.execute(player, parts)) {
            return false;
        }

        final ExecutorService serviceLoader = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("GameLoadingThread").build());

        player.getPacketSender().sendMessage("Migration of data started.");
        serviceLoader.execute(jsonToSQLDefinitions::migrate);

        return false;
    }
}
