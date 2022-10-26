package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.entitys.ImmortalWither.ImmortalWither;
import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.util.ImmortalityAdvancementGiver;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.hempflingclub.immortality.util.ImmortalityStatus;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class HolyDagger extends Item {
    public HolyDagger(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity playerEntity, LivingEntity entity, Hand hand) {
        if (playerEntity.getMainHandStack() == stack && playerEntity.isSneaking() && playerEntity.isInRange(entity, 2) && (ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.isTrueImmortal(playerEntity) || ImmortalityStatus.isSemiImmortal(playerEntity) || ImmortalityStatus.getLiverImmortality(playerEntity))) {
            if (!entity.isPlayer()) {
                if (entity.hasCustomName()) {
                    if (!ImmortalityStatus.hasTargetGiftedImmortal(playerEntity)) {
                        if (!ImmortalityStatus.hasTargetGiverImmortal(entity) && !(entity instanceof ImmortalWither)) {
                            ImmortalityStatus.setTargetGiftedImmortal(playerEntity, entity.getUuid());
                            ImmortalityStatus.setTargetGiverImmortal(entity, playerEntity.getUuid());
                            if (!playerEntity.getWorld().isClient()) {
                                playerEntity.getWorld().playSoundFromEntity(null, playerEntity, SoundEvents.ENTITY_GHAST_SCREAM, SoundCategory.NEUTRAL, 5, 1);
                                ((ServerWorld) playerEntity.getWorld()).spawnParticles(ParticleTypes.SOUL, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), 64, 0, 5, 0, 1);
                                entity.damage(new DamageSource("immortality.soulBound").setBypassesArmor().setBypassesProtection().setUnblockable(), 1000);
                                ImmortalityAdvancementGiver.giveSoulBoundAchievement(playerEntity);
                                playerEntity.sendMessage(Text.translatable("immortality.status.hasSoulBound", Objects.requireNonNull(entity.getCustomName()).getString()), true);
                                if (!ImmortalityStatus.isTrueImmortal(playerEntity)) {
                                    stack.damage(1, playerEntity, e -> e.sendToolBreakStatus(playerEntity.getActiveHand()));
                                }
                                if (stack.getDamage() >= stack.getMaxDamage()) {
                                    stack.decrement(1);
                                }
                            }
                            return ActionResult.CONSUME;
                        } else {
                            playerEntity.sendMessage(Text.translatable("immortality.status.cannotSoulBound_alreadySoulBound"), true);
                        }
                    } else {
                        playerEntity.sendMessage(Text.translatable("immortality.status.cannotSoulBound"), true);
                    }
                } else {
                    playerEntity.sendMessage(Text.translatable("immortality.status.needsCustomName"), true);
                }
            }
        }
        return ActionResult.FAIL;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        if (!world.isClient) {
            if (!playerEntity.isSneaking()) {
                if (ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.isSemiImmortal(playerEntity)) {
                    if (ImmortalityData.getLiverExtracted(ImmortalityStatus.getPlayerComponent(playerEntity)) || ImmortalityStatus.isSemiImmortal(playerEntity)) {
                        ImmortalityData.setHeartExtractionAmount(ImmortalityStatus.getPlayerComponent(playerEntity), ImmortalityData.getHeartExtractionAmount(ImmortalityStatus.getPlayerComponent(playerEntity)) + 1);
                        if (ImmortalityData.getHeartExtractionAmount(ImmortalityStatus.getPlayerComponent(playerEntity)) < 7) {
                            playerEntity.sendMessage(Text.translatable("immortality.status.heart_extraction", 7 - ImmortalityData.getHeartExtractionAmount(ImmortalityStatus.getPlayerComponent(playerEntity))), true);
                        } else {
                            world.playSoundFromEntity(null, playerEntity, SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.PLAYERS, 1, 1);
                            playerEntity.setHealth(1);
                            for (PlayerEntity players : playerEntity.getWorld().getPlayers()) {
                                players.playSound(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 1, 1);
                            }
                            //Remove Immortality Hearts
                            ImmortalityStatus.removeEverything(playerEntity);
                            playerEntity.damage(new DamageSource(Text.translatable("immortality", playerEntity.getName()).getString()).setBypassesArmor().setBypassesProtection().setUnblockable(), 2000000000);
                        }
                    } else {
                        if (!ImmortalityData.getLiverOnceExtracted(ImmortalityStatus.getPlayerComponent(playerEntity))) {
                            ImmortalityData.setLiverOnceExtracted(ImmortalityStatus.getPlayerComponent(playerEntity), true);
                        }
                        ImmortalityData.setLiverExtracted(ImmortalityStatus.getPlayerComponent(playerEntity), true);
                        ImmortalityData.setLiverExtractionTime(ImmortalityStatus.getPlayerComponent(playerEntity), ImmortalityStatus.getCurrentTime(playerEntity));
                        ImmortalityStatus.addRegrowingLiver(playerEntity);
                        playerEntity.giveItemStack(new ItemStack(ImmortalityItems.LiverOfImmortality));
                        //If Trilogy
                        if (!ImmortalityStatus.isTrueImmortal(playerEntity)) {
                            itemStack.damage(1, playerEntity, e -> e.sendToolBreakStatus(playerEntity.getActiveHand()));
                        }
                        playerEntity.tick();
                        playerEntity.sendMessage(Text.translatable("immortality.status.liver_removed"), true);
                        if (itemStack.getDamage() >= itemStack.getMaxDamage()) {
                            itemStack.decrement(1);
                        }
                        ImmortalityAdvancementGiver.giveHolyDaggerAchievement(playerEntity);
                        ImmortalityAdvancementGiver.giveImmortalityAchievements(playerEntity);
                    }
                }
            }
        }
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("immortality.tooltip.item.holy_dagger_1").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.holy_dagger_2").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.holy_dagger_3").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.holy_dagger_4").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.holy_dagger_5").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.holy_dagger_6").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.holy_dagger_7").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.holy_dagger_8").formatted(Formatting.LIGHT_PURPLE));
    }
}
