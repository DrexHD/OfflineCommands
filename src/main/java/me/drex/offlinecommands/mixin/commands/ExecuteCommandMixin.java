package me.drex.offlinecommands.mixin.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.offlinecommands.commands.OfflineEntityArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.ExecuteCommand;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;

@Mixin(ExecuteCommand.class)
public abstract class ExecuteCommandMixin {
    @Redirect(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/commands/arguments/EntityArgument;getOptionalEntities(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)Ljava/util/Collection;"
        )
    )
    private static Collection<? extends Entity> getOfflineEntities(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        return OfflineEntityArgument.getOfflineEntities(commandContext, string);
    }
}
