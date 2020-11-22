package net.dodian.plugins.impl.skill;

import net.dodian.game.events.EventHandler;
import net.dodian.game.events.EventListener;
import net.dodian.game.events.impl.player.interact.PlayerButtonClickEvent;
import net.dodian.game.events.impl.player.interact.item.PlayerItemOnItemUsage;
import net.dodian.game.events.impl.player.interact.npc.PlayerNpcFirstClickEvent;
import net.dodian.game.events.impl.player.interact.object.PlayerItemOnObjectEvent;
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
    private static Item CHISEL = new Item(1755);
    private static final Item NEEDLE = new Item(1733);
    private static final Item THREAD = new Item(1734);
    private static final Item RING_MOULD = new Item(1592);
    private static final Item NECKLACE_MOULD = new Item(1597);
    private static final Item AMULET_MOULD = new Item(1595);
    /* Crafting animations */
    private static final Animation LEATHER_ANIMATION = new Animation(1249);
    private static Animation GOLD_ANIMATION = new Animation(899);
    /* Crafting checks */
    private ArrayList<leatherCraft> craftLeatherCheck = null;
    private gemCraft craftGemCheck = null;

    private enum leatherTan {
        REGULAR_LEATHER("Soft leather",50, 1739, 1741),
        EMPTY_ONE("",-1, -1, -1),
        EMPTY_TWO("",-1, -1, -1),
        EMPTY_THREE("",-1, -1, -1),
        GREEN_LEATHER("Green d'hide",1000, 1753, 1745),
        BLUE_LEATHER("Blue d'hide", 2000, 1751, 2505),
        RED_LEATHER("Red d'hide",5000, 1749, 2507),
        BLACK_LEATHER("Black d'hide",10000, 1747, 2509)
        ;
        String name;
        int price, required, received;
        leatherTan(String name, int... items) {
            this.name = name;
            this.price = items[0];
            this.required = items[1];
            this.received = items[2];
        }
    }
    public leatherTan getLeatherData(Item item) {
        for(leatherTan data : leatherTan.values()) {
            if(data.received == item.getId()) {
                return data;
            }
        }
        return null;
    }

    private enum leatherCraft {
        LEATHER_BODY(leatherTan.REGULAR_LEATHER, 14, 264, new Item(1129)),
        LEATHER_GLOVES(leatherTan.REGULAR_LEATHER, 1, 144, new Item(1059)),
        LEATHER_BOOTS(leatherTan.REGULAR_LEATHER, 7, 160, new Item(1061)),
        LEATHER_VAMBRACES(leatherTan.REGULAR_LEATHER, 11, 232, new Item(1063)),
        LEATHER_CHAPS(leatherTan.REGULAR_LEATHER, 18, 304, new Item(1095)),
        LEATHER_COIF(leatherTan.REGULAR_LEATHER, 38, 416, new Item(1169)),
        LEATHER_COWL(leatherTan.REGULAR_LEATHER, 9, 168, new Item(1167)),
        GREEN_LEATHER_VAMBRACES(leatherTan.GREEN_LEATHER, 50, 970, new Item(1065)),
        GREEN_LEATHER_CHAPS(leatherTan.GREEN_LEATHER, 54, 970, new Item(1099)),
        GREEN_LEATHER_BODY(leatherTan.GREEN_LEATHER, 58, 970, new Item(1135)),
        BLUE_LEATHER_VAMBRACES(leatherTan.BLUE_LEATHER, 62, 1580, new Item(2487)),
        BLUE_LEATHER_CHAPS(leatherTan.BLUE_LEATHER, 66, 1580, new Item(2493)),
        BLUE_LEATHER_BODY(leatherTan.BLUE_LEATHER, 70, 1580, new Item(2499)),
        RED_LEATHER_VAMBRACES(leatherTan.RED_LEATHER, 73, 2460, new Item(2489)),
        RED_LEATHER_CHAPS(leatherTan.RED_LEATHER, 76, 2460, new Item(2495)),
        RED_LEATHER_BODY(leatherTan.RED_LEATHER, 79, 2460, new Item(2501)),
        BLACK_LEATHER_VAMBRACES(leatherTan.BLACK_LEATHER, 82, 3720, new Item(2491)),
        BLACK_LEATHER_CHAPS(leatherTan.BLACK_LEATHER, 85, 3720, new Item(2497)),
        BLACK_LEATHER_BODY(leatherTan.BLACK_LEATHER, 88, 3720, new Item(2503))
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

    private enum gemCraft {
        SAPPHIRE(1623, 20, 888, 300, new Item(1607)),
        EMERALD(1621, 27, 889, 408, new Item(1605)),
        RUBY(1619, 34, 887, 510, new Item(1603)),
        DIAMOND(1617, 43, 886, 648, new Item(1601)),
        DRAGONSTONE(1631, 55, 885, 822, new Item(1615)),
        ONYX(6571, 67, 2717,1008, new Item(6573))
        ;
        int itemId, level, anim, xp;
        Item produce;
        gemCraft(int itemId, int level, int anim, int xp, Item produce) {
            this.itemId = itemId;
            this.level = level;
            this.anim = anim;
            this.xp = xp;
            this.produce = produce;
        }
    }
    public gemCraft getGemData(Item item) {
        for(gemCraft data : gemCraft.values()) {
            if(data.itemId == item.getId()) {
                return data;
            }
        }
        return null;
    }

    @EventHandler
    public void onItemUsage(PlayerItemOnItemUsage event) {
        boolean gotNeedle = event.getUsed().getId() == NEEDLE.getId() || event.getUsedOn().getId() == NEEDLE.getId();
        boolean gotChisel = event.getUsed().getId() == CHISEL.getId() || event.getUsedOn().getId() == CHISEL.getId();
        if(gotNeedle) {
            Item otherItem = event.getUsed().getId() == NEEDLE.getId() ? event.getUsedOn() : event.getUsed();
            leatherTan leatherData = getLeatherData(otherItem);
            if(leatherData == leatherTan.REGULAR_LEATHER) {
                event.getPlayer().setWalkToTask(null);
                TaskManager.cancelTasks(event.getPlayer());
                craftLeatherCheck = getLeatherData(leatherData);
                event.getPlayer().getPacketSender().sendInterface(2311);
            } else if (leatherData != null && leatherData.price != -1) {
                event.getPlayer().setWalkToTask(null);
                TaskManager.cancelTasks(event.getPlayer());
                craftLeatherCheck = getLeatherData(leatherData);
                if(!craftLeatherCheck.isEmpty()) {
                    Item[] item = new Item[craftLeatherCheck.size()];
                    for(int i = 0; i < craftLeatherCheck.size(); i++)
                        item[i] = craftLeatherCheck.get(i).produce;
                    event.getPlayer().getPacketSender().sendMakeItemOption(item);
                }
            }
         } else if(gotChisel) {
            Item otherItem = event.getUsed().getId() == CHISEL.getId() ? event.getUsedOn() : event.getUsed();
            gemCraft craftData = getGemData(otherItem);
            if(craftData != null) {
                event.getPlayer().setWalkToTask(null);
                TaskManager.cancelTasks(event.getPlayer());
                event.getPlayer().getPacketSender().sendMakeItemOption(craftData.produce);
                craftGemCheck = craftData;
            }
        }
    }

    @EventHandler
    public void onButtonClick(PlayerButtonClickEvent event) {
        if(event.getButton() == 2422) //Leather craft interface close!
            event.getPlayer().getPacketSender().sendInterfaceRemoval();
        else if(craftGemCheck != null) {
            int amount = event.getButton() == 2799 ? 1 :
            event.getButton() == 2798 ? 5 :
            event.getButton() == 1748 ? 10 :
            event.getButton() == 1747 ? event.getPlayer().getInventory().getAmount(craftGemCheck.itemId) : 0;
            gemCraft(event, craftGemCheck, amount);
            craftGemCheck = null;
        } else if (craftLeatherCheck != null && craftLeatherCheck.get(0).leather == leatherTan.REGULAR_LEATHER) {
            int amount = 0, index = event.getButton() >= 8633 && event.getButton() <= 8653 ? event.getButton() - 8633 : -1;
            if(index == -1) {
                return;
            }
            if(index % 3 == 2)
                amount = 1;
            else if (index % 3 == 1)
                amount = 5;
            else if (index % 3 == 0)
                amount = 10;
            leatherCraft craft = craftLeatherCheck.get(index / 3);
            craftLeather(event, craft, amount);
        } else if (craftLeatherCheck != null) {
            int amount = 0, index = event.getButton() >= 8886 && event.getButton() <= 8897 ? event.getButton() - 8886 : -1;
            if(index == -1) {
                return;
            }
            leatherCraft craft = craftLeatherCheck.get(index / 4);
            if(index % 4 == 3)
                amount = 1;
            else if (index % 4 == 2)
                amount = 5;
            else if (index % 4 == 1)
                amount = 10;
            else if (index % 4 == 0)
                amount = event.getPlayer().getInventory().getAmount(craft.leather.received);
            craftLeather(event, craft, amount);
        } else {
            int amount = 0, index = event.getButton() >= 14793 && event.getButton() <= 14824 ? event.getButton() - 14793 : -1;
            leatherTan tan = index == -1 ? null : leatherTan.values()[index%8];
            if(index == -1 || tan == null || tan.price == -1 || event.getPlayer().getInventory().getAmount(tan.required) == 0) {
                if(tan != null && tan.price != -1 && event.getPlayer().getInventory().getAmount(tan.required) == 0)
                    event.getPlayer().getPacketSender().sendMessage("You do not have any leather of that kind to tan!");
                return;
            }
            if(index / 8 == 3)
                amount = 1;
            else if(index / 8 == 2)
                amount = 5;
            else if(index / 8 == 1)
                amount = 10;
            else if(index / 8 == 0)
                amount = event.getPlayer().getInventory().getAmount(tan.required);
            if(event.getPlayer().getInventory().getAmount(tan.required) < amount) {
                amount = event.getPlayer().getInventory().getAmount(tan.required);
                event.getPlayer().getPacketSender().sendMessage("You have ran out of leather!");
            }
            int cost = amount * tan.price;
            if(event.getPlayer().getInventory().getAmount(995) < cost) {
                amount = event.getPlayer().getInventory().getAmount(995) / tan.price;
                event.getPlayer().getPacketSender().sendMessage(amount == 0 ? "You do not have enough coins to tan!" : "You have ran out of coins!");
            }
            if(amount > 0)
            event.getPlayer().getInventory().delete(995, amount * tan.price);
            event.getPlayer().getInventory().delete(tan.required, amount);
            event.getPlayer().getInventory().add(tan.received, amount);
        }
    }

    @EventHandler
    public void onItemOnObject(PlayerItemOnObjectEvent event) {
        if(event.getItem().getId() != 2357 || (event.getObject().getId() != 3994 && event.getObject().getId() != 11666)) {
            return;
        }
        event.getPlayer().getPacketSender().sendInterface(4161);
    }

    @EventHandler
    public void onFirstClickNpc(PlayerNpcFirstClickEvent event) {
        if(event.getNpc().getId() == 5809) {
            /* Tanner stuff! */
            for(leatherTan data : leatherTan.values()) {
                event.getPlayer().getPacketSender().sendString(14777 + data.ordinal(), data.name);
                event.getPlayer().getPacketSender().sendString(14785 + data.ordinal(), data.price != -1 ? data.price + " coins" : "");
                event.getPlayer().getPacketSender().sendInterfaceModel(14769 + data.ordinal(), data.required, 250);
            }
            event.getPlayer().getPacketSender().sendInterface(14670);
        }
    }

    private <T extends PlayerButtonClickEvent> void craftLeather(T event, leatherCraft data, int amount) {
            TaskManager.cancelTasks(event.getPlayer());
            event.getPlayer().getPacketSender().sendInterfaceRemoval();
            if(event.getPlayer().getSkillManager().getCurrentLevel(Skill.CRAFTING) < data.level) {
                event.getPlayer().getPacketSender().sendMessage("You need level " + data.level + " crafting to craft this item!");
                return;
            }
            event.getPlayer().performAnimation(LEATHER_ANIMATION);
            Task task = new Task(1, event.getPlayer(), true) {
            int cycle = 3, count = amount;
            Player p = event.getPlayer();
            @Override
            protected void execute() {
                /* Stops action! */
                if (p.getMovementQueue().getMovementStatus().equals(MovementStatus.DISABLED)
                || count < 1) {
                    stop();
                }
                if (!p.getInventory().contains(THREAD) || !p.getInventory().contains(NEEDLE)) {
                    p.getPacketSender().sendMessage("You are missing a " + (!p.getInventory().contains(THREAD) ? "thread" : "needle") + "!");
                    stop();
                }
                if (!p.getInventory().contains(data.leather.received)) {
                    p.getPacketSender().sendMessage("You have ran out of "+ new Item(data.leather.received).getDefinition().getName().toLowerCase() +"!");
                    stop();
                }
                /* Cycle action! */
                if(cycle > 0)
                    cycle--;
                else if (!isStopped()) {
                    count--;
                    p.getInventory().delete(THREAD);
                    //TODO: Add chance for needle to break?
                    if(Misc.getRandom(10) < 1) //1:11 chance for needle to break!
                        p.getInventory().delete(NEEDLE);
                    p.getInventory().delete(new Item(data.leather.received, 1));
                    p.getInventory().add(data.produce);
                    p.getSkillManager().addExperience(Skill.CRAFTING, data.xp);
                    p.getPacketSender().sendMessage("You crafted a " + data.produce.getDefinition().getName().toLowerCase());
                    cycle = 3;
                    if(count > 0)
                        p.performAnimation(LEATHER_ANIMATION);
                }
            }
                @Override
                public void stop() {
                    setEventRunning(false);
                    cycle = 0;
                    event.getPlayer().performAnimation(new Animation(65535));
                }
        };
        TaskManager.submit(task);
    }

    private <T extends PlayerButtonClickEvent> void gemCraft(T event, gemCraft craft, int amount) {
        event.getPlayer().getPacketSender().sendInterfaceRemoval();
        if(event.getPlayer().getSkillManager().getCurrentLevel(Skill.CRAFTING) < craft.level) {
            event.getPlayer().getPacketSender().sendMessage("You need level " + craft.level + " crafting to cut this gem!");
            return;
        }
        event.getPlayer().performAnimation(new Animation(craft.anim));
        Task task = new Task(1, event.getPlayer(), true) {
            int cycle = 3, count = amount;
            Player p = event.getPlayer();
            @Override
            protected void execute() {
                /* Stops action! */
                if (p.getMovementQueue().getMovementStatus().equals(MovementStatus.DISABLED)
                        || count < 1) {
                    stop();
                }
                else if (!p.getInventory().contains(CHISEL)) {
                    p.getPacketSender().sendMessage("You are missing a chisel!");
                    stop();
                }
                else if(!p.getInventory().contains(craft.itemId)) {
                    p.getPacketSender().sendMessage("You are out of " + new Item(craft.itemId).getDefinition().getName().toLowerCase());
                    stop();
                }
                /* Cycle action! */
                if(cycle > 0)
                    cycle--;
                else {
                    count--;
                    p.getInventory().delete(new Item(craft.itemId));
                    p.getInventory().add(craft.produce);
                    p.getSkillManager().addExperience(Skill.CRAFTING, craft.xp);
                    p.getPacketSender().sendMessage("You cut the " + new Item(craft.itemId).getDefinition().getName().toLowerCase() + " into a " + craft.produce.getDefinition().getName().toLowerCase() + ".");
                    cycle = 3;
                    if(count > 0)
                        p.performAnimation(new Animation(craft.anim));
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
