package net.hempflingclub.immortality.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.hempflingclub.immortality.statuseffect.ModEffectRegistry;
import net.hempflingclub.immortality.util.IPlayerDataSaver;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.minecraft.entity.effect.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerTickHandler implements ServerTickEvents.StartTick {
    @Override
    public void onStartTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            //Run Stuff
            if (ImmortalityData.getImmortality((IPlayerDataSaver) player)) {
                if (player.getWorld().getTime() % 20 == 0) {
                    //Include Functionality for Death Leveling
                    int immortalDeaths = ImmortalityData.getImmortalDeaths((IPlayerDataSaver) player);
                    if (immortalDeaths >= 25) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 20, 2, false, false));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 20, 1, false, false));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 20 * 20, 0, false, false));
                    } else if (immortalDeaths >= 20) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 20, 2, false, false));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 20, 1, false, false));
                    } else if (immortalDeaths >= 15) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 20, 2, false, false));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 20, 0, false, false));
                    } else if (immortalDeaths >= 10) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 20, 1, false, false));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 20, 0, false, false));
                    } else if (immortalDeaths >= 5) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 20, 0, false, false));
                    }
                    player.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.immortality, 20 * 20, 0, false, false));
                }
            }
            //Not Immortal
            if (ImmortalityData.getVoidHeart((IPlayerDataSaver) player)) {
                if (player.getWorld().getTime() % 20 == 0) {
                    player.getHungerManager().add(1, 1);
                    player.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.void_heart, 20 * 20, 0, false, false));
                }
            }
            //Not Void Heart
        }
    }
}

