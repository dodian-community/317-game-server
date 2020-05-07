package net.dodian.tools;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Setter;
import net.dodian.GameConstants;
import net.dodian.Server;
import net.dodian.managers.DefinitionsManager;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Position;
import net.dodian.orm.models.definitions.ItemDefinition;
import net.dodian.orm.models.definitions.NpcDefinition;
import net.dodian.orm.models.definitions.NpcSpawnDefinition;
import net.dodian.orm.repositories.ItemRepository;
import net.dodian.orm.repositories.NpcRepository;
import net.dodian.orm.repositories.NpcSpawnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@Setter
public class JsonToSQLDefinitions {

    private final ObjectMapper mapper;
    private final ItemRepository itemRepository;
    private final NpcRepository npcRepository;
    private final NpcSpawnRepository npcSpawnRepository;
    private final DefinitionsManager definitionsManager;

    private Player player;

    @Autowired
    public JsonToSQLDefinitions(ObjectMapper mapper, ItemRepository itemRepository, NpcRepository npcRepository, NpcSpawnRepository npcSpawnRepository, DefinitionsManager definitionsManager) {
        this.mapper = mapper.findAndRegisterModules()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        this.itemRepository = itemRepository;
        this.npcRepository = npcRepository;
        this.npcSpawnRepository = npcSpawnRepository;
        this.definitionsManager = definitionsManager;
    }

    public void migrate() {
        migrateItems();
        migrateNpcs();
        migrateNpcSpawns();
    }

    private void migrateItems() {
        String itemDefinitionsJson = GameConstants.DEFINITIONS_DIRECTORY + "items.json";

        if(!new File(itemDefinitionsJson).exists()) {
            logAndInformIssuer("Couldn't find a items.json to migrate.");
            return;
        }

        logAndInformIssuer("Migrating item definitions from JSON to SQL.");

        try {
            List<ItemDefinition> itemDefinitions = mapper.readValue(new File(itemDefinitionsJson), new TypeReference<List<ItemDefinition>>() {});
            itemRepository.saveAll(itemDefinitions);
            logAndInformIssuer("Migrated item definitions successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void migrateNpcs() {
        String npcDefinitionsJson = GameConstants.DEFINITIONS_DIRECTORY + "npc_defs.json";

        if(!new File(npcDefinitionsJson).exists()) {
            logAndInformIssuer("Couldn't find a npc_defs.json to migrate.");
            return;
        }

        logAndInformIssuer("Migrating npc definitions from JSON to SQL.");

        try {
            List<NpcDefinition> npcDefinitions = mapper.readValue(new File(npcDefinitionsJson), new TypeReference<List<NpcDefinition>>() {});
            npcRepository.saveAll(npcDefinitions);
            logAndInformIssuer("Migrated npc definitions successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void migrateNpcSpawns() {
        String npcSpawnDefinitionsJson = GameConstants.DEFINITIONS_DIRECTORY + "npc_spawns.json";

        if(!new File(npcSpawnDefinitionsJson).exists()) {
            logAndInformIssuer("Couldn't find npc_spawns.json to migrate");
            return;
        }

        logAndInformIssuer("Migrating npc spawn definitions from JSON to SQL.");

        try {
            List<NpcSpawnDefinition> npcSpawnDefinitions = mapper.readValue(new File(npcSpawnDefinitionsJson), new TypeReference<>() {});
            npcSpawnRepository.saveAll(npcSpawnDefinitions);

            npcSpawnDefinitions.forEach(spawnDefinition -> World.getNpcAddQueue().add(
                new NPC(
                    spawnDefinition.getNpcId(),
                    new Position(spawnDefinition.getX(), spawnDefinition.getY(), spawnDefinition.getZ()),
                    "WEST",
                    this.definitionsManager)
                )
            );
            logAndInformIssuer("Migrated npc spawn definitions successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logAndInformIssuer(String message) {
        Server.getLogger().info(message);

        if(player != null) {
            player.getPacketSender().sendMessage(message);
        }
    }
}
