package me.drex.offlinecommands.mixin.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.offlinecommands.OfflineCommands;
import me.drex.offlinecommands.commands.OfflineEntityArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.ExecuteCommand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.SlotRange;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.function.Predicate;

@Mixin(ExecuteCommand.class)
public abstract class ExecuteCommandMixin {

    @Redirect(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/commands/arguments/EntityArgument;getEntities(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)Ljava/util/Collection;"
        )
    )
    private static Collection<? extends Entity> getOfflineEntities(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        return OfflineEntityArgument.getOfflineEntities(commandContext, string);
    }

    @Inject(
        method = "countItems(Ljava/lang/Iterable;Lnet/minecraft/world/inventory/SlotRange;Ljava/util/function/Predicate;)I",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer(Iterable<? extends Entity> iterable, SlotRange slotRange, Predicate<ItemStack> predicate, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntities(iterable);
    }

}
