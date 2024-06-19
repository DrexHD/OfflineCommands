package me.drex.offlinecommands.mixin.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.offlinecommands.OfflineCommands;
import me.drex.offlinecommands.commands.OfflineEntityArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.ExperienceCommand;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(ExperienceCommand.class)
public abstract class ExperienceCommandMixin {

    @Redirect(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/commands/arguments/EntityArgument;getPlayer(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)Lnet/minecraft/server/level/ServerPlayer;"
        )
    )
    private static ServerPlayer getOfflinePlayer(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        return OfflineEntityArgument.getOfflinePlayer(commandContext, string);
    }

    @Redirect(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/commands/arguments/EntityArgument;getPlayers(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)Ljava/util/Collection;"
        )
    )
    private static Collection<ServerPlayer> getOfflinePlayers(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        return OfflineEntityArgument.getOfflinePlayers(commandContext, string);
    }

    @Inject(
        method = "queryExperience",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer$queryExperience(CommandSourceStack commandSourceStack, ServerPlayer serverPlayer, @Coerce Object type, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntity(serverPlayer);
    }

    @Inject(
        method = "addExperience",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer$addExperience(CommandSourceStack commandSourceStack, Collection<? extends ServerPlayer> collection, int i, @Coerce Object type, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntities(collection);
    }

    @Inject(
        method = "setExperience",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer$setExperience(CommandSourceStack commandSourceStack, Collection<? extends ServerPlayer> collection, int i, @Coerce Object type, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntities(collection);
    }

}
