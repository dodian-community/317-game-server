package net.dodian.old.engine.task.impl;

import net.dodian.old.engine.task.Task;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.npc.NPC;

/**
 * A {@link Task} implementation which handles the respawn
 * of an npc.
 * 
 * @author Professor Oak
 */
public class NPCRespawnTask extends Task {

	public NPCRespawnTask(NPC npc, int respawn) {
		super(respawn);
		this.npc = npc;
	}

	private final NPC npc;

	@Override
	public void execute() {
		
		//Create a new npc with fresh stats..
		NPC npc_ = new NPC(npc.getId(), npc.getSpawnPosition());
		
		//Register the npc
		World.getNpcAddQueue().add(npc_);
		
		//Stop the task
		stop();
	}

}
