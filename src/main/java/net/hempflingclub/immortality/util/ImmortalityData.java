package net.hempflingclub.immortality.util;

import net.minecraft.nbt.NbtCompound;

public final class ImmortalityData {
    public static void setImmortality(IImmortalityPlayerComponent playerData, boolean status) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putBoolean("immortal", status);
        playerData.setPlayerData(nbt);
    }

    public static boolean getImmortality(IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getBoolean("immortal");
    }

    public static void setLiverImmortality(IImmortalityPlayerComponent playerData, boolean status) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putBoolean("immortalLiver", status);
        playerData.setPlayerData(nbt);
    }

    public static boolean getLiverImmortality(IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getBoolean("immortalLiver");
    }

    public static void setLiverExtracted(IImmortalityPlayerComponent playerData, boolean status) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putBoolean("immortalLiverExtracted", status);
        playerData.setPlayerData(nbt);
    }

    public static boolean getLiverExtracted(IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getBoolean("immortalLiverExtracted");
    }

    public static void setLiverOnceExtracted(IImmortalityPlayerComponent playerData, boolean status) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putBoolean("immortalLiverOnceExtracted", status);
        playerData.setPlayerData(nbt);
    }

    public static boolean getLiverOnceExtracted(IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getBoolean("immortalLiverOnceExtracted");
    }

    public static void setLiverExtractionTime(IImmortalityPlayerComponent playerData, int time) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putInt("immortalLiverExtractionTime", time);
        playerData.setPlayerData(nbt);
    }

    public static int getLiverExtractionTime(IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getInt("immortalLiverExtractionTime");
    }

    public static void setHeartExtractionAmount(IImmortalityPlayerComponent playerData, int amount) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putInt("immortalHeartExtractionAmount", amount);
        playerData.setPlayerData(nbt);
    }

    public static int getHeartExtractionAmount(IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getInt("immortalHeartExtractionAmount");
    }

    public static void setImmortalDeaths(IImmortalityPlayerComponent playerData, int deaths) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putInt("immortalDeaths", deaths);
        playerData.setPlayerData(nbt);
    }

    public static int getImmortalDeaths(IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getInt("immortalDeaths");
    }

    public static void setVoidHeart(IImmortalityPlayerComponent playerData, boolean status) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putBoolean("voidheart", status);
        playerData.setPlayerData(nbt);
    }

    public static boolean getVoidHeart(IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getBoolean("voidheart");
    }

    public static void setExtractedLivers(IImmortalityPlayerComponent playerData, int amount) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putInt("extractedLivers", amount);
        playerData.setPlayerData(nbt);
    }

    public static int getExtractedLivers(IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getInt("extractedLivers");
    }

    public static void setLifeElixirTime(IImmortalityPlayerComponent playerData, int time) {
        NbtCompound nbt = playerData.getPlayerData();
        nbt.putInt("lifeElixirTime", time);
        playerData.setPlayerData(nbt);
    }

    public static int getLifeElixirTime(IImmortalityPlayerComponent playerData) {
        NbtCompound nbt = playerData.getPlayerData();
        return nbt.getInt("lifeElixirTime");
    }

    public static void setSemiImmortality(IImmortalityPlayerComponent playerComponent, boolean status) {
        NbtCompound nbt = playerComponent.getPlayerData();
        nbt.putBoolean("semiImmortality", status);
        playerComponent.setPlayerData(nbt);
    }

    public static boolean getSemiImmortality(IImmortalityPlayerComponent playerComponent) {
        NbtCompound nbt = playerComponent.getPlayerData();
        return nbt.getBoolean("semiImmortality");
    }

    public static void setLifeElixirDropTime(IImmortalityPlayerComponent playerComponent, int time) {
        NbtCompound nbt = playerComponent.getPlayerData();
        nbt.putInt("lifeElixirDropTime", time);
        playerComponent.setPlayerData(nbt);
    }

    public static int getLifeElixirDropTime(IImmortalityPlayerComponent playerComponent) {
        NbtCompound nbt = playerComponent.getPlayerData();
        return nbt.getInt("lifeElixirDropTime");
    }

    public static void setKilledByBaneOfLifeTime(IImmortalityPlayerComponent playerComponent, int time) {
        NbtCompound nbt = playerComponent.getPlayerData();
        nbt.putInt("killedByBaneOfLifeTime", time);
        playerComponent.setPlayerData(nbt);
    }

    public static int getKilledByBaneOfLifeTime(IImmortalityPlayerComponent playerComponent) {
        NbtCompound nbt = playerComponent.getPlayerData();
        return nbt.getInt("killedByBaneOfLifeTime");
    }

    public static void setKilledByBaneOfLifeCount(IImmortalityPlayerComponent playerComponent, int count) {
        NbtCompound nbt = playerComponent.getPlayerData();
        nbt.putInt("killedByBaneOfLifeCount", count);
        playerComponent.setPlayerData(nbt);
    }

    public static int getKilledByBaneOfLifeCount(IImmortalityPlayerComponent playerComponent) {
        NbtCompound nbt = playerComponent.getPlayerData();
        return nbt.getInt("killedByBaneOfLifeCount");
    }

    public static void setSemiImmortalLostHeartTime(IImmortalityPlayerComponent playerComponent, int time) {
        NbtCompound nbt = playerComponent.getPlayerData();
        nbt.putInt("semiImmortalLostHeartTime", time);
        playerComponent.setPlayerData(nbt);
    }

    public static int getSemiImmortalLostHeartTime(IImmortalityPlayerComponent playerComponent) {
        NbtCompound nbt = playerComponent.getPlayerData();
        return nbt.getInt("semiImmortalLostHeartTime");
    }
}
