package me.drex.offlinecommands.mixin.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.offlinecommands.OfflineCommands;
import me.drex.offlinecommands.commands.OfflineEntityArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.ClearInventoryCommands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.function.Predicate;

@Mixin(ClearInventoryCommands.class)
public abstract class ClearInventoryCommandsMixin {

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
        method = "clearInventory",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer(CommandSourceStack commandSourceStack, Collection<ServerPlayer> collection, Predicate<ItemStack> predicate, int i, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntities(collection);
    }
}
