package net.dodian.old.world.model;

import java.util.HashMap;
import java.util.Map;

public enum BossPet {

	CHAOS_ELEMENTAL(2054, 2055, 11995),
	VENENATIS(6504, 495, 13177),
	CALLISTO(6609, 497, 13178),
	;
	
	BossPet(int bossNpcId, int petNpcId, int itemId) {
		this.bossNpcId = bossNpcId;
		this.petNpcId = petNpcId;
		this.itemId = itemId;
	}
	
	private final int 
	bossNpcId, 
	petNpcId,
	itemId;
	
	public int getBossId() {
		return bossNpcId;
	}

	public int getPetId() {
		return petNpcId;
	}

	public int getItemId() {
		return itemId;
	}
	
	private static Map<Integer, BossPet> pets = new HashMap<Integer, BossPet>();

	public static BossPet getForId(int id) {
		return pets.get(id);
	}

	static {
		for(BossPet pet : BossPet.values()) {
			pets.put(pet.getBossId(), pet);
			pets.put(pet.getPetId(), pet);
			pets.put(pet.getItemId(), pet);
		}
	}
}
