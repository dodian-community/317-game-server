package net.dodian.packets.handlers.impl;

import net.dodian.events.EventsProvider;
import net.dodian.events.impl.player.PlayerGetCombatSpellEvent;
import net.dodian.events.impl.player.interact.npc.PlayerAttackNpcEvent;
import net.dodian.events.impl.player.interact.npc.PlayerMagicOnNpcEvent;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.combat.magic.CombatSpell;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.npc.MagicAttackNpcPacket;
import net.dodian.packets.impl.npc.MeleeAttackNpcPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NpcOptionPacketHandler implements PacketListener {

    private final EventsProvider eventsProvider;

    @Autowired
    public NpcOptionPacketHandler(EventsProvider eventsProvider) {
        this.eventsProvider = eventsProvider;
    }

    @PacketHandler
    public void onMeleeAttack(MeleeAttackNpcPacket packet) {
        if(packet.getNpcIndex() < 0 || packet.getNpcIndex() > World.getNpcs().capacity()) {
            return;
        }

        NPC target = World.getNpcs().get(packet.getNpcIndex());
        if(target == null || target.getDefinition() == null || !target.getDefinition().isAttackable()) {
            return;
        }

        if(target.getHitpoints() <= 0) {
            packet.getPlayer().getMovementQueue().reset();
            return;
        }

        // TODO: Make the attack cancellable
        eventsProvider.executeListeners(PlayerAttackNpcEvent.class, Boolean.class, packet.getPlayer(), target);

        packet.getPlayer().getCombat().attack(target);
    }

    @PacketHandler
    public void onMagicAttack(MagicAttackNpcPacket packet) {
        if(packet.getNpcIndex() < 0 || packet.getNpcIndex() > World.getNpcs().capacity() || packet.getSpellId() < 0) {
            return;
        }

        NPC target = World.getNpcs().get(packet.getNpcIndex());
        if(target == null || target.getDefinition() == null) {
            return;
        }

        if(!target.getDefinition().isAttackable()) {
            return;
        }

        if(target.getHitpoints() <= 0) {
            packet.getPlayer().getMovementQueue().reset();
            return;
        }

        CombatSpell spell = eventsProvider.executeListeners(PlayerGetCombatSpellEvent.class, CombatSpell.class,
                                packet.getPlayer(), packet.getSpellId()
                            ).orElse(null);

        if(spell == null) {
            packet.getPlayer().getMovementQueue().reset();
            return;
        }

        eventsProvider.executeListeners(PlayerMagicOnNpcEvent.class, CombatSpell.class,
                packet.getPlayer(), target, spell);

        packet.getPlayer().setPositionToFace(target.getPosition());
        packet.getPlayer().getCombat().setCastSpell(spell);
        packet.getPlayer().getCombat().attack(target);
    }
}
