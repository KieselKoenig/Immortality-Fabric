package net.hempflingclub.immortality.util;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;

public final class ImmortalityStatus {
    /**
     * Gives (1) Immortality Heart
     *
     * @param playerEntity you can Typecast to this using (playerEntity) however please check if player by playerEntity.isPlayer() before
     */
    public static void addImmortalityHearts(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        EntityAttributeModifier healthAddition = new EntityAttributeModifier("immortalityHearts", 2, EntityAttributeModifier.Operation.ADDITION);
        maxHealth.addPersistentModifier(healthAddition);
    }

    public static void addNegativeHearts(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        EntityAttributeModifier healthSubtraction = new EntityAttributeModifier("negativeImmortalityHearts", -2, EntityAttributeModifier.Operation.ADDITION);
        maxHealth.addPersistentModifier(healthSubtraction);
    }

    public static void addRegrowingLiver(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        EntityAttributeModifier healthSubtraction = new EntityAttributeModifier("regrowingImmortalityLiver", -10, EntityAttributeModifier.Operation.ADDITION);
        maxHealth.addPersistentModifier(healthSubtraction);
    }

    public static void incrementImmortalityDeath(PlayerEntity playerEntity) {
        ImmortalityData.setImmortalDeaths((IPlayerDataSaver) playerEntity, ImmortalityData.getImmortalDeaths((IPlayerDataSaver) playerEntity) + 1);
    }

    public static void resetImmortalityDeath(PlayerEntity playerEntity) {
        ImmortalityData.setImmortalDeaths((IPlayerDataSaver) playerEntity, 0);
    }

    public static void addImmortalityArmor(PlayerEntity playerEntity) {
        EntityAttributeInstance armorT = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
        assert armorT != null;
        EntityAttributeModifier addArmorTough = new EntityAttributeModifier("immortalityHardening", 1, EntityAttributeModifier.Operation.ADDITION);
        armorT.addPersistentModifier(addArmorTough);
    }

    public static void removeImmortalityArmor(PlayerEntity playerEntity) {
        EntityAttributeInstance armorT = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
        assert armorT != null;
        for (EntityAttributeModifier entityModifier : armorT.getModifiers()) {
            if (entityModifier.getName().equals("immortalityHardening")) {
                armorT.removeModifier(entityModifier);
            }
        }
    }


    /**
     * Removes Immortality / False Immortality / Void Heart / Immortality Hearts / regrowingImmortalityLiver / negativeImmortalityHearts and resets Immortality Deaths
     *
     * @param playerEntity you can Typecast to this using (playerEntity) however please check if player by playerEntity.isPlayer() before
     */
    public static void removeEverything(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        ImmortalityData.setImmortality((IPlayerDataSaver) playerEntity, false);
        ImmortalityData.setLiverImmortality((IPlayerDataSaver) playerEntity, false);
        ImmortalityData.setVoidHeart((IPlayerDataSaver) playerEntity, false);
        resetImmortalityDeath(playerEntity);
        removeImmortalityArmor(playerEntity);
        for (EntityAttributeModifier entityModifier : maxHealth.getModifiers()) {
            if (
                    entityModifier.getName().equals("immortalityHearts") ||
                            entityModifier.getName().equals("regrowingImmortalityLiver") ||
                            entityModifier.getName().equals("negativeImmortalityHearts")) {
                maxHealth.removeModifier(entityModifier);
            }
        }
    }

    public static void removeRegrowing(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        for (EntityAttributeModifier entityModifier : maxHealth.getModifiers()) {
            if (entityModifier.getName().equals("regrowingImmortalityLiver")) {
                maxHealth.removeModifier(entityModifier);
            }
        }
    }

    public static void removeNegativeHearts(PlayerEntity playerEntity) {
        resetImmortalityDeath(playerEntity);
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        for (EntityAttributeModifier entityModifier : maxHealth.getModifiers()) {
            if (entityModifier.getName().equals("negativeImmortalityHearts")) {
                maxHealth.removeModifier(entityModifier);
            }
        }
    }

    public static void removeFalseImmortality(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        ImmortalityData.setLiverImmortality((IPlayerDataSaver) playerEntity, false);
        resetImmortalityDeath(playerEntity);
        for (EntityAttributeModifier entityModifier : maxHealth.getModifiers()) {
            if (entityModifier.getName().equals("negativeImmortalityHearts")) {
                maxHealth.removeModifier(entityModifier);
            }
        }
    }
}
