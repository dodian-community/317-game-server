package net.dodian.packets.handlers.impl;

import net.dodian.GameConstants;
import net.dodian.Server;
import net.dodian.old.definitions.WeaponInterfaces;
import net.dodian.old.util.Misc;
import net.dodian.old.world.content.Dueling;
import net.dodian.old.world.entity.combat.magic.Autocasting;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.old.world.model.Flag;
import net.dodian.old.world.model.Item;
import net.dodian.old.world.model.Skill;
import net.dodian.old.world.model.container.impl.Equipment;
import net.dodian.old.world.model.dialogue.DialogueManager;
import net.dodian.old.world.model.equipment.BonusManager;
import net.dodian.packets.handlers.PacketHandler;
import net.dodian.packets.handlers.PacketListener;
import net.dodian.packets.impl.player.EquipItemPacket;
import org.springframework.stereotype.Component;

@Component
public class EquipItemPacketHandler implements PacketListener {

    @PacketHandler
    public void onEquipItem(EquipItemPacket packet) {
        Player player = packet.getPlayer();

        if(packet.getPlayer() == null || packet.getPlayer().getHitpoints() <= 0) {
            return;
        }

        if(packet.getSlot() < 0 || packet.getSlot() > packet.getPlayer().getInventory().capacity()) {
            return;
        }

        Item item = packet.getPlayer().getInventory().getItems()[packet.getSlot()].copy();
        if(item.getId() != packet.getId()) {
            packet.getPlayer().getPacketSender().sendMessage("You're already wielding that item.");
            return;
        }

        if(packet.getPlayer().getInterfaceId() > 0 && packet.getPlayer().getInterfaceId() != Equipment.EQUIPMENT_SCREEN_INTERFACE_ID) {
            packet.getPlayer().getPacketSender().sendInterfaceRemoval();
        }

        if(packet.getSlot() >= 0 && packet.getSlot() <= 28) {
            if(item.getId() != packet.getId()) {
                return;
            }

            /*
             * Making sure item exists and that id is consistent.
             */
            if (packet.getId() == item.getId()) {

                //Check if player can wield the item..
                if(item.getDefinition().getRequirements() != null) {
                    for (Skill skill : Skill.values()) {
                        if (item.getDefinition().getRequirements()[skill.ordinal()] > player.getSkillManager().getMaxLevel(skill)) {
                            StringBuilder vowel = new StringBuilder();
                            if (skill.getName().startsWith("a") || skill.getName().startsWith("e") || skill.getName().startsWith("i") || skill.getName().startsWith("o") || skill.getName().startsWith("u")) {
                                vowel.append("an ");
                            } else {
                                vowel.append("a ");
                            }
                            player.getPacketSender().sendMessage("You need " + vowel.toString() + Misc.formatText(skill.getName()) + " level of at least " + item.getDefinition().getRequirements()[skill.ordinal()] + " to wear this.");
                            return;
                        }
                    }
                }

                //Check if it's a proper equipable item!
                int equipmentSlot = item.getDefinition().getEquipmentType().getSlot();

                //Item hasn't been added yet?
                if(equipmentSlot == -1) {
                    Server.getLogger().info("Attempting to equip item "+item.getId()+" which has no definition.");
                    return;
                }

                //Duel, locked weapon?
                if(player.getDueling().inDuel()) {
                    for(int i = 11; i < player.getDueling().getRules().length; i++) {
                        if(player.getDueling().getRules()[i]) {
                            Dueling.DuelRule duelRule = Dueling.DuelRule.forId(i);
                            if(equipmentSlot == duelRule.getEquipmentSlot() || duelRule == Dueling.DuelRule.NO_SHIELD && item.getDefinition().isDoubleHanded()) {
                                DialogueManager.sendStatement(player, "The rules that were set do not allow this item to be equipped.");
                                return;
                            }
                        }
                    }
                    if(equipmentSlot == Equipment.WEAPON_SLOT || item.getDefinition().isDoubleHanded()) {
                        if(player.getDueling().getRules()[Dueling.DuelRule.LOCK_WEAPON.ordinal()]) {
                            DialogueManager.sendStatement(player, "Weapons have been locked in this duel!");
                            return;
                        }
                    }
                }

                Item equipItem = player.getEquipment().forSlot(equipmentSlot).copy();
                if (equipItem.getDefinition().isStackable() && equipItem.getId() == item.getId()) {
                    int amount = Math.min(equipItem.getAmount() + item.getAmount(), Integer.MAX_VALUE);
                    player.getInventory().delete(item, false);
                    player.getEquipment().getItems()[equipmentSlot].setAmount(amount);
                    equipItem.setAmount(amount);
                } else {
                    if (item.getDefinition().isDoubleHanded() && item.getDefinition().getEquipmentType().getSlot() == Equipment.WEAPON_SLOT) {

                        int slotsRequired = player.getEquipment().isSlotOccupied(Equipment.SHIELD_SLOT) && player.getEquipment().isSlotOccupied(Equipment.WEAPON_SLOT) ? 1 : 0;
                        if (player.getInventory().getFreeSlots() < slotsRequired) {
                            player.getInventory().full();
                            return;
                        }

                        Item shield = player.getEquipment().getItems()[Equipment.SHIELD_SLOT];
                        Item weapon = player.getEquipment().getItems()[Equipment.WEAPON_SLOT];
                        player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(-1, 0));
                        player.getInventory().delete(item);
                        player.getEquipment().set(equipmentSlot, item);


                        if (weapon.getId() != -1) {
                            player.getInventory().setItem(packet.getSlot(), weapon);
                        }

                        if (shield.getId() != -1) {
                            player.getInventory().add(shield);
                        }

                    } else if (equipmentSlot == Equipment.SHIELD_SLOT && player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getDefinition().isDoubleHanded()) {
                        player.getInventory().setItem(packet.getSlot(), player.getEquipment().getItems()[Equipment.WEAPON_SLOT]);
                        player.getEquipment().setItem(Equipment.WEAPON_SLOT, new Item(-1));
                        player.getEquipment().setItem(Equipment.SHIELD_SLOT, item);
                        resetWeapon(player);
                    } else {
                        if (equipItem.getId() != -1 && item.getDefinition().getEquipmentType().getSlot() == equipItem.getDefinition().getEquipmentType().getSlot()) {
                            if(player.getInventory().contains(equipItem.getId())) {
                                player.getInventory().delete(item, false);
                                player.getInventory().add(equipItem, false);
                            } else {
                                player.getInventory().setItem(packet.getSlot(), equipItem);
                            }
                            player.getEquipment().setItem(equipmentSlot, item);
                        } else {
                            player.getInventory().setItem(packet.getSlot(), new Item(-1, 0));
                            player.getEquipment().setItem(item.getDefinition().getEquipmentType().getSlot(), item);
                        }
                    }
                }

                //Refresh inventory
                if(GameConstants.QUEUE_SWITCHING_REFRESH) {
                    player.setUpdateInventory(true);
                } else {
                    player.getInventory().refreshItems();
                }

                if (equipmentSlot == Equipment.WEAPON_SLOT) {
                    resetWeapon(player);
                }

                if(player.getEquipment().get(Equipment.WEAPON_SLOT).getId() != 4153) {
                    player.getCombat().reset();
                }

                BonusManager.update(player);
                player.getEquipment().refreshItems();

                if(equipmentSlot == Equipment.WEAPON_SLOT) {
                    player.setQueuedAppearanceUpdate(true); //1 ticking
                } else {
                    player.getUpdateFlag().flag(Flag.PLAYER_APPEARANCE);
                }
            }
        }
    }

    public static void resetWeapon(Player player) {
        player.setSpecialActivated(false);
        WeaponInterfaces.assign(player);
        if(player.getCombat().getAutocastSpell() != null) {
            Autocasting.setAutocast(player, null);
            player.getPacketSender().sendMessage("Autocast spell cleared.");
        }
    }
}
