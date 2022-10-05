package net.hempflingclub.immortality.item.itemtypes;

import net.hempflingclub.immortality.item.UsableItems;
import net.hempflingclub.immortality.util.IPlayerDataSaver;
import net.hempflingclub.immortality.util.ImmortalityData;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Objects;

public class HolyDagger extends Item {
    public HolyDagger(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        playerEntity.getItemCooldownManager().set(itemStack.getItem(), 20 * 300);
        if (!world.isClient) {
            if (ImmortalityData.getImmortality((IPlayerDataSaver) playerEntity)) {
                if (ImmortalityData.getLiverExtracted((IPlayerDataSaver) playerEntity)) {
                    playerEntity.sendMessage(Text.literal("Your Liver has yet to regrow"), true);
                } else {
                    ImmortalityData.setLiverExtracted((IPlayerDataSaver) playerEntity, true);
                    ImmortalityData.setLiverExtractionTime((IPlayerDataSaver) playerEntity, (int) Objects.requireNonNull(world.getServer()).getOverworld().getTime());
                    EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                    EntityAttributeModifier healthSubtraction = new EntityAttributeModifier("regrowingImmortalityLiver", -10, EntityAttributeModifier.Operation.ADDITION);
                    assert maxHealth != null;
                    maxHealth.addPersistentModifier(healthSubtraction);
                    playerEntity.setHealth(playerEntity.getMaxHealth());
                    playerEntity.giveItemStack(new ItemStack(UsableItems.LiverOfImmortality));
                    itemStack.damage(1, playerEntity, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                    playerEntity.tick();
                    playerEntity.sendMessage(Text.literal("You have removed your Liver"), true);
                    if (itemStack.getDamage() >= itemStack.getMaxDamage()) {
                        itemStack.decrement(1);
                    }
                }
            }
        }
        return TypedActionResult.consume(itemStack);
    }
}
