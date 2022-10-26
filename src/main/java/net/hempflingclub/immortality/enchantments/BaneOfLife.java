package net.hempflingclub.immortality.enchantments;

import net.hempflingclub.immortality.util.ImmortalityStatus;
import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class BaneOfLife extends DamageEnchantment {
    protected BaneOfLife(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type.ordinal(), slotTypes);
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return false;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public float getAttackDamage(int level, EntityGroup group) {
        if (group != EntityGroup.UNDEAD) {
            return Math.max(0, level * 3f);
        } else {
            return Math.max(0, level * 1.5f);
        }
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (target instanceof LivingEntity livingEntityTarget) {
            if (livingEntityTarget.isPlayer()) {
                PlayerEntity playerEntityTarget = (PlayerEntity) livingEntityTarget;
                if (!ImmortalityStatus.getImmortality(playerEntityTarget) && !ImmortalityStatus.isSemiImmortal(playerEntityTarget) && !ImmortalityStatus.getLiverImmortality(playerEntityTarget) && !ImmortalityStatus.isTrueImmortal(playerEntityTarget)) {
                    //No Immortality
                    playerEntityTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 15 * 20, level - 1, true, true));
                    livingEntityTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 15 * 20, level - 1, true, true));
                }
            } else if (livingEntityTarget.getGroup() != EntityGroup.UNDEAD) {
                livingEntityTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 15 * 20, level - 1, true, true));
                livingEntityTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 15 * 20, level - 1, true, true));
            }
        }
    }
}
