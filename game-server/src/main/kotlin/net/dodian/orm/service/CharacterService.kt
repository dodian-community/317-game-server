package net.dodian.orm.service

import net.dodian.old.net.login.LoginResponses
import net.dodian.old.world.entity.impl.player.Player
import net.dodian.orm.models.entities.character.Character
import net.dodian.orm.repositories.CharacterRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.util.*

@Service
@Scope("prototype")
class CharacterService @Autowired constructor(val characterRepository: CharacterRepository) {

    fun save(character: Character) {
        this.characterRepository.save(character)
    }

    fun load(player: Player): Optional<Int> {
        val optionalCharacter = this.characterRepository.findByName(player.username)
        if(optionalCharacter.isEmpty) {
            return Optional.of(LoginResponses.NEW_ACCOUNT)
        }

        player.updateInfo(optionalCharacter.get())
        return Optional.of(LoginResponses.LOGIN_SUCCESSFUL)
    }
}