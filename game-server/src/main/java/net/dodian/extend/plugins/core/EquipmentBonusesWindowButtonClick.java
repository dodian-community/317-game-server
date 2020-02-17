package net.dodian.extend.plugins.core;

import net.dodian.extend.events.controllers.ButtonClickEventListener;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.equipment.BonusManager;
import org.springframework.stereotype.Component;

import static net.dodian.constants.InterfaceButton.OPEN_EQUIPMENT_SCREEN;

@Component
public class EquipmentBonusesWindowButtonClick implements ButtonClickEventListener {

    @Override
    public void onClick(Player player, int button) {
        if(button == OPEN_EQUIPMENT_SCREEN && !player.busy()) {
            BonusManager.open(player);
        } else {
            player.getPacketSender().sendMessage("You cannot do that right now.");
        }
    }
}
