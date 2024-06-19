package me.drex.offlinecommands.mixin.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.offlinecommands.OfflineCommands;
import me.drex.offlinecommands.commands.OfflineEntityArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.TagCommand;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(TagCommand.class)
public abstract class TagCommandMixin {

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
        method = "addTag",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer$addTag(CommandSourceStack commandSourceStack, Collection<? extends Entity> collection, String string, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntities(collection);
    }

    @Inject(
        method = "removeTag",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer$removeTag(CommandSourceStack commandSourceStack, Collection<? extends Entity> collection, String string, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntities(collection);
    }

    @Inject(
        method = "listTags",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer$listTags(CommandSourceStack commandSourceStack, Collection<? extends Entity> collection, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntities(collection);
    }

}
