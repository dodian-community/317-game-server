package net.dodian.old.net.packet.impl;

import lombok.Data;
import net.dodian.extend.events.npc.NpcOptionEventListener;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketConstants;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.combat.CombatType;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.PlayerRights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static net.dodian.old.world.entity.combat.CombatType.MAGIC;
import static net.dodian.old.world.entity.combat.CombatType.MELEE;

@Component
public class NPCOptionPacketListener implements PacketListener {

    private final List<NpcOptionEventListener> npcOptionEvents;

    @Autowired
    public NPCOptionPacketListener(List<NpcOptionEventListener> npcOptionEvents) {
        this.npcOptionEvents = npcOptionEvents;
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player == null || player.getHitpoints() <= 0) {
            return;
        }

        if (player.busy()) {
            return;
        }

        Optional<ClickEvent> clickEvent = handleClick(player, packet);
        switch (packet.getOpcode()) {
            case PacketConstants.ATTACK_NPC_OPCODE:
                handleAttack(player, packet, MELEE).ifPresent(attackEvent -> {
                    npcOptionEvents.forEach(event -> event.onAttackNpc(player, attackEvent));
                });
                break;
            case PacketConstants.MAGE_NPC_OPCODE:
                handleAttack(player, packet, MAGIC).ifPresent(attackEvent -> {
                    npcOptionEvents.forEach(event -> event.onMageNpc(player, attackEvent));
                });
                break;
            case PacketConstants.FIRST_CLICK_OPCODE:
                clickEvent.ifPresent(click -> npcOptionEvents.forEach(event -> event.onFirstClick(player, click)));
                break;
            case PacketConstants.SECOND_CLICK_OPCODE:
                clickEvent.ifPresent(click -> npcOptionEvents.forEach(event -> event.onSecondClick(player, click)));
                break;
            case PacketConstants.THIRD_CLICK_OPCODE:
                clickEvent.ifPresent(click -> npcOptionEvents.forEach(event -> event.onThirdClick(player, click)));
                break;
            case PacketConstants.FOURTH_CLICK_OPCODE:
                clickEvent.ifPresent(click -> npcOptionEvents.forEach(event -> event.onFourthClick(player, click)));
                break;
        }
    }

    private Optional<AttackEvent> handleAttack(Player player, Packet packet, CombatType combatType) {
        int npcIndex = 0;
        int spellId = 0;

        if(combatType.equals(MELEE)) {
            npcIndex = packet.readShortA();
        } else if(combatType.equals(MAGIC)) {
            npcIndex = packet.readLEShortA();
            spellId = packet.readShortA();
        }

        if(npcIndex < 0 || npcIndex > World.getNpcs().capacity() || (combatType.equals(MAGIC) && spellId < 0)) {
            return Optional.empty();
        }

        final NPC target = World.getNpcs().get(npcIndex);
        if(target == null
        || target.getDefinition() == null
        || !target.getDefinition().isAttackable()) {
            return Optional.empty();
        }

        if(target.getHitpoints() <= 0) {
            player.getMovementQueue().reset();
            return Optional.empty();
        }

        return Optional.of(new AttackEvent(target, spellId));
    }

    private static Optional<ClickEvent> handleClick(Player player, Packet packet) {
        int index = packet.readLEShort();

        if (index < 0 || index > World.getNpcs().capacity()) {
            return Optional.empty();
        }

        final NPC npc = World.getNpcs().get(index);

        if (npc == null) {
            return Optional.empty();
        }

        player.setEntityInteraction(npc);

        if (player.getRights() == PlayerRights.ADMINISTRATOR) {
            player.getPacketSender().sendMessage("Click npc id: " + npc.getId());
        }

        return Optional.of(new ClickEvent(npc));
    }

	@Data
	public static class AttackEvent {
		private NPC npc;
		private int spellId;

		public AttackEvent(NPC npc, int spellId) {
			this.npc = npc;
			this.spellId = spellId;
		}
	}

	@Data
	public static class ClickEvent {
		private NPC npc;

		public ClickEvent(NPC npc) {
			this.npc = npc;
		}
	}

    /**
     * NPCS
     **/
    private static final int EMBLEM_TRADER = 315;
    private static final int MAKE_OVER_MAGE = 1306;
    private static final int FINANCIAL_ADVISOR = 3310;
    private static final int PERDU = 7456;
    private static final int PARTY_PETE = 5792;
    private static final int SHOP_KEEPER = 506;
}
