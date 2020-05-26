package net.dodian.plugins.impl.skill;

import net.dodian.game.events.EventHandler;
import net.dodian.game.events.EventListener;
import net.dodian.game.events.impl.player.interact.item.PlayerItemEvent;
import net.dodian.game.events.impl.player.interact.object.PlayerObjectEvent;
import net.dodian.game.events.impl.player.interact.object.PlayerObjectFirstClickEvent;
import net.dodian.old.engine.task.Task;
import net.dodian.old.engine.task.TaskManager;
import net.dodian.old.util.Misc;
import net.dodian.old.world.entity.impl.object.GameObject;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.Skill;
import net.dodian.old.world.model.movement.MovementStatus;
import org.springframework.stereotype.Component;

@Component
public class Fletching implements EventListener {
    private int amount;

    private enum fletchData {
        HEADLESS_ARROWS(1, 10, new Item(52, 15), new Item(314, 15), new Item(53, 15)),
        OAK_SHORTBOW(15, 50, new Item(52, 1), new Item(314, 1), new Item(53, 1)),
        ;
        int level, xp;
        Item[] items; //[0] = used, [1] = usedOn, [2] = produce
        fletchData(int level, int xp, Item... items) {
            this.level = level;
            this.items = items;
        }
    }
    public static fletchData getAxe(Player player, Item used, Item usedOn) {
        for(fletchData data : fletchData.values()) {
            if((used.equals(data.items[0]) && usedOn.equals(data.items[1])) || (used.equals(data.items[1]) && usedOn.equals(data.items[0]))) {
                return player.getInventory().contains(new Item[]{data.items[0], data.items[1]}) ? data : null;
            }
        }
        return null;
    }

    @EventHandler
    public void onItemOnItem(PlayerItemEvent event) {
        //Yikes!
        //initiate(event, tree, axe);
        //TODO: Add support for use item on item packet! Aswell as use item on stuff!
    }

        private <T extends PlayerObjectEvent> void initiate(T event, fletchData data, int amount) {
            event.getPlayer().performAnimation(new Animation(444)); //444 = placeholder!
            Task task = new Task(1, event.getPlayer(), true) {
            int cycle = 3, count = amount;
            @Override
            protected void execute() {
                /* Stops action! */
                if (event.getPlayer().getInventory().isFull() ||
                event.getPlayer().getMovementQueue().getMovementStatus().equals(MovementStatus.DISABLED)
                || count < 1) {
                    stop();
                }
                if(!event.getPlayer().getInventory().contains(new Item[]{data.items[0], data.items[1]})) {
                    //TODO Add message?
                    stop();
                }
                /* Cycle action! */
                if(cycle > 0)
                    cycle--;
                else {
                    count--;
                    event.getPlayer().getInventory().delete(data.items[0]);
                    event.getPlayer().getInventory().delete(data.items[1]);
                    event.getPlayer().getInventory().add(data.items[2]);
                    event.getPlayer().getSkillManager().addExperience(Skill.FLETCHING, data.xp);
                    event.getPlayer().getPacketSender().sendMessage("You fletch " + (data.items[2].getAmount() > 1 ? "some" : "one") + " " + data.items[2].getDefinition().getName().toLowerCase());
                    cycle = 3;
                }
            }
                @Override
                public void stop() {
                    event.getPlayer().performAnimation(new Animation(65535));
                    this.setEventRunning(false);
                }
        };
        TaskManager.submit(task);
    }
}
