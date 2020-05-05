package net.dodian.plugins.impl;

import net.dodian.events.EventHandler;
import net.dodian.events.EventListener;
import net.dodian.events.impl.player.interact.npc.PlayerAttackNpcEvent;
import org.springframework.stereotype.Component;

@Component
public class DadBossPlugin implements EventListener {

    @EventHandler
    public Boolean onAttackDad(PlayerAttackNpcEvent event) {
        if(event.getNpc().getId() != 4130) {
            return true;
        }

        if(event.player.skillManager.getCombatLevel() < 60) {
            event.player.packetSender.sendMessage("You need to be more than 60 combat to attack Dad.");
            return false;
        }

        return true;
    }
}
