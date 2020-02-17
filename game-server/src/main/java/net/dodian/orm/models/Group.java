package net.dodian.orm.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usergroup")
@Data
public class Group {
    @Id
    @Column(name = "usergroupid")
    private int id;
    private String title;
    private boolean premium;
    private boolean staff;
    private boolean maintenance;
    private int rights;

    public Group() {

    }

    public Group(int id, String title) {
        this.id = id;
        this.title = title;
        this.rights = 0;
    }

    public Group(int id, String title, int rights) {
        this.id = id;
        this.title = title;
        this.rights = rights;
    }

    public Group(int id, String title, int rights, boolean premium, boolean staff, boolean maintenance) {
        this.id = id;
        this.title = title;
        this.rights = rights;
        this.premium = premium;
        this.staff = staff;
        this.maintenance = maintenance;
    }
}
