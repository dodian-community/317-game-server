package net.dodian.old.world.model;

public enum SkullType {

	WHITE_SKULL(0),
	RED_SKULL(1);
	
	SkullType(int iconId) {
		this.iconId = iconId;
	}
	
	private final int iconId;
	
	public int getIconId() {
		return iconId;
	}
}
