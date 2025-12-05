package me.drex.offlinecommands.mixin.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.drex.offlinecommands.OfflineCommands;
import me.drex.offlinecommands.commands.OfflineEntityArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.commands.AttributeCommand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AttributeCommand.class)
public abstract class AttributeCommandMixin {

    @Redirect(
        method = "*",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/commands/arguments/EntityArgument;getEntity(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)Lnet/minecraft/world/entity/Entity;"
        )
    )
    private static Entity getOfflineEntity(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        return OfflineEntityArgument.getOfflineEntity(commandContext, string);
    }

    @Inject(
        method = "getAttributeValue",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer$getAttributeValue(CommandSourceStack commandSourceStack, Entity entity, Holder<Attribute> holder, double d, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntity(entity);
    }

    @Inject(
        method = "getAttributeBase",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer$getAttributeBase(CommandSourceStack commandSourceStack, Entity entity, Holder<Attribute> holder, double d, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntity(entity);
    }

    @Inject(
        method = "getAttributeModifier",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer$getAttributeModifier(CommandSourceStack commandSourceStack, Entity entity, Holder<Attribute> holder, Identifier resourceLocation, double d, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntity(entity);
    }

    @Inject(
        method = "setAttributeBase",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer$setAttributeBase(CommandSourceStack commandSourceStack, Entity entity, Holder<Attribute> holder, double d, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntity(entity);
    }

    @Inject(
        method = "addModifier",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer$addModifier(CommandSourceStack commandSourceStack, Entity entity, Holder<Attribute> holder, Identifier resourceLocation, double d, AttributeModifier.Operation operation, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntity(entity);
    }

    @Inject(
        method = "removeModifier",
        at = @At("RETURN")
    )
    private static void saveOfflinePlayer$removeModifier(CommandSourceStack commandSourceStack, Entity entity, Holder<Attribute> holder, Identifier resourceLocation, CallbackInfoReturnable<Integer> cir) {
        OfflineCommands.saveEntity(entity);
    }

}
