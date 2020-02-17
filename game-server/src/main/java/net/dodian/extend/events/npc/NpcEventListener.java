package net.dodian.extend.events.npc;

import net.dodian.extend.events.EventListener;
import net.dodian.old.world.entity.impl.npc.NPC;

public interface NpcEventListener extends EventListener {
    void onDeath(NPC npc);
    void onSpawn(NPC npc);
}
