package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.item.ImmortalityItems;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.hempflingclub.immortality.util.ImmortalityStatus;
import net.minecraft.client.item.TooltipContext;
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
            if (ImmortalityStatus.getImmortality(user)) {
                if (ImmortalityData.getLiverExtracted(ImmortalityStatus.getPlayerComponent(user))) {
                    ImmortalityData.setHeartExtractionAmount(ImmortalityStatus.getPlayerComponent(user), ImmortalityData.getHeartExtractionAmount(ImmortalityStatus.getPlayerComponent(user)) + 1);
                    if (ImmortalityData.getHeartExtractionAmount(ImmortalityStatus.getPlayerComponent(user)) < 3) {
                        user.sendMessage(Text.translatable("immortality.status.heart_extraction", 3 - ImmortalityData.getHeartExtractionAmount(ImmortalityStatus.getPlayerComponent(user))), true);
                    } else {
                        world.playSoundFromEntity(null, user, SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.PLAYERS, 1, 1);
                        user.setHealth(1);
                        //Remove Immortality Hearts
                        ImmortalityStatus.removeEverything(user);
                        user.damage(new DamageSource(Text.translatable("immortality", user.getName()).getString()).setBypassesArmor().setBypassesProtection().setUnblockable(), 2000000000);
                    }
                } else {
                    if (!ImmortalityData.getLiverOnceExtracted(ImmortalityStatus.getPlayerComponent(user))) {
                        ImmortalityData.setLiverOnceExtracted(ImmortalityStatus.getPlayerComponent(user), true);
                    }
                    ImmortalityData.setLiverExtracted(ImmortalityStatus.getPlayerComponent(user), true);
                    ImmortalityData.setLiverExtractionTime(ImmortalityStatus.getPlayerComponent(user), (int) Objects.requireNonNull(world.getServer()).getOverworld().getTime());
                    ImmortalityStatus.addRegrowingLiver(user);
                    user.setHealth(user.getMaxHealth());
                    user.giveItemStack(new ItemStack(ImmortalityItems.LiverOfImmortality));
                    //If Trilogy
                    if (!(ImmortalityData.getImmortalDeaths(ImmortalityStatus.getPlayerComponent(user)) >= 30 && ImmortalityData.getLiverOnceExtracted(ImmortalityStatus.getPlayerComponent(user)) && ImmortalityStatus.getVoidHeart(user) && ImmortalityStatus.getImmortality(user))) {
                        itemStack.damage(1, user, e -> e.sendToolBreakStatus(user.getActiveHand()));
                    }
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
