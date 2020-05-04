package net.dodian.orm.models.definitions;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class NpcDefinition {
    @Id
    private int id;
    private String name;
    private String examine;
    private int size;
    private int walkRadius;
    public boolean attackable;
    private boolean retreats;
    private boolean aggressive;
    private boolean poisonous;
    private int respawn;
    private int maxHit;
    private int hitpoints = 10;
    private int attackSpeed;
    private int attackAnim;
    private int defenceAnim;
    private int deathAnim;
    private int combatLevel;
    private int attackLevel;
    private int strengthLevel;
    private int rangedLevel;
    private int magicLevel;
    private int defenceMelee;
    private int defenceRange;
    private int defenceMage;
    private int slayerLevel;
    private int combatFollowDistance;
}