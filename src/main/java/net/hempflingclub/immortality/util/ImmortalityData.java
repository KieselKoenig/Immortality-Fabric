package net.hempflingclub.immortality.util;

import net.minecraft.nbt.NbtCompound;

public class ImmortalityData {
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
}
