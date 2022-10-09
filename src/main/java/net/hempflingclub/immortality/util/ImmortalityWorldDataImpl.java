package net.hempflingclub.immortality.util;

import net.hempflingclub.immortality.Immortality;
import net.minecraft.nbt.NbtCompound;

public final class ImmortalityWorldDataImpl implements IImmortalityWorldComponent {
    private int dragonKills;

    @Override
    public void setDragonKills(int kills) {
        dragonKills = kills;
    }

    @Override
    public int getDragonKills() {
        return dragonKills;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.dragonKills = tag.getInt(Immortality.MOD_ID + "DragonKills");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt(Immortality.MOD_ID + "DragonKills", this.dragonKills);
    }
}
