package net.dodian.plugins.impl.dungeons;

import net.dodian.events.EventHandler;
import net.dodian.events.EventListener;
import net.dodian.events.impl.player.interact.object.PlayerObjectEvent;
import net.dodian.events.impl.player.interact.object.PlayerObjectFirstClickEvent;
import net.dodian.events.impl.player.interact.object.PlayerObjectSecondClickEvent;
import net.dodian.old.world.entity.impl.object.GameObject;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Position;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class OrangeKeyDungeon implements EventListener {

    private final GameObject yanilleDungeonMonkeybarsLeft = new GameObject(23567, new Position(2597, 9489), "You need a orange key to swing the monkey bars!");
    private final GameObject yanilleDungeonMonkeybarsRight = new GameObject(23567, new Position(2598, 9489), "You need a orange key to swing the monkey bars!");
    private final GameObject yanilleDungeonPipe = new GameObject(23140, new Position(2576, 9506), "You need a orange key to go squeeze through this pipe!");


    private final GameObject[] objects = new GameObject[]{
            yanilleDungeonMonkeybarsLeft,
            yanilleDungeonMonkeybarsRight,
            yanilleDungeonPipe
    };

    private boolean hasOrangeKey(Player player) {
        return player.getInventory().contains(1544);
    }

    public <T extends PlayerObjectEvent> void handleClick(T event) {
        if (!Arrays.asList(objects).contains(event.getObject())) {
            return;
        }

        if (!hasOrangeKey(event.getPlayer())) {
            Arrays.stream(objects)
                    .filter(object -> object.equals(event.getObject()))
                    .findFirst()
                    .ifPresent(object -> event.getPlayer().getPacketSender().sendMessage(object.getMessage()));
            return;
        }

        if (event.getObject().equals(yanilleDungeonMonkeybarsLeft)) {
            event.getPlayer().moveTo(new Position(2598, 9494));
            return;
        }
        if (event.getObject().equals(yanilleDungeonMonkeybarsRight)) {
            event.getPlayer().moveTo(new Position(2598, 9494));
            return;
        }

        if (event.getObject().equals(yanilleDungeonPipe)) {
            event.getPlayer().moveTo(new Position(2572, 9506));
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
