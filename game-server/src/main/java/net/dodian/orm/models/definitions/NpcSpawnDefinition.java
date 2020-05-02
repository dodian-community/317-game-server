package net.dodian.orm.models.definitions;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class NpcSpawnDefinition {
    @Id
    private int id;
    private int npcId;
    private String face = "SOUTH";
    private int type = 10;
    private int x;
    private int y;
    private int z;
    private String description;
}
