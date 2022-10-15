package net.hempflingclub.immortality.util;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.hempflingclub.immortality.Immortality;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public interface IImmortalityPlayerComponent extends ComponentV3 {
    ComponentKey<IImmortalityPlayerComponent> KEY = ComponentRegistry.getOrCreate(new Identifier(Immortality.MOD_ID, "player-data"), IImmortalityPlayerComponent.class);

    void setPlayerData(NbtCompound nbt);
    NbtCompound getPlayerData();
}
