package me.drex.offlinecommands.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public interface OfflineEntitySelector {

    List<ServerPlayer> findOfflinePlayer(CommandSourceStack commandSourceStack) throws CommandSyntaxException;

}
