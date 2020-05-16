package net.dodian.packets.handlers.impl;

import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.grounditems.GroundItemManager;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.RegionChangePacket;
import org.springframework.stereotype.Component;

@Component
public class RegionChangePacketHandler extends PacketListener {

    @PacketHandler
    public void onRegionChange(RegionChangePacket packet) {
        Player player = packet.getPlayer();

        if (player.isAllowRegionChangePacket()) {
            GroundItemManager.onRegionChange(player);
            player.setAllowRegionChangePacket(false);
        }
    }
}
