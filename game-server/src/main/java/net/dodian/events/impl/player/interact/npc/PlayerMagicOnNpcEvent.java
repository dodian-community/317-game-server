package net.dodian.events.impl.player.interact.npc;

import net.dodian.events.GameEvent;
import net.dodian.old.world.entity.combat.magic.CombatSpell;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerMagicOnNpcEvent extends PlayerNpcEvent {
    private CombatSpell spell;

    public GameEvent create(Player player, NPC npc, CombatSpell spell) {
        super.create(player, npc);
        this.spell = spell;
        return this;
    }
}
