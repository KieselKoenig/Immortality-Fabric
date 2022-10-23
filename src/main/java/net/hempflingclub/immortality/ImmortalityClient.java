package net.hempflingclub.immortality;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.hempflingclub.immortality.entitys.ImmortalEntitys;
import net.hempflingclub.immortality.entitys.ImmortalWitherModel;
import net.hempflingclub.immortality.entitys.ImmortalWitherRenderer;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ImmortalityClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        final EntityModelLayer ImmortalWitherLayer = new EntityModelLayer(new Identifier(Immortality.MOD_ID, "immortal-wither"), "main");
        EntityRendererRegistry.register(ImmortalEntitys.ImmortalWither, ImmortalWitherRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ImmortalWitherLayer, () -> ImmortalWitherModel.getTexturedModelData(new Dilation(0.0f)));
    }
}
