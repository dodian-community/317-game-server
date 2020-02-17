package net.dodian.orm.models;

import lombok.Data;
import net.dodian.orm.converters.StringToIntArrayConverter;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
public class Account {
    @Id
    @Column(name = "userid")
    private int id;
    private String username;
    @Column(name = "usergroupid")
    private int groupId;
    @Column(name = "membergroupids")
    @Convert(converter = StringToIntArrayConverter.class)
    private Integer[] memberGroupIds;

    public Account() {

    }

    public Account(String username) {
        this.username = username;
    }

    public Account(int id, String username, int groupId) {
        this.id = id;
        this.username = username;
        this.groupId = groupId;
    }
}
