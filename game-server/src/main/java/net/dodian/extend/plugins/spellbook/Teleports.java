package net.dodian.extend.plugins.spellbook;

import net.dodian.GameConstants;
import net.dodian.extend.events.controllers.ButtonClickEventListener;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Position;
import net.dodian.old.world.model.teleportation.TeleportHandler;
import net.dodian.old.world.model.teleportation.TeleportType;
import org.springframework.stereotype.Component;

import static net.dodian.GameConstants.DEFAULT_POSITION;

@Component
public class Teleports implements ButtonClickEventListener {

    @Override
    public void onClick(Player player, int button) {

        if(button == 39101) {
            TeleportHandler.teleport(player, DEFAULT_POSITION, TeleportType.PURO_PURO);
        }
    }
}
