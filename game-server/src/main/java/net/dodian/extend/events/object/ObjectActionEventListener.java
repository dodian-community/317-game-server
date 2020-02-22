package net.dodian.extend.events.object;

import net.dodian.old.world.entity.impl.object.GameObject;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.stereotype.Component;

@Component
public interface ObjectActionEventListener {
    void onFirstClick(Player player, GameObject gameObject);
    void onSecondClick(Player player, GameObject gameObject);
    void onThirdClick(Player player, GameObject gameObject);
    void onFourthClick(Player player, GameObject gameObject);
    void onFifthClick(Player player, GameObject gameObject);
}
