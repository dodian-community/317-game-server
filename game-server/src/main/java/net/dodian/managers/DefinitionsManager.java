package net.dodian.managers;

import lombok.Getter;
import net.dodian.orm.models.definitions.ItemDefinition;
import net.dodian.orm.models.definitions.NpcDefinition;
import net.dodian.orm.models.definitions.NpcDropDefinition;
import net.dodian.orm.models.definitions.NpcSpawnDefinition;
import net.dodian.orm.repositories.ItemRepository;
import net.dodian.orm.repositories.NpcDropRepository;
import net.dodian.orm.repositories.NpcRepository;
import net.dodian.orm.repositories.NpcSpawnRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Scope("singleton")
public class DefinitionsManager {
    private final ItemRepository itemRepository;
    private final NpcRepository npcRepository;
    private final NpcDropRepository npcDropRepository;
    private final NpcSpawnRepository npcSpawnRepository;

    private List<ItemDefinition> itemDefinitions = new ArrayList<>();
    private List<NpcDefinition> npcDefinitions = new ArrayList<>();
    private List<NpcDropDefinition> npcDropDefinitions = new ArrayList<>();
    private List<NpcSpawnDefinition> npcSpawnDefinitions = new ArrayList<>();

    public DefinitionsManager(
            ItemRepository itemRepository,
            NpcRepository npcRepository,
            NpcDropRepository npcDropRepository,
            NpcSpawnRepository npcSpawnRepository
    ) {
        this.itemRepository = itemRepository;
        this.npcRepository = npcRepository;
        this.npcDropRepository = npcDropRepository;
        this.npcSpawnRepository = npcSpawnRepository;
    }

    public void loadDefinitions() {
        itemDefinitions = itemRepository.findAll();
        npcDefinitions = npcRepository.findAll();
        npcDropDefinitions = npcDropRepository.findAll();
        npcSpawnDefinitions = npcSpawnRepository.findAll();
    }

    public void loadItemDefinitions() {
        itemDefinitions = itemRepository.findAll();
    }

    public ItemDefinition getItemDefinitionById(int id) {
        return this.itemDefinitions
                .stream()
                .filter(def -> def.getId() == id)
                .findFirst()
                .orElse(new ItemDefinition());
    }

    public NpcDefinition getNpcDefinitionById(int id) {
        return this.npcDefinitions
                .stream()
                .filter(def -> def.getId() == id)
                .findFirst()
                .orElse(new NpcDefinition());
    }
}
