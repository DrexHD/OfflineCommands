package me.drex.offlinecommands;

import me.drex.offlinecommands.mixin.accessor.PlayerListAccessor;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfflineCommands implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("offlinecommands");

    @Override
    public void onInitialize() {
    }

    public static void saveEntities(Iterable<? extends Entity> entities) {
        for (Entity entity : entities) {
            saveEntity(entity);
        }
    }

    public static void saveEntity(Entity entity) {
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }
        PlayerList playerList = player.getServer().getPlayerList();
        if (playerList.getPlayers().contains(player)) {
            // Player is online
            return;
        }
        ((PlayerListAccessor) playerList).invokeSave(player);
    }

}