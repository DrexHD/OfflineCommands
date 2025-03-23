package me.drex.offlinecommands.mixin;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import me.drex.offlinecommands.util.OfflineEntitySelector;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.impl.event.interaction.FakePlayerNetworkHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static me.drex.offlinecommands.OfflineCommands.LOGGER;

@Mixin(EntitySelector.class)
public class EntitySelectorMixin implements OfflineEntitySelector {

    @Shadow
    @Final
    private @Nullable String playerName;

    @Override
    public List<ServerPlayer> findOfflinePlayer(CommandSourceStack source) throws CommandSyntaxException {
        if (this.playerName == null) {
            return Collections.emptyList();
        }
        MinecraftServer server = source.getServer();
        PlayerList playerList = server.getPlayerList();
        Optional<GameProfile> optionalProfile = server.getProfileCache().get(this.playerName);
        if (optionalProfile.isEmpty()) {
            return Collections.emptyList();
        }
        ServerPlayer serverPlayer = playerList.getPlayerForLogin(optionalProfile.get(), ClientInformation.createDefault());
        new FakePlayerNetworkHandler(serverPlayer);

        Optional<CompoundTag> optional = playerList.load(serverPlayer);
        // [VanillaCopy] - PlayerList.placeNewPlayer
        ResourceKey<Level> resourceKey = optional.flatMap(compoundTag -> DimensionType.parseLegacy(new Dynamic<>(NbtOps.INSTANCE, compoundTag.get("Dimension"))).resultOrPartial(LOGGER::error)).orElse(Level.OVERWORLD);
        ServerLevel serverLevel = server.getLevel(resourceKey);
        if (serverLevel == null) {
            serverLevel = server.overworld();
        }
        serverPlayer.setServerLevel(serverLevel);
        serverPlayer.loadGameTypes(optional.orElse(null));

        return Lists.newArrayList(serverPlayer);
    }

}
