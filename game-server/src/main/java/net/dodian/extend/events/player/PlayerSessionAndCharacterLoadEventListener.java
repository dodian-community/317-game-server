package net.dodian.extend.events.player;

import org.springframework.stereotype.Component;

@Component
public interface PlayerSessionAndCharacterLoadEventListener extends CharacterLoadEventListener, PlayerSessionEventListener {
}
