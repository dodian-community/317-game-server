package net.dodian.commands;

import net.dodian.commands.annotations.*;
import net.dodian.old.net.packet.Packet;
import net.dodian.old.world.entity.impl.player.Player;
import net.dodian.orm.models.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Command {
    private String name;
    private String usage;
    private List<Group> permissions = new ArrayList<>();
    private HashMap<String, String> permissionMessages = new HashMap<>();

    private boolean staffOnly = false;
    private boolean premiumOnly = false;

    private boolean maintenanceCommand = false;

    private HashMap<String, Command> subCommands = new HashMap<>();

    /**
     * If you override the parent, make sure that you also run the super,
     * so the command gets the default and necessary parameters.
     */
    public Command() {
        permissionMessages.put("Premium", "You need to be premium to execute this command.");
        permissionMessages.put("Staff", "You need to be a staff member to execute this command.");
        permissionMessages.put("Admin", "You need to be an administrator to execute this command.");
        permissionMessages.put("Maintenance", "You need to be a maintainer to execute this command.");

        if(this.getClass().isAnnotationPresent(Name.class)) {
            this.name = this.getClass().getAnnotation(Name.class).value().toLowerCase();
        }

        if(this.getClass().isAnnotationPresent(Usage.class)) {
            this.usage = this.getClass().getAnnotation(Usage.class).value();
        }

        if(this.getClass().isAnnotationPresent(PremiumOnly.class)) {
            this.premiumOnly = true;
        }

        if(this.getClass().isAnnotationPresent(MaintenanceCommand.class)) {
            this.maintenanceCommand = true;
        }

        if(this.getClass().isAnnotationPresent(StaffOnly.class)) {
            this.staffOnly = true;
        }

        if(this.getClass().isAnnotationPresent(PermissionDeniedMessage.class)) {
            PermissionDeniedMessage message = this.getClass().getAnnotation(PermissionDeniedMessage.class);
            this.permissionMessages.put(message.name(), message.message());
        }
    }

    public String getName() {
        return name;
    }

    public String getUsage() {
        return usage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public HashMap<String, Command> getSubCommands() {
        return subCommands;
    }

    public Command getSubCommand(String name) {
        return subCommands.get(name);
    }

    public List<Group> getPermissions() {
        return permissions;
    }

    public HashMap<String, String> getPermissionMessages() {
        return permissionMessages;
    }

    public String getPermissionMessage(String name) {
        return permissionMessages.get(name);
    }

    public boolean isStaffOnly() {
        return staffOnly;
    }

    public boolean isPremiumOnly() {
        return premiumOnly;
    }

    public boolean isMaintenanceCommand() {
        return maintenanceCommand;
    }

    public void addSubCommand(Command command) {
        this.subCommands.put(command.getName().toLowerCase(), command);
    }

    /**
     * Every command that extends this should call the super,
     * because this makes sure that the player has the necessary access
     * to execute the command.
     *
     * If your execute override doesn't call this super, be sure to verify that
     * the user has the necessary access, otherwise it can cause great harm to the game!
     *
     * @param player The player that is trying to execute the command.
     * @param parts The command / parameters
     * @return Whether or not the execution of the command failed
     */
    public boolean execute(Player player, List<String> parts) {
        if(this.isPremiumOnly() && (!player.isPremium() && !player.isStaff())) {
            player.getPacketSender().sendMessage(this.getPermissionMessage("Premium"));
            return false;
        }

        if(this.isStaffOnly() && !player.isStaff()) {
            if(this.getPermissionMessage("Maintenance") != null
                    && this.getPermissionMessage("Staff").length() > 0) {
                player.getPacketSender().sendMessage(this.getPermissionMessage("Staff"));
            }

            return false;
        }

        if(this.isMaintenanceCommand() && !player.isMaintenance()) {
            if(this.getPermissionMessage("Maintenance") != null
                    && this.getPermissionMessage("Maintenance").length() > 0) {
                player.getPacketSender().sendMessage(this.getPermissionMessage("Maintenance"));
            }

            return false;
        }

        if(parts.size() > 0) {
            String command = parts.get(0);
            Command commandObject = this.getSubCommands().get(command);
            if(commandObject != null) {
                parts.remove(0);
                return commandObject.execute(player, parts);
            }
        }

        return true;
    }

    public boolean execute(Player player, List<String> parts, Packet packet) {
        return execute(player, parts);
    }
}
