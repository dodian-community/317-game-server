package net.dodian.orm.repositories

import net.dodian.orm.models.entities.character.Character
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CharacterRepository : JpaRepository<Character, Int> {
    fun findByName(name: String): Optional<Character>
}