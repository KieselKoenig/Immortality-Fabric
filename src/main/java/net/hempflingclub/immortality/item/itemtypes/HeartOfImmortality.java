package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.util.ImmortalityStatus;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class HeartOfImmortality extends Item {
    public HeartOfImmortality(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity player) {
        PlayerEntity playerEntity = (PlayerEntity) player;
        if (!world.isClient()) {
            //Server
            if (ImmortalityStatus.getLiverImmortality(playerEntity)) {
                ImmortalityStatus.removeFalseImmortality(playerEntity);
            }
            if (!ImmortalityStatus.getImmortality(playerEntity)) {
                ImmortalityStatus.setImmortality(playerEntity, true);
            }
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0, false, false));
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 50, 0, false, false));
        } else {
            //Client
            if (!ImmortalityStatus.getImmortality(playerEntity)) {
                MinecraftClient.getInstance().gameRenderer.showFloatingItem(new ItemStack(ImmortalityItems.HeartOfImmortality));
            }
        }
        return super.finishUsing(stack, world, playerEntity);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("immortality.tooltip.item.heart_of_immortality_1").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.heart_of_immortality_2").formatted(Formatting.DARK_PURPLE));
    }
}
