package net.dodian.plugins.impl;

import net.dodian.events.EventHandler;
import net.dodian.events.EventListener;
import net.dodian.events.impl.player.interact.PlayerButtonClickEvent;
import net.dodian.events.impl.player.interact.npc.PlayerNpcSecondClickEvent;
import net.dodian.events.impl.player.interact.object.PlayerObjectSecondClickEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Banking implements EventListener {

    @EventHandler
    public void onButtonClick(PlayerButtonClickEvent event) {
        if(event.getButton() != 50001) {
            return;
        }

        event.getPlayer().getPacketSender().sendInterfaceRemoval();
    }

    @EventHandler
    public void onNpcSecondClick(PlayerNpcSecondClickEvent event) {
        int[] bankerIds = new int[]{394};

        if(Arrays.stream(bankerIds).noneMatch(id -> id == event.getNpc().getId())) {
            return;
        }

        event.getPlayer().getBank(event.getPlayer().getCurrentBankTab()).open();
    }

    @EventHandler
    public void onObjectSecondClick(PlayerObjectSecondClickEvent event) {
        Integer[] bankIds = new Integer[]{25808, 6943};

        if(!Arrays.asList(bankIds).contains(event.getObject().getId())) {
            return;
        }

        event.getPlayer().getBank(event.getPlayer().getCurrentBankTab()).open();
    }
}
