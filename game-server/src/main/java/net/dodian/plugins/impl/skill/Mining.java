package net.dodian.plugins.impl.skill;

import net.dodian.game.events.EventHandler;
import net.dodian.game.events.EventListener;
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
public class Mining implements EventListener {
    private enum pickaxeData {
        BRONZE(1265, 1, new Animation(625), 1.03),
        IRON(1267, 1, new Animation(626), 1.05),
        STEEL(1269, 6, new Animation(627), 1.09),
        MITHRIL(1273, 21, new Animation(628), 1.13),
        ADAMANT(1271, 31, new Animation(629), 1.16),
        RUNE(1275, 41, new Animation(624), 1.20),
        DRAGON(15259, 61, new Animation(624), 1.25)
        ;
        int id, level;
        Animation animation;
        double speed;
        pickaxeData(int id, int level, Animation animation, double speed) {
            this.id = id;
            this.animation = animation;
            this.level = level;
            this.speed = speed;
        }
    }
    public static pickaxeData getPickaxe(Player player) {
        pickaxeData axe = null;
        for(pickaxeData data : pickaxeData.values()) {
            if(player.getInventory().contains(data.id) || player.getEquipment().contains(data.id)) {
                axe = player.getSkillManager().getCurrentLevel(Skill.MINING) >= data.level ? data : axe;
            }
        }
        return axe;
    }
    public static Animation pickaxeAnimation(pickaxeData data) {
        return data.animation;
    }

    private enum rockData {
        ESSENCE(1, 50, 900, 1436, 7471),
        COPPER(1, 110, 2000, 436, 7484),
        TIN(1, 110, 2000, 438, 7485)
        ;

        int level, xp, time, produceItem;
        int[] object;
        rockData(int level, int xp, int time, int produceItem, int... object) {
            this.level = level;
            this.xp = xp;
            this.time = time;
            this.produceItem = produceItem;
            this.object = object;
        }
    }
    public rockData getRock(int id) {
        for(rockData data : rockData.values()) {
            for (int i = 0; i < data.object.length; i++)
                if(data.object[i] == id)
                    return data;
        }
        return null;
    }

    @EventHandler
    public void onFirstClickObject(PlayerObjectFirstClickEvent event) {
        rockData rock = getRock(event.getObject().getId());
        if(rock == null) {
            return;
        }
        pickaxeData pickaxe = getPickaxe(event.getPlayer());
        if(pickaxe == null) {
            event.getPlayer().getPacketSender().sendMessage("You do not have a suitable pickaxe for your level to use!");
            return;
        }
        TaskManager.cancelTasks(event.getPlayer()); //Might cause issues to other tasks?
        initiate(event, rock, pickaxe);
    }

    private <T extends PlayerObjectEvent> void initiate(T event, rockData rock, pickaxeData pickaxe) {
        final pickaxeData[] emotePickaxe = {pickaxe};
        event.getPlayer().performAnimation(pickaxe.animation);
        event.getPlayer().getPacketSender().sendMessage("You swing your pickaxe at the " + new GameObject(rock.object[0], event.getObject().getPosition()).getDefinition().getName().toLowerCase() + "...");
        Task task = new Task(1, event.getPlayer(), true) {
            int cycle = (int) getMiningSpeed(event.getPlayer(), pickaxe, rock.time) / 900;
            @Override
            protected void execute() {
                /* Stops action! */
                if (event.getPlayer().getInventory().isFull() ||
                event.getPlayer().getMovementQueue().getMovementStatus().equals(MovementStatus.DISABLED)) {
                    event.getPlayer().performAnimation(new Animation(65535));
                    stop();
                }
                /* Cycle action! */
                emotePickaxe[0] = getPickaxe(event.getPlayer()); //Need to check if a player has a different pickaxe!
                if(cycle % 3 == 0)
                    event.getPlayer().performAnimation(emotePickaxe[0].animation);
                if(cycle > 0)
                    cycle--;
                else { //Every 3 ticks preform a check for loot!
                    //TODO: Add lootation chance per rock!
                    /*
                    Prototype: (baseChance + woodCuttingLevel / 3) * pickaxeBonus : 255
                     */
                    Item item = new Item(rock.produceItem, 1);
                    event.getPlayer().getInventory().add(item);
                    event.getPlayer().getSkillManager().addExperience(Skill.MINING, rock.xp);
                    event.getPlayer().getPacketSender().sendMessage("You mine some " + item.getDefinition().getName().toLowerCase() + ".");
                    cycle = (int) getMiningSpeed(event.getPlayer(), pickaxe, rock.time) / 900;
                }
            }
        };
        TaskManager.submit(task);
    }

    private long getMiningSpeed(Player player, pickaxeData pickaxe, int rockTime) {
        double pickBonus = pickaxe.speed;
        double level = player.getSkillManager().getCurrentLevel(Skill.MINING) / 600D;
        double random = Misc.getRandom(150) / 100D;
        double bonus = pickBonus * random + level;
        double time = rockTime / bonus;
        //System.out.println("Time = "+time/900L);
        return (long) time;
    }
}
