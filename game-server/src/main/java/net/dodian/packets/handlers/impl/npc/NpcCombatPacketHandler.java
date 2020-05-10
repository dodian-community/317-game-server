package net.dodian.packets.handlers.impl.npc;

import net.dodian.Server;
import net.dodian.game.events.impl.player.PlayerGetCombatSpellEvent;
import net.dodian.game.events.impl.player.interact.npc.PlayerAttackNpcEvent;
import net.dodian.game.events.impl.player.interact.npc.PlayerMagicOnNpcEvent;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.combat.magic.CombatSpell;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.npc.MagicAttackNpcPacket;
import net.dodian.packets.impl.npc.MeleeAttackNpcPacket;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.logging.Level;

@Component
public class NpcCombatPacketHandler extends PacketListener {

    private Optional<NPC> getTarget(int npcIndex, Player player) {
        if(npcIndex < 0 || npcIndex > World.getNpcs().capacity()) {
            return Optional.empty();
        }

        NPC target = World.getNpcs().get(npcIndex);
        if(target == null || target.getDefinition() == null || !target.getDefinition().isAttackable()) {
            return Optional.empty();
        }

        if(target.getHitpoints() <= 0) {
            player.getMovementQueue().reset();
            return Optional.empty();
        }

        return Optional.of(target);
    }

    @PacketHandler
    public void onMeleeAttack(MeleeAttackNpcPacket packet) {
        getTarget(packet.getNpcIndex(), packet.getPlayer())
            .ifPresent(target -> {
                if(!eventsProvider.executeListeners(new PlayerAttackNpcEvent().create(packet.getPlayer(), target), Boolean.class).orElse(false)) {
                    Server.getLogger().log(Level.WARNING, "Listener prevented player from attacking NPC.");
                    return;
                }

                packet.getPlayer().getCombat().attack(target);
            });
    }

    @PacketHandler
    public void onMagicAttack(MagicAttackNpcPacket packet) {
        getTarget(packet.getNpcIndex(), packet.getPlayer())
            .ifPresent(target -> {
                if(packet.getSpellId() < 0) {
                    return;
                }

                if(!eventsProvider.executeListeners(new PlayerAttackNpcEvent().create(packet.getPlayer(), target), Boolean.class).orElse(false)) {
                    Server.getLogger().log(Level.WARNING, "Listener prevented player from attacking NPC.");
                    return;
                }

                CombatSpell spell = eventsProvider.executeListeners(new PlayerGetCombatSpellEvent().create(packet.getPlayer(), packet.getSpellId()), CombatSpell.class).orElse(null);

                if(spell == null) {
                    packet.getPlayer().getMovementQueue().reset();
                    return;
                }

                eventsProvider.executeListeners(new PlayerMagicOnNpcEvent().create(packet.getPlayer(), target, spell));

                packet.getPlayer().setPositionToFace(target.getPosition());
                packet.getPlayer().getCombat().setCastSpell(spell);
                packet.getPlayer().getCombat().attack(target);
            });
    }
}
