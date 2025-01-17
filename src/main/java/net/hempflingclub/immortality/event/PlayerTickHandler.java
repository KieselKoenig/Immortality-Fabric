package net.hempflingclub.immortality.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.hempflingclub.immortality.statuseffect.ModEffectRegistry;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.hempflingclub.immortality.util.ImmortalityStatus;
import net.minecraft.entity.effect.*;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class PlayerTickHandler implements ServerTickEvents.StartTick {
    @Override
    public void onStartTick(MinecraftServer server) {
        int currentTime = ImmortalityStatus.getCurrentTime(server);
        if (currentTime % 20 == 0) {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                //Run Stuff
                if (currentTime % 100 == 0) { // Every 5sec
                    if (ImmortalityData.getLiverExtracted(ImmortalityStatus.getPlayerComponent(player))) {
                        if (ImmortalityStatus.getRegeneratingHearts(player) == 0) {
                            ImmortalityStatus.addRegrowingLiver(player);
                        }
                    }
                    if (ImmortalityStatus.hasTargetGiftedImmortal(player)) {
                        if (!(ImmortalityStatus.isSemiImmortal(player) || ImmortalityStatus.getLiverImmortality(player) || ImmortalityStatus.getImmortality(player) || ImmortalityStatus.isTrueImmortal(player)) || ImmortalityStatus.getTargetGiftedImmortalLivingEntity(player) == null) {
                            ImmortalityStatus.removeTargetGiftedImmortal(player);
                        }
                    }
                }
                if (ImmortalityData.getHeartExtractionAmount(ImmortalityStatus.getPlayerComponent(player)) > 0) {
                    ImmortalityData.setHeartExtractionAmount(ImmortalityStatus.getPlayerComponent(player), 0);
                }
                if (currentTime >= (ImmortalityStatus.getLifeElixirDropTime(player) + 300 * 20)) {
                    ImmortalityStatus.resetLifeElixirDropTime(player);
                }
                if (ImmortalityStatus.getKilledByBaneOfLifeCount(player) > 0) {
                    if (ImmortalityStatus.getImmortality(player) || ImmortalityStatus.isTrueImmortal(player)) {
                        //Give Effect
                        if (!player.hasStatusEffect(ModEffectRegistry.bane_of_life)) {
                            player.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.bane_of_life, 5 * 20, 0, true, true));
                        }
                    }
                    if (currentTime >= (ImmortalityStatus.getKilledByBaneOfLifeTime(player) + 60 * 20) || ImmortalityStatus.getKilledByBaneOfLifeTime(player) == 0) {
                        if ((ImmortalityStatus.getImmortality(player) || ImmortalityStatus.isTrueImmortal(player)) && ImmortalityStatus.isSemiImmortal(player)) {
                            ImmortalityStatus.convertSemiImmortalityIntoOtherImmortality(player);
                        }
                        ImmortalityStatus.resetKilledByBaneOfLifeTime(player);
                        ImmortalityStatus.resetKilledByBaneOfLifeCount(player);
                    }
                } else if ((ImmortalityStatus.getNegativeHearts(player) > 0) && (currentTime >= (ImmortalityStatus.getSemiImmortalityLostHeartTime(player) + 300 * 20)) && ImmortalityStatus.isSemiImmortal(player)) {
                    ImmortalityStatus.removeOneNegativeHeart(player);
                    player.setHealth(player.getHealth() + 2);
                    player.sendMessage(Text.translatable("immortality.status.heart_restored"), true);
                    if (ImmortalityStatus.getNegativeHearts(player) > 0) {
                        ImmortalityStatus.setSemiImmortalityLostHeartTime(player, currentTime);
                    } else {
                        ImmortalityStatus.resetSemiImmortalityLostHeartTime(player);
                    }
                }
                if (ImmortalityStatus.getImmortality(player)) {
                    if (ImmortalityStatus.getLiverImmortality(player)) {
                        //Illegal State shouldn't have both
                        ImmortalityStatus.removeFalseImmortality(player);
                        ImmortalityStatus.setImmortality(player, false);
                    }
                    if (ImmortalityData.getLiverExtracted(ImmortalityStatus.getPlayerComponent(player))) {
                        //Give Extraction debuffs
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * 5, 0, false, false));
                        if (currentTime >= (ImmortalityData.getLiverExtractionTime(ImmortalityStatus.getPlayerComponent(player)) + (20 * 300)) || ImmortalityData.getLiverExtractionTime(ImmortalityStatus.getPlayerComponent(player)) == 0) { // After 5mins Liver has regrown
                            ImmortalityStatus.removeRegrowing(player);
                            ImmortalityData.setLiverExtracted(ImmortalityStatus.getPlayerComponent(player), false);
                            player.sendMessage(Text.translatable("immortality.status.liver_regrown"), true);
                        }
                    }
                    //Include Functionality for Death Leveling
                    int immortalDeaths = ImmortalityData.getImmortalDeaths(ImmortalityStatus.getPlayerComponent(player));
                    if (ImmortalityStatus.isTrueImmortal(player)) {
                        //He has Trilogy and Required Hearts
                        //Radiating Immortality repairing unliving things
                        boolean repaired = false;
                        for (ItemStack item : player.getArmorItems()) {
                            if (item.isDamaged() && !repaired) {
                                item.setDamage(item.getDamage() - 1);
                                repaired = true;
                            }
                        }
                        for (ItemStack item : player.getHandItems()) {
                            if (item.isDamaged() && !repaired) {
                                item.setDamage(item.getDamage() - 1);
                                repaired = true;
                            }
                        }
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 5, 2, false, false));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 5, 1, false, false));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 20 * 5, 0, false, false));
                        player.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.trilogy, 20 * 5, 0, false, false));
                    } else if (immortalDeaths >= 25) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 5, 2, false, false));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 5, 1, false, false));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 20 * 5, 0, false, false));
                        player.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.immortality, 20 * 5, 0, false, false));
                    } else if (immortalDeaths >= 20) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 5, 2, false, false));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 5, 1, false, false));
                        player.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.immortality, 20 * 5, 0, false, false));
                    } else if (immortalDeaths >= 15) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 5, 2, false, false));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 5, 0, false, false));
                        player.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.immortality, 20 * 5, 0, false, false));
                    } else if (immortalDeaths >= 10) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 5, 1, false, false));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 5, 0, false, false));
                        player.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.immortality, 20 * 5, 0, false, false));
                    } else if (immortalDeaths >= 5) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 5, 0, false, false));
                        player.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.immortality, 20 * 5, 0, false, false));
                    } else {
                        player.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.immortality, 20 * 5, 0, false, false));
                    }
                    if (player.isOnFire()) {
                        player.getWorld().playSoundFromEntity(null, player, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS, 1, 1);
                        player.setOnFire(false);
                    }
                    if (ImmortalityStatus.isSemiImmortal(player)) {
                        player.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.semi_immortality, 20 * 5, 0, false, false));
                    }
                } else if (ImmortalityStatus.getLiverImmortality(player)) {
                    if (player.isOnFire()) {
                        player.getWorld().playSoundFromEntity(null, player, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS, 1, 1);
                        player.setOnFire(false);
                    }
                    player.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.liver_immortality, 20 * 5, 0, false, false));
                } else if (ImmortalityStatus.isSemiImmortal(player)) {
                    player.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.semi_immortality, 20 * 5, 0, false, false));
                }
                //Not Immortal
                if (ImmortalityStatus.getVoidHeart(player)) {
                    if (!ImmortalityStatus.isTrueImmortal(player)) {
                        player.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.void_heart, 20 * 5, 0, false, false));
                    }
                    if (currentTime % 600 == 0 || (ImmortalityStatus.isTrueImmortal(player)) || (currentTime % 150 == 0 && (ImmortalityStatus.getImmortality(player) || ImmortalityStatus.isSemiImmortal(player)))) {
                        player.getHungerManager().add(1, 1);
                    }
                }
                //Not Void Heart

                //Not Liver Immortality
            }
        }
    }
}

