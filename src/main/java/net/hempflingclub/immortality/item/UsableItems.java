package net.hempflingclub.immortality.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.hempflingclub.immortality.Immortality;
import net.hempflingclub.immortality.item.itemtypes.HeartOfImmortality;
import net.hempflingclub.immortality.item.itemtypes.VoidHeart;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class UsableItems {
    public static final Item HeartOfImmortality = registerItem("heart_of_immortality",
            new HeartOfImmortality(
                    new FabricItemSettings()
                            .group(net.hempflingclub.immortality.item.ItemGroup.Immortality)
                            .maxCount(1)
                            .food(
                                    new FoodComponent.Builder()
                                            .hunger(0)
                                            .saturationModifier(0)
                                            .alwaysEdible()
                                            .meat()
                                            .build())));
    public static final Item VoidHeart = registerItem("void_heart",
            new VoidHeart(
                    new FabricItemSettings()
                            .group(net.hempflingclub.immortality.item.ItemGroup.Immortality)
                            .maxCount(1)
                            .food(
                                    new FoodComponent.Builder()
                                            .hunger(0)
                                            .saturationModifier(0)
                                            .alwaysEdible()
                                            .meat()
                                            .build())));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Immortality.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Immortality.LOGGER.debug("Registering Useable Items for " + Immortality.MOD_ID);
    }
}
