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
import net.minecraft.entity.damage.DamageSource;
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

public class HeartOfImmortality extends Item {
    public HeartOfImmortality(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity player) {
        if (!world.isClient() && !ImmortalityData.getLiverImmortality((IPlayerDataSaver) player)) {
            //Server
            ImmortalityData.setImmortality(((IPlayerDataSaver) player), !ImmortalityData.getImmortality((IPlayerDataSaver) player));
            boolean status = ImmortalityData.getImmortality((IPlayerDataSaver) player);
            if (status) {
                ((PlayerEntity) player).sendMessage(Text.translatable("immortality.achieve.heart_of_immortality"), true);
                world.playSoundFromEntity(null, player, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1, 1);
                player.setHealth(player.getMaxHealth());
            } else {
                world.playSoundFromEntity(null, player, SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.PLAYERS, 1, 1);
                player.setHealth(1);
                //Remove Immortality Hearts
                EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                assert maxHealth != null;
                for (EntityAttributeModifier entityModifier : maxHealth.getModifiers()) {
                    if (entityModifier.getName().equals("immortalityHearts") || entityModifier.getName().equals("immortalityAbsorbedLiver")) {
                        maxHealth.removeModifier(entityModifier);
                    }
                }

                player.damage(new DamageSource(Text.translatable("immortality", player.getName()).getString()).setBypassesArmor().setBypassesProtection().setUnblockable(),
                        2000000000);
                ImmortalityData.setImmortalDeaths((IPlayerDataSaver) player, 0);
            }
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 50, 0, false, false));
        } else {
            //Client
            MinecraftClient.getInstance().gameRenderer.showFloatingItem(new ItemStack(ImmortalityItems.HeartOfImmortality));
        }
        return super.finishUsing(stack, world, player);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("immortality.tooltip.item.heart_of_immortality_1").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("immortality.tooltip.item.heart_of_immortality_2").formatted(Formatting.DARK_PURPLE));
    }
}
