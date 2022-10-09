package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.util.IPlayerDataSaver;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class LiverOfImmortality extends Item {
    public LiverOfImmortality(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity player) {
        if (!world.isClient() && !ImmortalityData.getImmortality((IPlayerDataSaver) player)) {
            //Server
            boolean status = ImmortalityData.getLiverImmortality((IPlayerDataSaver) player);
            ImmortalityData.setLiverImmortality(((IPlayerDataSaver) player), true);
            if (status) {
                EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                assert maxHealth != null;
                for (EntityAttributeModifier entityModifier : maxHealth.getModifiers()) {
                    if (entityModifier.getName().equals("immortalityHearts") || entityModifier.getName().equals("negativeImmortalityHearts")) {
                        maxHealth.removeModifier(entityModifier);
                    }
                }
                ImmortalityData.setImmortalDeaths((IPlayerDataSaver) player, 0);
            } else {
                ((PlayerEntity) player).sendMessage(Text.translatable("immortality.achieve.liver_of_immortality"), true);
                world.playSoundFromEntity(null, player, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1, 1);
                player.setHealth(player.getMaxHealth());
            }
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 50, 0, false, false));
        } else if (!world.isClient() &&
                ImmortalityData.getImmortalDeaths((IPlayerDataSaver) player) >= 30 &&
                ImmortalityData.getLiverOnceExtracted((IPlayerDataSaver) player) &&
                ImmortalityData.getVoidHeart((IPlayerDataSaver) player) &&
                ImmortalityData.getImmortality((IPlayerDataSaver) player)) {
            //User has Trilogy
            player.getWorld().playSoundFromEntity(null, player, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1, 1);
            if (ImmortalityData.getLiverExtracted((IPlayerDataSaver) player) && Objects.requireNonNull(player.getServer()).getOverworld().getTime() >= ImmortalityData.getLiverExtractionTime((IPlayerDataSaver) player) + (20 * 300)) {
                //Remove Debuff
                ImmortalityData.setLiverExtractionTime((IPlayerDataSaver) player, 0);
                ((PlayerEntity) player).sendMessage(Text.translatable("immortality.status.liver_regrown"), true);
            } else {
                //Give Buff
                ((PlayerEntity) player).sendMessage(Text.translatable("immortality.status.liver_absorbed"), true);
                EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                EntityAttributeModifier healthAddition = new EntityAttributeModifier("immortalityAbsorbedLiver", 10, EntityAttributeModifier.Operation.ADDITION);
                assert maxHealth != null;
                maxHealth.addPersistentModifier(healthAddition);
                player.setHealth(player.getMaxHealth());
            }
        } else {
            //Client
            MinecraftClient.getInstance().gameRenderer.showFloatingItem(new ItemStack(ImmortalityItems.LiverOfImmortality));
        }
        return super.finishUsing(stack, world, player);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("immortality.tooltip.item.liver_of_immortality_1").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.liver_of_immortality_2").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.liver_of_immortality_3").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.liver_of_immortality_4").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.liver_of_immortality_5").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.liver_of_immortality_6").formatted(Formatting.DARK_PURPLE));
    }
}
