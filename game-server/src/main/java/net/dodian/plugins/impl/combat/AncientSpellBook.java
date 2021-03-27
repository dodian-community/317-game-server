package net.dodian.plugins.impl.combat;

import lombok.Getter;
import net.dodian.game.events.EventHandler;
import net.dodian.game.events.EventListener;
import net.dodian.game.events.impl.player.PlayerGetCombatSpellEvent;
import net.dodian.game.events.impl.player.interact.PlayerButtonClickEvent;
import net.dodian.old.world.entity.combat.magic.CombatSpell;
import net.dodian.old.world.model.Position;
import net.dodian.old.world.model.teleportation.TeleportHandler;
import net.dodian.old.world.model.teleportation.TeleportType;
import net.dodian.plugins.Plugin;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AncientSpellBook implements Plugin, EventListener {

    @EventHandler
    public CombatSpell getCombatSpell(PlayerGetCombatSpellEvent event) {
        return null;
    }

    @EventHandler
    public void onTeleport(PlayerButtonClickEvent event) {
        Arrays.stream(Locations.values())
                .filter(location -> location.getButton() == event.getButton())
                .findFirst()
                .ifPresent(location -> TeleportHandler.teleport(event.getPlayer(), new Position(location.getX(), location.getY(), location.getZ()), TeleportType.ANCIENT));
    }

    @Getter
    private enum Locations {
        YANILLE(2606, 3102, 0, 21741),
        SEERS(2534, 3229, 0, 13035),
        TAVERLY(2443, 3134, 0, 13045),
        ARDOUGNE(2662, 3309, 0, 13053),
        CATHERBY(2343,3115,0,13061),
        FISHING_GUILD(2601,3212,0, 13069),
        LEGENDS_GUILD(2536,3156,0,13079),
        GNOME_STRONGHOLD(2592,3179,0,13087)
        ;

        private int x;
        private int y;
        private int z;
        private int button;

        Locations(int x, int y, int z, int button) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.button = button;
        }
    }
}
