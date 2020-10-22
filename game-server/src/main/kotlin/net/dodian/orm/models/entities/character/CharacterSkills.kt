package net.dodian.orm.models.entities.character

import javax.persistence.Embeddable
import javax.persistence.Embedded

@Embeddable
class CharacterSkills(
    @Embedded var attack: CharacterSkill = CharacterSkill(),
    @Embedded var defence: CharacterSkill = CharacterSkill(),
    @Embedded var strength: CharacterSkill = CharacterSkill(),
    @Embedded var hitpoints: CharacterSkill = CharacterSkill(),
    @Embedded var prayer: CharacterSkill = CharacterSkill(),
    @Embedded var magic: CharacterSkill = CharacterSkill(),
    @Embedded var ranged: CharacterSkill = CharacterSkill(),
    @Embedded var cooking: CharacterSkill = CharacterSkill(),
    @Embedded var woodcutting: CharacterSkill = CharacterSkill(),
    @Embedded var fletching: CharacterSkill = CharacterSkill(),
    @Embedded var fishing: CharacterSkill = CharacterSkill(),
    @Embedded var firemaking: CharacterSkill = CharacterSkill(),
    @Embedded var crafting: CharacterSkill = CharacterSkill(),
    @Embedded var mining: CharacterSkill = CharacterSkill(),
    @Embedded var smithing: CharacterSkill = CharacterSkill(),
    @Embedded var herblore: CharacterSkill = CharacterSkill(),
    @Embedded var agility: CharacterSkill = CharacterSkill(),
    @Embedded var thieving: CharacterSkill = CharacterSkill(),
    @Embedded var slayer: CharacterSkill = CharacterSkill(),
    @Embedded var runecrafting: CharacterSkill = CharacterSkill()
)