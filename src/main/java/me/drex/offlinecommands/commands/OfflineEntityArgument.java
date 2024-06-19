package me.drex.offlinecommands.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.offlinecommands.util.OfflineEntitySelector;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.Collection;
import java.util.List;

public class OfflineEntityArgument extends EntityArgument {

    private OfflineEntityArgument() {
        super(false, false);
    }

    public static Collection<ServerPlayer> getOfflinePlayers(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        EntitySelector entitySelector = commandContext.getArgument(string, EntitySelector.class);
        List<ServerPlayer> list = entitySelector.findPlayers(commandContext.getSource());
        if (list.isEmpty()) {
            list = ((OfflineEntitySelector) entitySelector).findOfflinePlayer(commandContext.getSource());
            if (list.isEmpty()) {
                throw NO_PLAYERS_FOUND.create();
            }
        }
        return list;
    }

    public static Collection<ServerPlayer> getOptionalOfflinePlayers(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        EntitySelector entitySelector = commandContext.getArgument(string, EntitySelector.class);
        List<ServerPlayer> list = entitySelector.findPlayers(commandContext.getSource());
        if (list.isEmpty()) {
            list = ((OfflineEntitySelector) entitySelector).findOfflinePlayer(commandContext.getSource());
        }
        return list;
    }

    public static ServerPlayer getOfflinePlayer(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        EntitySelector entitySelector = commandContext.getArgument(string, EntitySelector.class);
        List<ServerPlayer> list = entitySelector.findPlayers(commandContext.getSource());
        if (list.isEmpty()) {
            list = ((OfflineEntitySelector) entitySelector).findOfflinePlayer(commandContext.getSource());
        }
        // Replicate vanilla...
        if (list.size() != 1) {
            throw EntityArgument.NO_PLAYERS_FOUND.create();
        }
        return list.get(0);
    }

    public static Collection<? extends Entity> getOfflineEntities(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        EntitySelector entitySelector = commandContext.getArgument(string, EntitySelector.class);
        List<? extends Entity> list = entitySelector.findEntities(commandContext.getSource());

        if (list.isEmpty()) {
            list = ((OfflineEntitySelector) entitySelector).findOfflinePlayer(commandContext.getSource());
            if (list.isEmpty()) {
                throw NO_ENTITIES_FOUND.create();
            }
        }
        return list;
    }

    public static Collection<? extends Entity> getOptionalOfflineEntities(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        EntitySelector entitySelector = commandContext.getArgument(string, EntitySelector.class);
        List<? extends Entity> list = entitySelector.findEntities(commandContext.getSource());

        if (list.isEmpty()) {
            list = ((OfflineEntitySelector) entitySelector).findOfflinePlayer(commandContext.getSource());
        }
        return list;
    }

    public static Entity getOfflineEntity(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        EntitySelector entitySelector = commandContext.getArgument(string, EntitySelector.class);
        List<? extends Entity> list = entitySelector.findEntities(commandContext.getSource());
        if (list.isEmpty()) {
            list = ((OfflineEntitySelector) entitySelector).findOfflinePlayer(commandContext.getSource());
            if (list.isEmpty()) {
                throw EntityArgument.NO_ENTITIES_FOUND.create();
            }
        }
        if (list.size() > 1) {
            throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create();
        }
        return list.get(0);
    }

}
