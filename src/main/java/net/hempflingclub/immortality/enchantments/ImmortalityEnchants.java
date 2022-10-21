package net.hempflingclub.immortality.enchantments;

import net.hempflingclub.immortality.Immortality;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class ImmortalityEnchants {
    private final static EquipmentSlot[] equipmentSlotListMainHand = {EquipmentSlot.MAINHAND};
    public static Enchantment Bane_Of_Life = new BaneOfLife(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.WEAPON, equipmentSlotListMainHand);

    public static void registerEnchantments() {
        Immortality.LOGGER.debug("Registering Enchantments for " + Immortality.MOD_ID);
        Registry.register(Registry.ENCHANTMENT, new Identifier(Immortality.MOD_ID, "bane_of_life"), Bane_Of_Life);
    }
}
