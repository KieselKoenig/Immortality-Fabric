package net.hempflingclub.immortality.util;

import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class ImmortalityData {
    public static void setImmortality(@NotNull IImmortalityPlayerComponent playerData, boolean status) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putBoolean("immortal", status);
        playerData.setPlayerData(nbt);
    }

    public static boolean getImmortality(@NotNull IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getBoolean("immortal");
    }

    public static void setLiverImmortality(@NotNull IImmortalityPlayerComponent playerData, boolean status) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putBoolean("immortalLiver", status);
        playerData.setPlayerData(nbt);
    }

    public static boolean getLiverImmortality(@NotNull IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getBoolean("immortalLiver");
    }

    public static void setLiverExtracted(@NotNull IImmortalityPlayerComponent playerData, boolean status) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putBoolean("immortalLiverExtracted", status);
        playerData.setPlayerData(nbt);
    }

    public static boolean getLiverExtracted(@NotNull IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getBoolean("immortalLiverExtracted");
    }

    public static void setLiverOnceExtracted(@NotNull IImmortalityPlayerComponent playerData, boolean status) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putBoolean("immortalLiverOnceExtracted", status);
        playerData.setPlayerData(nbt);
    }

    public static boolean getLiverOnceExtracted(@NotNull IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getBoolean("immortalLiverOnceExtracted");
    }

    public static void setLiverExtractionTime(@NotNull IImmortalityPlayerComponent playerData, int time) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putInt("immortalLiverExtractionTime", time);
        playerData.setPlayerData(nbt);
    }

    public static int getLiverExtractionTime(@NotNull IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getInt("immortalLiverExtractionTime");
    }

    public static void setHeartExtractionAmount(@NotNull IImmortalityPlayerComponent playerData, int amount) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putInt("immortalHeartExtractionAmount", amount);
        playerData.setPlayerData(nbt);
    }

    public static int getHeartExtractionAmount(@NotNull IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getInt("immortalHeartExtractionAmount");
    }

    public static void setImmortalDeaths(@NotNull IImmortalityPlayerComponent playerData, int deaths) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putInt("immortalDeaths", deaths);
        playerData.setPlayerData(nbt);
    }

    public static int getImmortalDeaths(@NotNull IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getInt("immortalDeaths");
    }

    public static void setVoidHeart(@NotNull IImmortalityPlayerComponent playerData, boolean status) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putBoolean("voidheart", status);
        playerData.setPlayerData(nbt);
    }

    public static boolean getVoidHeart(@NotNull IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getBoolean("voidheart");
    }

    public static void setExtractedLivers(@NotNull IImmortalityPlayerComponent playerData, int amount) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putInt("extractedLivers", amount);
        playerData.setPlayerData(nbt);
    }

    public static int getExtractedLivers(@NotNull IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getInt("extractedLivers");
    }

    public static void setLifeElixirTime(@NotNull IImmortalityPlayerComponent playerData, int time) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putInt("lifeElixirTime", time);
        playerData.setPlayerData(nbt);
    }

    public static int getLifeElixirTime(@NotNull IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getInt("lifeElixirTime");
    }

    public static void setSemiImmortality(@NotNull IImmortalityPlayerComponent playerComponent, boolean status) {
        NbtCompound nbt = playerComponent.getPlayerData();
        nbt.putBoolean("semiImmortality", status);
        playerComponent.setPlayerData(nbt);
    }

    public static boolean getSemiImmortality(@NotNull IImmortalityPlayerComponent playerComponent) {
        NbtCompound nbt = playerComponent.getPlayerData();
        return nbt.getBoolean("semiImmortality");
    }

    public static void setLifeElixirDropTime(@NotNull IImmortalityPlayerComponent playerComponent, int time) {
        NbtCompound nbt = playerComponent.getPlayerData();
        nbt.putInt("lifeElixirDropTime", time);
        playerComponent.setPlayerData(nbt);
    }

    public static int getLifeElixirDropTime(@NotNull IImmortalityPlayerComponent playerComponent) {
        NbtCompound nbt = playerComponent.getPlayerData();
        return nbt.getInt("lifeElixirDropTime");
    }

    public static void setKilledByBaneOfLifeTime(@NotNull IImmortalityPlayerComponent playerComponent, int time) {
        NbtCompound nbt = playerComponent.getPlayerData();
        nbt.putInt("killedByBaneOfLifeTime", time);
        playerComponent.setPlayerData(nbt);
    }

    public static int getKilledByBaneOfLifeTime(@NotNull IImmortalityPlayerComponent playerComponent) {
        NbtCompound nbt = playerComponent.getPlayerData();
        return nbt.getInt("killedByBaneOfLifeTime");
    }

    public static void setKilledByBaneOfLifeCount(@NotNull IImmortalityPlayerComponent playerComponent, int count) {
        NbtCompound nbt = playerComponent.getPlayerData();
        nbt.putInt("killedByBaneOfLifeCount", count);
        playerComponent.setPlayerData(nbt);
    }

    public static int getKilledByBaneOfLifeCount(@NotNull IImmortalityPlayerComponent playerComponent) {
        NbtCompound nbt = playerComponent.getPlayerData();
        return nbt.getInt("killedByBaneOfLifeCount");
    }

    public static void setSemiImmortalLostHeartTime(@NotNull IImmortalityPlayerComponent playerComponent, int time) {
        NbtCompound nbt = playerComponent.getPlayerData();
        nbt.putInt("semiImmortalLostHeartTime", time);
        playerComponent.setPlayerData(nbt);
    }

    public static int getSemiImmortalLostHeartTime(@NotNull IImmortalityPlayerComponent playerComponent) {
        NbtCompound nbt = playerComponent.getPlayerData();
        return nbt.getInt("semiImmortalLostHeartTime");
    }

    public static void setSoulBoundGiftedEntityUUID(@NotNull IImmortalityPlayerComponent playerComponent, UUID uuid) {
        NbtCompound nbt = playerComponent.getPlayerData();
        nbt.putUuid("soulBoundGiftedImmortal", uuid);
        playerComponent.setPlayerData(nbt);
    }

    public static UUID getSoulBoundGiftedEntityUUID(@NotNull IImmortalityPlayerComponent playerComponent) {
        NbtCompound nbt = playerComponent.getPlayerData();
        return nbt.getUuid("soulBoundGiftedImmortal");
    }

    public static boolean doesSoulBoundGiftedEntityUUIDExist(@NotNull IImmortalityPlayerComponent playerComponent) {
        NbtCompound nbt = playerComponent.getPlayerData();
        return nbt.containsUuid("soulBoundGiftedImmortal");
    }

    public static void removeSoulBoundGiftedEntityUUID(@NotNull IImmortalityPlayerComponent playerComponent) {
        NbtCompound nbt = playerComponent.getPlayerData();
        nbt.remove("soulBoundGiftedImmortal");
        playerComponent.setPlayerData(nbt);
    }

    public static void setSoulBoundGiverEntityUUID(@NotNull IImmortalityLivingEntityComponent livingEntityComponent, UUID uuid) {
        NbtCompound nbt = livingEntityComponent.getLivingEntityData();
        nbt.putUuid("soulBoundGiverImmortal", uuid);
        livingEntityComponent.setLivingEntityData(nbt);
    }

    public static UUID getSoulBoundGiverEntityUUID(@NotNull IImmortalityLivingEntityComponent livingEntityComponent) {
        NbtCompound nbt = livingEntityComponent.getLivingEntityData();
        return nbt.getUuid("soulBoundGiverImmortal");
    }

    public static boolean doesSoulBoundGiverEntityUUIDExist(@NotNull IImmortalityLivingEntityComponent livingEntityComponent) {
        NbtCompound nbt = livingEntityComponent.getLivingEntityData();
        return nbt.containsUuid("soulBoundGiverImmortal");
    }

    public static void setImmortalWitherDeaths(@NotNull IImmortalityLivingEntityComponent livingEntityComponent, int deaths) {
        NbtCompound nbt = livingEntityComponent.getLivingEntityData();
        nbt.putInt("immortalWitherDeaths", deaths);
        livingEntityComponent.setLivingEntityData(nbt);
    }

    public static int getImmortalWitherDeaths(@NotNull IImmortalityLivingEntityComponent livingEntityComponent) {
        NbtCompound nbt = livingEntityComponent.getLivingEntityData();
        return nbt.getInt("immortalWitherDeaths");
    }

    public static void setSummonedTeleport(@NotNull IImmortalityPlayerComponent playerComponent, boolean status) {
        NbtCompound nbt = playerComponent.getPlayerData();
        nbt.putBoolean("summonTeleport", status);
        playerComponent.setPlayerData(nbt);
    }

    public static boolean getSummonedTeleport(@NotNull IImmortalityPlayerComponent playerComponent) {
        NbtCompound nbt = playerComponent.getPlayerData();
        return nbt.getBoolean("summonTeleport");
    }
}
