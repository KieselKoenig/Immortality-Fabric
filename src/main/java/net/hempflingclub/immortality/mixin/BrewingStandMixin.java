package net.hempflingclub.immortality.mixin;

import net.hempflingclub.immortality.item.ImmortalityItems;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingStandBlockEntity.class)
public final class BrewingStandMixin {
    static private final ItemStack toReplaceItemStack = new ItemStack(ImmortalityItems.LifeElixir);

    @Inject(method = "craft", at = @At(value = "TAIL"))
    private static void immortality$fixPotionSetter(World world, BlockPos pos, DefaultedList<ItemStack> slots, CallbackInfo ci) {
        if (!world.isClient()) {
            int idx = 0;
            for (ItemStack itemStack : slots) {
                if (itemStack.getTranslationKey().equals("item.minecraft.potion.effect.life_elixir_potion")) {
                    slots.set(idx, new ItemStack(Items.AIR));
                    ItemEntity itemEntity = new ItemEntity(EntityType.ITEM, world); // Create the lightning bolt
                    itemEntity.setPosition(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
                    itemEntity.setStack(toReplaceItemStack);// Set its position. This will make the lightning bolt strike the player (probably not what you want)
                    world.spawnEntity(itemEntity);
                }
                idx++;
            }
        }
    }

    //if(stack.getItem().equals(targetItemStack)) {

    //inventory.set(slot,new ItemStack(ImmortalityItems.LifeElixir));
    //}


}
