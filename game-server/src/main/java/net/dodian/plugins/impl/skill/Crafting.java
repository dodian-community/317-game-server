package net.dodian.plugins.impl.skill;

import net.dodian.game.events.EventHandler;
import net.dodian.game.events.EventListener;
import net.dodian.game.events.impl.player.interact.PlayerButtonClickEvent;
import net.dodian.game.events.impl.player.interact.item.PlayerItemOnItemUsage;
import net.dodian.old.engine.task.Task;
import net.dodian.old.engine.task.TaskManager;
import net.dodian.old.net.packet.PacketSender;
import net.dodian.old.util.Misc;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.Skill;
import net.dodian.old.world.model.movement.MovementStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Crafting implements EventListener {
    private static Item CHISEL = new Item(5605);
    private static Item NEEDLE = new Item(1733);
    private static Item THREAD = new Item(1734);
    private static Animation LEATHERANIMATION = new Animation(555);
    private ArrayList<leatherCraft> craftCheck = null;

    private enum leatherTan {
        REGULAR_LEATHER(50, 1739, 1741),
        EMPTY_ONE(-1, -1, -1),
        GREEN_LEATHER(100, 1753, 1745),
        BLUE_LEATHER(-1, -1, -1),
        EMPTY_TWO(-1, -1, -1),
        EMPTY_THREE(-1, -1, -1),
        RED_LEATHER(-1, -1, -1),
        BLACK_LEATHER(-1, -1, -1)
        ;
        int price, required, received;
        leatherTan(int... items) {
            this.price = items[0];
            this.required = items[1];
            this.received = items[2];
        }
    }
    public static leatherTan getLeatherData(Item item) {
        for(leatherTan data : leatherTan.values()) {
            if(data.received == item.getId()) {
                return data;
            }
        }
        return null;
    }

    private enum leatherCraft {
        LEATHER_BODY(leatherTan.REGULAR_LEATHER, 14, 4, new Item(1129)),
        LEATHER_GLOVES(leatherTan.REGULAR_LEATHER, 1, 4, new Item(1059)),

        GREEN_LEATHER_VAMBRACES(leatherTan.GREEN_LEATHER, 57, 4, new Item(1065)),
        GREEN_LEATHER_CHAPS(leatherTan.GREEN_LEATHER, 60, 4, new Item(1099)),
        GREEN_LEATHER_BODY(leatherTan.GREEN_LEATHER, 63, 4, new Item(1135))
        ;
        leatherTan leather;
        int level, xp;
        Item produce;
        leatherCraft(leatherTan leather, int level, int xp, Item produce) {
            this.leather = leather;
            this.level = level;
            this.xp = xp;
            this.produce = produce;
        }
    }
    public static ArrayList<leatherCraft> getLeatherData(leatherTan tanItem) {
        ArrayList<leatherCraft> craftData = new ArrayList<>();
        for(leatherCraft data : leatherCraft.values()) {
            if(data.leather == tanItem) {
                craftData.add(data);
            }
        }
        return craftData;
    }

    @EventHandler
    public void onItemUsage(PlayerItemOnItemUsage event) {
        boolean gotNeedle = event.getUsed().getId() == NEEDLE.getId() || event.getUsedOn().getId() == NEEDLE.getId();
        System.out.println("test: " + gotNeedle);
        if(gotNeedle) { //Leather code!
            Item otherItem = event.getUsed().getId() == NEEDLE.getId() ? event.getUsedOn() : event.getUsed();
            leatherTan leatherData = getLeatherData(otherItem);
            System.out.println("test 2: " + leatherData);
            if(leatherData == leatherTan.REGULAR_LEATHER) {
                ArrayList<leatherCraft> craftCheck = getLeatherData(leatherData);
                event.getPlayer().getPacketSender().sendInterface(2311);
            } else if (leatherData != null && leatherData.price != -1) {
                ArrayList<leatherCraft> craftCheck = getLeatherData(leatherData);
                if(!craftCheck.isEmpty()) {
                    Item[] item = new Item[craftCheck.size()];
                    for(int i = 0; i < craftCheck.size(); i++)
                        item[i] = craftCheck.get(i).produce;
                    event.getPlayer().getPacketSender().sendMakeItemOption(item);
                }
            }
         }
    }

    @EventHandler
    public void onButtonClick(PlayerButtonClickEvent event) {
        if(PacketSender.itemOptions.isEmpty()) {
            return;
        }
        //TODO: Add some shiet here?!
        if(event.getButton() == 2422)
            event.getPlayer().getPacketSender().sendInterfaceRemoval();
    }

        private <T extends PlayerButtonClickEvent> void craftLeather(T event, leatherCraft data, int amount) {
            TaskManager.cancelTasks(event.getPlayer());
            event.getPlayer().getPacketSender().sendInterfaceRemoval();
            if(event.getPlayer().getSkillManager().getCurrentLevel(Skill.CRAFTING) < data.level) {
                event.getPlayer().getPacketSender().sendMessage("You need level " + data.level + " crafting to craft this item!");
                return;
            }
            event.getPlayer().performAnimation(LEATHERANIMATION);
            Task task = new Task(1, event.getPlayer(), true) {
            int cycle = 3, count = Math.min(amount, 26);
            Player p = event.getPlayer();
            @Override
            protected void execute() {
                /* Stops action! */
                if (p.getMovementQueue().getMovementStatus().equals(MovementStatus.DISABLED)
                || count < 1) {
                    stop();
                }  else if (!p.getInventory().contains(THREAD) || !p.getInventory().contains(NEEDLE)) {
                    count = 0;
                    p.getPacketSender().sendMessage("You are missing a knife!");
                    stop();
                }
                /* Cycle action! */
                if(cycle > 0)
                    cycle--;
                else {
                    count--;
                    p.getInventory().delete(THREAD);
                    //TODO: Add chance for needle to break?
                    if(Misc.getRandom(10) < 1) //1:11 chance for needle to break!
                        p.getInventory().delete(NEEDLE);
                    p.getInventory().add(data.produce);
                    p.getSkillManager().addExperience(Skill.CRAFTING, data.xp);
                    p.getPacketSender().sendMessage("You crafted a " + data.produce.getDefinition().getName().toLowerCase());
                    cycle = 3;
                    if(count > 0) //TODO: Add animation!
                        p.performAnimation(LEATHERANIMATION);
                }
            }
                @Override
                public void stop() {
                    count = 0;
                    event.getPlayer().performAnimation(new Animation(65535));
                    this.setEventRunning(false);
                }
        };
        TaskManager.submit(task);
    }
}
