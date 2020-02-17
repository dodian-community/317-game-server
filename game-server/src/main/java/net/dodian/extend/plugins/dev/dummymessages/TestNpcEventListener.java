package net.dodian.extend.plugins.dev.dummymessages;

import net.dodian.extend.events.npc.NpcEventListener;
import net.dodian.old.world.entity.impl.npc.NPC;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class TestNpcEventListener implements NpcEventListener {
    @Override
    public void onDeath(NPC npc) {
        System.out.println("Testing NPC death event.");
    }

    @Override
    public void onSpawn(NPC npc) {
        System.out.println("Testing NPC spawn event.");
    }
}
