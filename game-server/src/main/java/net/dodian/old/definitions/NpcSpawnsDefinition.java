package net.dodian.old.definitions;

import net.dodian.GameConstants;
import net.dodian.old.util.JsonLoader;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.model.Position;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Represents Npc spawns.
 * @author Professor Oak
 *
 */
public class NpcSpawnsDefinition {

	/**
	 * Parses the npc spawns
	 * @return
	 */
	public static JsonLoader parse() {
		return new JsonLoader() {
			@Override
			public void load(JsonObject reader, Gson builder) {
				
				int npcId = reader.get("npcId").getAsInt();
				int x = reader.get("x").getAsInt();
				int y = reader.get("y").getAsInt();
				int z = 0;
				
				if(reader.has("z")) {
					z = reader.get("z").getAsInt();
				}
						
				//Spawn npc
				World.getNpcAddQueue().add(new NPC(npcId, new Position(x, y, z)));
			}
			@Override
			public String filePath() {
				return GameConstants.DEFINITIONS_DIRECTORY + "npc_spawns.json";
			}
		};
	}
}
