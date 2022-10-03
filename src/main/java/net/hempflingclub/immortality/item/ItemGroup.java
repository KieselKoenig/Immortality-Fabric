package net.hempflingclub.immortality.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
public class ItemGroup {
    public static final net.minecraft.item.ItemGroup Immortality = FabricItemGroupBuilder
            .build(
            new Identifier(net.hempflingclub.immortality.Immortality.MOD_ID,
                    "immortality"),
            () -> new ItemStack(UsableItems.HeartOfImmortality));
}
