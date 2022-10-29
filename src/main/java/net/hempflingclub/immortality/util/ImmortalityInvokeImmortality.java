package net.hempflingclub.immortality.util;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.hempflingclub.immortality.enchantments.ImmortalityEnchants;
import net.hempflingclub.immortality.entitys.ImmortalWither.ImmortalWither;
import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.statuseffect.ModEffectRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.TeleportTarget;

import java.util.Objects;

public final class ImmortalityInvokeImmortality {
    public final static DamageSource soulBoundDamageSource = new DamageSource("immortality.soulBound").setBypassesArmor().setBypassesProtection().setUnblockable();
    private static boolean killedByImmortalWither = false;
    private static final float witherDamageMultiplier = 2.5f;
    private static final float witherDamageMultiplierEasy = 2.0f;

    public static float damageManager(LivingEntity livingEntity, DamageSource dmgSource, float damageAmount) {
        if (livingEntity.isPlayer() && !livingEntity.getWorld().isClient()) {
            PlayerEntity playerEntity = (PlayerEntity) livingEntity;
            if (ImmortalityStatus.hasTargetGiftedImmortal(playerEntity) && dmgSource.getSource() != null && dmgSource.getSource() != playerEntity) {
                LivingEntity immortalEntity = ImmortalityStatus.getTargetGiftedImmortalLivingEntity(playerEntity);
                if (immortalEntity instanceof WolfEntity wolfEntity) {
                    if (ImmortalityStatus.getSummonedTeleport(playerEntity) && wolfEntity.isTeammate(playerEntity)) {
                        if (wolfEntity.getWorld() != playerEntity.getWorld() || wolfEntity.distanceTo(playerEntity) > 10) {
                            wolfEntity.fallDistance = 0;
                            FabricDimensions.teleport(wolfEntity, (ServerWorld) playerEntity.getWorld(), new TeleportTarget(playerEntity.getPos(), Vec3d.ZERO, wolfEntity.getYaw(), wolfEntity.getPitch()));
                            ((ServerWorld) wolfEntity.getWorld()).spawnParticles(ParticleTypes.SOUL, wolfEntity.getX(), wolfEntity.getY(), wolfEntity.getZ(), 64, 0, 5, 0, 1);
                        }
                        wolfEntity.setAngryAt(dmgSource.getSource().getUuid());
                        wolfEntity.chooseRandomAngerTime();
                    }
                }
            }
            if ((ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.getLiverImmortality(playerEntity) || ImmortalityStatus.isSemiImmortal(playerEntity) || ImmortalityStatus.isTrueImmortal(playerEntity))
                    &&
                    ((livingEntity.getHealth() - damageAmount) <= 0 ||
                            (dmgSource != null && dmgSource.getSource() instanceof WitherSkullEntity witherSkullEntity &&
                                    witherSkullEntity.getOwner() instanceof ImmortalWither &&
                                    (livingEntity.getHealth() - ((damageAmount) * ((playerEntity.getWorld().getDifficulty() == Difficulty.HARD || playerEntity.getWorld().getDifficulty() == Difficulty.NORMAL) ? witherDamageMultiplier : witherDamageMultiplierEasy))) <= 0))) {
                // This is Server, Player is Immortal and would've Died
                if (playerEntity.getY() <= playerEntity.world.getBottomY() && dmgSource == DamageSource.OUT_OF_WORLD) {
                    //If in Void taking damage then Teleport to Spawnpoint/Bed of Player, When no Bed is found then yeet them to Overworld Spawn
                    FabricDimensions.teleport(playerEntity
                            , Objects.requireNonNull(playerEntity.world.getServer()).getWorld(((ServerPlayerEntity) playerEntity).getSpawnPointDimension())
                            , new TeleportTarget(
                                    Vec3d.of((((ServerPlayerEntity) playerEntity).getSpawnPointPosition()) == null ?
                                            Objects.requireNonNull(playerEntity.getWorld().getServer()).getOverworld().getSpawnPos() :
                                            (((ServerPlayerEntity) playerEntity).getSpawnPointPosition()))
                                    , Vec3d.ZERO, playerEntity.getHeadYaw(), playerEntity.getPitch()
                            ));
                    playerEntity.fallDistance = 0;
                } else if (dmgSource != DamageSource.OUT_OF_WORLD) {
                    playerEntity.getWorld().playSoundFromEntity(null, playerEntity, SoundEvents.BLOCK_AMETHYST_CLUSTER_FALL, SoundCategory.PLAYERS, 5, 1);
                    ((ServerWorld) playerEntity.getWorld()).spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), 64, 0, 5, 0, 1);
                    //Extinguish and refill Air
                    playerEntity.setAir(playerEntity.getMaxAir());
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20, 0, false, false));
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1, false, false));
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 15 * 20, 2, true, true));
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
                    if (dmgSource.getSource() instanceof ImmortalWither || (dmgSource.getSource() instanceof WitherSkullEntity witherSkullEntity && witherSkullEntity.getOwner() instanceof ImmortalWither)) {
                        killedByImmortalWither = true;
                    }
                    if ((ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.isTrueImmortal(playerEntity)) && !ImmortalityStatus.isSemiImmortal(playerEntity)) {
                        //If real Immortality not LiverImmortality then use Leveling Mechanic
                        if (dmgSource.getSource() != null && dmgSource.getSource() != playerEntity) {
                            if (dmgSource.getSource().isPlayer()) {
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
                            } else if (killedByImmortalWither) {
                                ImmortalityStatus.setKilledByBaneOfLifeTime(playerEntity, ImmortalityStatus.getCurrentTime(playerEntity));
                                ImmortalityStatus.incrementKilledByBaneOfLifeCount(playerEntity);
                                if (ImmortalityStatus.getKilledByBaneOfLifeCount(playerEntity) >= 3) {
                                    if (!ImmortalityStatus.isSemiImmortal(playerEntity)) {
                                        ImmortalityStatus.setSemiImmortality(playerEntity, true);
                                    }
                                }
                                playerEntity.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.bane_of_life, 60 * 20, 0, true, true));
                            }
                        }
                        if (dmgSource != soulBoundDamageSource && (ImmortalityData.getImmortalDeaths(ImmortalityStatus.getPlayerComponent(playerEntity)) + 1) % 5 == 0 && ImmortalityStatus.getBonusArmor(playerEntity) < ImmortalityStatus.immortalityBaseArmor * 10) {
                            ImmortalityStatus.addImmortalityArmor(playerEntity);
                            playerEntity.sendMessage(Text.translatable("immortality.status.skinHardened"), true);
                        }
                    } else if (ImmortalityStatus.getLiverImmortality(playerEntity) || ImmortalityStatus.isSemiImmortal(playerEntity)) {
                        //If LiverImmortality then use Degrading Mechanic
                        //Remove 1 Heart per Death
                        if (dmgSource.getSource() != null && dmgSource.getSource() != playerEntity) {
                            if (dmgSource.getSource().isPlayer()) {
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
                            } else if (killedByImmortalWither) {
                                ImmortalityStatus.setKilledByBaneOfLifeTime(playerEntity, ImmortalityStatus.getCurrentTime(playerEntity));
                                ImmortalityStatus.incrementKilledByBaneOfLifeCount(playerEntity);
                                ImmortalityStatus.addNegativeHearts(playerEntity);
                                if (ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.getImmortality(playerEntity)) {
                                    playerEntity.addStatusEffect(new StatusEffectInstance(ModEffectRegistry.bane_of_life, 60 * 20, 0, true, true));
                                }
                            }
                        }
                        if (ImmortalityStatus.isSemiImmortal(playerEntity)) {
                            ImmortalityStatus.setSemiImmortalityLostHeartTime(playerEntity, ImmortalityStatus.getCurrentTime(playerEntity));
                        }
                        ImmortalityStatus.addNegativeHearts(playerEntity);
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
                            playerEntity.setHealth(1); //Guaranteed Death, Just in Case
                            return damageAmount + 1;
                        }
                    }
                    //Catching True Immortality in every Case, and also temporary Semi Immortality
                    ImmortalityAdvancementGiver.giveImmortalityAchievements(playerEntity);
                }
                //Prevent Death
                playerEntity.setHealth(playerEntity.getMaxHealth());
                return 0;
            } else if (dmgSource != null && dmgSource.getSource() instanceof WitherSkullEntity witherSkullEntity && witherSkullEntity.getOwner() instanceof ImmortalWither) {
                return ((damageAmount) * ((playerEntity.getWorld().getDifficulty() == Difficulty.HARD || playerEntity.getWorld().getDifficulty() == Difficulty.NORMAL) ? witherDamageMultiplier : witherDamageMultiplierEasy));
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
                        if (!ImmortalityStatus.hasTargetGiftedImmortal(giverImmortal) && ImmortalityStatus.getTargetGiftedImmortalLivingEntity(giverImmortal) != livingEntity) { //Prevent Abuse of Multiple SoulBounds
                            ImmortalityStatus.setTargetGiftedImmortal(giverImmortal, livingEntity.getUuid());
                            giverImmortal.sendMessage(Text.translatable("immortality.status.soulBond_restored"), true);
                        }
                        if (ImmortalityStatus.getTargetGiftedImmortalLivingEntity(giverImmortal) == livingEntity) {
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
                                    livingEntity.fallDistance = 0;
                                    FabricDimensions.teleport(livingEntity, (ServerWorld) giverImmortal.getWorld(), new TeleportTarget(giverImmortal.getPos(), Vec3d.ZERO, livingEntity.getHeadYaw(), livingEntity.getPitch()));
                                    ((ServerWorld) livingEntity.getWorld()).spawnParticles(ParticleTypes.SOUL, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 64, 0, 5, 0, 1);
                                } else if (dmgSource != DamageSource.OUT_OF_WORLD) {
                                    if (livingEntity.isOnFire()) {
                                        livingEntity.extinguish();
                                    }
                                    giverImmortal.setHealth(1);
                                    giverImmortal.damage(soulBoundDamageSource, 1000);
                                    livingEntity.getWorld().playSoundFromEntity(null, livingEntity, SoundEvents.BLOCK_AMETHYST_CLUSTER_FALL, SoundCategory.NEUTRAL, 5, 1);
                                    ((ServerWorld) livingEntity.getWorld()).spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 64, 0, 5, 0, 1);
                                    livingEntity.setAir(livingEntity.getMaxAir());
                                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20, 0, false, false));
                                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1, false, false));
                                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 15 * 20, 2, true, true));
                                }
                                livingEntity.setHealth(livingEntity.getMaxHealth());
                                return 0;
                            }
                        }
                        return damageAmount;
                    }
                } else if (livingEntity instanceof ImmortalWither immortalWither) {
                    //If Bane Of Life, it will count as 2 Kills
                    if (dmgSource.getSource() != null && dmgSource.getSource() != livingEntity && dmgSource.getSource().isPlayer()) {
                        PlayerEntity attackingPlayer = (PlayerEntity) dmgSource.getSource();
                        if (attackingPlayer.getMainHandStack().hasEnchantments()) {
                            if (EnchantmentHelper.getLevel(ImmortalityEnchants.Bane_Of_Life, attackingPlayer.getMainHandStack()) > 0) {
                                ImmortalityStatus.incrementImmortalWitherDeaths(immortalWither);
                                //Will give Liver on every Death
                                attackingPlayer.giveItemStack(new ItemStack(ImmortalityItems.LiverOfImmortality));
                            }
                        }
                    }
                    ImmortalityStatus.incrementImmortalWitherDeaths(immortalWither);
                    if (ImmortalityStatus.getImmortalWitherDeaths(immortalWither) < 5) { // Kill it 5 Times, and it won't be able to recover
                        immortalWither.setHealth(immortalWither.getMaxHealth() * (1 - ((1.0F * ImmortalityStatus.getImmortalWitherDeaths(immortalWither)) / 5)));
                        immortalWither.getWorld().playSoundFromEntity(null, immortalWither, SoundEvents.BLOCK_AMETHYST_CLUSTER_FALL, SoundCategory.HOSTILE, 5, 1);
                        ((ServerWorld) immortalWither.getWorld()).spawnParticles(ParticleTypes.SOUL, immortalWither.getX(), immortalWither.getY(), immortalWither.getZ(), 64, 0, 5, 0, 1);
                        immortalWither.setInvulTimer(220);
                        return 0;
                    } else if (dmgSource != DamageSource.OUT_OF_WORLD) {
                        return damageAmount;
                    } else {
                        return 0;
                    }
                }
            }
        }
        return damageAmount;
    }
}
