package net.dodian.events.impl.player.interact.npc;

import net.dodian.events.GameEvent;
import net.dodian.events.impl.player.PlayerEvent;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

@Component
public abstract class PlayerNpcEvent extends PlayerEvent {
    protected NPC npc;

    public GameEvent create(Player player, NPC npc) {
        this.player = player;
        this.npc = npc;
        return this;
    }
}
