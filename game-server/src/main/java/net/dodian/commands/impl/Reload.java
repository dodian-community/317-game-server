package net.dodian.commands.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.dodian.commands.Command;
import net.dodian.commands.annotations.MaintenanceCommand;
import net.dodian.commands.annotations.Name;
import net.dodian.managers.DefinitionsManager;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Name("Reload")
@MaintenanceCommand
@Component
public class Reload extends Command {

    private final DefinitionsManager definitionsManager;

    @Autowired
    public Reload(DefinitionsManager definitionsManager) {
        this.definitionsManager = definitionsManager;
    }

    @Override
    public boolean execute(Player player, List<String> parts) {
        if(!super.execute(player, parts)) {
            return false;
        }

        if(parts.size() >= 1) {
            final ExecutorService serviceLoader = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("GameLoadingThread").build());

            if(parts.get(0).equalsIgnoreCase("items")) {
                serviceLoader.execute(definitionsManager::loadItemDefinitions);
            }
        }

        return true;
    }
}
