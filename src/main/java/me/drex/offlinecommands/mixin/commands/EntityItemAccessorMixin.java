package me.drex.offlinecommands.mixin.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.offlinecommands.OfflineCommands;
import me.drex.offlinecommands.commands.OfflineEntityArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.item.EntityItemAccessor;
import net.minecraft.server.commands.item.ItemAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.slot.SlotSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

// Required for the /item command
@Mixin(EntityItemAccessor.class)
public abstract class EntityItemAccessorMixin {
    @Shadow
    @Final
    private Collection<? extends Entity> entities;

    @Redirect(
        method = "lambda$static$2",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/commands/arguments/EntityArgument;getEntities(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)Ljava/util/Collection;"
        )
    )
    private static Collection<? extends Entity> getOfflineEntities(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        return OfflineEntityArgument.getOfflineEntities(commandContext, string);
    }

    @Inject(
        method = "setItems",
        at = @At("RETURN")
    )
    private void saveOfflinePlayer(CommandSourceStack source, SlotSource slotSource, ItemAccessor.SetterFunction<Entity> function, CallbackInfo ci) {
        OfflineCommands.saveEntities(entities);
    }

}
