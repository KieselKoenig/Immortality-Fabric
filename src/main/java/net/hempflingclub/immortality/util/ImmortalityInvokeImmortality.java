package net.hempflingclub.immortality.util;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.hempflingclub.immortality.item.ImmortalityItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

import java.util.Objects;

public class ImmortalityInvokeImmortality {
    public static float damageManager(LivingEntity entity, DamageSource dmgSource, float damageAmount) {
        if (!entity.world.isClient
                && (ImmortalityData.getImmortality((IPlayerDataSaver) entity) || ImmortalityData.getLiverImmortality((IPlayerDataSaver) entity))
                && (entity.getHealth() - damageAmount) <= 0
                && entity.isPlayer()) {
            // This is Server, Player is Immortal and would've Died
            PlayerEntity playerEntity = (PlayerEntity) entity;
            playerEntity.getWorld().playSoundFromEntity(null, playerEntity, SoundEvents.BLOCK_AMETHYST_CLUSTER_FALL, SoundCategory.PLAYERS, 5, 1);
            playerEntity.getWorld().addFireworkParticle(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), 0, 0, 0, new NbtCompound());
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
                //Extinguish and refill Air
                playerEntity.setAir(playerEntity.getMaxAir());
                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20, 0, false, false));
                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1, false, false));
                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 30 * 20, 2, false, false));
                //Increase Immortals Death Counter, if DamageType is not Void Damage
                ImmortalityStatus.incrementImmortalityDeath(playerEntity);
                //Increase Death Counter in Statistics
                playerEntity.incrementStat(Stats.DEATHS);
                if (ImmortalityData.getImmortality((IPlayerDataSaver) playerEntity)) {
                    //If real Immortality not LiverImmortality then use Leveling Mechanic
                    //Add 1 Heart per Immortal Death
                    ImmortalityStatus.addImmortalityArmor(playerEntity);
                    playerEntity.setHealth(playerEntity.getMaxHealth());
                    if (ImmortalityData.getImmortalDeaths((IPlayerDataSaver) playerEntity) == 25) {
                        playerEntity.giveItemStack(new ItemStack(ImmortalityItems.VoidHeart));
                        playerEntity.sendMessage(Text.literal("You have trained a VoidHeart"), true);
                    }
                } else if (ImmortalityData.getLiverImmortality((IPlayerDataSaver) playerEntity)) {
                    //If LiverImmortality then use Degrading Mechanic
                    //Remove 1 Heart per Death
                    if (playerEntity.getMaxHealth() <= 2) {
                        //0 Hearts then remove LiverImmortality
                        ImmortalityStatus.removeFalseImmortality(playerEntity);
                        playerEntity.setHealth(playerEntity.getMaxHealth());
                        if (dmgSource.getAttacker() != null || dmgSource.getAttacker() == playerEntity) {
                            playerEntity.damage(new DamageSource(Text.translatable("immortality.last.death.player", playerEntity.getName(), Objects.requireNonNull(playerEntity.getAttacker()).getName()).getString()).setBypassesArmor().setBypassesProtection().setUnblockable(),
                                    2000000000);
                        } else {
                            playerEntity.damage(new DamageSource(Text.translatable("immortality.last.death", playerEntity.getName().getString()).getString()).setBypassesArmor().setBypassesProtection().setUnblockable(),
                                    2000000000);
                        }
                        return 0;
                    } else {
                        ImmortalityStatus.addNegativeHearts(playerEntity);
                        playerEntity.setHealth(playerEntity.getMaxHealth());
                    }


                }
            }
            //Prevent Death
            playerEntity.setHealth(playerEntity.getMaxHealth());
            return 0;
        }
        //Can Survive Damage or ain't immortal
        return damageAmount;
    }
}
