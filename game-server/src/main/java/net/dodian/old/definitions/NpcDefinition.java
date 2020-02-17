package net.dodian.old.definitions;

import net.dodian.GameConstants;
import net.dodian.old.util.JsonLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Represents an npc's definition.
 * Holds its information, such as
 * name and combat level.
 * 
 * @author Professor Oak
 */
public class NpcDefinition {

	/**
	 * Parses the npc definitions
	 * @return
	 */
	public static JsonLoader parse() {
		return new JsonLoader() {
			@Override
			public void load(JsonObject reader, Gson builder) {
				
				final int npcId = reader.get("npcId").getAsInt();
				
				definitions[npcId] = new NpcDefinition();
				
				definitions[npcId].id = npcId;
				definitions[npcId].name = reader.get("name").getAsString();
				definitions[npcId].examine = reader.get("examine").getAsString();
				
				if(reader.has("size")) {
					definitions[npcId].size = reader.get("size").getAsInt();
				}
				if(reader.has("walk-radius")) {
					definitions[npcId].walkRadius = reader.get("walk-radius").getAsInt();
				}
				if(reader.has("combat-follow")) {
					definitions[npcId].combatFollowDistance = reader.get("combat-follow").getAsInt();
				}
				if(reader.has("attackable")) {
					definitions[npcId].attackable = reader.get("attackable").getAsBoolean();
				}
				if(reader.has("aggressive")) {
					definitions[npcId].aggressive = reader.get("aggressive").getAsBoolean();
				}
				if(reader.has("retreats")) {
					definitions[npcId].retreats = reader.get("retreats").getAsBoolean();
				}
				if(reader.has("poisonous")) {
					definitions[npcId].poisonous = reader.get("poisonous").getAsBoolean();
				}
				if(reader.has("respawn")) {
					definitions[npcId].respawn = reader.get("respawn").getAsInt();
				}
				if(reader.has("max-hit")) {
					definitions[npcId].maxHit = reader.get("max-hit").getAsInt();
				}
				if(reader.has("attack-speed")) {
					definitions[npcId].attackSpeed = reader.get("attack-speed").getAsInt();
				}
				if(reader.has("attack-anim")) {
					definitions[npcId].attackAnim = reader.get("attack-anim").getAsInt();
				}
				if(reader.has("defence-anim")) {
					definitions[npcId].defenceAnim = reader.get("defence-anim").getAsInt();
				}
				if(reader.has("death-anim")) {
					definitions[npcId].deathAnim = reader.get("death-anim").getAsInt();
				}
				if(reader.has("hitpoints")) {
					definitions[npcId].hitpoints = reader.get("hitpoints").getAsInt();
				}
				if(reader.has("combat-level")) {
					definitions[npcId].combatLevel = reader.get("combat-level").getAsInt();
				}
				if(reader.has("attack-level")) {
					definitions[npcId].attackLevel = reader.get("attack-level").getAsInt();
				}
				if(reader.has("strength-level")) {
					definitions[npcId].strengthLevel = reader.get("strength-level").getAsInt();
				}
				if(reader.has("ranged-level")) {
					definitions[npcId].rangedLevel = reader.get("ranged-level").getAsInt();
				}
				if(reader.has("magic-level")) {
					definitions[npcId].magicLevel = reader.get("magic-level").getAsInt();
				}
				if(reader.has("defence-melee")) {
					definitions[npcId].defenceMelee = reader.get("defence-melee").getAsInt();
				}
				if(reader.has("defence-ranged")) {
					definitions[npcId].defenceRange = reader.get("defence-ranged").getAsInt();
				}
				if(reader.has("defence-magic")) {
					definitions[npcId].defenceMage = reader.get("defence-magic").getAsInt();
				}
				if(reader.has("slayer-req")) {
					definitions[npcId].slayerLevel = reader.get("slayer-req").getAsInt();
				}
			}

			@Override
			public String filePath() {
				return GameConstants.DEFINITIONS_DIRECTORY + "npc_defs.json";
			}
		};
	}
	
	/** An array containing all of the npc definitions. */
	private static NpcDefinition[] definitions = new NpcDefinition[14500];

	public static NpcDefinition forId(int id) {
		return id > definitions.length ? null : definitions[id];
	}

	/** The id of the npc. */
	private int id;

	/** The name of the npc. */
	private String name;

	/** The examine of the npc. */
	private String examine;

	/** The npc size. */
	private int size = 1;

	/** Does the npc randomly walk? */
	private int walkRadius;

	/** If the npc is attackable. */
	private boolean attackable;

	/** If the npc retreats. */
	private boolean retreats = true;
	
	/** If the npc is aggressive **/
	private boolean aggressive;

	/** If the npc poisons. */
	private boolean poisonous;

	/** Time it takes for this npc to respawn. */
	private int respawn;

	/** The max hit of this npc. */
	private int maxHit;

	/** The amount of hp this npc has. */
	private int hitpoints = 10;

	/** The attack speed of this npc. */
	private int attackSpeed;

	/** The attack animation of this npc. */
	private int attackAnim;

	/** The defence animation of this npc. */
	private int defenceAnim;

	/** The death animation of this npc. */
	private int deathAnim;

	/** This npc's combat level */
	private int combatLevel;
	
	/** This npc's attack bonus. */
	private int attackLevel;
	private int strengthLevel;
	private int rangedLevel;
	private int magicLevel;

	/** This npc's melee resistance. */
	private int defenceMelee;

	/** This npc's range resistance. */
	private int defenceRange;

	/** This npc's defence resistance. */
	private int defenceMage;

	/** This npc's slayer level required to attack. */
	private int slayerLevel;
	
	/** This npc's maximum follow distance in combat **/
	private int combatFollowDistance = 7; //Default is 7
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExamine() {
		return examine;
	}

	public void setExamine(String examine) {
		this.examine = examine;
	}

	public boolean isAttackable() {
		return attackable;
	}

	public void setAttackable(boolean attackable) {
		this.attackable = attackable;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean doesRetreat() {
		return retreats;
	}

	public void setRetreats(boolean retreats) {
		this.retreats = retreats;
	}

	public boolean isPoisonous() {
		return poisonous;
	}

	public void setPoisonous(boolean poisonous) {
		this.poisonous = poisonous;
	}

	public int getRespawn() {
		return respawn;
	}

	public void setRespawn(int respawn) {
		this.respawn = respawn;
	}

	public int getMaxHit() {
		return maxHit;
	}

	public void setMaxHit(int maxHit) {
		this.maxHit = maxHit;
	}

	public int getHitpoints() {
		return hitpoints;
	}

	public void setHitpoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}

	public int getAttackSpeed() {
		return attackSpeed;
	}

	public void setAttackSpeed(int attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	public int getAttackAnim() {
		return attackAnim;
	}

	public void setAttackAnim(int attackAnim) {
		this.attackAnim = attackAnim;
	}

	public int getDefenceAnim() {
		return defenceAnim;
	}

	public void setDefenceAnim(int defenceAnim) {
		this.defenceAnim = defenceAnim;
	}

	public int getDeathAnim() {
		return deathAnim;
	}

	public void setDeathAnim(int deathAnim) {
		this.deathAnim = deathAnim;
	}

	public int getAttackLevel() {
		return attackLevel;
	}

	public void setAttackLevel(int attackLevel) {
		this.attackLevel = attackLevel;
	}

	public int getStrengthLevel() {
		return strengthLevel;
	}

	public void setStrengthLevel(int strengthLevel) {
		this.strengthLevel = strengthLevel;
	}

	public int getRangedLevel() {
		return rangedLevel;
	}

	public void setRangedLevel(int rangedLevel) {
		this.rangedLevel = rangedLevel;
	}

	public int getMagicLevel() {
		return magicLevel;
	}

	public void setMagicLevel(int magicLevel) {
		this.magicLevel = magicLevel;
	}

	public int getDefenceMelee() {
		return defenceMelee;
	}

	public void setDefenceMelee(int defenceMelee) {
		this.defenceMelee = defenceMelee;
	}

	public int getDefenceRange() {
		return defenceRange;
	}

	public void setDefenceRange(int defenceRange) {
		this.defenceRange = defenceRange;
	}

	public int getDefenceMage() {
		return defenceMage;
	}

	public void setDefenceMage(int defenceMage) {
		this.defenceMage = defenceMage;
	}

	public int getSlayerLevel() {
		return slayerLevel;
	}

	public void setSlayerLevel(int slayerLevel) {
		this.slayerLevel = slayerLevel;
	}

	public int getWalkRadius() {
		return walkRadius;
	}

	public void setWalkRadius(int walkRadius) {
		this.walkRadius = walkRadius;
	}

	public int getCombatLevel() {
		return combatLevel;
	}

	public void setCombatLevel(int combatLevel) {
		this.combatLevel = combatLevel;
	}
	public int getCombatFollowDistance() {
		return combatFollowDistance;
	}

	public void setCombatFollowDistance(int combatFollowDistance) {
		this.combatFollowDistance = combatFollowDistance;
	}

	public boolean isAggressive() {
		return aggressive;
	}

	public void setAggressive(boolean aggressive) {
		this.aggressive = aggressive;
	}
}
