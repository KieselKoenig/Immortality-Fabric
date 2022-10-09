package net.hempflingclub.immortality.util;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.hempflingclub.immortality.Immortality;
import net.minecraft.util.Identifier;

public interface IImmortalityWorldComponent extends ComponentV3 {
    ComponentKey<IImmortalityWorldComponent> KEY = ComponentRegistry.getOrCreate(new Identifier(Immortality.MOD_ID, "world-data"), IImmortalityWorldComponent.class);

    void setDragonKills(int kills);

    int getDragonKills();
}
