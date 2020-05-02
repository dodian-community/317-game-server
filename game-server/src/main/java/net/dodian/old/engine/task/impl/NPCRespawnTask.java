package net.dodian.old.engine.task.impl;

import net.dodian.managers.DefinitionsManager;
import net.dodian.old.engine.task.Task;
import net.dodian.old.world.World;
import net.dodian.old.world.entity.impl.npc.NPC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * A {@link Task} implementation which handles the respawn
 * of an npc.
 * 
 * @author Professor Oak
 */
public class NPCRespawnTask extends Task implements BeanFactoryAware {

	private DefinitionsManager definitionsManager;

	public NPCRespawnTask(NPC npc, int respawn) {
		super(respawn);
		this.npc = npc;
	}

	private final NPC npc;

	@Override
	public void execute() {
		
		//Create a new npc with fresh stats..
		NPC npc_ = new NPC(npc.getId(), npc.getSpawnPosition(), definitionsManager);
		
		//Register the npc
		World.getNpcAddQueue().add(npc_);
		
		//Stop the task
		stop();
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.definitionsManager = beanFactory.getBean(DefinitionsManager.class);
	}
}
