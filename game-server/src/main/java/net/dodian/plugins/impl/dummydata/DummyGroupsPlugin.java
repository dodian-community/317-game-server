package net.dodian.plugins.impl.dummydata;

import net.dodian.events.EventHandler;
import net.dodian.events.EventListener;
import net.dodian.events.impl.server.ServerStartedUpEvent;
import net.dodian.events.impl.server.ServerStartingUpEvent;
import net.dodian.orm.models.Group;
import net.dodian.orm.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
public class DummyGroupsPlugin implements EventListener {

    private final GroupRepository groupRepository;

    @Autowired
    public DummyGroupsPlugin(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @EventHandler
    public void onServerStarting(ServerStartingUpEvent event) {
        List<Group> groups = new ArrayList<>();

        Group memberGroup = new Group(1, "Regular Member");

        Group premiumGroup = new Group(2, "Premium Member");
        premiumGroup.setPremium(true);

        Group moderatorGroup = new Group(3, "Moderator", 1);
        moderatorGroup.setStaff(true);

        Group developerGroup = new Group(4, "Developer", 2);
        developerGroup.setStaff(true);
        developerGroup.setMaintenance(true);

        Group adminGroup = new Group(5, "Administrator", 2);
        adminGroup.setStaff(true);
        adminGroup.setMaintenance(true);

        groups.add(memberGroup);
        groups.add(premiumGroup);
        groups.add(moderatorGroup);
        groups.add(developerGroup);
        groups.add(adminGroup);

        this.groupRepository.saveAll(groups);
    }

    @EventHandler
    public void onServerStarted(ServerStartedUpEvent event) {
        groupRepository.findAll().forEach(group -> System.out.println("Group: " + group.getTitle()));
    }
}
