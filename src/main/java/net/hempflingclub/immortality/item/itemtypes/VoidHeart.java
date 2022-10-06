package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.util.IPlayerDataSaver;
import net.hempflingclub.immortality.util.ImmortalityData;
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

public class VoidHeart extends Item {
    public VoidHeart(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity player) {
        if (!world.isClient()) {
            //Server
            ImmortalityData.setVoidHeart(((IPlayerDataSaver) player), !ImmortalityData.getVoidHeart((IPlayerDataSaver) player));
            boolean status = ImmortalityData.getVoidHeart((IPlayerDataSaver) player);
            if (status) {
                ((PlayerEntity) player).sendMessage(Text.literal("You consume the Void."), true);
                world.playSoundFromEntity(null, player, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1, 1);
                player.setHealth(player.getMaxHealth());
            } else {
                ((PlayerEntity) player).sendMessage(Text.literal("You have forsaken the Void."), true);
                world.playSoundFromEntity(null, player, SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.PLAYERS, 1, 1);
                player.setHealth(1);
                ((PlayerEntity) player).getHungerManager().setFoodLevel(0);
                ((PlayerEntity) player).getHungerManager().setSaturationLevel(0);
                player.tick();
            }
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 50, 0, false, false));
        } else {
            //Client
            MinecraftClient.getInstance().gameRenderer.showFloatingItem(new ItemStack(ImmortalityItems.VoidHeart));
        }
        return super.finishUsing(stack, world, player);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("immortality.tooltip.item.void_heart_1").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.void_heart_2").formatted(Formatting.LIGHT_PURPLE));
    }
}