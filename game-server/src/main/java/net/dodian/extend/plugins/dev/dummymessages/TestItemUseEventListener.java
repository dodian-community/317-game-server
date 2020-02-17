package net.dodian.extend.plugins.dev.dummymessages;

import net.dodian.extend.events.items.ItemUseEventListener;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.object.GameObject;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Item;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("Dev")
public class TestItemUseEventListener implements ItemUseEventListener {

    @Override
    public void itemOnItem(Player player, Item itemUsed, Item itemUsedWith) {
        System.out.println("Testing item on item event...");
    }

    @Override
    public void itemOnNpc(Player player, Item item, NPC npc) {
        System.out.println("Testing item on npc event...");
    }

    @Override
    public void itemOnObject(Player player, Item item, GameObject object) {
        System.out.println("Testing item on object event...");
    }

    @Override
    public void itemOnPlayer(Player player, Item item, Player onItemPlayer) {
        System.out.println("Testing item on player event...");
    }
}
