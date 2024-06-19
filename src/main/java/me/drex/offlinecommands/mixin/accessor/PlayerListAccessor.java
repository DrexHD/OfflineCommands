package me.drex.offlinecommands.mixin.accessor;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerList.class)
public interface PlayerListAccessor {

    @Invoker
    void invokeSave(ServerPlayer serverPlayer);

}
