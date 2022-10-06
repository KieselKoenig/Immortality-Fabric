package net.hempflingclub.immortality.event;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PlayerOnKillEntity {
    public static void initialize() {
        //Initialize Events
        final String targetMobKey = "entity.minecraft.ender_dragon";
        PlayerKillEntityCallback.EVENT.register((playerEntity, killedEntity) -> {
            //Code run when Player Kills Entity
            if (killedEntity.getType().getTranslationKey().equals(targetMobKey)) {
                try {
//                    ImmortalityData.setMobKillCounter(ImmortalityData.getMobKillCounter() + 1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                for (ServerPlayerEntity player : playerEntity.getWorld().getPlayers()) {
                    try {
//                        player.sendMessage(Text.literal("Mob killed by " +
//                                playerEntity.getName().getString() +
//                                " Total Kills: " +
//                                ImmortalityData.getMobKillCounter()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                for (ServerPlayerEntity player : playerEntity.getWorld().getPlayers()) {
                    player.sendMessage(Text.literal("Mob killed by " + playerEntity.getName().getString() + " But not target Mob"));
                }
            }
        });
    }
}
