package net.hempflingclub.immortality.util;

import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;

public class ImmortalityComponents implements WorldComponentInitializer {
    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(IImmortalityWorldComponent.KEY, it -> new ImmortalityWorldDataImpl());
    }
}
