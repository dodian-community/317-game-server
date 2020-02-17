package net.dodian.orm.models.definitions;

import lombok.Data;
import net.dodian.old.definitions.WeaponInterfaces.WeaponInterface;
import net.dodian.old.world.model.EquipmentType;
import net.dodian.orm.converters.StringToDoubleArrayConverter;
import net.dodian.orm.converters.StringToIntArrayConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class ItemDefinition {
    @Id
    private int id;
    private String name = "";
    @Column(length = 1000)
    private String examine = "";

    private boolean stackable;
    private boolean tradeable;
    private boolean sellable;
    private boolean dropable;
    private boolean noted;
    private boolean doubleHanded;

    private int value;
    private int noteId = -1;
    private int blockAnim = 424;
    private int standAnim = 808;
    private int walkAnim = 819;
    private int runAnim = 824;
    private int standTurnAnim = 823;
    private int turn180Anim = 820;
    private int turn90CWAnim = 821;
    private int turn90CCWAnim = 821;
    private int interfaceId;
    @Convert(converter = StringToDoubleArrayConverter.class)
    private Double[] bonuses;
    @Convert(converter = StringToIntArrayConverter.class)
    private Integer[] requirements;
    private EquipmentType equipmentType;
    private WeaponInterface weaponInterface;
}

