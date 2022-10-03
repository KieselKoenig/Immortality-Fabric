package net.hempflingclub.immortality.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.hempflingclub.immortality.util.IPlayerDataSaver;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PlayerTickHandler implements ServerTickEvents.StartTick {
    @Override
    public void onStartTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            //Run Stuff
            if (ImmortalityData.getImmortality((IPlayerDataSaver) player)) {
                if (player.getWorld().getTime() % 20 == 0) {
                    player.sendMessage(Text.literal(player.getIp()));
                }
            }
            if (ImmortalityData.getVoidHeart((IPlayerDataSaver) player)) {
                if (player.getWorld().getTime() % 20 == 0) {
                    player.getHungerManager().add(1, 1);
                }
            }

        }
    }
}
