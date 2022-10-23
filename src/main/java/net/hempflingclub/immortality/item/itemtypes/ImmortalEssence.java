package net.hempflingclub.immortality.item.itemtypes;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class ImmortalEssence extends Item {

    public ImmortalEssence(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("immortality.tooltip.item.immortal_essence_1").formatted(Formatting.RED));
    }
}
