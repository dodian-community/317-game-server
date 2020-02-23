package net.dodian.extend.plugins.core.spawns;

import net.dodian.extend.events.system.ServerEventListener;
import net.dodian.managers.NpcSpawnsManager;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YanilleNpcSpawns implements ServerEventListener {

    private final NpcSpawnsManager npcSpawnsManager;

    @Autowired
    public YanilleNpcSpawns(NpcSpawnsManager npcSpawnsManager) {
        this.npcSpawnsManager = npcSpawnsManager;
    }

    @Override
    public void onStartup() {

    }

    @Override
    public void onShutdown() {

    }

    @Override
    public void onStartedUp() {
        npcSpawnsManager.addAndSpawnNpc(new NPC(3086, new Position(2599,3103, 0)));
    }
}
