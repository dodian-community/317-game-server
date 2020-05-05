package net.dodian.events.impl.player.interact.npc;

import lombok.Getter;
import net.dodian.events.GameEvent;
import net.dodian.events.impl.player.PlayerEvent;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

@Component
@Getter
public abstract class PlayerNpcEvent extends PlayerEvent {
    public NPC npc;

    public GameEvent create(Player player, NPC npc) {
        this.player = player;
        this.npc = npc;
        return this;
    }
}
