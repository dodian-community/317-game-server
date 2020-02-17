package net.dodian.extend.events.player;

import net.dodian.extend.events.EventListener;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface CharacterLoadEventListener extends EventListener {
    Optional<Integer> onCharacterLoad(Player player);
}
