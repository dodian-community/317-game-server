package net.dodian.discord.listeners.guild;

import net.dodian.discord.JDAProvider;
import net.dodian.discord.listeners.ListenerAdapterBase;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePositionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class RoleListener extends ListenerAdapterBase {

    @Autowired
    public RoleListener(JDAProvider jdaProvider) {
        super(jdaProvider);
    }

    @Override
    public void onRoleCreate(@Nonnull RoleCreateEvent event) {
        // TODO: Implement functionality to push role to database.
        super.onRoleCreate(event);
    }

    @Override
    public void onRoleDelete(@Nonnull RoleDeleteEvent event) {
        // TODO: Implement functionality to delete role from database, and remove it from users.
        super.onRoleDelete(event);
    }

    @Override
    public void onRoleUpdateColor(@Nonnull RoleUpdateColorEvent event) {
        // TODO: Implement role updating to change the update data.
        super.onRoleUpdateColor(event);
    }

    @Override
    public void onRoleUpdateName(@Nonnull RoleUpdateNameEvent event) {
        // TODO: Implement role updating to change the update data.
        super.onRoleUpdateName(event);
    }

    @Override
    public void onRoleUpdatePermissions(@Nonnull RoleUpdatePermissionsEvent event) {
        // TODO: Implement role updating to change the update data.
        super.onRoleUpdatePermissions(event);
    }

    @Override
    public void onRoleUpdatePosition(@Nonnull RoleUpdatePositionEvent event) {
        // TODO: Implement role updating to change the update data.
        super.onRoleUpdatePosition(event);
    }
}
