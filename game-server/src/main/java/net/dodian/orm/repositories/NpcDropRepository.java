package net.dodian.orm.repositories;

import net.dodian.orm.models.definitions.NpcDropDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NpcDropRepository extends JpaRepository<NpcDropDefinition, Integer> {
    List<NpcDropDefinition> findAllByNpcId(int npcId);
    List<NpcDropDefinition> findAllByItemId(int itemId);
}
