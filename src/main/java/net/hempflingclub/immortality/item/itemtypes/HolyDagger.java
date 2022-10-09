package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.util.IPlayerDataSaver;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class HolyDagger extends Item {
    public HolyDagger(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            if (ImmortalityData.getImmortality((IPlayerDataSaver) user)) {
                if (ImmortalityData.getLiverExtracted((IPlayerDataSaver) user)) {
                    ImmortalityData.setHeartExtractionAmount((IPlayerDataSaver) user, ImmortalityData.getHeartExtractionAmount((IPlayerDataSaver) user) + 1);
                    if (ImmortalityData.getHeartExtractionAmount((IPlayerDataSaver) user) < 3) {
                        user.sendMessage(Text.translatable("immortality.status.heart_extraction", 3 - ImmortalityData.getHeartExtractionAmount((IPlayerDataSaver) user)), true);
                    } else {
                        world.playSoundFromEntity(null, user, SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.PLAYERS, 1, 1);
                        user.setHealth(1);
                        //Remove Immortality Hearts
                        EntityAttributeInstance maxHealth = user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                        ImmortalityData.setImmortality((IPlayerDataSaver) user, false);
                        ImmortalityData.setVoidHeart((IPlayerDataSaver) user, false);
                        assert maxHealth != null;
                        for (EntityAttributeModifier entityModifier : maxHealth.getModifiers()) {
                            if (entityModifier.getName().equals("immortalityHearts") || entityModifier.getName().equals("immortalityAbsorbedLiver")) {
                                maxHealth.removeModifier(entityModifier);
                            }
                        }
                        user.damage(new DamageSource(Text.translatable("immortality", user.getName()).getString()).setBypassesArmor().setBypassesProtection().setUnblockable(),
                                2000000000);
                        ImmortalityData.setImmortalDeaths((IPlayerDataSaver) user, 0);
                    }
                } else {
                    if (!ImmortalityData.getLiverOnceExtracted((IPlayerDataSaver) user)) {
                        ImmortalityData.setLiverOnceExtracted((IPlayerDataSaver) user, true);
                    }
                    ImmortalityData.setLiverExtracted((IPlayerDataSaver) user, true);
                    ImmortalityData.setLiverExtractionTime((IPlayerDataSaver) user, (int) Objects.requireNonNull(world.getServer()).getOverworld().getTime());
                    EntityAttributeInstance maxHealth = user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                    EntityAttributeModifier healthSubtraction = new EntityAttributeModifier("regrowingImmortalityLiver", -10, EntityAttributeModifier.Operation.ADDITION);
                    assert maxHealth != null;
                    maxHealth.addPersistentModifier(healthSubtraction);
                    user.setHealth(user.getMaxHealth());
                    user.giveItemStack(new ItemStack(ImmortalityItems.LiverOfImmortality));
                    itemStack.damage(1, user, e -> e.sendToolBreakStatus(user.getActiveHand()));
                    user.tick();
                    user.sendMessage(Text.translatable("immortality.status.liver_removed"), true);
                    if (itemStack.getDamage() >= itemStack.getMaxDamage()) {
                        itemStack.decrement(1);
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
    }
}
