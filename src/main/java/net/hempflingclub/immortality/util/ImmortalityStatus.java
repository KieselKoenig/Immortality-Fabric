package net.hempflingclub.immortality.util;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;

public final class ImmortalityStatus {
    private static final int immortalityHearts = 2;
    private static final int negativeImmortalityHearts = -2;
    private static final int regrowingImmortalityLiver = -10;
    private static final int immortalityHardening = 1;
    private static final int immortalityBaseArmor = 1;

    /**
     * Gives (1) Immortality Heart
     *
     * @param playerEntity you can Typecast to this using (playerEntity) however please check if player by playerEntity.isPlayer() before
     */
    public static void addImmortalityHearts(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        EntityAttributeModifier healthAddition = new EntityAttributeModifier("immortalityHearts", immortalityHearts, EntityAttributeModifier.Operation.ADDITION);
        maxHealth.addPersistentModifier(healthAddition);
    }

    public static void addNegativeHearts(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        EntityAttributeModifier healthSubtraction = new EntityAttributeModifier("negativeImmortalityHearts", negativeImmortalityHearts, EntityAttributeModifier.Operation.ADDITION);
        maxHealth.addPersistentModifier(healthSubtraction);
    }

    public static void addRegrowingLiver(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        EntityAttributeModifier healthSubtraction = new EntityAttributeModifier("regrowingImmortalityLiver", regrowingImmortalityLiver, EntityAttributeModifier.Operation.ADDITION);
        maxHealth.addPersistentModifier(healthSubtraction);
    }

    public static void incrementImmortalityDeath(PlayerEntity playerEntity) {
        ImmortalityData.setImmortalDeaths((IPlayerDataSaver) playerEntity, ImmortalityData.getImmortalDeaths((IPlayerDataSaver) playerEntity) + 1);
    }

    public static void resetImmortalityDeath(PlayerEntity playerEntity) {
        ImmortalityData.setImmortalDeaths((IPlayerDataSaver) playerEntity, 0);
    }

    public static void addImmortalityArmorT(PlayerEntity playerEntity) {
        EntityAttributeInstance armorT = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
        assert armorT != null;
        EntityAttributeModifier addArmorTough = new EntityAttributeModifier("immortalityHardening", immortalityHardening, EntityAttributeModifier.Operation.ADDITION);
        armorT.addPersistentModifier(addArmorTough);
    }

    public static void removeImmortalityArmorT(PlayerEntity playerEntity) {
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
        removeImmortalityArmorT(playerEntity);
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

    public static void addImmortalityArmor(PlayerEntity playerEntity) {
        EntityAttributeInstance armor = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
        assert armor != null;
        EntityAttributeModifier addArmor = new EntityAttributeModifier("immortalityBaseArmor", immortalityBaseArmor, EntityAttributeModifier.Operation.ADDITION);
        armor.addPersistentModifier(addArmor);
    }

    public static void removeImmortalityArmor(PlayerEntity playerEntity) {
        EntityAttributeInstance armor = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
        assert armor != null;
        for (EntityAttributeModifier entityModifier : armor.getModifiers()) {
            if (entityModifier.getName().equals("immortalityBaseArmor")) {
                armor.removeModifier(entityModifier);
            }
        }
    }

    public static int getBonusHearts(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        int bonusHearts = 0;
        for (EntityAttributeModifier entityModifier : maxHealth.getModifiers()) {
            if (entityModifier.getName().equals("immortalityHearts")) {
                bonusHearts += immortalityHearts;
            }
        }
        return bonusHearts;
    }

    public static int getNegativeHearts(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        int negativeHearts = 0;
        for (EntityAttributeModifier entityModifier : maxHealth.getModifiers()) {
            if (entityModifier.getName().equals("negativeImmortalityHearts")) {
                negativeHearts += immortalityHearts;
            }
        }
        return negativeHearts;
    }

    public static int getRegeneratingHearts(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        int regeneratingHearts = 0;
        for (EntityAttributeModifier entityModifier : maxHealth.getModifiers()) {
            if (entityModifier.getName().equals("regrowingImmortalityLiver")) {
                regeneratingHearts += immortalityHearts;
            }
        }
        return regeneratingHearts;
    }

    public static int getBonusArmor(PlayerEntity playerEntity) {
        EntityAttributeInstance armor = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
        assert armor != null;
        int bonusArmor = 0;
        for (EntityAttributeModifier entityModifier : armor.getModifiers()) {
            if (entityModifier.getName().equals("immortalityBaseArmor")) {
                bonusArmor += immortalityBaseArmor;
            }
        }
        return bonusArmor;
    }

    public static int getBonusArmorT(PlayerEntity playerEntity) {
        EntityAttributeInstance armorT = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
        assert armorT != null;
        int bonusArmorT = 0;
        for (EntityAttributeModifier entityModifier : armorT.getModifiers()) {
            if (entityModifier.getName().equals("immortalityBaseArmor")) {
                bonusArmorT += immortalityHardening;
            }
        }
        return bonusArmorT;
    }
}
