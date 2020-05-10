package net.dodian.plugins.impl.dungeons;

import net.dodian.game.events.EventHandler;
import net.dodian.game.events.EventListener;
import net.dodian.game.events.impl.player.interact.object.PlayerObjectEvent;
import net.dodian.game.events.impl.player.interact.object.PlayerObjectFirstClickEvent;
import net.dodian.game.events.impl.player.interact.object.PlayerObjectSecondClickEvent;
import net.dodian.old.world.entity.impl.object.GameObject;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Position;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class YellowKeyDungeon implements EventListener {

    private final GameObject yanilleDungeonledge = new GameObject(23548, new Position(2580, 9513), "You need a yellow key to balancing this ledge!");
    private final GameObject yanilleDungeonWatchtowerstairs = new GameObject(16664, new Position(2569, 3122), "You need a Yellow key to go down these stairs!");


    private final GameObject[] objects = new GameObject[]{
            yanilleDungeonledge,
            yanilleDungeonWatchtowerstairs
    };

    private boolean hasYellowKey(Player player) {
        return player.getInventory().contains(1545);
    }

    public <T extends PlayerObjectEvent> void handleClick(T event) {
        if (!Arrays.asList(objects).contains(event.getObject())) {
            return;
        }

        if (!hasYellowKey(event.getPlayer())) {
            Arrays.stream(objects)
                    .filter(object -> object.equals(event.getObject()))
                    .findFirst()
                    .ifPresent(object -> event.getPlayer().getPacketSender().sendMessage(object.getMessage()));
            return;
        }

        if (event.getObject().equals(yanilleDungeonledge)) {
            event.getPlayer().moveTo(new Position(2580, 9520));
            return;
        }
        if (event.getObject().equals(yanilleDungeonWatchtowerstairs)) {
            event.getPlayer().moveTo(new Position(2569, 9525));
            return;
        }


    }

    @EventHandler
    public void onFirstClickObject(PlayerObjectFirstClickEvent event) {
        handleClick(event);
    }

    @EventHandler
    public void onSecondClickObject(PlayerObjectSecondClickEvent event) {
        handleClick(event);
    }
}
