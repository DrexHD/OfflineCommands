package me.drex.offlinecommands.mixin.commands.data;

import me.drex.offlinecommands.OfflineCommands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.commands.data.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityDataAccessor.class)
public abstract class EntityDataAccessorMixin {

    @Shadow @Final private Entity entity;

    @Inject(
        method = "setData",
        at = @At("RETURN")
    )
    private void saveOfflinePlayer(CompoundTag compoundTag, CallbackInfo ci) {
        OfflineCommands.saveEntity(this.entity);
    }

}
