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
public class Woodcutting implements EventListener {

    private enum AxeData {
        BRONZE(1351, 1, new Animation(879), 1.03),
        IRON(1349, 1, new Animation(877), 1.05),
        STEEL(1353, 6, new Animation(875), 1.09),
        MITHRIL(1355, 21, new Animation(871), 1.13),
        ADAMANT(1357, 31, new Animation(869), 1.16),
        RUNE(1359, 41, new Animation(867), 1.20),
        DRAGON(6739, 61, new Animation(2846), 1.25),
        THIRD_AGE(20011, 81, new Animation(7264), 1.40);
        int id, level;
        Animation animation;
        double speed;

        AxeData(int id, int level, Animation animation, double speed) {
            this.id = id;
            this.animation = animation;
            this.level = level;
            this.speed = speed;
        }
    }

    public static AxeData getAxe(Player player) {
        AxeData axe = null;
        for (AxeData data : AxeData.values()) {
            if (player.getInventory().contains(data.id) || player.getEquipment().contains(data.id)) {
                axe = player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) >= data.level ? data : axe;
            }
        }
        return axe;
    }

    public static Animation axeAnimation(AxeData data) {
        return data.animation;
    }

    private enum TreeData {
        NORMAL(1, 80, 1200, 1511, 1276),
        OAK(15, 152, 1800, 1521, 1751),
        WILLOW(30, 272, 3000, 1519, 1750),
        MAPLE(45, 400, 4200, 1517, 1759),
        YEW(60, 700, 5400, 1515, 1753),
        MAGIC(75, 1000, 7200, 1513, 1761);

        int level, xp, time, produceItem;
        int[] object;

        TreeData(int level, int xp, int time, int produceItem, int... object) {
            this.level = level;
            this.xp = xp;
            this.time = time;
            this.produceItem = produceItem;
            this.object = object;
        }
    }

    public TreeData getRock(int id) {
        for (TreeData data : TreeData.values()) {
            for (int i = 0; i < data.object.length; i++)
                if (data.object[i] == id)
                    return data;
        }
        return null;
    }

    @EventHandler
    public void onFirstClickObject(PlayerObjectFirstClickEvent event) {
        TreeData tree = getRock(event.getObject().getId());
        if (tree == null) {
            return;
        }

        if (tree.level > event.getPlayer().getSkillManager().getCurrentLevel(Skill.WOODCUTTING)) {
            event.getPlayer().getPacketSender().sendMessage("You need level " + tree.level + " woodcutting to cut the " + event.getObject().getDefinition().getName() + "!");
            return;
        }

        AxeData axe = getAxe(event.getPlayer());
        if (axe == null) {
            event.getPlayer().getPacketSender().sendMessage("You do not have a suitable axe for your level to use!");
            return;
        }

        TaskManager.cancelTasks(event.getPlayer()); //Might cause issues to other tasks?
        initiate(event, tree, axe);
    }

    private <T extends PlayerObjectEvent> void initiate(T event, TreeData tree, AxeData axe) {
        final AxeData[] emoteAxe = {axe};
        event.getPlayer().performAnimation(axe.animation);
        event.getPlayer().getPacketSender().sendMessage("You swing your axe at the " + new GameObject(tree.object[0], event.getObject().getPosition()).getDefinition().getName().toLowerCase() + "...");
        Task task = new Task(1, event.getPlayer(), true) {
            double cycle = getWoodcuttingSpeed(event.getPlayer(), axe, tree.time) / 900D;

            @Override
            protected void execute() {
                /* Stops action! */
                if (event.getPlayer().getInventory().isFull() ||
                        event.getPlayer().getMovementQueue().getMovementStatus().equals(MovementStatus.DISABLED)) {
                    stop();
                }
                /* Cycle action! */
                emoteAxe[0] = getAxe(event.getPlayer()); //Need to check if a player has a different axe!
                if (emoteAxe[0] == null)
                    stop();
                if ((int) cycle % 3 == 0)
                    event.getPlayer().performAnimation(emoteAxe[0].animation);
                if (cycle > 0)
                    cycle--;
                else {
                    Item item = new Item(tree.produceItem, 1);
                    event.getPlayer().getInventory().add(item);
                    event.getPlayer().getSkillManager().addExperience(Skill.WOODCUTTING, tree.xp);
                    event.getPlayer().getPacketSender().sendMessage("You cut some " + item.getDefinition().getName().toLowerCase() + ".");
                    cycle = getWoodcuttingSpeed(event.getPlayer(), axe, tree.time) / 900D;
                    if (Misc.getRandom(49) == 0) {
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

    private long getWoodcuttingSpeed(Player player, AxeData axe, int treeTime) {
        double pickBonus = axe.speed;
        double level = player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) / 600D;
        double random = Misc.getRandom(150) / 100D;
        double bonus = pickBonus * random + level;
        double time = treeTime / bonus;
        //System.out.println("Time = "+time/900L);
        return (long) time;
    }
}
