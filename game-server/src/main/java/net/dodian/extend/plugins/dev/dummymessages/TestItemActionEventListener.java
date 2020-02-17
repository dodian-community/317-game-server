package net.dodian.extend.plugins.dev.dummymessages;

import net.dodian.extend.events.items.ItemActionEventListener;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class TestItemActionEventListener implements ItemActionEventListener {

    @Override
    public void onFirstClick(Player player, Packet packet) {
        System.out.println("Testing item first click event...");
    }

    @Override
    public void onSecondClick(Player player, Packet packet) {
        System.out.println("Testing item second click event...");
    }

    @Override
    public void onThirdClick(Player player, Packet packet) {
        System.out.println("Testing item third click event...");
    }
}
