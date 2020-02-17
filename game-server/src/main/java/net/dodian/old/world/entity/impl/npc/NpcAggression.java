package net.dodian.old.world.entity.impl.npc;

import net.dodian.old.util.Misc;
import net.dodian.old.world.entity.combat.CombatFactory;
import net.dodian.old.world.entity.combat.method.CombatMethod;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.container.impl.Equipment;
import net.dodian.old.world.model.Locations;

/**
 * Handles the behavior of aggressive {@link Npc}s around players within the
 * <code>NPC_TARGET_DISTANCE</code> radius.
 * 
 * @author lare96
 */
public final class NpcAggression {

	/**
	 * Time that has to be spent in a region before npcs stop acting aggressive
	 * toward a specific player.
	 */
	public static final int NPC_TOLERANCE_SECONDS = 300; //5 mins

	public static void onTick(Player player) {

		//Don't handle aggression if we've been in the region for quite some time.
		if (player.getAggressionTolerance().finished()) {
			return;
		}

		//Make sure we can attack the player
		if(CombatFactory.inCombat(player) && !Locations.Location.inMulti(player)) {
			return;
		}

		// Loop through all of the aggressive npcs.
		for (NPC npc : player.getLocalNpcs()) {

			//Make sure the npc is available to attack the player.
			if(npc == null || npc.getHitpoints() <= 0
					|| !npc.getDefinition().isAggressive()) {
				continue;
			}

			//Randomly attack different players if they're a team.
			if(CombatFactory.inCombat(npc)) {
				if(Locations.Location.inMulti(npc)) {
					if(Misc.getRandom(10) <= 2) {
						if(player.getLocalPlayers().size() > 0) {

							//Get a random player from the player's localPlayers list.
							Player randomPlayer = Misc.getCombinedPlayerList(player).get(Misc.getRandom(player.getLocalPlayers().size()));

							//Attack the new player if they're a valid target.
							if(CombatFactory.validTarget(npc, randomPlayer)) {
								npc.getCombat().attack(randomPlayer);
								break;
							}
						}
					}
				}
				continue;
			}

			//Are we processing bandits?
			boolean bandits = npc.getId() == 690;
			if(!bandits) {

				//Don't handle aggression if we've been in the region for quite some time.
				if (player.getAggressionTolerance().finished()) {
					break;
				}

				//Unless in Wilderness, don't be aggressive to players with twice our combat level.
				if (player.getSkillManager().getCombatLevel() > (npc.getDefinition().getCombatLevel() * 2) && player.getLocation() != Locations.Location.WILDERNESS) {
					continue;
				}
			}

			//Make sure we have the proper distance to attack the player.
			final int distanceToPlayer = npc.getSpawnPosition().getDistance(player.getPosition());
			
			//Get the npc's combat method
			final CombatMethod method = CombatFactory.getMethod(npc);
			
			//Get the max distance this npc can attack from.
			//We should always attack if we're at least 3 tiles from the player.
			final int npcMaxDistance = method.getAttackDistance(npc) < 3 ? 3 : method.getAttackDistance(npc);
			
			if(distanceToPlayer < npc.getDefinition().getCombatFollowDistance()
					&& distanceToPlayer <= npcMaxDistance) {

				//Make sure that we can actually attack the player.
				if(CombatFactory.canAttack(npc, method, player)) {

					//Bandits
					if(bandits) {
						int zammy = Equipment.getItemCount(player, "Zamorak", true);
						int sara = Equipment.getItemCount(player, "Saradomin", true);
						if(!(zammy > 0 || sara > 0)) {
							continue;
						}
						if(Misc.getRandom(2) == 1) {
							String s = zammy > 0 ? "Zamorak" : "Saradomin";
							if(Misc.getRandom(2) == 1) {
								npc.forceChat("Filthy "+s+" follower scum!");
							} else {
								npc.forceChat(""+s+" scum! You will regret coming here!");
							}
						}
					}

					//Attack the player!
					npc.getCombat().attack(player);
					break;
				}
			}
		}
	}

}
