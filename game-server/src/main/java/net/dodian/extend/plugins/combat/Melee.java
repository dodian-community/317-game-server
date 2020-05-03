package net.dodian.extend.plugins.combat;

import net.dodian.extend.events.npc.NpcOptionEventListener;
import net.dodian.old.net.packet.impl.NPCOptionPacketListener.AttackEvent;
import net.dodian.old.net.packet.impl.NPCOptionPacketListener.ClickEvent;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

@Component
public class Melee implements NpcOptionEventListener {

    @Override
    public void onFirstClick(Player player, ClickEvent clickEvent) {

    }

    @Override
    public void onSecondClick(Player player, ClickEvent clickEvent) {

    }

    @Override
    public void onThirdClick(Player player, ClickEvent clickEvent) {

    }

    @Override
    public void onFourthClick(Player player, ClickEvent clickEvent) {

    }

    @Override
    public void onAttackNpc(Player player, AttackEvent attackEvent) {
        int index = attackEvent.getNpc().getIndex();
        if(index < 0 || index > World.getNpcs().capacity()) {
            return;
        }

        final NPC interact = World.getNpcs().get(attackEvent.getNpc().getIndex());

        if(interact == null || interact.getDefinition() == null) {
            return;
        }

        if(!interact.getDefinition().isAttackable()) {
            return;
        }

        if(interact.getHitpoints() <= 0) {
            player.getMovementQueue().reset();
            return;
        }

        player.getCombat().attack(interact);
    }

    @Override
    public void onMageNpc(Player player, AttackEvent attackEvent) {

    }
}
