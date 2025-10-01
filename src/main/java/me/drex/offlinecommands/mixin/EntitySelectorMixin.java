package me.drex.offlinecommands.mixin;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import me.drex.offlinecommands.util.OfflineEntitySelector;
import net.fabricmc.fabric.impl.event.interaction.FakePlayerNetworkHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.PlayerSpawnFinder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
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
    public List<ServerPlayer> findOfflinePlayer(CommandSourceStack source) {
        if (this.playerName == null) {
            return Collections.emptyList();
        }
        MinecraftServer server = source.getServer();
        PlayerList playerList = server.getPlayerList();
        Optional<NameAndId> optionalProfile = server.services().nameToIdCache().get(this.playerName);
        if (optionalProfile.isEmpty()) {
            return Collections.emptyList();
        }

        ServerPlayer serverPlayer = new ServerPlayer(server, server.overworld(), new GameProfile(optionalProfile.get().id(), optionalProfile.get().name()), ClientInformation.createDefault());
        new FakePlayerNetworkHandler(serverPlayer);
        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(serverPlayer.problemPath(), LOGGER)) {
            Optional<ValueInput> optional = playerList.loadPlayerData(optionalProfile.get())
                .map(compoundTag -> TagValueInput.create(scopedCollector, server.registryAccess(), compoundTag));
            // [VanillaCopy] - PrepareSpawnTask.start
            ServerPlayer.SavedPosition savedPosition = optional.flatMap(valueInput -> valueInput.read(ServerPlayer.SavedPosition.MAP_CODEC))
                .orElse(ServerPlayer.SavedPosition.EMPTY);
            LevelData.RespawnData respawnData = server.getWorldData().overworldData().getRespawnData();
            ServerLevel spawnLevel = savedPosition.dimension().map(server::getLevel).orElseGet(() -> {
                ServerLevel serverLevelx = server.getLevel(respawnData.dimension());
                return serverLevelx != null ? serverLevelx : server.overworld();
            });
            Vec3 spawnPosition = savedPosition.position().orElseGet(() -> PlayerSpawnFinder.findSpawn(spawnLevel, respawnData.pos()).join());

            Vec2 spawnAngle = savedPosition.rotation().orElse(new Vec2(respawnData.yaw(), respawnData.pitch()));

            // [VanillaCopy] - PrepareSpawnTask$Ready.spawn
            optional.ifPresent(serverPlayer::load);
            serverPlayer.snapTo(spawnPosition, spawnAngle.x, spawnAngle.y);

            return Lists.newArrayList(serverPlayer);
        }

    }

}
