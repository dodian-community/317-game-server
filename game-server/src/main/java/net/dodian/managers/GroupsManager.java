package net.dodian.managers;

import com.google.gson.JsonObject;
import lombok.Getter;
import net.dodian.config.GameConfiguration;
import net.dodian.orm.models.Group;
import net.dodian.orm.repositories.GroupRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Getter
@Scope("singleton")
public class GroupsManager {

    private final GroupRepository groupRepository;

    public GroupsManager(GroupRepository groupRepository, GameConfiguration gameConfiguration) {
        this.groupRepository = groupRepository;
        gameConfiguration.get("groups").getAsJsonArray().forEach(jsonElement -> {
            JsonObject groupConfigurations = jsonElement.getAsJsonObject();

            Group group = null;
            if(groupConfigurations.has("id")) {
                Optional<Group> optionalGroup = groupRepository.findById(groupConfigurations.get("id").getAsInt());

                if(optionalGroup.isPresent()) {
                    group = optionalGroup.get();
                } else {
                    return;
                }
            } else {
                return;
            }

            group.setPremium(false);
            group.setStaff(false);
            group.setMaintenance(false);

            if(groupConfigurations.has("isPremium")) {
                group.setPremium(groupConfigurations.get("isPremium").getAsBoolean());
            }

            if(groupConfigurations.has("isStaff")) {
                group.setStaff(groupConfigurations.get("isStaff").getAsBoolean());
            }

            if(groupConfigurations.has("isMaintenance")) {
                group.setMaintenance(groupConfigurations.get("isMaintenance").getAsBoolean());
            }

            if(groupConfigurations.has("rights")) {
                group.setRights(groupConfigurations.get("rights").getAsInt());
            }

            groupRepository.save(group);
        });
    }

    public Optional<Group> getGroupById(int id) {
        return groupRepository.findById(id);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }
}
