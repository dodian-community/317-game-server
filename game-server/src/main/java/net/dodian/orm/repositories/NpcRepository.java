package net.dodian.orm.repositories;

import net.dodian.orm.models.definitions.NpcDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NpcRepository extends JpaRepository<NpcDefinition, Integer> {
    Optional<NpcDefinition> findByName(String name);
    List<NpcDefinition> findAllByName(String name);
}
