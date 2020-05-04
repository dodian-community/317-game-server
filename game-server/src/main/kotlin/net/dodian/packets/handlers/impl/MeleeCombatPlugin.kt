package net.dodian.packets.handlers.impl

import net.dodian.events.EventsProvider
import net.dodian.events.impl.player.interact.item.PlayerItemFirstClickEvent
import net.dodian.events.impl.player.interact.npc.PlayerAttackNpcEvent
import net.dodian.old.world.World
import net.dodian.old.world.entity.impl.npc.NPC
import net.dodian.packets.handlers.PacketHandler
import net.dodian.packets.handlers.PacketListener
import net.dodian.packets.impl.item.ItemActionPacket
import net.dodian.packets.impl.npc.MeleeAttackNpcPacket
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

class MeleeCombatPlugin