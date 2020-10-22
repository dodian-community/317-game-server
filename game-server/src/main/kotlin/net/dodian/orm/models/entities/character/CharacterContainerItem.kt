package net.dodian.orm.models.entities.character

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class CharacterContainerItem(itemId: Int, amount: Int) {
    @Id
    var id: Int = 0
    var itemId: Int? = itemId
    var amount: Int? = amount
}