package net.dodian.packets.handlers.impl;

import net.dodian.old.world.model.Position;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.GameMovementPacket;
import org.springframework.stereotype.Component;

@Component
public class PlayerMovementPacketHandler implements PacketListener {

    @PacketHandler
    public void onPlayerWalk(GameMovementPacket packet) {
        if(packet.getPlayer() == null || packet.getPlayer().getHitpoints() <= 0) {
            return;
        }

        packet.getPlayer().getMovementQueue().setFollowCharacter(null);

        if(packet.getPlayer().getInteractingEntity() != null) {
            packet.getPlayer().setEntityInteraction(null);
        }

        packet.getPlayer().setWalkToTask(null);

        packet.getPlayer().getCombat().setCastSpell(null);
        packet.getPlayer().getCombat().reset();

        // TODO: Implement requirements check

        packet.getPlayer().getPacketSender().sendInterfaceRemoval();

        if(packet.getSteps() < 0) {
            return;
        }

        boolean invalidStep = false;

        if(packet.getPlayer().getPosition().getDistance(packet.getPositions()[0]) >= 22) {
            invalidStep = true;
        } else {
            for(int i = 0; i < packet.getSteps(); i++) {
                packet.getPositions()[i + 1] = new Position(packet.getPath()[i][0] + packet.getFirstStepX(), packet.getPath()[i][1] + packet.getFirstStepY(), packet.getPlayer().getPosition().getZ());
                if(!packet.getPositions()[i + 1].isWithinDistance(packet.getPlayer().getPosition(), 40)) {
                    invalidStep = true;
                    break;
                }
            }
        }

        if(invalidStep) {
            packet.getPlayer().getMovementQueue().reset();
            //	System.out.println(""+player.getUsername()+" invalid step. Steps: "+steps+" Position0: "+positions[0]);
            return;
        }

        if (packet.getPlayer().getMovementQueue().addFirstStep(packet.getPositions()[0])) {
            for (int i = 1; i < packet.getPositions().length; i++) {
                packet.getPlayer().getMovementQueue().addStep(packet.getPositions()[i]);
            }
        }
    }
}
