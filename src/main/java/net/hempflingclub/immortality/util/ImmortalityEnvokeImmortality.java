package net.hempflingclub.immortality.util;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.hempflingclub.immortality.item.UsableItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

import java.util.Objects;

public class ImmortalityEnvokeImmortality {
    public static float damageManager(LivingEntity entity, DamageSource dmgSource, float damageAmount) {
        if (!entity.world.isClient
                && ImmortalityData.getImmortality((IPlayerDataSaver) entity)
                && (entity.getHealth() - damageAmount) <= 0
                && entity.isPlayer()) {
            // This is Server, Player is Immortal and would've Died
            PlayerEntity playerEntity = (PlayerEntity) entity;
            playerEntity.playSound(SoundEvents.BLOCK_AMETHYST_CLUSTER_FALL, SoundCategory.PLAYERS, 1, 1);
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
                //Increase Immortals Death Counter, if DamageType is not Void Damage
                ImmortalityData.setImmortalDeaths((IPlayerDataSaver) playerEntity, ImmortalityData.getImmortalDeaths((IPlayerDataSaver) playerEntity) + 1);
                //Increase Death Counter in Statistics
                playerEntity.incrementStat(Stats.DEATHS);
//                if(ImmortalityData.getImmortalDeaths((IPlayerDataSaver) playerEntity) % 5 == 0){
                EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                assert maxHealth != null;
                //Add 1 Heart per Immortal Death
                maxHealth.addPersistentModifier(new EntityAttributeModifier("immortalityHearts", 2, EntityAttributeModifier.Operation.ADDITION));
//                }
                if (ImmortalityData.getImmortalDeaths((IPlayerDataSaver) playerEntity) % 25 == 0) {
                    playerEntity.giveItemStack(new ItemStack(UsableItems.VoidHeart));
                    playerEntity.sendMessage(Text.literal("You have trained a VoidHeart"), true);
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
