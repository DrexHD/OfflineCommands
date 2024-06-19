package me.drex.offlinecommands;

import me.drex.offlinecommands.mixin.accessor.PlayerListAccessor;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO Test if offline commands work on players that never joined
// TODO getOptionalEntities / getOptionalPlayer

// TODO Optimization potential: Add a cache for offline player data (to prevent frequent load / save)
public class OfflineCommands implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("offlinecommands");

    @Override
    public void onInitialize() {
//		SharedConstants.IS_RUNNING_IN_IDE = true;
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