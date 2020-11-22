package net.dodian.plugins.impl.skill;

import net.dodian.game.events.EventHandler;
import net.dodian.game.events.EventListener;
import net.dodian.game.events.impl.player.interact.npc.PlayerNpcEvent;
import net.dodian.game.events.impl.player.interact.npc.PlayerNpcFirstClickEvent;
import net.dodian.game.events.impl.player.interact.npc.PlayerNpcSecondClickEvent;
import net.dodian.game.events.impl.player.interact.object.PlayerObjectEvent;
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
public class Fishing implements EventListener {
    private enum toolData {
        NET(303, -1, new Animation(621)),
        FLY_FISHING_ROD(309, 314, new Animation(622)),
        LOBSTER_POT(301, -1, new Animation(619)),
        HARPOON(311, -1, new Animation(618))
        ;
        int id, bait;
        Animation animation;
        toolData(int id, int bait, Animation animation) {
            this.id = id;
            this.bait = bait;
            this.animation = animation;
        }
    }
    public static toolData getTool(Player player) {
        for(toolData data : toolData.values()) {
            if(player.getInventory().contains(data.id) || player.getEquipment().contains(data.id)) {
                return data;
            }
        }
        return null;
    }

    private enum fishingData {
        SHRIMPS(1,toolData.NET, 1, 110, 1200, 317, 1510),
        TROUT(2,toolData.FLY_FISHING_ROD, 20, 200, 1800, 335, 1510),
        LOBSTER(1,toolData.LOBSTER_POT, 40, 440, 2400, 377, 1511),
        SWORDFISH(2,toolData.HARPOON, 50, 650, 3000, 371, 1511),
        MONKFISH(1,toolData.NET, 60, 780, 2400, 7944, 1514),
        SHARK(2,toolData.HARPOON, 70, 1100, 4200, 383, 1514),
        SEA_TURTLE(1,toolData.LOBSTER_POT, 85, 1450, 4800, 395, 1517),
        MANTA_RAY(2,toolData.HARPOON, 95, 1900, 5400, 389, 1517)
        ;
        toolData tool;
        int click, level, xp, time, produceItem, spotId;
        fishingData(int click, toolData tool, int level, int xp, int time, int produceItem, int spotId) {
            this.click = click;
            this.tool = tool;
            this.level = level;
            this.xp = xp;
            this.time = time;
            this.produceItem = produceItem;
            this.spotId = spotId;
        }
    }
    public static fishingData getSpot(int click, int id) {
        for(fishingData data : fishingData.values()) {
            if(data.click == click && data.spotId == id) {
                return data;
            }
        }
        return null;
    }

    @EventHandler
    public void onFirstClickNpc(PlayerNpcFirstClickEvent event) {
        fishingData fish = getSpot(1, event.getNpc().getId());
        if(fish == null) {
            return;
        }
        if(fish.level > event.getPlayer().getSkillManager().getCurrentLevel(Skill.FISHING)) {
            event.getPlayer().getPacketSender().sendMessage("You need level " + fish.level + " fishing to fish from the "+ event.getNpc().getDefinition().getName() +"!");
            return;
        }
        Item tool = new Item(fish.tool.id, 1);
        if(!event.getPlayer().getInventory().contains(tool)) {
            event.getPlayer().getPacketSender().sendMessage("You need a " + tool.getDefinition().getName().toLowerCase() + " to fish from this "+event.getNpc().getDefinition().getName().toLowerCase()+"!");
            return;
        }
        else if(fish.tool.bait != -1 && !event.getPlayer().getInventory().contains(fish.tool.bait)) {
            event.getPlayer().getPacketSender().sendMessage("You have ran out of " + new Item(fish.tool.bait).getDefinition().getName().toLowerCase() + ".");
            return;
        }
        TaskManager.cancelTasks(event.getPlayer()); //Might cause issues to other tasks?
        initiate(event, fish);
    }

    @EventHandler
    public void onSecondClickNpc(PlayerNpcSecondClickEvent event) {
        fishingData fish = getSpot(2, event.getNpc().getId());
        if(fish == null) {
            return;
        }
        if(fish.level > event.getPlayer().getSkillManager().getCurrentLevel(Skill.FISHING)) {
            event.getPlayer().getPacketSender().sendMessage("You need level " + fish.level + " fishing to fish from the "+ event.getNpc().getDefinition().getName() +"!");
            return;
        }
        Item tool = new Item(fish.tool.id, 1);
        if(!event.getPlayer().getInventory().contains(tool)) {
            event.getPlayer().getPacketSender().sendMessage("You need a " + tool.getDefinition().getName().toLowerCase() + " to fish from this "+event.getNpc().getDefinition().getName().toLowerCase()+"!");
            return;
        }
        else if(fish.tool.bait != -1 && !event.getPlayer().getInventory().contains(fish.tool.bait)) {
            event.getPlayer().getPacketSender().sendMessage("You have ran out of " + new Item(fish.tool.bait).getDefinition().getName().toLowerCase() + ".");
            return;
        }
        TaskManager.cancelTasks(event.getPlayer()); //Might cause issues to other tasks?
        initiate(event, fish);
    }

    private <T extends PlayerNpcEvent> void initiate(T event, fishingData fish) {
        event.getPlayer().performAnimation(fish.tool.animation);
        event.getPlayer().getPacketSender().sendMessage("You start to fish the " + event.getNpc().getDefinition().getName().toLowerCase() + "...");
        Task task = new Task(1, event.getPlayer(), true) {
            double cycle = fish.time / 600D;
            @Override
            protected void execute() {
                /* Stops action! */
                if (event.getPlayer().getInventory().isFull() ||
                        event.getPlayer().getMovementQueue().getMovementStatus().equals(MovementStatus.DISABLED)) {
                    stop();
                }
                /* Initiate check for tool + bait! */
                Item tool = new Item(fish.tool.id, 1);
                if(!event.getPlayer().getInventory().contains(tool)) {
                    event.getPlayer().getPacketSender().sendMessage("You need a " + tool.getDefinition().getName().toLowerCase() + " to fish from this "+event.getNpc().getDefinition().getName().toLowerCase()+"!");
                    stop();
                }
                else if(fish.tool.bait != -1 && !event.getPlayer().getInventory().contains(fish.tool.bait)) {
                    event.getPlayer().getPacketSender().sendMessage("You have ran out of " + new Item(fish.tool.bait).getDefinition().getName().toLowerCase() + ".");
                    stop();
                }
                /* Cycle action! */
                if((int)cycle % 3 == 0)
                    event.getPlayer().performAnimation(fish.tool.animation);
                if(cycle > 0)
                    cycle--;
                else {
                    if(fish.produceItem == 335 || fish.produceItem == 331) { //Trout / salmon!
                        fish.produceItem = event.getPlayer().getSkillManager().getCurrentLevel(Skill.FISHING) >= 30 && Misc.getRandom(6) < 3 ? 331 : 335;
                    }
                    Item item = new Item(fish.produceItem, 1);
                    if(fish.tool.bait != -1)
                        event.getPlayer().getInventory().delete(fish.tool.bait, 1);
                    event.getPlayer().getInventory().add(item);
                    event.getPlayer().getSkillManager().addExperience(Skill.FISHING, fish.produceItem == 331 ? fish.xp + 100 : fish.xp);
                    event.getPlayer().getPacketSender().sendMessage("You mine some " + item.getDefinition().getName().toLowerCase() + ".");
                    cycle = fish.time / 600D;
                    if(Misc.getRandom(49) == 0) {
                        event.getPlayer().getPacketSender().sendMessage("You take a rest");
                        stop();
                    }
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
