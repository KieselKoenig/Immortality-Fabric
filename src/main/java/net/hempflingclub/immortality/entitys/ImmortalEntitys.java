package net.hempflingclub.immortality.entitys;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.hempflingclub.immortality.Immortality;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class ImmortalEntitys {
    public static final EntityType<ImmortalWither> ImmortalWither = registerEntity("immortal-wither", EntityType.Builder.create(ImmortalWither::new, SpawnGroup.MONSTER).makeFireImmune().allowSpawningInside(Blocks.WITHER_ROSE).setDimensions(0.9f, 3.5f).maxTrackingRange(50));

    private static <T extends Entity> EntityType<T> registerEntity(String name, EntityType.Builder<T> entityType) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(Immortality.MOD_ID, name), entityType.build(name));
    }

    public static void registerEntitys() {
        Immortality.LOGGER.debug("Registering Entitys for " + Immortality.MOD_ID);
        FabricDefaultAttributeRegistry.register(ImmortalWither, net.hempflingclub.immortality.entitys.ImmortalWither.createImmortalWitherAttributes());
    }
}
