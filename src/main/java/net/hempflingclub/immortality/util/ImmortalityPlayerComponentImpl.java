package net.hempflingclub.immortality.util;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;

public class ImmortalityPlayerComponentImpl implements IImmortalityPlayerComponent, AutoSyncedComponent {
    private NbtCompound nbtData;

    @Override
    public void setPlayerData(NbtCompound nbt) {
        this.nbtData = nbt;
    }

    @Override
    public NbtCompound getPlayerData() {
        if (this.nbtData == null) {
            this.nbtData = new NbtCompound();
        }
        return this.nbtData;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains("immortality.data", 10)) {
            this.nbtData = tag.getCompound("immortality.data");
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (this.nbtData != null) {
            tag.put("immortality.data", this.nbtData);
        }
    }
}
