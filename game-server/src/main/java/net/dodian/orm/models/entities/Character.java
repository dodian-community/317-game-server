package net.dodian.orm.models.entities;

import lombok.Data;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Character {
    @Id
    private int id;
    private String name;
    private Date created;
    private Date lastLogin;
    @Embedded
    private CharacterStats characterStats;
}
