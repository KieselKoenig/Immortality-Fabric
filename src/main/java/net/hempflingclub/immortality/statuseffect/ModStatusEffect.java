package net.hempflingclub.immortality.statuseffect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ModStatusEffect extends StatusEffect {
    public ModStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    public ModStatusEffect onRegister() {
        return this;
    }
}
