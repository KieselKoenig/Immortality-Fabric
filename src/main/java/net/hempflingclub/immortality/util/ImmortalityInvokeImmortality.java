package net.hempflingclub.immortality.util;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.hempflingclub.immortality.enchantments.ImmortalityEnchants;
import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.statuseffect.ModEffectRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

import java.util.Objects;

public final class ImmortalityInvokeImmortality {
    public final static DamageSource soulBoundDamageSource = new DamageSource("immortality.soulBound").setBypassesArmor().setBypassesProtection().setUnblockable();

    public static float damageManager(LivingEntity livingEntity, DamageSource dmgSource, float damageAmount) {
        if (livingEntity.isPlayer()) {
            PlayerEntity playerEntity = (PlayerEntity) livingEntity;
            if (!livingEntity.world.isClient
                    && (ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.getLiverImmortality(playerEntity) || ImmortalityStatus.isSemiImmortal(playerEntity) || ImmortalityStatus.isSemiImmortal(playerEntity))
                    && (livingEntity.getHealth() - damageAmount) <= 0) {
                // This is Server, Player is Immortal and would've Died
                if (playerEntity.getY() <= playerEntity.world.getBottomY() && dmgSource == DamageSource.OUT_OF_WORLD) {
                    //If in Void taking damage then Teleport to Spawnpoint/Bed of Player, When no Bed is found then yeet them to Overworld Spawn
                    FabricDimensions.teleport(playerEntity
                            , Objects.requireNonNull(playerEntity.world.getServer()).getWorld(((ServerPlayerEntity) playerEntity).getSpawnPointDimension())
                            , new TeleportTarget(
                                    Vec3d.of((((ServerPlayerEntity) playerEntity).getSpawnPointPosition()) == null ?
                                            Objects.requireNonNull(playerEntity.getWorld().getServer()).getOverworld().getSpawnPos() :
                                            (((ServerPlayerEntity) playerEntity).getSpawnPointPosition()))
                                    , Vec3d.ZERO, playerEntity.headYaw, playerEntity.getPitch()
                            ));
                    playerEntity.fallDistance = 0;
                } else if (dmgSource != DamageSource.OUT_OF_WORLD) {
                    playerEntity.getWorld().playSoundFromEntity(null, playerEntity, SoundEvents.BLOCK_AMETHYST_CLUSTER_FALL, SoundCategory.PLAYERS, 5, 1);
                    ((ServerWorld) playerEntity.getWorld()).spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), 64, 0, 5, 0, 1);
                    //Extinguish and refill Air
                    playerEntity.setAir(playerEntity.getMaxAir());
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20, 0, false, false));
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1, false, false));
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 15 * 20, 2, false, false));
                    //Increase Immortals Death Counter, if DamageType is not Void Damage
                    if (!((ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.isTrueImmortal(playerEntity)) && ImmortalityStatus.isSemiImmortal(playerEntity)) && (dmgSource != soulBoundDamageSource && (ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.isTrueImmortal(playerEntity)))) {
                        ImmortalityStatus.incrementImmortalityDeath(playerEntity);
                        //Immortals shouldn't be strengthened by SoulBound Deaths
                        if (ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.isTrueImmortal(playerEntity)) {
                            ImmortalityStatus.addImmortalityArmorT(playerEntity);
                        }
                    }
                    //Increase Death Counter in Statistics
                    playerEntity.incrementStat(Stats.DEATHS);
                    if (ImmortalityStatus.getImmortality(playerEntity) && !ImmortalityStatus.isSemiImmortal(playerEntity)) {
                        //If real Immortality not LiverImmortality then use Leveling Mechanic
                        playerEntity.setHealth(playerEntity.getMaxHealth());
                        if (dmgSource.getSource() != null && dmgSource.getSource() != playerEntity && dmgSource.getSource().isPlayer()) {
                            PlayerEntity attackingPlayer = (PlayerEntity) dmgSource.getSource();
                            if (attackingPlayer.getMainHandStack().hasEnchantments()) {
                                if (EnchantmentHelper.getLevel(ImmortalityEnchants.Bane_Of_Life, attackingPlayer.getMainHandStack()) > 0) {
                                    //Killed By Bane Of Life
                                    ImmortalityStatus.setKilledByBaneOfLifeTime(playerEntity, ImmortalityStatus.getCurrentTime(playerEntity));
                                    ImmortalityStatus.incrementKilledByBaneOfLifeCount(playerEntity);
                                    if (ImmortalityStatus.getKilledByBaneOfLifeCount(playerEntity) >= 3) {
                                        if (!ImmortalityStatus.isSemiImmortal(playerEntity)) {
                                            ImmortalityStatus.setSemiImmortality(playerEntity, true);
                                        }
                                    }
                                    playerEntity.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.bane_of_life, 60 * 20, 0, true, true));
                                    if (!ImmortalityData.getLiverExtracted(ImmortalityStatus.getPlayerComponent(playerEntity))) {
                                        ImmortalityData.setLiverExtracted(ImmortalityStatus.getPlayerComponent(playerEntity), true);
                                        ImmortalityData.setLiverExtractionTime(ImmortalityStatus.getPlayerComponent(playerEntity), ImmortalityStatus.getCurrentTime(playerEntity));
                                        ImmortalityStatus.addRegrowingLiver(playerEntity);
                                        playerEntity.sendMessage(Text.translatable("immortality.status.liver_removed_forcefully"), true);
                                        attackingPlayer.giveItemStack(new ItemStack(ImmortalityItems.LiverOfImmortality));
                                    }
                                }
                            }
                        }
                        if (ImmortalityData.getImmortalDeaths(ImmortalityStatus.getPlayerComponent(playerEntity)) == 25 && dmgSource != soulBoundDamageSource) {
                            playerEntity.giveItemStack(new ItemStack(ImmortalityItems.VoidHeart));
                            playerEntity.sendMessage(Text.translatable("immortality.status.trainedVoidHeart"), true);
                        }
                        if (dmgSource != soulBoundDamageSource && (ImmortalityData.getImmortalDeaths(ImmortalityStatus.getPlayerComponent(playerEntity)) + 1) % 5 == 0 && ImmortalityData.getImmortalDeaths(ImmortalityStatus.getPlayerComponent(playerEntity)) < 50) {
                            ImmortalityStatus.addImmortalityArmor(playerEntity);
                            playerEntity.sendMessage(Text.translatable("immortality.status.skinHardened"), true);
                        }
                    } else if (ImmortalityStatus.getLiverImmortality(playerEntity) || ImmortalityStatus.isSemiImmortal(playerEntity)) {
                        //If LiverImmortality then use Degrading Mechanic
                        //Remove 1 Heart per Death
                        if (dmgSource.getSource() != null && dmgSource.getSource() != playerEntity && dmgSource.getSource().isPlayer()) {
                            PlayerEntity attackingPlayer = (PlayerEntity) dmgSource.getSource();
                            if (attackingPlayer.getMainHandStack().hasEnchantments()) {
                                if (EnchantmentHelper.getLevel(ImmortalityEnchants.Bane_Of_Life, attackingPlayer.getMainHandStack()) > 0) {
                                    ImmortalityStatus.setKilledByBaneOfLifeTime(playerEntity, ImmortalityStatus.getCurrentTime(playerEntity));
                                    ImmortalityStatus.incrementKilledByBaneOfLifeCount(playerEntity);
                                    ImmortalityStatus.addNegativeHearts(playerEntity); //Second Negative Hearts
                                    if (ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.getImmortality(playerEntity)) {
                                        playerEntity.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.bane_of_life, 60 * 20, 0, true, true));
                                        if (!ImmortalityData.getLiverExtracted(ImmortalityStatus.getPlayerComponent(playerEntity))) {
                                            ImmortalityData.setLiverExtracted(ImmortalityStatus.getPlayerComponent(playerEntity), true);
                                            ImmortalityData.setLiverExtractionTime(ImmortalityStatus.getPlayerComponent(playerEntity), ImmortalityStatus.getCurrentTime(playerEntity));
                                            ImmortalityStatus.addRegrowingLiver(playerEntity);
                                            playerEntity.sendMessage(Text.translatable("immortality.status.liver_removed_forcefully"), true);
                                            attackingPlayer.giveItemStack(new ItemStack(ImmortalityItems.LiverOfImmortality));
                                        }
                                    }
                                }
                            }
                        }
                        if (ImmortalityStatus.isSemiImmortal(playerEntity)) {
                            ImmortalityStatus.setSemiImmortalityLostHeartTime(playerEntity, ImmortalityStatus.getCurrentTime(playerEntity));
                        }
                        ImmortalityStatus.addNegativeHearts(playerEntity);
                        playerEntity.setHealth(playerEntity.getMaxHealth());
                        if (playerEntity.getMaxHealth() < 2) {
                            //0 Hearts then remove LiverImmortality
                            if (ImmortalityStatus.getLiverImmortality(playerEntity)) {
                                ImmortalityStatus.removeFalseImmortality(playerEntity);
                            }
                            if (ImmortalityStatus.isSemiImmortal(playerEntity)) {
                                ImmortalityStatus.removeNegativeHearts(playerEntity);
                            }
                            for (PlayerEntity players : playerEntity.getWorld().getPlayers()) {
                                players.playSound(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 1, 1);
                            }
                            if (ImmortalityStatus.getLifeElixirDropTime(playerEntity) == 0 || ImmortalityStatus.getCurrentTime(playerEntity) >= (ImmortalityStatus.getLifeElixirDropTime(playerEntity) + 300 * 20)) {
                                ImmortalityStatus.setLifeElixirDropTime(playerEntity, ImmortalityStatus.getCurrentTime(playerEntity));
                                ItemEntity itemEntity = new ItemEntity(EntityType.ITEM, playerEntity.getWorld());
                                itemEntity.setPosition(playerEntity.getPos());
                                itemEntity.setStack(new ItemStack(ImmortalityItems.LifeElixir));
                                playerEntity.getWorld().spawnEntity(itemEntity);
                            }
                            if (!ImmortalityStatus.isSemiImmortal(playerEntity)) {
                                for (PlayerEntity player : Objects.requireNonNull(playerEntity.getServer()).getPlayerManager().getPlayerList()) {
                                    if (dmgSource.getSource() != null && dmgSource.getSource() != playerEntity) {
                                        player.sendMessage(Text.translatable("immortality.last.death.player", playerEntity.getName().getString(), dmgSource.getSource().getName().getString()));
                                    } else {
                                        player.sendMessage(Text.translatable("immortality.last.death", playerEntity.getName().getString()));
                                    }
                                }
                            } else {
                                if ((ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.isTrueImmortal(playerEntity))) {
                                    if (ImmortalityStatus.isTrueImmortal(playerEntity)) {
                                        for (PlayerEntity player : Objects.requireNonNull(playerEntity.getServer()).getPlayerManager().getPlayerList()) {
                                            if (dmgSource.getSource() != null && dmgSource.getSource() != playerEntity) {
                                                player.sendMessage(Text.translatable("immortality.trueImmortal_slayed.death.player", playerEntity.getName().getString(), dmgSource.getSource().getName().getString()));
                                            } else {
                                                player.sendMessage(Text.translatable("immortality.trueImmortal_slayed.death", playerEntity.getName().getString()));
                                            }
                                        }
                                    } else {
                                        for (PlayerEntity player : Objects.requireNonNull(playerEntity.getServer()).getPlayerManager().getPlayerList()) {
                                            if (dmgSource.getSource() != null && dmgSource.getSource() != playerEntity) {
                                                player.sendMessage(Text.translatable("immortality.immortal_slayed.death.player", playerEntity.getName().getString(), dmgSource.getSource().getName().getString()));
                                            } else {
                                                player.sendMessage(Text.translatable("immortality.immortal_slayed.death", playerEntity.getName().getString()));
                                            }
                                        }
                                    }
                                    ImmortalityStatus.convertSemiImmortalityIntoOtherImmortality(playerEntity);
                                } else {
                                    //Just Semi Immortal
                                    for (PlayerEntity player : Objects.requireNonNull(playerEntity.getServer()).getPlayerManager().getPlayerList()) {
                                        if (dmgSource.getSource() != null && dmgSource.getSource() != playerEntity) {
                                            player.sendMessage(Text.translatable("immortality.semiImmortal_slayed.death.player", playerEntity.getName().getString(), dmgSource.getSource().getName().getString()));
                                        } else {
                                            player.sendMessage(Text.translatable("immortality.semiImmortal_slayed.death", playerEntity.getName().getString()));
                                        }
                                    }
                                    ImmortalityStatus.removeNegativeHearts(playerEntity);
                                }
                                ImmortalityStatus.resetKilledByBaneOfLifeTime(playerEntity);
                                ImmortalityStatus.resetKilledByBaneOfLifeCount(playerEntity);
                            }
                            if (ImmortalityStatus.hasTargetGiftedImmortal(playerEntity)) {
                                if (ImmortalityStatus.getTargetGiftedImmortalLivingEntity(playerEntity) != null) {
                                    Objects.requireNonNull(ImmortalityStatus.getTargetGiftedImmortalLivingEntity(playerEntity)).setHealth(1);
                                    Objects.requireNonNull(ImmortalityStatus.getTargetGiftedImmortalLivingEntity(playerEntity)).damage(soulBoundDamageSource, 1000);
                                } else {
                                    ImmortalityStatus.removeTargetGiftedImmortal(playerEntity);
                                }
                            }
                            return damageAmount;
                        }
                    }
                }
                //Prevent Death
                playerEntity.setHealth(playerEntity.getMaxHealth());
                return 0;
            }
            //Can Survive Damage or ain't immortal
            return damageAmount;
        } else {
            if ((livingEntity.getHealth() - damageAmount) <= 0) {
                //Non Player would die
                if (ImmortalityStatus.hasTargetGiverImmortal(livingEntity)) {
                    //Is SoulBound to Player
                    if (ImmortalityStatus.getTargetGiverImmortalPlayerEntity(livingEntity) != null) {
                        //Player is Online
                        PlayerEntity giverImmortal = ImmortalityStatus.getTargetGiverImmortalPlayerEntity(livingEntity);
                        if (!(ImmortalityStatus.getImmortality(giverImmortal) || ImmortalityStatus.isTrueImmortal(giverImmortal) || ImmortalityStatus.isSemiImmortal(giverImmortal) || ImmortalityStatus.getLiverImmortality(giverImmortal))) {
                            //Player is no longer Immortal
                            return damageAmount;
                        } else {
                            //Check if Killed by Bane Of Life
                            if (dmgSource.getSource() != null && dmgSource.getSource() != livingEntity && dmgSource.getSource().isPlayer()) {
                                PlayerEntity attackingPlayer = (PlayerEntity) dmgSource.getSource();
                                if (attackingPlayer.getMainHandStack().hasEnchantments()) {
                                    if (EnchantmentHelper.getLevel(ImmortalityEnchants.Bane_Of_Life, attackingPlayer.getMainHandStack()) > 0) {
                                        //Killed By Bane Of Life
                                        giverImmortal.sendMessage(Text.translatable("immortality.soulBound_killed_with_baneOfLife", Objects.requireNonNull(livingEntity.getCustomName()).getString(), attackingPlayer.getName().getString()));
                                        giverImmortal.setHealth(1);
                                        giverImmortal.damage(soulBoundDamageSource, 1000);
                                        return damageAmount;
                                    }
                                }
                            }
                            if (livingEntity.getY() <= livingEntity.world.getBottomY() && dmgSource == DamageSource.OUT_OF_WORLD) {
                                //If in Void taking damage then Teleport to Spawnpoint/Bed of Player, When no Bed is found then yeet them to Overworld Spawn
                                FabricDimensions.teleport(livingEntity
                                        , Objects.requireNonNull(livingEntity.world.getServer()).getWorld(((ServerPlayerEntity) livingEntity).getSpawnPointDimension())
                                        , new TeleportTarget(
                                                Vec3d.of((((ServerPlayerEntity) livingEntity).getSpawnPointPosition()) == null ?
                                                        Objects.requireNonNull(livingEntity.getWorld().getServer()).getOverworld().getSpawnPos() :
                                                        (((ServerPlayerEntity) livingEntity).getSpawnPointPosition()))
                                                , Vec3d.ZERO, livingEntity.headYaw, livingEntity.getPitch()
                                        ));
                                livingEntity.fallDistance = 0;
                            } else if (dmgSource != DamageSource.OUT_OF_WORLD) {
                                giverImmortal.setHealth(1);
                                giverImmortal.damage(soulBoundDamageSource, 1000);
                                livingEntity.getWorld().playSoundFromEntity(null, livingEntity, SoundEvents.BLOCK_AMETHYST_CLUSTER_FALL, SoundCategory.NEUTRAL, 5, 1);
                                ((ServerWorld) livingEntity.getWorld()).spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 64, 0, 5, 0, 1);
                                livingEntity.setAir(livingEntity.getMaxAir());
                                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20, 0, false, false));
                                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1, false, false));
                                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 15 * 20, 2, false, false));
                            }
                            livingEntity.setHealth(livingEntity.getMaxHealth());
                            return 0;
                        }
                    }
                }
            }
        }
        return damageAmount;
    }
}
