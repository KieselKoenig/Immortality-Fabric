package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.hempflingclub.immortality.util.ImmortalityStatus;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
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

public class LiverOfImmortality extends Item {
    public LiverOfImmortality(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity player) {
        PlayerEntity playerEntity = (PlayerEntity) player;
        if (!world.isClient() && !ImmortalityStatus.getImmortality(playerEntity)) {
            //Server
            boolean status = ImmortalityStatus.getLiverImmortality(playerEntity);
            ImmortalityStatus.setLiverImmortality(playerEntity, true);
            if (status) {
                if (playerEntity.isPlayer()) {
                    ImmortalityStatus.removeNegativeHearts(playerEntity);
                }
            } else {
                playerEntity.sendMessage(Text.translatable("immortality.achieve.liver_of_immortality"), true);
                world.playSoundFromEntity(null, playerEntity, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1, 1);
                playerEntity.setHealth(playerEntity.getMaxHealth());
            }
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0, false, false));
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 50, 0, false, false));
        } else if (ImmortalityStatus.getImmortality(playerEntity) && ImmortalityData.getLiverExtracted(ImmortalityStatus.getPlayerComponent(playerEntity)) && !world.isClient()) {
            //Remove Debuff
            ImmortalityData.setLiverExtractionTime(ImmortalityStatus.getPlayerComponent(playerEntity), 0);
            playerEntity.sendMessage(Text.translatable("immortality.status.liver_regrown"), true);

        } else if (!world.isClient() && ImmortalityData.getImmortalDeaths(ImmortalityStatus.getPlayerComponent(playerEntity)) >= 30 && ImmortalityData.getLiverOnceExtracted(ImmortalityStatus.getPlayerComponent(playerEntity)) && ImmortalityStatus.getVoidHeart(playerEntity) && ImmortalityStatus.getImmortality(playerEntity)) {
            //User has Trilogy
            playerEntity.getWorld().playSoundFromEntity(null, playerEntity, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1, 1);
            //Give Buff
            playerEntity.sendMessage(Text.translatable("immortality.status.liver_absorbed"), true);
            if (playerEntity.isPlayer()) {
                ImmortalityStatus.addImmortalityHearts(playerEntity);
                playerEntity.setHealth(playerEntity.getMaxHealth());
            }
        } else if (world.isClient()) {
            //Client
            if (!ImmortalityStatus.getLiverImmortality(playerEntity) && !ImmortalityStatus.getImmortality(playerEntity)) {
                MinecraftClient.getInstance().gameRenderer.showFloatingItem(new ItemStack(ImmortalityItems.LiverOfImmortality));
            }
        }
        return super.finishUsing(stack, world, playerEntity);
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
