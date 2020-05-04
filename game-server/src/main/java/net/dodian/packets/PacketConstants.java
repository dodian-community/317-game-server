package net.dodian.packets;

/**
 * Defining all packets and other packet-related-constants 
 * that are in the 317 protocol.
 * @author Gabriel Hannason
 */
public class PacketConstants {
	
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
	MAGIC_NPC_OPCODE = 131,
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
}
