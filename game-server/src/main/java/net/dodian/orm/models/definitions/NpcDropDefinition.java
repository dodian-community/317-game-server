package net.dodian.orm.models.definitions;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class NpcDropDefinition {
    @Id
    private int id;
    private int npcId;
    private int itemId;
    private int minimumAmount;
    private int maximumAmount;
    private double dropChance;
    private boolean announce;
}
