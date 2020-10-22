package net.dodian.orm.models.entities.character

import net.dodian.GameConstants.DEFAULT_POSITION
import net.dodian.old.world.entity.combat.FightType
import net.dodian.old.world.entity.impl.player.Player
import net.dodian.old.world.model.MagicSpellbook
import net.dodian.old.world.model.Position
import net.dodian.old.world.model.SkullType
import java.util.*
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Character(@Id var id: Int, var name: String) {
    var created: Date? = null
    var lastLogin: Date? = null

    var username: String? = null

    var runEnergy: Int = 100
    var specialPercentage: Int = 100
    var running: Boolean = false

    @Embedded
    var position: Position = DEFAULT_POSITION

    var spellBook: MagicSpellbook = MagicSpellbook.ANCIENT
    var fightType: FightType = FightType.UNARMED_PUNCH
    var autoRetaliate: Boolean = false
    var clanChat: String = "public"

    var recoilDamage: Int = 0
    var poisonDamage: Int = 0
    var poisonImmunity: Int = 0
    var fireImmunity: Int = 0

    var teleBlockTimer: Int = 0
    var prayerBlockTimer: Int = 0

    var targetTeleportUnlocked: Boolean = false
    var preserveUnlocked: Boolean = false

    var targetSearchTimer: Int = 0
    var specialAttackRestoreTimer: Int = 0

    var skullTimer: Int = 0
    var skullType: SkullType? = null

    @OneToMany
    var inventory: List<CharacterContainerItem> = mutableListOf()
    @OneToMany
    var equipment: List<CharacterContainerItem> = mutableListOf()

    @Embedded
    var appearance: CharacterAppearance = CharacterAppearance()

    @Embedded
    var characterSkills: CharacterSkills = CharacterSkills()

    fun update(player: Player) {
        this.position = player.position
        this.spellBook = player.spellbook
        this.fightType = player.combat.fightType
        this.autoRetaliate = player.combat.autoRetaliate()
        this.clanChat = player.clanChatName
        this.recoilDamage = player.recoilDamage
        this.poisonDamage = player.poisonDamage
        this.poisonImmunity = player.combat.poisonImmunityTimer.secondsRemaining()
        this.fireImmunity = player.combat.fireImmunityTimer.secondsRemaining()
        this.teleBlockTimer = player.combat.teleBlockTimer.secondsRemaining()
        this.prayerBlockTimer = player.combat.prayerBlockTimer.secondsRemaining()
        this.targetTeleportUnlocked = player.isTargetTeleportUnlocked
        this.preserveUnlocked = player.isPreserveUnlocked
        this.targetSearchTimer = player.targetSearchTimer.secondsRemaining()
        this.specialAttackRestoreTimer = player.specialAttackRestore.secondsRemaining()
        this.skullTimer = player.skullTimer
        this.skullType = player.skullType

        player.inventory.items.forEach { item ->
            this.inventory += (CharacterContainerItem(item.id, item.amount))
        }

        player.equipment.items.forEach { item ->
            this.equipment += (CharacterContainerItem(item.id, item.amount))
        }
    }
}