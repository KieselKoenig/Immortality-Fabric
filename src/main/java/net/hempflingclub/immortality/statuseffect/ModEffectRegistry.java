package net.hempflingclub.immortality.statuseffect;

import net.hempflingclub.immortality.Immortality;
import net.hempflingclub.immortality.statuseffect.effects.ImmortalityEffect;
import net.hempflingclub.immortality.statuseffect.effects.LiverImmortalityEffect;
import net.hempflingclub.immortality.statuseffect.effects.TrilogyEffect;
import net.hempflingclub.immortality.statuseffect.effects.VoidHeartEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;

public class ModEffectRegistry {
    public static final ModStatusEffect immortality = new ImmortalityEffect(StatusEffectCategory.BENEFICIAL, 0xedc423);
    public static final ModStatusEffect liver_immortality = new LiverImmortalityEffect(StatusEffectCategory.BENEFICIAL, 0xedc423);
    public static final ModStatusEffect void_heart = new VoidHeartEffect(StatusEffectCategory.BENEFICIAL, 0x000000);
    public static final ModStatusEffect trilogy = new TrilogyEffect(StatusEffectCategory.BENEFICIAL, 0x000000);

    public static void registerAll() {
        try {
            for (Field field : ModEffectRegistry.class.getDeclaredFields()) {
                if (ModStatusEffect.class.isAssignableFrom(field.getType())) {
                    Identifier id = new Identifier(Immortality.MOD_ID, field.getName());
                    Registry.register(Registry.STATUS_EFFECT, id, ((ModStatusEffect) field.get(null)).onRegister());
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

