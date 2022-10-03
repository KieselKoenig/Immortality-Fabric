package net.hempflingclub.immortality.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.hempflingclub.immortality.Immortality;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class UsableItems {
    public static final Item HeartOfImmortality = registerItem("heart_of_immortality", new Item(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).food(new FoodComponent.Builder().hunger(0).saturationModifier(0).alwaysEdible().meat().build())));
    public static final Item VoidHeart = registerItem("void_heart", new Item(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).food(new FoodComponent.Builder().hunger(0).saturationModifier(0).alwaysEdible().meat().build())));
    private static Item registerItem(String name, Item item){
        return Registry.register(Registry.ITEM, new Identifier(Immortality.MOD_ID, name), item);
    }
    public static void registerModItems(){
        Immortality.LOGGER.debug("Registering Useable Items for "+ Immortality.MOD_ID);
    }
}
