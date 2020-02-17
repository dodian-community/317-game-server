package net.dodian.extend.plugins.dev.dummymessages;

import net.dodian.extend.events.controllers.ButtonClickEventListener;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class TestButtonClickEventListener implements ButtonClickEventListener {

    @Override
    public void onClick(Player player, int button) {
        System.out.println("Clicked button: " + button);
    }
}
