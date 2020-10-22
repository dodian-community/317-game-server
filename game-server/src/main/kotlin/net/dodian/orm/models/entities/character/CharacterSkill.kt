package net.dodian.orm.models.entities.character

import javax.persistence.Embeddable

@Embeddable
class CharacterSkill(
    var level: Int = 1,
    var maxLevel: Int = 1,
    var experience: Int = 0,
    var xpLocked: Boolean = false
)