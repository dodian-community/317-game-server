package net.dodian.old.world.model.syntax.impl;

import net.dodian.old.util.Misc;
import net.dodian.old.world.content.Presetables;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.syntax.EnterSyntax;

public class ChangePresetName implements EnterSyntax {
	
	private int presetIndex;
	
	public ChangePresetName(final int presetIndex) {
		this.presetIndex = presetIndex;
	}

	@Override
	public void handleSyntax(Player player, String input) {
		
		player.getPacketSender().sendInterfaceRemoval();
		
		input = Misc.formatText(input);
		
		if(!Misc.isValidName(input)) {
			player.getPacketSender().sendMessage("Invalid name for preset. Please enter characters only.");
			player.setCurrentPreset(null);
			Presetables.open(player);
			return;
		}
		
		if(player.getPresets()[presetIndex] != null) {
			
			player.getPresets()[presetIndex].setName(input);
			player.getPacketSender().sendMessage("The preset's name has been updated.");
			
			Presetables.open(player);
		}
	}

	@Override
	public void handleSyntax(Player player, int input) {
	}

}
