package net.dodian.old.engine;

import net.dodian.old.engine.task.TaskManager;
import net.dodian.old.world.World;
import net.dodian.old.world.content.clan.ClanChatManager;
import org.springframework.stereotype.Component;

/**
 * The engine which processes the game.
 * Also contains a logic service which can 
 * be used for asynchronous tasks such as file writing.
 * 
 * @author lare96
 * @author Professor Oak
 */
@Component
public final class GameEngine implements Runnable {
	
	@Override
	public void run() {
		try {
			
			TaskManager.sequence();
			World.sequence();
					
		} catch (Throwable e) {
			e.printStackTrace();
			World.savePlayers();
			ClanChatManager.save();
		}
	}
}