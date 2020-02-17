package net.dodian.extend.events.items;

import net.dodian.extend.events.EventListener;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.npc.NPC;
import net.dodian.old.world.entity.impl.object.GameObject;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Item;

public interface ItemUseEventListener extends EventListener {
    default void itemOnEntity(Player player, Packet packet) {}

    void itemOnItem(Player player, Item itemUsed, Item itemUsedWith);

    void itemOnNpc(Player player, Item item, NPC npc);

    void itemOnObject(Player player, Item item, GameObject object);

    void itemOnPlayer(Player player, Item item, Player onItemPlayer);
}
