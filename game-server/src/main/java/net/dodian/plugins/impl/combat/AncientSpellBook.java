package net.dodian.plugins.impl.combat;

import lombok.Getter;
import net.dodian.events.EventHandler;
import net.dodian.events.EventListener;
import net.dodian.events.impl.player.PlayerGetCombatSpellEvent;
import net.dodian.events.impl.player.interact.PlayerButtonClickEvent;
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
        SEERS(2723, 3485, 0, 13035),
        TAVERLY(2895, 3457, 0, 13045),
        ARDOUGNE(2662, 3309, 0, 13053),
        CATHERBY(2804,3434,0,13061),
        FISHING_GUILD(2597,3409,0, 13069),
        LEGENDS_GUILD(2728,3346,0,13079),
        GNOME_STRONGHOLD(2472,3438,0,13087)
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
