package net.hempflingclub.immortality.util;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.entity.LivingEntity;

public class ImmortalityComponents implements WorldComponentInitializer, EntityComponentInitializer {
    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(IImmortalityWorldComponent.KEY, it -> new ImmortalityWorldDataImpl());
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(IImmortalityPlayerComponent.KEY, it -> new ImmortalityPlayerComponentImpl(), RespawnCopyStrategy.ALWAYS_COPY); // Soul is Immortal, not the Body
        registry.registerFor(LivingEntity.class, IImmortalityLivingEntityComponent.KEY, it -> new ImmortalityLivingEntityComponentImpl());
    }
}
