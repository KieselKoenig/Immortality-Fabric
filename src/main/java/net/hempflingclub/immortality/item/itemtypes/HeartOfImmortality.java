package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.item.UsableItems;
import net.hempflingclub.immortality.util.IPlayerDataSaver;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class HeartOfImmortality extends Item {
    public HeartOfImmortality(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity player) {
        if (!world.isClient()) {
            //Server
            ImmortalityData.setImmortality(((IPlayerDataSaver) player), !ImmortalityData.getImmortality((IPlayerDataSaver) player));
            boolean status = ImmortalityData.getImmortality((IPlayerDataSaver) player);
            if (status) {
                player.sendMessage(Text.literal("You have achieved Immortality."));
                world.playSoundFromEntity(null, player, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1, 1);
                player.setHealth(player.getMaxHealth());
            } else {
                player.sendMessage(Text.literal("You have given up your Immortality."));
                world.playSoundFromEntity(null, player, SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.PLAYERS, 1, 1);
                player.setHealth(1);
                player.damage(DamageSource.OUT_OF_WORLD, 2000000000);
            }
            player.sendMessage(Text.literal("You have eaten heart" + Math.floor(Math.random() * 10)));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 50));
        } else {
            //Client
            if (ImmortalityData.getImmortality((IPlayerDataSaver) player)) {
                MinecraftClient.getInstance().gameRenderer.showFloatingItem(new ItemStack(UsableItems.HeartOfImmortality));
            }
        }
        return super.finishUsing(stack, world, player);
    }
}
