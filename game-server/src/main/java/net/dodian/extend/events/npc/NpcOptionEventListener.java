package net.dodian.extend.events.npc;

import net.dodian.extend.events.EventListener;
import net.dodian.old.net.packet.impl.NPCOptionPacketListener.*;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

@Component
public interface NpcOptionEventListener extends EventListener {
    void onFirstClick(Player player, ClickEvent clickEvent);
    void onSecondClick(Player player, ClickEvent clickEvent);
    void onThirdClick(Player player, ClickEvent clickEvent);
    void onFourthClick(Player player, ClickEvent clickEvent);
    void onAttackNpc(Player player, AttackEvent attackEvent);
    void onMageNpc(Player player, AttackEvent attackEvent);
}
