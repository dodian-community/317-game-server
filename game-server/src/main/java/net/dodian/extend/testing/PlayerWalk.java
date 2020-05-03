package net.dodian.extend.testing;

import net.dodian.old.world.model.Position;
import net.dodian.packets.listeners.GameMovementPacketListener;
import org.springframework.stereotype.Component;

@Component
public class PlayerWalk extends GameMovementPacketListener {

    @Override
    public void handle() {
        if(this.packet.getPlayer() == null || this.packet.getPlayer().getHitpoints() <= 0) {
            return;
        }

        this.packet.getPlayer().getMovementQueue().setFollowCharacter(null);

        if(this.packet.getPlayer().getInteractingEntity() != null) {
            this.packet.getPlayer().setEntityInteraction(null);
        }

        this.packet.getPlayer().setWalkToTask(null);

        this.packet.getPlayer().getCombat().setCastSpell(null);
        this.packet.getPlayer().getCombat().reset();

        // TODO: Implement requirements check

        this.packet.getPlayer().getPacketSender().sendInterfaceRemoval();

        if(this.packet.getSteps() < 0) {
            return;
        }

        boolean invalidStep = false;

        if(this.packet.getPlayer().getPosition().getDistance(this.packet.getPositions()[0]) >= 22) {
            invalidStep = true;
        } else {
            for(int i = 0; i < this.packet.getSteps(); i++) {
                this.packet.getPositions()[i + 1] = new Position(this.packet.getPath()[i][0] + this.packet.getFirstStepX(), this.packet.getPath()[i][1] + this.packet.getFirstStepY(), this.packet.getPlayer().getPosition().getZ());
                if(!this.packet.getPositions()[i + 1].isWithinDistance(this.packet.getPlayer().getPosition(), 40)) {
                    invalidStep = true;
                    break;
                }
            }
        }

        if(invalidStep) {
            this.packet.getPlayer().getMovementQueue().reset();
            //	System.out.println(""+player.getUsername()+" invalid step. Steps: "+steps+" Position0: "+positions[0]);
            return;
        }

        if (this.packet.getPlayer().getMovementQueue().addFirstStep(this.packet.getPositions()[0])) {
            for (int i = 1; i < this.packet.getPositions().length; i++) {
                this.packet.getPlayer().getMovementQueue().addStep(this.packet.getPositions()[i]);
            }
        }
    }
}
