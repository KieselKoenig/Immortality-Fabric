package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.item.UsableItems;
import net.hempflingclub.immortality.util.IPlayerDataSaver;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.minecraft.client.MinecraftClient;
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
import net.minecraft.world.World;

public class LiverOfImmortality extends Item {
    public LiverOfImmortality(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity player) {
        if (!world.isClient() && !ImmortalityData.getImmortality((IPlayerDataSaver) player)) {
            //Server
            ImmortalityData.setLiverImmortality(((IPlayerDataSaver) player), !ImmortalityData.getLiverImmortality((IPlayerDataSaver) player));
            boolean status = ImmortalityData.getLiverImmortality((IPlayerDataSaver) player);
            if (status) {
                ((PlayerEntity) player).sendMessage(Text.literal("You have achieved Immortality by eating an Immortal Liver."), true);
                world.playSoundFromEntity(null, player, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1, 1);
                player.setHealth(player.getMaxHealth());
            } else {
                world.playSoundFromEntity(null, player, SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.PLAYERS, 1, 1);
                EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                assert maxHealth != null;
                for (EntityAttributeModifier entityModifier : maxHealth.getModifiers()) {
                    if (entityModifier.getName().equals("immortalityHearts")) {
                        maxHealth.removeModifier(entityModifier);
                    }
                }
                player.setHealth(1);
                player.damage(new DamageSource(Text.translatable("immortality", player.getName()).getString()).setBypassesArmor().setBypassesProtection().setUnblockable(),
                        2000000000);
                ImmortalityData.setImmortalDeaths((IPlayerDataSaver) player, 0);
            }
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 50, 0, false, false));
        } else {
            //Client
            MinecraftClient.getInstance().gameRenderer.showFloatingItem(new ItemStack(UsableItems.LiverOfImmortality));
        }
        return super.finishUsing(stack, world, player);
    }
}
