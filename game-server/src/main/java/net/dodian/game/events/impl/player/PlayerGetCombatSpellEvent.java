package net.dodian.game.events.impl.player;

import lombok.Getter;
import net.dodian.game.events.GameEvent;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PlayerGetCombatSpellEvent extends PlayerEvent {
    private int spellId;

    public GameEvent create(Player player, int spellId) {
        super.create(player);
        this.spellId = spellId;
        return this;
    }
}
