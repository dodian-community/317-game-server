package net.dodian.old.engine.task.impl;

import net.dodian.old.definitions.NpcDropDefinition;
import net.dodian.old.engine.task.Task;
import net.dodian.old.engine.task.TaskManager;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Animation;
import net.dodian.old.world.model.movement.MovementStatus;

/**
 * Represents an npc's death task, which handles everything
 * an npc does before and after their death animation (including it), 
 * such as dropping their drop table items.
 * 
 * @author relex lawl
 */

public class NPCDeathTask extends Task {

	/**
	 * The NPCDeathTask constructor.
	 * @param npc	The npc being killed.
	 */
	public NPCDeathTask(NPC npc) {
		super(2);
		this.npc = npc;
		this.ticks = 2;
	}

	/**
	 * The npc setting off the death task.
	 */
	private final NPC npc;

	/**
	 * The amount of ticks on the task.
	 */
	private int ticks = 2;

	/**
	 * The player who killed the NPC
	 */
	private Player killer = null;

	@Override
	public void execute() {
		try {

			switch (ticks) {
			case 2:
				npc.getMovementQueue().setMovementStatus(MovementStatus.DISABLED).reset();

				killer = npc.getCombat().getKiller(true);

				npc.performAnimation(new Animation(npc.getDefinition().getDeathAnim()));

				npc.getCombat().reset();

				if(npc.getInteractingEntity() != null) {
					npc.setEntityInteraction(null);
				}

				break;
			case 0:
				if(killer != null) {

					//Check if the npc death should be handled
					//customly, depending on its location.
					if(npc.getLocation().handleKilledNPC(killer, npc)) {
						stop();
						return;
					}

					//Drop items for the player
					NpcDropDefinition.handleDrop(killer, npc);
					
					//Handle bots death
					if(npc.getBotHandler() != null) {
						npc.getBotHandler().onDeath(killer);
					}

				}
				stop();
				break;
			}

			ticks--;
		} catch(Exception e) {
			e.printStackTrace();
			stop();
		}
	}

	@Override
	public void stop() {
		setEventRunning(false);

		npc.setDying(false);

		//respawn
		if(npc.getDefinition().getRespawn() > 0) {
			TaskManager.submit(new NPCRespawnTask(npc, npc.getDefinition().getRespawn()));
		}

		World.getNpcRemoveQueue().add(npc);
	}
}
