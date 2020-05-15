package net.dodian.plugins.impl.skill;

import net.dodian.game.events.EventHandler;
import net.dodian.game.events.EventListener;
import net.dodian.game.events.impl.player.interact.object.PlayerObjectFirstClickEvent;
import net.dodian.old.engine.task.Task;
import net.dodian.old.engine.task.TaskManager;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.Skill;
import net.dodian.old.world.model.movement.MovementStatus;
import org.springframework.stereotype.Component;

@Component
public class Mining implements EventListener {
    private enum pickaxeData {
        BRONZE(1265, 1, new Animation(625), 0.03),
        IRON(1267, 1, new Animation(626), 0.05),
        STEEL(1269, 6, new Animation(627), 0.09),
        MITHRIL(1273, 21, new Animation(628), 0.13),
        ADAMANT(1271, 31, new Animation(629), 0.16),
        RUNE(1275, 41, new Animation(624), 0.20),
        DRAGON(15259, 61, new Animation(624), 0.25)
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
        COPPER(1, 10, 436, 7484),
        TIN(1, 10, 438, 7485)
        ;

        int level, xp, produceItem;
        int[] object;
        rockData(int level, int xp, int produceItem, int... object) {
            this.level = level;
            this.xp = xp;
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
        initiate(event.getPlayer(), rock, pickaxe);
    }

    private void initiate(Player player, rockData rock, pickaxeData pickaxe) {
        Task task = new Task(1, player, true) {
            int cycle = 3;
            @Override
            protected void execute() {
                if(cycle == 3)
                    player.performAnimation(pickaxe.animation);
                if(cycle > 0)
                    cycle--;
                else { //Every 3 ticks preform a check for loot!
                    //TODO: Add lootation chance per rock!
                    player.getInventory().add(new Item(rock.produceItem, 1));
                    player.getPacketSender().sendMessage("I obtained a ore!");
                    cycle = 3;
                }
                /* Stops action! */
                if(player.getInventory().isFull() ||
                player.getMovementQueue().isMoving() ||
                player.getMovementQueue().getMovementStatus().equals(MovementStatus.DISABLED))
                    stop();
            }
        };
        TaskManager.submit(task);
    }
}
