package me.drex.offlinecommands.mixin.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.offlinecommands.OfflineCommands;
import me.drex.offlinecommands.commands.OfflineEntityArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.server.commands.EffectCommands;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(EffectCommands.class)
public abstract class EffectCommandsMixin {

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
        method = "giveEffect",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer(CommandSourceStack commandSourceStack, Collection<? extends Entity> collection, Holder<MobEffect> holder, @Nullable Integer integer, int i, boolean bl, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntities(collection);
    }

    @Inject(
        method = "clearEffect",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer(CommandSourceStack commandSourceStack, Collection<? extends Entity> collection, Holder<MobEffect> holder, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntities(collection);
    }

    @Inject(
        method = "clearEffects",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer(CommandSourceStack commandSourceStack, Collection<? extends Entity> collection, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntities(collection);
    }

}
