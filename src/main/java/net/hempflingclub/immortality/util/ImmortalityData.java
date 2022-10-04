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
