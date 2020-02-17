package net.dodian.old.net.packet.impl;

import net.dodian.old.net.packet.Packet;
import net.dodian.old.net.packet.PacketListener;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.PlayerStatus;
import net.dodian.old.world.model.container.impl.Bank;

/**
 * This packet listener is called when an item is dragged onto a bank tab.
 * 
 * @author Professor Oak
 */

public class BankTabCreationPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int interfaceId = packet.readInt();
		int fromSlot = packet.readShort();
		int to_tab = packet.readLEShort();
		
		if(player == null || player.getHitpoints() <= 0) {
			return;
		}
		
		int fromBankTab = interfaceId - Bank.CONTAINER_START;
		if(fromBankTab >= 0 && fromBankTab < Bank.TOTAL_BANK_TABS) {
			if(player.getStatus() == PlayerStatus.BANKING && player.getInterfaceId() == 5292) {
				
				if(player.isSearchingBank()) {
					fromBankTab = Bank.BANK_SEARCH_TAB_INDEX;
				}
							
				Item item = player.getBank(fromBankTab).getItems()[fromSlot].copy();
				if(fromBankTab != Bank.getTabForItem(player, item.getId())) {
					return;
				}
				
				//Let's move the item to the new tab
				int slot = player.getBank(fromBankTab).getSlot(item.getId());
				if(slot != fromSlot) {
					return;
				}
				
				//Temporarily disable note whilst we do switch
				final boolean note = player.withdrawAsNote();
				player.setNoteWithdrawal(false);

				//Make the item switch
				player.getBank(fromBankTab).switchItem(player.getBank(to_tab), item, slot, 
						false, false);
				
				//Re-set the note var
				player.setNoteWithdrawal(note);

				//Update all tabs
				Bank.reconfigureTabs(player);
				
				//Refresh items in our current tab
				if(!player.isSearchingBank()) {
					player.getBank(player.getCurrentBankTab()).refreshItems();
				}
			}
		}
	}
}
