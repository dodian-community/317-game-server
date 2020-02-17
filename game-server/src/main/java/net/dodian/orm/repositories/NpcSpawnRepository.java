package net.dodian.orm.repositories;

import net.dodian.orm.models.definitions.NpcSpawnDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NpcSpawnRepository extends JpaRepository<NpcSpawnDefinition, Integer> {
    List<NpcSpawnDefinition> findAllByNpcId(int npcId);
}
