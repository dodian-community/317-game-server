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
public class RedKeyDungeon implements EventListener {

    private final GameObject yanilleDungeonDoor = new GameObject(1540, new Position(2603, 3082), "You need a red key to enter this door!");
    private final GameObject yanilleDungeonStairsDown = new GameObject(16664, new Position(2603, 3078), "You need a red key to go down these stairs!");
    private final GameObject dungeonDoor = new GameObject(11728, new Position(2601, 9482), "You need a red key to enter this door!");
    private final GameObject bossStairsDown = new GameObject(15656, new Position(2620,9497), "You need a red key to go down these stairs!");
    private final GameObject bossStairsUp = new GameObject(23564, new Position(2615, 9504), "You need a red key to go up these stairs!");

    private GameObject[] objects = new GameObject[] {
        yanilleDungeonDoor,
        yanilleDungeonStairsDown,
        dungeonDoor,
        bossStairsDown,
        bossStairsUp
    };

    private boolean hasRedKey(Player player) {
        return player.getInventory().contains(1543);
    }

    public <T extends PlayerObjectEvent> void handleClick(T event) {
        if(!Arrays.asList(objects).contains(event.getObject())) {
            return;
        }

        if(!hasRedKey(event.getPlayer())) {
            Arrays.stream(objects)
                    .filter(object -> object.equals(event.getObject()))
                    .findFirst()
                    .ifPresent(object -> event.getPlayer().getPacketSender().sendMessage(object.getMessage()));
            return;
        }

        if(event.getObject().equals(yanilleDungeonDoor)) {
            event.getPlayer().moveTo(new Position(2603, 3081));
            return;
        }

        if(event.getObject().equals(yanilleDungeonStairsDown)) {
            event.getPlayer().moveTo(new Position(2602, 9479));
            return;
        }

        if(event.getObject().equals(dungeonDoor)) {
            event.getPlayer().moveTo(new Position(2601, 9482));
            return;
        }

        if(event.getObject().equals(bossStairsDown)) {
            event.getPlayer().moveTo(new Position(2614, 9505));
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
