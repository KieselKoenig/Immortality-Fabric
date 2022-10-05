package net.hempflingclub.immortality.util;

import net.minecraft.nbt.NbtCompound;

public class ImmortalityData {
    public static void setImmortality(IPlayerDataSaver playerdata, boolean status) {
        NbtCompound nbt = playerdata.getPersistentData();
        nbt.putBoolean("immortal", status);
    }

    public static boolean getImmortality(IPlayerDataSaver playerdata) {
        NbtCompound nbt = playerdata.getPersistentData();
        return nbt.getBoolean("immortal");
    }

    public static void setLiverImmortality(IPlayerDataSaver playerdata, boolean status) {
        NbtCompound nbt = playerdata.getPersistentData();
        nbt.putBoolean("immortalLiver", status);
    }

    public static boolean getLiverImmortality(IPlayerDataSaver playerdata) {
        NbtCompound nbt = playerdata.getPersistentData();
        return nbt.getBoolean("immortalLiver");
    }

    public static void setLiverExtracted(IPlayerDataSaver playerdata, boolean status) {
        NbtCompound nbt = playerdata.getPersistentData();
        nbt.putBoolean("immortalLiverExtracted", status);
    }

    public static boolean getLiverExtracted(IPlayerDataSaver playerdata) {
        NbtCompound nbt = playerdata.getPersistentData();
        return nbt.getBoolean("immortalLiverExtracted");
    }

    public static void setLiverOnceExtracted(IPlayerDataSaver playerdata, boolean status) {
        NbtCompound nbt = playerdata.getPersistentData();
        nbt.putBoolean("immortalLiverOnceExtracted", status);
    }

    public static boolean getLiverOnceExtracted(IPlayerDataSaver playerdata) {
        NbtCompound nbt = playerdata.getPersistentData();
        return nbt.getBoolean("immortalLiverOnceExtracted");
    }

    public static void setLiverExtractionTime(IPlayerDataSaver playerdata, int time) {
        NbtCompound nbt = playerdata.getPersistentData();
        nbt.putInt("immortalLiverExtractionTime", time);
    }

    public static int getLiverExtractionTime(IPlayerDataSaver playerdata) {
        NbtCompound nbt = playerdata.getPersistentData();
        return nbt.getInt("immortalLiverExtractionTime");
    }

    public static void setImmortalDeaths(IPlayerDataSaver playerdata, int deaths) {
        NbtCompound nbt = playerdata.getPersistentData();
        nbt.putInt("immortalDeaths", deaths);
    }

    public static int getImmortalDeaths(IPlayerDataSaver playerdata) {
        NbtCompound nbt = playerdata.getPersistentData();
        return nbt.getInt("immortalDeaths");
    }

    public static void setVoidHeart(IPlayerDataSaver playerdata, boolean status) {
        NbtCompound nbt = playerdata.getPersistentData();
        nbt.putBoolean("voidheart", status);
    }

    public static boolean getVoidHeart(IPlayerDataSaver playerdata) {
        NbtCompound nbt = playerdata.getPersistentData();
        return nbt.getBoolean("voidheart");
    }
}
