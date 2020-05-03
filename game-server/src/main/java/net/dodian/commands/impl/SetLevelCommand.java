package net.dodian.commands.impl;

import net.dodian.commands.Command;
import net.dodian.commands.annotations.MaintenanceCommand;
import net.dodian.commands.annotations.Name;
import net.dodian.old.world.content.SkillManager;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Skill;
import org.springframework.stereotype.Component;

import java.util.List;

@Name("setlevel")
@MaintenanceCommand
@Component
public class SetLevelCommand extends Command {

    @Override
    public boolean execute(Player player, List<String> parts) {
        if(!super.execute(player, parts)) {
            return false;
        }

        if(parts.size() < 2) {
            return false;
        }

        Skill skill = Skill.valueOf(parts.get(0).toUpperCase());
        int level = Integer.parseInt(parts.get(1));
        int xp = SkillManager.getExperienceForLevel(level);

        player.getSkillManager()
            .setExperience(skill, xp)
            .setCurrentLevel(skill, level)
            .setMaxLevel(skill, level);

        int currentLevel = player.getSkillManager().getCurrentLevel(skill);

        player.getPacketSender().sendMessage("You set your " + skill.getName() + " to level " + currentLevel + ".");

        return true;
    }
}
