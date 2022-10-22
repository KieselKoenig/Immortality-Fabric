package net.hempflingclub.immortality.util;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.hempflingclub.immortality.Immortality;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public interface IImmortalityLivingEntityComponent extends ComponentV3 {
    ComponentKey<IImmortalityLivingEntityComponent> KEY = ComponentRegistry.getOrCreate(new Identifier(Immortality.MOD_ID, "living-entity-data"), IImmortalityLivingEntityComponent.class);

    void setLivingEntityData(NbtCompound nbt);
    NbtCompound getLivingEntityData();
}
