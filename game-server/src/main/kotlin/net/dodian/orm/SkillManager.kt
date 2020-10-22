package net.dodian.orm

import net.dodian.old.world.content.SkillManager.AMOUNT_OF_SKILLS
import net.dodian.old.world.entity.impl.player.Player
import org.springframework.stereotype.Component
import java.util.*

@Component
class SkillManager(player: Player) {

    final val AMOUNT_OF_SKILLS = Skill.values().size
    final val MAX_EXPERIENCE = 250000000

    private final val skills: Skills

    init {
        this.skills = Skills()
        for (i in 0 until AMOUNT_OF_SKILLS) {
            skills.level[i] = 1
            skills.maxLevel[i] = 1
            skills.experience[i] = 0
        }
        skills.level[Skill.HITPOINTS.ordinal] = 10
        skills.maxLevel[Skill.HITPOINTS.ordinal] = 10
        skills.experience[Skill.HITPOINTS.ordinal] = 1184
    }

    fun addExperience(skill: Skill, experience: Int): SkillManager {
        if(this.skills.experience[skill.ordinal] >= MAX_EXPERIENCE) {
            return this
        }

        val startingLevel: Int = skills.maxLevel[skill.ordinal]
        this.skills.experience[skill.ordinal] =
                if(this.skills.experience[skill.ordinal] + experience > MAX_EXPERIENCE)
                    MAX_EXPERIENCE
                else
                    this.skills.experience[skill.ordinal] + experience

        return this
    }

    internal class Skills {
        val level: IntArray
        val maxLevel: IntArray
        val experience: IntArray
        val experienceLocked: BooleanArray

        init {
            level = IntArray(AMOUNT_OF_SKILLS)
            maxLevel = IntArray(AMOUNT_OF_SKILLS)
            experience = IntArray(AMOUNT_OF_SKILLS)
            experienceLocked = BooleanArray(AMOUNT_OF_SKILLS)
        }
    }

    enum class Skill(val id: Int, val chatboxInterface: Int, val button: Int) {
        ATTACK(1, 6247, 52004),
        DEFENCE(2, 6253, 52010),
        STRENGTH(3, 6206, 52007),
        HITPOINTS(4, 6216, 52001),
        RANGED(5, 4443, 52013),
        PRAYER(6, 6242, 52019),
        MAGIC(7, 6211, 52016),
        COOKING(8, 6226, 24222),
        WOODCUTTING(9, 4272, 24228),
        FLETCHING(10, 6231, 24227),
        FISHING(11, 6258, 24219),
        FIREMAKING(12, 4282, 24225),
        CRAFTING(13, 6263, 24224),
        SMITHING(14, 6221, 24216),
        MINING(15, 4416, 24213),
        HERBLORE(16, 6237, 24218),
        AGILITY(17, 4277, 24215),
        THIEVING(18, 4261, 24221),
        SLAYER(19, 12122, 24230),
        FARMING(20, 5267, 24231),
        RUNECRAFTING(21, 4267, 24229),
        CONSTRUCTION(22, 7267, -1),
        HUNTER(23, 8267, 24232)
        ;

        fun forButton(button: Int): Optional<Skill> {
            return enumValues<Skill>().asList().stream()
                    .filter { skill -> skill.button == button }
                    .findFirst()
        }
    }
}