package net.hempflingclub.immortality.util;

import net.hempflingclub.immortality.entitys.ImmortalWither.ImmortalWither;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.UUID;

public final class ImmortalityStatus {
    public static final int immortalityHearts = 2;
    public static final int negativeImmortalityHearts = -2;
    public static final int regrowingImmortalityLiver = -10;
    public static final int immortalityHardening = 1;
    public static final int immortalityBaseArmor = 1;
    public static final int lifeElixirHealth = 2;

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
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void addNegativeHearts(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        EntityAttributeModifier healthSubtraction = new EntityAttributeModifier("negativeImmortalityHearts", negativeImmortalityHearts, EntityAttributeModifier.Operation.ADDITION);
        maxHealth.addPersistentModifier(healthSubtraction);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
        playerEntity.tick();
    }

    public static void addRegrowingLiver(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        EntityAttributeModifier healthSubtraction = new EntityAttributeModifier("regrowingImmortalityLiver", regrowingImmortalityLiver, EntityAttributeModifier.Operation.ADDITION);
        maxHealth.addPersistentModifier(healthSubtraction);
        incrementExtractedLiver(playerEntity);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
        playerEntity.tick();
    }

    public static void incrementImmortalityDeath(PlayerEntity playerEntity) {
        ImmortalityData.setImmortalDeaths(getPlayerComponent(playerEntity), ImmortalityData.getImmortalDeaths(getPlayerComponent(playerEntity)) + 1);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void resetImmortalityDeath(PlayerEntity playerEntity) {
        ImmortalityData.setImmortalDeaths(getPlayerComponent(playerEntity), 0);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void resetExtractedLivers(PlayerEntity playerEntity) {
        ImmortalityData.setExtractedLivers(getPlayerComponent(playerEntity), 0);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void addImmortalityArmorT(PlayerEntity playerEntity) {
        EntityAttributeInstance armorT = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
        assert armorT != null;
        EntityAttributeModifier addArmorTough = new EntityAttributeModifier("immortalityHardening", immortalityHardening, EntityAttributeModifier.Operation.ADDITION);
        armorT.addPersistentModifier(addArmorTough);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void removeImmortalityArmorT(PlayerEntity playerEntity) {
        EntityAttributeInstance armorT = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
        assert armorT != null;
        for (EntityAttributeModifier entityModifier : armorT.getModifiers()) {
            if (entityModifier.getName().equals("immortalityHardening")) {
                armorT.removeModifier(entityModifier);
            }
        }
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }


    /**
     * Removes Immortality / False Immortality / Void Heart / Immortality Hearts / regrowingImmortalityLiver / negativeImmortalityHearts and resets Immortality Deaths
     *
     * @param playerEntity you can Typecast to this using (playerEntity) however please check if player by playerEntity.isPlayer() before
     */
    public static void removeEverything(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        ImmortalityData.setImmortality(getPlayerComponent(playerEntity), false);
        ImmortalityData.setLiverImmortality(getPlayerComponent(playerEntity), false);
        ImmortalityData.setVoidHeart(getPlayerComponent(playerEntity), false);
        setSemiImmortality(playerEntity, false);
        resetImmortalityDeath(playerEntity);
        resetExtractedLivers(playerEntity);
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
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void removeRegrowing(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        for (EntityAttributeModifier entityModifier : maxHealth.getModifiers()) {
            if (entityModifier.getName().equals("regrowingImmortalityLiver")) {
                maxHealth.removeModifier(entityModifier);
            }
        }
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void removeNegativeHearts(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        for (EntityAttributeModifier entityModifier : maxHealth.getModifiers()) {
            if (entityModifier.getName().equals("negativeImmortalityHearts")) {
                maxHealth.removeModifier(entityModifier);
            }
        }
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void removeFalseImmortality(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        ImmortalityData.setLiverImmortality(getPlayerComponent(playerEntity), false);
        resetImmortalityDeath(playerEntity);
        for (EntityAttributeModifier entityModifier : maxHealth.getModifiers()) {
            if (entityModifier.getName().equals("negativeImmortalityHearts")) {
                maxHealth.removeModifier(entityModifier);
            }
        }
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void addImmortalityArmor(PlayerEntity playerEntity) {
        EntityAttributeInstance armor = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
        assert armor != null;
        EntityAttributeModifier addArmor = new EntityAttributeModifier("immortalityBaseArmor", immortalityBaseArmor, EntityAttributeModifier.Operation.ADDITION);
        armor.addPersistentModifier(addArmor);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void removeImmortalityArmor(PlayerEntity playerEntity) {
        EntityAttributeInstance armor = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
        assert armor != null;
        for (EntityAttributeModifier entityModifier : armor.getModifiers()) {
            if (entityModifier.getName().equals("immortalityBaseArmor")) {
                armor.removeModifier(entityModifier);
            }
        }
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
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
                regeneratingHearts += regeneratingHearts;
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
            if (entityModifier.getName().equals("immortalityHardening")) {
                bonusArmorT += immortalityHardening;
            }
        }
        return bonusArmorT;
    }

    public static IImmortalityPlayerComponent getPlayerComponent(PlayerEntity playerEntity) {
        return IImmortalityPlayerComponent.KEY.get(playerEntity);
    }

    public static void setImmortality(PlayerEntity playerEntity, boolean status) {
        ImmortalityData.setImmortality(getPlayerComponent(playerEntity), status);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static boolean getImmortality(PlayerEntity playerEntity) {
        return ImmortalityData.getImmortality(getPlayerComponent(playerEntity));
    }

    public static void setLiverImmortality(PlayerEntity playerEntity, boolean status) {
        ImmortalityData.setLiverImmortality(getPlayerComponent(playerEntity), status);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static boolean getLiverImmortality(PlayerEntity playerEntity) {
        return ImmortalityData.getLiverImmortality(getPlayerComponent(playerEntity));
    }

    public static void setVoidHeart(PlayerEntity playerEntity, boolean status) {
        ImmortalityData.setVoidHeart(getPlayerComponent(playerEntity), status);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static boolean getVoidHeart(PlayerEntity playerEntity) {
        return ImmortalityData.getVoidHeart(getPlayerComponent(playerEntity));
    }

    public static boolean isTrueImmortal(PlayerEntity playerEntity) {
        return (getImmortality(playerEntity) && getVoidHeart(playerEntity) && canEatLiverOfImmortality(playerEntity) && hasTrueImmortalDeaths(playerEntity));
    }

    public static boolean hasTrueImmortalDeaths(PlayerEntity playerEntity) {
        return (getMissingDeathsToTrueImmortality(playerEntity) <= 0);
    }

    public static boolean canEatLiverOfImmortality(PlayerEntity playerEntity) {
        return ((getMissingLiversToEatLiverOfImmortality(playerEntity) <= 0 && getImmortality(playerEntity) && ImmortalityData.getLiverOnceExtracted(getPlayerComponent(playerEntity))) || !getImmortality(playerEntity));
    }

    public static int getMissingDeathsToTrueImmortality(PlayerEntity playerEntity) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        int requiredDeaths = 30;
        return (requiredDeaths - ImmortalityData.getImmortalDeaths(playerComponent));
    }

    public static int getMissingLiversToEatLiverOfImmortality(PlayerEntity playerEntity) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        int requiredExtracts = 3;
        return (requiredExtracts - ImmortalityData.getExtractedLivers(playerComponent));
    }

    public static void incrementExtractedLiver(PlayerEntity playerEntity) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        ImmortalityData.setExtractedLivers(playerComponent, ImmortalityData.getExtractedLivers(playerComponent) + 1);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void resetLifeElixirTime(PlayerEntity playerEntity) {
        setLifeElixirTime(playerEntity, 0);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void setLifeElixirTime(PlayerEntity playerEntity, int time) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        ImmortalityData.setLifeElixirTime(playerComponent, time);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static int getLifeElixirTime(PlayerEntity playerEntity) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        return ImmortalityData.getLifeElixirTime(playerComponent);
    }

    public static boolean shouldLifeElixirApply(PlayerEntity playerEntity) {
        long serverTime = Objects.requireNonNull(playerEntity.getServer()).getOverworld().getTime();
        int effectTime = 300 * 20;
        return (serverTime >= (getLifeElixirTime(playerEntity) + effectTime * 0.99) && getLifeElixirTime(playerEntity) != 0);
    }

    public static void addLifeElixirHealth(PlayerEntity playerEntity) {
        if (isTrueImmortal(playerEntity) || (Math.random() >= 0.75 && !(ImmortalityStatus.isSemiImmortal(playerEntity) || ImmortalityStatus.getLiverImmortality(playerEntity) || ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.isTrueImmortal(playerEntity))) || (Math.random() >= 0.5 && (ImmortalityStatus.isSemiImmortal(playerEntity) || ImmortalityStatus.getLiverImmortality(playerEntity) || ImmortalityStatus.getImmortality(playerEntity) || ImmortalityStatus.isTrueImmortal(playerEntity)))) {
            EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
            assert maxHealth != null;
            EntityAttributeModifier healthAddition = new EntityAttributeModifier("lifeElixir", lifeElixirHealth, EntityAttributeModifier.Operation.ADDITION);
            maxHealth.addPersistentModifier(healthAddition);
            resetLifeElixirTime(playerEntity);
            playerEntity.syncComponent(IImmortalityPlayerComponent.KEY); // Ensures their NBT gets saved, even if killed the next tick (hopefully)
            ImmortalityAdvancementGiver.giveLifeElixirAchievement(playerEntity);
            playerEntity.sendMessage(Text.translatable("immortality.status.life_elixir"), true);
        } else {
            playerEntity.sendMessage(Text.translatable("immortality.status.life_elixir_failed"), true);
            resetLifeElixirTime(playerEntity);
            playerEntity.syncComponent(IImmortalityPlayerComponent.KEY); // Ensures their NBT gets saved, even if killed the next tick (hopefully)
        }
    }

    public static int getLifeElixirHealth(PlayerEntity playerEntity) {
        EntityAttributeInstance elixirH = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert elixirH != null;
        int bonusLifeElixirH = 0;
        for (EntityAttributeModifier entityModifier : elixirH.getModifiers()) {
            if (entityModifier.getName().equals("lifeElixir")) {
                bonusLifeElixirH += lifeElixirHealth;
            }
        }
        return bonusLifeElixirH;
    }

    public static int getCurrentTime(PlayerEntity playerEntity) {
        return (int) Objects.requireNonNull(playerEntity.getServer()).getOverworld().getTime();
    }

    public static int getCurrentTime(MinecraftServer server) {
        return (int) server.getOverworld().getTime();
    }

    public static boolean isSemiImmortal(PlayerEntity playerEntity) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        return ImmortalityData.getSemiImmortality(playerComponent);
    }

    public static void setSemiImmortality(PlayerEntity playerEntity, boolean status) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        ImmortalityData.setSemiImmortality(playerComponent, status);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void convertFalseIntoSemiImmortality(PlayerEntity playerEntity) {
        removeFalseImmortality(playerEntity);
        setSemiImmortality(playerEntity, true);
    }

    public static int getLifeElixirDropTime(PlayerEntity playerEntity) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        return ImmortalityData.getLifeElixirDropTime(playerComponent);
    }

    public static void setLifeElixirDropTime(PlayerEntity playerEntity, int time) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        ImmortalityData.setLifeElixirDropTime(playerComponent, time);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void resetLifeElixirDropTime(PlayerEntity playerEntity) {
        setLifeElixirDropTime(playerEntity, 0);
    }

    public static int getKilledByBaneOfLifeTime(PlayerEntity playerEntity) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        return ImmortalityData.getKilledByBaneOfLifeTime(playerComponent);
    }

    public static void setKilledByBaneOfLifeTime(PlayerEntity playerEntity, int time) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        ImmortalityData.setKilledByBaneOfLifeTime(playerComponent, time);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void resetKilledByBaneOfLifeTime(PlayerEntity playerEntity) {
        setKilledByBaneOfLifeTime(playerEntity, 0);
    }

    public static int getKilledByBaneOfLifeCount(PlayerEntity playerEntity) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        return ImmortalityData.getKilledByBaneOfLifeCount(playerComponent);
    }

    public static void setKilledByBaneOfLifeCount(PlayerEntity playerEntity, int count) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        ImmortalityData.setKilledByBaneOfLifeCount(playerComponent, count);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void resetKilledByBaneOfLifeCount(PlayerEntity playerEntity) {
        setKilledByBaneOfLifeCount(playerEntity, 0);
    }

    public static void incrementKilledByBaneOfLifeCount(PlayerEntity playerEntity) {
        setKilledByBaneOfLifeCount(playerEntity, getKilledByBaneOfLifeCount(playerEntity) + 1);
    }

    public static void convertSemiImmortalityIntoOtherImmortality(PlayerEntity playerEntity) {
        removeNegativeHearts(playerEntity);
        setSemiImmortality(playerEntity, false);
    }

    public static void setSemiImmortalityLostHeartTime(PlayerEntity playerEntity, int time) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        ImmortalityData.setSemiImmortalLostHeartTime(playerComponent, time);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void resetSemiImmortalityLostHeartTime(PlayerEntity playerEntity) {
        setSemiImmortalityLostHeartTime(playerEntity, 0);
    }

    public static int getSemiImmortalityLostHeartTime(PlayerEntity playerEntity) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        return ImmortalityData.getSemiImmortalLostHeartTime(playerComponent);
    }

    public static void removeOneNegativeHeart(PlayerEntity playerEntity) {
        EntityAttributeInstance maxHealth = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        for (EntityAttributeModifier entityModifier : maxHealth.getModifiers()) {
            if (entityModifier.getName().equals("negativeImmortalityHearts")) {
                maxHealth.removeModifier(entityModifier);
                break;
            }
        }
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void setTargetGiftedImmortal(PlayerEntity playerEntity, UUID targetUuid) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        ImmortalityData.setSoulBoundGiftedEntityUUID(playerComponent, targetUuid);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static void removeTargetGiftedImmortal(PlayerEntity playerEntity) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        ImmortalityData.removeSoulBoundGiftedEntityUUID(playerComponent);
        playerEntity.syncComponent(IImmortalityPlayerComponent.KEY);
    }

    public static UUID getTargetGiftedImmortal(PlayerEntity playerEntity) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        return ImmortalityData.getSoulBoundGiftedEntityUUID(playerComponent);
    }

    public static LivingEntity getTargetGiftedImmortalLivingEntity(PlayerEntity playerEntity) {
        UUID target = getTargetGiftedImmortal(playerEntity);
        for (ServerWorld world : Objects.requireNonNull(playerEntity.getServer()).getWorlds()) {
            if (world.getEntity(target) != null) {
                if (Objects.requireNonNull(world.getEntity(target)).isLiving()) {
                    if (ImmortalityStatus.hasTargetGiverImmortal((LivingEntity) world.getEntity(target))) {
                        return (LivingEntity) world.getEntity(target);
                    }
                }
            }
        }
        return null;
    }

    public static boolean hasTargetGiftedImmortal(PlayerEntity playerEntity) {
        IImmortalityPlayerComponent playerComponent = getPlayerComponent(playerEntity);
        return ImmortalityData.doesSoulBoundGiftedEntityUUIDExist(playerComponent);
    }

    public static IImmortalityLivingEntityComponent getLivingEntityComponent(LivingEntity livingEntity) {
        return IImmortalityLivingEntityComponent.KEY.get(livingEntity);
    }

    public static void setTargetGiverImmortal(LivingEntity livingEntity, UUID targetUuid) {
        IImmortalityLivingEntityComponent livingEntityComponent = getLivingEntityComponent(livingEntity);
        ImmortalityData.setSoulBoundGiverEntityUUID(livingEntityComponent, targetUuid);
        livingEntity.syncComponent(IImmortalityLivingEntityComponent.KEY);
    }

    public static UUID getTargetGiverImmortal(LivingEntity livingEntity) {
        IImmortalityLivingEntityComponent livingEntityComponent = getLivingEntityComponent(livingEntity);
        return ImmortalityData.getSoulBoundGiverEntityUUID(livingEntityComponent);
    }

    public static boolean hasTargetGiverImmortal(LivingEntity livingEntity) {
        IImmortalityLivingEntityComponent livingEntityComponent = getLivingEntityComponent(livingEntity);
        return ImmortalityData.doesSoulBoundGiverEntityUUIDExist(livingEntityComponent);
    }

    public static PlayerEntity getTargetGiverImmortalPlayerEntity(LivingEntity livingEntity) {
        return Objects.requireNonNull(livingEntity.getServer()).getPlayerManager().getPlayer(getTargetGiverImmortal(livingEntity));
    }

    public static void incrementImmortalWitherDeaths(ImmortalWither immortalWither) {
        IImmortalityLivingEntityComponent livingEntityComponent = getLivingEntityComponent(immortalWither);
        ImmortalityData.setImmortalWitherDeaths(livingEntityComponent, getImmortalWitherDeaths(immortalWither) + 1);
    }

    public static int getImmortalWitherDeaths(ImmortalWither immortalWither) {
        IImmortalityLivingEntityComponent livingEntityComponent = getLivingEntityComponent(immortalWither);
        return ImmortalityData.getImmortalWitherDeaths(livingEntityComponent);
    }
}
