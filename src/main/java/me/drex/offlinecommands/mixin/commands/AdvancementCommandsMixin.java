package me.drex.offlinecommands.mixin.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.offlinecommands.OfflineCommands;
import me.drex.offlinecommands.commands.OfflineEntityArgument;
import net.minecraft.advancements.Advancement;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.AdvancementCommands;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(AdvancementCommands.class)
public abstract class AdvancementCommandsMixin {

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
        method = "perform",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer(CommandSourceStack commandSourceStack, Collection<ServerPlayer> collection, @Coerce Object action, Collection<Advancement> collection2, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntities(collection);
    }

    @Inject(
        method = "performCriterion",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer(CommandSourceStack commandSourceStack, Collection<ServerPlayer> collection, @Coerce Object action, Advancement advancement, String string, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntities(collection);
    }

}
