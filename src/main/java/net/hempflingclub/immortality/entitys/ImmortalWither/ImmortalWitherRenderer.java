package net.hempflingclub.immortality.entitys.ImmortalWither;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hempflingclub.immortality.Immortality;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class ImmortalWitherRenderer extends MobEntityRenderer<ImmortalWither, ImmortalWitherModel<ImmortalWither>> {
    private static final Identifier INVULNERABLE_TEXTURE = new Identifier(Immortality.MOD_ID, "textures/entity/immortal-wither/wither_invulnerable.png");
    private static final Identifier TEXTURE = new Identifier(Immortality.MOD_ID, "textures/entity/immortal-wither/wither.png");

    public ImmortalWitherRenderer(EntityRendererFactory.Context context) {
        super(context, new ImmortalWitherModel<>(context.getPart(EntityModelLayers.WITHER)), 1.0F);
    }

    protected int getBlockLight(ImmortalWither immortalWither, BlockPos blockPos) {
        return 15;
    }

    @Override
    public Identifier getTexture(ImmortalWither immortalWither) {
        int i = immortalWither.getInvulnerableTimer();
        return i > 0 && (i > 80 || i / 5 % 2 != 1) ? INVULNERABLE_TEXTURE : TEXTURE;
    }

    protected void scale(ImmortalWither immortalWither, MatrixStack matrixStack, float f) {
        float g = 2.0F;
        int i = immortalWither.getInvulnerableTimer();
        if (i > 0) {
            g -= ((float) i - f) / 220.0F * 0.5F;
        }

        matrixStack.scale(g, g, g);
    }
}
