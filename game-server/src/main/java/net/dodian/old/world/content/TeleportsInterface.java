package net.dodian.old.world.content;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.dodian.old.world.entity.combat.bountyhunter.BountyHunter;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Position;
import net.dodian.old.world.model.dialogue.DialogueManager;
import net.dodian.old.world.model.dialogue.DialogueOptions;
import net.dodian.old.world.model.teleportation.TeleportHandler;
import net.dodian.old.world.model.teleportation.TeleportType;

public class TeleportsInterface {

	private static final int OPEN_INTERFACE_BUTTON = 39104;

	private enum TeleportsData {
		ROCK_CRABS(38200, 38202, new Position(2706, 3713)),
		PACK_YAKS(38200, 38206, new Position(2323, 3800)),
		EXPERIMENTS(38200, 38210, new Position(3556, 9944)),
		ZOMBIES(38200, 38214, new Position(3487, 3283)),
		BANDITS(38200, 38218, new Position(3170, 2992)),
		
		//Wildy teles
		DITCH(38600, 38602, new Position(3089, 3523)),
		WEST_DRAGONS(38600, 38606, new Position(2978, 3598)),
		OBELISK(38600, 38610, new Position(3156, 3615)),
		GRAVEYARD(38600, 38614, new Position(3148, 3671)),
		BANDIT_CAMP(38600, 38618, new Position(3034, 3690)),
		HUNTERS_HILL(38600, 38622, new Position(3143, 3773)),
		DEMONIC_RUINS(38600, 38626, new Position(3283, 3877)),
		RUNITE_ROCKS(38600, 38630, new Position(3059, 3884)),
		THE_GATE(38600, 38634, new Position(3225, 3903)),
		TARGET_TELEPORT(38600, 38638, null),
		
		//Boss teles
		VENENATIS(38300, 38302, new Position(3239, 3734)),
		CALLISO(38300, 38306, new Position(3307, 3837)),
		CHAOS_ELEMENTAL(38300, 38310, new Position(3267, 3912)),
		
		;
		TeleportsData(int interfaceId, int button, Position pos) {
			this.interfaceId = interfaceId;
			this.button = button;
			this.pos = pos;
		}
		
		private int interfaceId;
		private int button;
		private Position pos;
		
		private static Map<Integer, TeleportsData> teleports = new HashMap<Integer, TeleportsData>();

		static {
			for (TeleportsData t : TeleportsData.values()) {
				teleports.put(t.button, t);
			}
		}
		
		public static TeleportsData forId(int button) {
			return teleports.get(button);
		}
	}
	
	private enum InterfaceData {
		MONSTERS(38200, 38102, 800),
		BOSSES(38300, 38105, 801),
		SKILLS(38400, 38108, 802),
		MINIGAMES(38500, 38111, 803),
		WILDERNESS(38600, 38114, 804)
		;

		InterfaceData(int interfaceId, int button, int config) {
			this.interfaceId = interfaceId;
			this.button = button;
			this.config = config;
		}

		private int interfaceId;
		private int button;
		private int config;
		

		private static Map<Integer, InterfaceData> interfaces = new HashMap<Integer, InterfaceData>();

		static {
			for (InterfaceData t : InterfaceData.values()) {
				interfaces.put(t.button, t);
			}
		}
		
		public static InterfaceData forId(int button) {
			return interfaces.get(button);
		}
	}

	private static void sendInterface(Player player, InterfaceData teleport) {

		//Deactivate other tabs
		for(InterfaceData t : InterfaceData.values()) {
			if(t == teleport) {
				continue;
			}
			player.getPacketSender().sendConfig(t.config, 0);
		}

		//Activate current tab
		player.getPacketSender().sendConfig(teleport.config, 1);

		//Send interface
		player.getPacketSender().sendInterface(teleport.interfaceId);

	}

	public static boolean handleButton(Player player, int button) {
		if(button != OPEN_INTERFACE_BUTTON) {
			if(!(player.getInterfaceId() >= 31000 && player.getInterfaceId() <= 38600)) {
				return false;
			}
		} else {
		//	sendInterface(player, InterfaceData.MONSTERS);
			sendInterface(player, InterfaceData.WILDERNESS);
			return true;
		}
		
		//Handle teleport options
		InterfaceData tab = InterfaceData.forId(button);
		if(tab != null) {
			
			//Only wildy/bosses
			if(tab != InterfaceData.WILDERNESS && tab != InterfaceData.BOSSES) {
				player.getPacketSender().sendMessage("This tab is currently disabled.");
				return true;
			}
			
			sendInterface(player, tab);
			return true;
		}
		
		//Handle teleport buttons
		TeleportsData tele = TeleportsData.forId(button);
		if(tele != null) {
			
			//Make sure we have proper interface open....
			if(player.getInterfaceId() != tele.interfaceId) {
				return true;
			}
			
			//Check bounty hunter
			if(tele == TeleportsData.TARGET_TELEPORT) {
				if(!player.isTargetTeleportUnlocked()) {
					player.getPacketSender().sendMessage("You have to learn this spell from the Emblem trader first.");
					return true;
				}
				Optional<Player> target = BountyHunter.getTargetFor(player);
				if(!target.isPresent()) {
					player.getPacketSender().sendMessage("You don't currently have a target.");
					return true;
				}
				DialogueManager.start(player, 14);
				player.setDialogueOptions(new DialogueOptions() {
					@Override
					public void handleOption(Player player, int option) {
						player.getPacketSender().sendInterfaceRemoval();
						if(option == 1) {
							if(TeleportHandler.checkReqs(player, target.get().getPosition())) {
								TeleportHandler.teleport(player, target.get().getPosition().copy(), TeleportType.NORMAL);
							}
						}
					}
				});	
				return true;
			}
			

			player.getPacketSender().sendInterfaceRemoval();
			
			if(TeleportHandler.checkReqs(player, tele.pos)) {
				TeleportHandler.teleport(player, tele.pos, TeleportType.NORMAL);
			}
			return true;
		}
		return false;
	}
}
