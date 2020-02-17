package net.dodian.old.net.packet;

import net.dodian.old.net.packet.impl.BankTabCreationPacketListener;
import net.dodian.old.net.packet.impl.ButtonClickPacketListener;
import net.dodian.old.net.packet.impl.TextClickPacketListener;
import net.dodian.old.net.packet.impl.ChangeAppearancePacketListener;
import net.dodian.old.net.packet.impl.ChatPacketListener;
import net.dodian.old.net.packet.impl.ChatSettingsPacketListener;
import net.dodian.old.net.packet.impl.CloseInterfacePacketListener;
import net.dodian.old.net.packet.impl.CommandPacketListener;
import net.dodian.old.net.packet.impl.DialoguePacketListener;
import net.dodian.old.net.packet.impl.DropItemPacketListener;
import net.dodian.old.net.packet.impl.EnterInputPacketListener;
import net.dodian.old.net.packet.impl.EquipPacketListener;
import net.dodian.old.net.packet.impl.ExamineItemPacketListener;
import net.dodian.old.net.packet.impl.ExamineNpcPacketListener;
import net.dodian.old.net.packet.impl.FinalizedMapRegionChangePacketListener;
import net.dodian.old.net.packet.impl.FollowPlayerPacketListener;
import net.dodian.old.net.packet.impl.HeightCheckPacketListener;
import net.dodian.old.net.packet.impl.ItemActionPacketListener;
import net.dodian.old.net.packet.impl.ItemContainerActionPacketListener;
import net.dodian.old.net.packet.impl.MagicOnPlayerPacketListener;
import net.dodian.old.net.packet.impl.MovementPacketListener;
import net.dodian.old.net.packet.impl.NPCOptionPacketListener;
import net.dodian.old.net.packet.impl.ObjectActionPacketListener;
import net.dodian.old.net.packet.impl.PickupItemPacketListener;
import net.dodian.old.net.packet.impl.PlayerInactivePacketListener;
import net.dodian.old.net.packet.impl.PlayerOptionPacketListener;
import net.dodian.old.net.packet.impl.PlayerRelationPacketListener;
import net.dodian.old.net.packet.impl.RegionChangePacketListener;
import net.dodian.old.net.packet.impl.SilencedPacketListener;
import net.dodian.old.net.packet.impl.SpecialAttackPacketListener;
import net.dodian.old.net.packet.impl.SwitchItemSlotPacketListener;
import net.dodian.old.net.packet.impl.TradeRequestPacketListener;
import net.dodian.old.net.packet.impl.UseItemPacketListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Defining all packets and other packet-related-constants 
 * that are in the 317 protocol.
 * @author Gabriel Hannason
 */
@Component
public class PacketConstants {

	public static final PacketListener[] PACKETS = new PacketListener[257];
	
	public static final int SPECIAL_ATTACK_OPCODE = 184;
	public static final int BUTTON_CLICK_OPCODE = 185;
	public static final int TEXT_CLICK_OPCODE = 186;
	public static final int SPAWN_TAB_ACTION_OPCODE = 187;
	public static final int CHAT_OPCODE_1 = 4;
	public static final int DROP_ITEM_OPCODE = 87;
	public static final int FINALIZED_MAP_REGION_OPCODE = 121;
	public static final int CHANGE_MAP_REGION_OPCODE = 210;
	public static final int CLOSE_INTERFACE_OPCODE = 130;
	public static final int EXAMINE_ITEM_OPCODE = 2;
	public static final int EXAMINE_NPC_OPCODE = 6;
	public static final int CHANGE_APPEARANCE = 11;
	public static final int DIALOGUE_OPCODE = 40;
	public static final int ENTER_AMOUNT_OPCODE = 208, ENTER_SYNTAX_OPCODE = 60;
	public static final int EQUIP_ITEM_OPCODE = 41;
	public static final int PLAYER_INACTIVE_OPCODE = 202;
	public static final int CHAT_SETTINGS_OPCODE = 95;
	public static final int COMMAND_OPCODE = 103;
	public static final int UPDATE_PLANE_OPCODE = 229;
	public static final int COMMAND_MOVEMENT_OPCODE = 98;
	public static final int GAME_MOVEMENT_OPCODE = 164;
	public static final int MINIMAP_MOVEMENT_OPCODE = 248;
	public static final int PICKUP_ITEM_OPCODE = 236;
	public static final int FIRST_ITEM_CONTAINER_ACTION_OPCODE = 145;
	public static final int SECOND_ITEM_CONTAINER_ACTION_OPCODE = 117;
	public static final int THIRD_ITEM_CONTAINER_ACTION_OPCODE = 43;
	public static final int FOURTH_ITEM_CONTAINER_ACTION_OPCODE = 129;
	public static final int FIFTH_ITEM_CONTAINER_ACTION_OPCODE = 135;
	public static final int SIXTH_ITEM_CONTAINER_ACTION_OPCODE = 138;
	public static final int ADD_FRIEND_OPCODE = 188;
	public static final int REMOVE_FRIEND_OPCODE = 215;
	public static final int ADD_IGNORE_OPCODE = 133;
	public static final int REMOVE_IGNORE_OPCODE = 74;
	public static final int SEND_PM_OPCODE = 126;
	public static final int ATTACK_PLAYER_OPCODE = 153;
	public static final int PLAYER_OPTION_1_OPCODE = 128;
	public static final int PLAYER_OPTION_2_OPCODE = 37;
	public static final int PLAYER_OPTION_3_OPCODE = 227;
	public static final int SWITCH_ITEM_SLOT_OPCODE = 214;
	public static final int FOLLOW_PLAYER_OPCODE = 73;
	public static final int MAGIC_ON_PLAYER_OPCODE = 249;
	public static final int BANK_TAB_CREATION_OPCODE = 216;
	public static final int TRADE_REQUEST_OPCODE = 139;
	public static final int DUEL_REQUEST_OPCODE = 128;
	
	public static final int 
	OBJECT_FIRST_CLICK_OPCODE = 132, 
	OBJECT_SECOND_CLICK_OPCODE = 252, 
	OBJECT_THIRD_CLICK_OPCODE = 70, 
	OBJECT_FOURTH_CLICK_OPCODE = 234, 
	OBJECT_FIFTH_CLICK_OPCODE = 228;
	
	public static final int 
	ATTACK_NPC_OPCODE = 72, 
	FIRST_CLICK_OPCODE = 155, 
	MAGE_NPC_OPCODE = 131, 
	SECOND_CLICK_OPCODE = 17, 
	THIRD_CLICK_OPCODE = 21, 
	FOURTH_CLICK_OPCODE = 18;
	
	public static final int FIRST_ITEM_ACTION_OPCODE = 122;
	public static final int SECOND_ITEM_ACTION_OPCODE = 75;
	public static final int THIRD_ITEM_ACTION_OPCODE = 16;
	
	public static final int
	ITEM_ON_NPC = 57,
	ITEM_ON_ITEM = 53,
	ITEM_ON_OBJECT = 192,
	ITEM_ON_GROUND_ITEM = 25,
	ITEM_ON_PLAYER = 14;

	@Autowired
	public PacketConstants(
			CommandPacketListener commandPacketListener,
			ButtonClickPacketListener buttonClickPacketListener,
			ItemActionPacketListener itemActionPacketListener,
			UseItemPacketListener useItemPacketListener,
			NPCOptionPacketListener npcOptionPacketListener
	) {
		for(int i = 0; i < PACKETS.length; i++) {
			PACKETS[i] = new SilencedPacketListener();
		}

		PACKETS[SPECIAL_ATTACK_OPCODE] = new SpecialAttackPacketListener();
		PACKETS[BUTTON_CLICK_OPCODE] = buttonClickPacketListener;
		PACKETS[TEXT_CLICK_OPCODE] = new TextClickPacketListener();
		PACKETS[CHAT_OPCODE_1] = new ChatPacketListener();

		PACKETS[DROP_ITEM_OPCODE] = new DropItemPacketListener();
		PACKETS[FINALIZED_MAP_REGION_OPCODE] = new FinalizedMapRegionChangePacketListener();
		PACKETS[CHANGE_MAP_REGION_OPCODE] = new RegionChangePacketListener();
		PACKETS[CLOSE_INTERFACE_OPCODE] = new CloseInterfacePacketListener();
		PACKETS[EXAMINE_ITEM_OPCODE] = new ExamineItemPacketListener();
		PACKETS[EXAMINE_NPC_OPCODE] = new ExamineNpcPacketListener();
		PACKETS[CHANGE_APPEARANCE] = new ChangeAppearancePacketListener();
		PACKETS[DIALOGUE_OPCODE] = new DialoguePacketListener();
		PACKETS[ENTER_AMOUNT_OPCODE] = new EnterInputPacketListener();
		PACKETS[EQUIP_ITEM_OPCODE] = new EquipPacketListener();
		PACKETS[PLAYER_INACTIVE_OPCODE] = new PlayerInactivePacketListener();
		PACKETS[CHAT_SETTINGS_OPCODE] = new ChatSettingsPacketListener();
		PACKETS[COMMAND_OPCODE] = commandPacketListener;
		PACKETS[UPDATE_PLANE_OPCODE] = new HeightCheckPacketListener();
		PACKETS[COMMAND_MOVEMENT_OPCODE] = new MovementPacketListener();
		PACKETS[GAME_MOVEMENT_OPCODE] = new MovementPacketListener();
		PACKETS[MINIMAP_MOVEMENT_OPCODE] = new MovementPacketListener();
		PACKETS[PICKUP_ITEM_OPCODE] = new PickupItemPacketListener();
		PACKETS[SWITCH_ITEM_SLOT_OPCODE] = new SwitchItemSlotPacketListener();
		PACKETS[FOLLOW_PLAYER_OPCODE] = new FollowPlayerPacketListener();
		PACKETS[MAGIC_ON_PLAYER_OPCODE] = new MagicOnPlayerPacketListener();
		PACKETS[BANK_TAB_CREATION_OPCODE] = new BankTabCreationPacketListener();

		PACKETS[FIRST_ITEM_CONTAINER_ACTION_OPCODE] = new ItemContainerActionPacketListener();
		PACKETS[SECOND_ITEM_CONTAINER_ACTION_OPCODE] = new ItemContainerActionPacketListener();
		PACKETS[THIRD_ITEM_CONTAINER_ACTION_OPCODE] = new ItemContainerActionPacketListener();
		PACKETS[FOURTH_ITEM_CONTAINER_ACTION_OPCODE] = new ItemContainerActionPacketListener();
		PACKETS[FIFTH_ITEM_CONTAINER_ACTION_OPCODE] = new ItemContainerActionPacketListener();
		PACKETS[SIXTH_ITEM_CONTAINER_ACTION_OPCODE] = new ItemContainerActionPacketListener();

		PACKETS[ATTACK_PLAYER_OPCODE] = new PlayerOptionPacketListener();
		PACKETS[PLAYER_OPTION_1_OPCODE] = new PlayerOptionPacketListener();
		PACKETS[PLAYER_OPTION_2_OPCODE] = new PlayerOptionPacketListener();
		PACKETS[PLAYER_OPTION_3_OPCODE] = new PlayerOptionPacketListener();


		PACKETS[OBJECT_FIRST_CLICK_OPCODE] = new ObjectActionPacketListener();
		PACKETS[OBJECT_SECOND_CLICK_OPCODE] = new ObjectActionPacketListener();
		PACKETS[OBJECT_THIRD_CLICK_OPCODE] = new ObjectActionPacketListener();
		PACKETS[OBJECT_FOURTH_CLICK_OPCODE] = new ObjectActionPacketListener();
		PACKETS[OBJECT_FIFTH_CLICK_OPCODE] = new ObjectActionPacketListener();

		PACKETS[ATTACK_NPC_OPCODE] = npcOptionPacketListener;
		PACKETS[FIRST_CLICK_OPCODE] = npcOptionPacketListener;
		PACKETS[MAGE_NPC_OPCODE] = npcOptionPacketListener;
		PACKETS[SECOND_CLICK_OPCODE] = npcOptionPacketListener;
		PACKETS[THIRD_CLICK_OPCODE] = npcOptionPacketListener;
		PACKETS[FOURTH_CLICK_OPCODE] = npcOptionPacketListener;

		PACKETS[FIRST_ITEM_ACTION_OPCODE] = itemActionPacketListener;
		PACKETS[SECOND_ITEM_ACTION_OPCODE] = itemActionPacketListener;
		PACKETS[THIRD_ITEM_ACTION_OPCODE] = itemActionPacketListener;

		PACKETS[ITEM_ON_NPC] = useItemPacketListener;
		PACKETS[ITEM_ON_ITEM] = useItemPacketListener;
		PACKETS[ITEM_ON_OBJECT] = useItemPacketListener;
		PACKETS[ITEM_ON_GROUND_ITEM] = useItemPacketListener;
		PACKETS[ITEM_ON_PLAYER] = useItemPacketListener;


		PACKETS[ADD_FRIEND_OPCODE] = new PlayerRelationPacketListener();
		PACKETS[REMOVE_FRIEND_OPCODE] = new PlayerRelationPacketListener();
		PACKETS[ADD_IGNORE_OPCODE] = new PlayerRelationPacketListener();
		PACKETS[REMOVE_IGNORE_OPCODE] = new PlayerRelationPacketListener();
		PACKETS[SEND_PM_OPCODE] = new PlayerRelationPacketListener();

		PACKETS[ENTER_AMOUNT_OPCODE] = new EnterInputPacketListener();
		PACKETS[ENTER_SYNTAX_OPCODE] = new EnterInputPacketListener();

		PACKETS[TRADE_REQUEST_OPCODE] = new TradeRequestPacketListener();
	}
}
