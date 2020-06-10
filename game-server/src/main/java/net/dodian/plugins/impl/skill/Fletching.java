package net.dodian.plugins.impl.skill;

import net.dodian.Server;
import net.dodian.game.events.EventHandler;
import net.dodian.game.events.EventListener;
import net.dodian.game.events.impl.player.interact.PlayerButtonClickEvent;
import net.dodian.game.events.impl.player.interact.item.PlayerItemEvent;
import net.dodian.game.events.impl.player.interact.item.PlayerItemOnItemEvent;
import net.dodian.game.events.impl.player.interact.item.PlayerItemOnItemUsage;
import net.dodian.game.events.impl.player.interact.object.PlayerItemUsageEvent;
import net.dodian.game.events.impl.player.interact.object.PlayerObjectEvent;
import net.dodian.game.events.impl.player.interact.object.PlayerObjectFirstClickEvent;
import net.dodian.old.engine.task.Task;
import net.dodian.old.engine.task.TaskManager;
import net.dodian.old.engine.task.impl.WalkToTask;
import net.dodian.old.net.packet.PacketSender;
import net.dodian.old.util.Misc;
import net.dodian.old.world.entity.impl.object.GameObject;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.Skill;
import net.dodian.old.world.model.movement.MovementStatus;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.impl.ButtonClickPacket;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.logging.Level;

@Component
public class Fletching implements EventListener {
    private static Item KNIFE = new Item(5605);
    private fletchArrowData arrowData = null;
    private ArrayList<fletchBowData> bowData = null;

    private enum fletchArrowData {
        ARROW_SHAFT(1, 1248,4, KNIFE, new Item(1511), new Item(52, 15)),
        HEADLESS_ARROWS(1, 65535,8, new Item(52, 15), new Item(314, 15), new Item(53, 15)),
        BRONZE_ARROWS(1, 65535,10, new Item(39, 15), new Item(53, 15), new Item(882, 15)),
        IRON_ARROWS(15, 65535,15, new Item(40, 15), new Item(53, 15), new Item(884, 15)),
        STEEL_ARROWS(30, 65535,25, new Item(41, 15), new Item(53, 15), new Item(886, 15)),
        MITHRIL_ARROWS(45, 65535,35, new Item(42, 15), new Item(53, 15), new Item(888, 15)),
        ADAMANT_ARROWS(60, 65535,45, new Item(43, 15), new Item(53, 15), new Item(890, 15)),
        RUNE_ARROWS(75, 65535,55, new Item(44, 15), new Item(53, 15), new Item(892, 15)),
        DRAGON_ARROWS(90, 65535,65, new Item(11237, 15), new Item(53, 15), new Item(11212, 15))
        ;
        int level, anim, xp;
        Item[] items; //[0] = used, [1] = usedOn, [2] = produce
        fletchArrowData(int level, int anim, int xp, Item... items) {
            this.level = level;
            this.anim = anim;
            this.xp = xp;
            this.items = items;
        }
    }
    public static fletchArrowData getArrowData(Player player, Item used, Item usedOn) {
        for(fletchArrowData data : fletchArrowData.values()) {
            if((used.getId() == data.items[0].getId() && used.getAmount() >= data.items[0].getAmount() && usedOn.getId() == data.items[1].getId() && usedOn.getAmount() >= data.items[1].getAmount())
            || (used.getId() == data.items[1].getId() && used.getAmount() >= data.items[1].getAmount() && usedOn.getId() == data.items[0].getId() && usedOn.getAmount() >= data.items[0].getAmount())) {
                return player.getInventory().contains(new Item[]{data.items[0], data.items[1]}) ? data : null;
            }
        }
        return null;
    }

    private enum fletchBowData {
        OAK_SHORTBOW_U(20, 1248,70, KNIFE, new Item(1521), new Item(54)),
        OAK_LONGBOW_U(25, 1248,80, KNIFE, new Item(1521), new Item(56)),
        WILLOW_SHORTBOW_U(35, 1248,100, KNIFE, new Item(1519), new Item(60)),
        WILLOW_LONGBOW_U(40, 1248,110, KNIFE, new Item(1519), new Item(58)),
        MAPLE_SHORTBOW_U(50, 1248,130, KNIFE, new Item(1517), new Item(64)),
        MAPLE_LONGBOW_U(55, 1248,140, KNIFE, new Item(1517), new Item(62)),
        YEW_SHORTBOW_U(65, 1248,165, KNIFE, new Item(1515), new Item(68)),
        YEW_LONGBOW_U(70, 1248,185, KNIFE, new Item(1515), new Item(66)),
        MAGIC_SHORTBOW_U(80, 1248,210, KNIFE, new Item(1513), new Item(70)),
        MAGIC_LONGBOW_U(85, 1248,230, KNIFE, new Item(1513), new Item(72)),
        OAK_SHORTBOW(20, 6679,70, new Item(54), new Item(1777), new Item(843)),
        OAK_LONGBOW(25, 6685,80, new Item(56), new Item(1777), new Item(845)),
        WILLOW_SHORTBOW(35, 6680,100, new Item(60), new Item(1777), new Item(849)),
        WILLOW_LONGBOW(40, 6686,110, new Item(58), new Item(1777), new Item(847)),
        MAPLE_SHORTBOW(50, 6681,130, new Item(64), new Item(1777), new Item(853)),
        MAPLE_LONGBOW(55, 6687,140, new Item(62), new Item(1777), new Item(851)),
        YEW_SHORTBOW(65, 6682,165, new Item(68), new Item(1777), new Item(857)),
        YEW_LONGBOW(70, 6688,185, new Item(66), new Item(1777), new Item(855)),
        MAGIC_SHORTBOW(80, 6683,210, new Item(72), new Item(1777), new Item(861)),
        MAGIC_LONGBOW(85, 6689,230, new Item(70), new Item(1777), new Item(859))
        ;
        int level, anim, xp;
        Item[] items; //[0] = used, [1] = usedOn, [2] = produce
        fletchBowData(int level, int anim, int xp, Item... items) {
            this.level = level;
            this.anim = anim;
            this.xp = xp;
            this.items = items;
        }
    }
    public static ArrayList<fletchBowData> getBowData(Player player, Item used, Item usedOn) {
        ArrayList<fletchBowData> fletchData = new ArrayList<>();
        for(fletchBowData data : fletchBowData.values()) {
            if((used.getId() == data.items[0].getId() && used.getAmount() >= data.items[0].getAmount() && usedOn.getId() == data.items[1].getId() && usedOn.getAmount() >= data.items[1].getAmount())
                    || (used.getId() == data.items[1].getId() && used.getAmount() >= data.items[1].getAmount() && usedOn.getId() == data.items[0].getId() && usedOn.getAmount() >= data.items[0].getAmount())) {
                if(player.getInventory().contains(new Item[]{data.items[0], data.items[1]}))
                    fletchData.add(data);
            }
        }
        return fletchData.isEmpty() ? null : fletchData;
    }

    @EventHandler
    public void onItemUsage(PlayerItemOnItemUsage event) {
        arrowData = getArrowData(event.getPlayer(), event.getUsed(), event.getUsedOn());
        if(arrowData != null) {
            event.getPlayer().setWalkToTask(null);
            TaskManager.cancelTasks(event.getPlayer());
            event.getPlayer().getPacketSender().sendMakeItemOption(arrowData.items[2]);
        }
        bowData = getBowData(event.getPlayer(), event.getUsed(), event.getUsedOn());
        if(bowData != null) {
            event.getPlayer().setWalkToTask(null);
            TaskManager.cancelTasks(event.getPlayer());
                Item[] displayItems = new Item[bowData.size()];
                for(int i = 0; i < bowData.size(); i++)
                    displayItems[i] = bowData.get(i).items[2];
                event.getPlayer().getPacketSender().sendMakeItemOption(displayItems);
        }
    }

    @EventHandler
    public void onButtonClick(PlayerButtonClickEvent event) {
        if(PacketSender.itemOptions.isEmpty()) {
            return;
        }

        if(arrowData != null) {
            int amount = 0;
            if(event.getButton() == 2799)
                amount = 1;
            else if (event.getButton() == 2798)
                amount = 5;
            else if (event.getButton() == 1748) //We need make X?
                amount = 10;
            else if (event.getButton() == 1747) { //Make all aka 28!!
                int itemOneAmount = event.getPlayer().getInventory().getAmount(arrowData.items[0].getId());
                int itemTwoAmount = event.getPlayer().getInventory().getAmount(arrowData.items[1].getId());
                int itemThreeAmount = event.getPlayer().getInventory().getAmount(arrowData.items[2].getId());
                amount = arrowData.items[0].equals(KNIFE) ? itemTwoAmount:
                itemOneAmount > itemTwoAmount ? itemTwoAmount / itemThreeAmount : itemOneAmount / itemThreeAmount;
            }
            initiate(event, arrowData, amount);
            arrowData = null;
        }

        if(bowData != null) {
            int amount = 0;
            int id = event.getButton() >= 8875 && event.getButton() <= 8878 ? 1 : 0;
            if(event.getButton() == 2799)
                amount = 1;
            else if (event.getButton() == 2798)
                amount = 5;
            else if (event.getButton() == 1748) //We need make X?
                amount = 10;
            else if (event.getButton() == 1747) { //Make all aka 28!!
                int itemOneAmount = event.getPlayer().getInventory().getAmount(bowData.get(id).items[0].getId());
                int itemTwoAmount = event.getPlayer().getInventory().getAmount(bowData.get(id).items[1].getId());
                int itemThreeAmount = event.getPlayer().getInventory().getAmount(bowData.get(id).items[2].getId());
                amount = bowData.get(id).items[0].equals(KNIFE) ? itemTwoAmount:
                        itemOneAmount > itemTwoAmount ? itemTwoAmount / itemThreeAmount : itemOneAmount / itemThreeAmount;
            }
            else if(event.getButton() == 8874 || event.getButton() == 8878)
                amount = 1;
            else if (event.getButton() == 8873 || event.getButton() == 8877)
                amount = 5;
            else if (event.getButton() == 8872 || event.getButton() == 8876) //We need make X?
                amount = 10;
            else if (event.getButton() == 8871 || event.getButton() == 8875) { //Make all aka 28!!
                int itemOneAmount = event.getPlayer().getInventory().getAmount(bowData.get(id).items[0].getId());
                int itemTwoAmount = event.getPlayer().getInventory().getAmount(bowData.get(id).items[1].getId());
                int itemThreeAmount = event.getPlayer().getInventory().getAmount(bowData.get(id).items[2].getId());
                amount = bowData.get(id).items[0].equals(KNIFE) ? itemTwoAmount:
                        itemOneAmount > itemTwoAmount ? itemTwoAmount / itemThreeAmount : itemOneAmount / itemThreeAmount;
            }
            initiate(event, bowData.get(id), amount);
            bowData = null;
        }
    }

        private <T extends PlayerButtonClickEvent> void initiate(T event, fletchArrowData data, int amount) {
            TaskManager.cancelTasks(event.getPlayer());
            event.getPlayer().getPacketSender().sendInterfaceRemoval();
            if(event.getPlayer().getSkillManager().getCurrentLevel(Skill.FLETCHING) < data.level) {
                event.getPlayer().getPacketSender().sendMessage("You need level " + data.level + " fletching to fletch this item!");
                return;
            }
            event.getPlayer().performAnimation(new Animation(data.anim));
            Task task = new Task(1, event.getPlayer(), true) {
            int cycle = 3, count = Math.min(amount, 28);
            Player p = event.getPlayer();
            @Override
            protected void execute() {
                /* Stops action! */
                if (p.getMovementQueue().getMovementStatus().equals(MovementStatus.DISABLED)
                || count < 1) {
                    stop();
                }
                else if (data.items[0].equals(KNIFE) && !p.getInventory().contains(data.items[0])) {
                    count = 0;
                    p.getPacketSender().sendMessage("You are missing a knife!");
                    stop();
                }
                else if(!p.getInventory().contains(new Item[]{data.items[0], data.items[1]})) {
                    p.getPacketSender().sendMessage("You need " + data.items[0].getAmount() + " " + data.items[0].getDefinition().getName().toLowerCase() + " and " + data.items[1].getAmount() + " " + data.items[1].getDefinition().getName().toLowerCase());
                    stop();
                }
                else if(data.items[0].getDefinition().isStackable() && data.items[1].getDefinition().isStackable()) {
                    if(data.items[0].getAmount() < p.getInventory().getAmount(data.items[0].getId()) &&
                    data.items[1].getAmount() < p.getInventory().getAmount(data.items[1].getId()) &&
                    p.getInventory().isFull())
                    stop();
                }
                /* Cycle action! */
                if(cycle > 0)
                    cycle--;
                else {
                    count--;
                    if(!data.items[0].equals(KNIFE))
                        p.getInventory().delete(data.items[0]);
                    p.getInventory().delete(data.items[1]);
                    p.getInventory().add(data.items[2]);
                    p.getSkillManager().addExperience(Skill.FLETCHING, data.xp * data.items[2].getAmount());
                    p.getPacketSender().sendMessage("You fletch " + (data.items[2].getAmount() > 1 ? "some" : "one") + " " + data.items[2].getDefinition().getName().toLowerCase());
                    cycle = 3;
                    if(count > 0)
                        p.performAnimation(new Animation(data.anim));
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
    private <T extends PlayerButtonClickEvent> void initiate(T event, fletchBowData data, int amount) {
        TaskManager.cancelTasks(event.getPlayer());
        event.getPlayer().getPacketSender().sendInterfaceRemoval();
        if(event.getPlayer().getSkillManager().getCurrentLevel(Skill.FLETCHING) < data.level) {
            event.getPlayer().getPacketSender().sendMessage("You need level " + data.level + " fletching to fletch this item!");
            return;
        }
        event.getPlayer().performAnimation(new Animation(data.anim));
        Task task = new Task(1, event.getPlayer(), true) {
            int cycle = 3, count = Math.min(amount, 28);
            Player p = event.getPlayer();
            @Override
            protected void execute() {
                /* Stops action! */
                if (p.getMovementQueue().getMovementStatus().equals(MovementStatus.DISABLED)
                        || count < 1) {
                    stop();
                }
                else if (data.items[0].equals(KNIFE) && !p.getInventory().contains(data.items[0])) {
                    count = 0;
                    p.getPacketSender().sendMessage("You are missing a knife!");
                    stop();
                }
                else if(!p.getInventory().contains(new Item[]{data.items[0], data.items[1]})) {
                    p.getPacketSender().sendMessage("You are out of " + data.items[1].getDefinition().getName().toLowerCase());
                    stop();
                }
                /* Cycle action! */
                if(cycle > 0)
                    cycle--;
                else {
                    count--;
                    if(!data.items[0].equals(KNIFE))
                        p.getInventory().delete(data.items[0]);
                    p.getInventory().delete(data.items[1]);
                    p.getInventory().add(data.items[2]);
                    p.getSkillManager().addExperience(Skill.FLETCHING, data.xp * data.items[2].getAmount());
                    p.getPacketSender().sendMessage("You fletch " + (data.items[2].getAmount() > 1 ? "some" : "one") + " " + data.items[2].getDefinition().getName().toLowerCase());
                    cycle = 3;
                    if(count > 0)
                        p.performAnimation(new Animation(data.anim));
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
