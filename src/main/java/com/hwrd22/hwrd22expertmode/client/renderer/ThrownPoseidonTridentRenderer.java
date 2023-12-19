package com.hwrd22.hwrd22expertmode.client.renderer;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import com.hwrd22.hwrd22expertmode.client.renderer.models.ThrownPoseidonTridentModel;
import com.hwrd22.hwrd22expertmode.entity.projectile.ThrownPoseidonTrident;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ThrownPoseidonTridentRenderer extends EntityRenderer<ThrownPoseidonTrident> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ExpertMode.MODID, "textures/entity/poseidon_trident.png");
    private final ThrownPoseidonTridentModel model;
    public ThrownPoseidonTridentRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ThrownPoseidonTridentModel(context.bakeLayer(ThrownPoseidonTridentModel.LAYER_LOCATION));
    }

    public void render(ThrownPoseidonTrident p_116111_, float p_116112_, float p_116113_, PoseStack p_116114_, @NotNull MultiBufferSource p_116115_, int p_116116_) {
        p_116114_.pushPose();
        p_116114_.mulPose(Axis.YP.rotationDegrees(Mth.lerp(p_116113_, p_116111_.yRotO, p_116111_.getYRot()) - 90.0F));
        p_116114_.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(p_116113_, p_116111_.xRotO, p_116111_.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(p_116115_, this.model.renderType(this.getTextureLocation(p_116111_)), false, p_116111_.isFoil());
        this.model.renderToBuffer(p_116114_, vertexconsumer, p_116116_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        p_116114_.popPose();
        super.render(p_116111_, p_116112_, p_116113_, p_116114_, p_116115_, p_116116_);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ThrownPoseidonTrident p_114482_) {
        return TEXTURE;
    }
}
