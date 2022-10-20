package net.hempflingclub.immortality.mixin;

import net.hempflingclub.immortality.item.ImmortalityItems;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.recipe.BrewingRecipeRegistry.registerPotionRecipe;

@Mixin(BrewingRecipeRegistry.class)
public final class BrewRecipeMixin {

    @Inject(method = "registerDefaults", at = @At("HEAD"))
    private static void overrideDefaultRecipes(CallbackInfo cb) {
        registerPotionRecipe(Potions.AWKWARD, ImmortalityItems.LiverOfImmortality, ImmortalityItems.LifeElixirPotion);
    }
}