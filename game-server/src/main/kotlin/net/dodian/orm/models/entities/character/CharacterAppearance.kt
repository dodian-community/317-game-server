package net.dodian.orm.models.entities.character

import net.dodian.old.world.model.Appearance
import net.dodian.old.world.model.Appearance.*
import javax.persistence.Embeddable

@Embeddable
class CharacterAppearance(
    var head: Int = 0,
    var chest: Int = 3,
    var arms: Int = 26,
    var hands: Int = 34,
    var legs: Int = 38,
    var feet: Int = 42,
    var hairColor: Int = 14,
    var torsoColor: Int = 2,
    var legsColor: Int = 14,
    var feetColor: Int = 5,
    var skinColor: Int = 4,
    var gender: Int = 0
) {
    fun set(appearance: Appearance): CharacterAppearance {
        this.head = appearance.look[HEAD]
        this.chest = appearance.look[CHEST]
        this.arms = appearance.look[ARMS]
        this.hands = appearance.look[HANDS]
        this.legs = appearance.look[LEGS]
        this.feet = appearance.look[FEET]
        this.hairColor = appearance.look[HAIR_COLOUR]
        this.torsoColor = appearance.look[TORSO_COLOUR]
        this.legsColor = appearance.look[LEG_COLOUR]
        this.feetColor = appearance.look[FEET_COLOUR]
        this.skinColor = appearance.look[SKIN_COLOUR]
        this.gender = appearance.look[GENDER]

        return this
    }
}