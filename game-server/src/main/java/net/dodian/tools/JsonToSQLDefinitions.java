package net.dodian.tools;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Setter;
import net.dodian.GameConstants;
import net.dodian.Server;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.orm.models.definitions.ItemDefinition;
import net.dodian.orm.models.definitions.NpcDefinition;
import net.dodian.orm.repositories.ItemRepository;
import net.dodian.orm.repositories.NpcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Component
@Setter
public class JsonToSQLDefinitions {

    private final Gson gson;
    private final ItemRepository itemRepository;
    private final NpcRepository npcRepository;

    private Player player;

    @Autowired
    public JsonToSQLDefinitions(Gson gson, ItemRepository itemRepository, NpcRepository npcRepository) {
        this.gson = gson;
        this.itemRepository = itemRepository;
        this.npcRepository = npcRepository;
    }

    public void migrate() {

        try {
            migrateItems();
            //migrateNpcs();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void migrateItems() throws FileNotFoundException {
        String itemDefinitionsJson = GameConstants.DEFINITIONS_DIRECTORY + "items.json";

        if(!new File(itemDefinitionsJson).exists()) {
            logAndInformIssuer("Couldn't find a items.json to migrate.");
            return;
        }

        logAndInformIssuer("Migrating item definitions from JSON to SQL.");
        FileReader fileReader = new FileReader(itemDefinitionsJson);

        List<ItemDefinition> itemDefinitions = gson.fromJson(fileReader, new TypeToken<List<ItemDefinition>>() {}.getType());
        itemRepository.saveAll(itemDefinitions);
        logAndInformIssuer("Migrated item definitions successfully.");
    }

    private void migrateNpcs() throws FileNotFoundException {
        String npcDefinitionsJson = GameConstants.DEFINITIONS_DIRECTORY + "npc_defs.json";

        if(!new File(npcDefinitionsJson).exists()) {
            logAndInformIssuer("Couldn't find a npc_defs.json to migrate.");
            return;
        }

        logAndInformIssuer("Migrating npc definitions from JSON to SQL.");
        FileReader fileReader = new FileReader(npcDefinitionsJson);
        List<NpcDefinition>  npcDefinitions = gson.fromJson(fileReader, new TypeToken<List<NpcDefinition>>() {}.getType());
        npcRepository.saveAll(npcDefinitions);
        logAndInformIssuer("Migrated npc definitions successfully.");
    }

    private void logAndInformIssuer(String message) {
        Server.getLogger().info(message);

        if(player != null) {
            player.getPacketSender().sendMessage(message);
        }
    }
}
