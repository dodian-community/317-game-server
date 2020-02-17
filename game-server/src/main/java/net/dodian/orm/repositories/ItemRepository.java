package net.dodian.orm.repositories;

import net.dodian.orm.models.definitions.ItemDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemDefinition, Integer> {
    Optional<ItemDefinition> findByName(String name);
}
