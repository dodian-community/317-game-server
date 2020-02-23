package net.dodian.managers;

import lombok.Getter;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.model.Position;
import net.dodian.orm.models.definitions.NpcSpawnDefinition;
import net.dodian.orm.repositories.NpcSpawnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
@Getter
public class NpcSpawnsManager {

    private NpcSpawnRepository npcSpawnRepository;

    @Autowired
    public NpcSpawnsManager(NpcSpawnRepository npcSpawnRepository) {
        this.npcSpawnRepository = npcSpawnRepository;
    }

    public void addAndSpawnNpc(NPC npc) {
        NpcSpawnDefinition npcSpawnDefinition = new NpcSpawnDefinition();
        npcSpawnDefinition.setNpcId(npc.getId());
        npcSpawnDefinition.setX(npc.getPosition().getX());
        npcSpawnDefinition.setY(npc.getPosition().getY());
        npcSpawnDefinition.setZ(npc.getPosition().getZ());

        npcSpawnRepository.save(npcSpawnDefinition);
        World.getNpcAddQueue().add(npc);
    }

    public void loadDefinitions() {
        npcSpawnRepository.findAll().forEach(ncpSpawn -> {
            Position position = new Position(ncpSpawn.getX(), ncpSpawn.getY(), ncpSpawn.getZ());
            World.getNpcAddQueue().add(new NPC(ncpSpawn.getId(), position));
        });
    }
}
