package com.hwrd22.hwrd22expertmode.client;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class RageHudOverlay {
    public static final ResourceLocation FILLED_RAGE = new ResourceLocation(ExpertMode.MODID, "textures/rage/rage_full.png");
    public static final ResourceLocation RAGE_PARTIAL_1 = new ResourceLocation(ExpertMode.MODID, "textures/rage/rage_partial_1.png");
    public static final ResourceLocation RAGE_PARTIAL_2 = new ResourceLocation(ExpertMode.MODID, "textures/rage/rage_partial_2.png");
    public static final ResourceLocation RAGE_PARTIAL_3 = new ResourceLocation(ExpertMode.MODID, "textures/rage/rage_partial_3.png");
    public static final ResourceLocation RAGE_PARTIAL_4 = new ResourceLocation(ExpertMode.MODID, "textures/rage/rage_partial_4.png");
    public static final ResourceLocation RAGE_PARTIAL_5 = new ResourceLocation(ExpertMode.MODID, "textures/rage/rage_partial_5.png");
    public static final ResourceLocation RAGE_PARTIAL_6 = new ResourceLocation(ExpertMode.MODID, "textures/rage/rage_partial_6.png");
    public static final ResourceLocation RAGE_PARTIAL_7 = new ResourceLocation(ExpertMode.MODID, "textures/rage/rage_partial_7.png");
    public static final ResourceLocation RAGE_PARTIAL_8 = new ResourceLocation(ExpertMode.MODID, "textures/rage/rage_partial_8.png");
    public static final ResourceLocation RAGE_PARTIAL_9 = new ResourceLocation(ExpertMode.MODID, "textures/rage/rage_partial_9.png");
    public static final ResourceLocation RAGE_PARTIAL_10 = new ResourceLocation(ExpertMode.MODID, "textures/rage/rage_partial_10.png");
    public static final ResourceLocation RAGE_PARTIAL_11 = new ResourceLocation(ExpertMode.MODID, "textures/rage/rage_partial_11.png");
    public static final ResourceLocation RAGE_PARTIAL_12 = new ResourceLocation(ExpertMode.MODID, "textures/rage/rage_partial_12.png");
    public static final ResourceLocation EMPTY_RAGE = new ResourceLocation(ExpertMode.MODID, "textures/rage/rage_empty.png");

    public static final IGuiOverlay HUD_RAGE = ((gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        int y = screenHeight;

        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, EMPTY_RAGE);
        for (int i = 0; i < 10; i++) {
            GuiComponent.blit(poseStack, 16 + (i * 12), y - 16, 0, 0, 12, 12, 12, 12);
        }
        RenderSystem.setShaderTexture(0, FILLED_RAGE);
        for (int i = 0; i < 10; i++) {
            if ((ClientRageData.get() / 1000) > i) {
                GuiComponent.blit(poseStack, 16 + (i * 12), y - 16, 0, 0, 12, 12, 12, 12);
            }
            else {
                if ((ClientRageData.get() - (i * 1000)) > 923) {
                    RenderSystem.setShaderTexture(0, RAGE_PARTIAL_12);
                    GuiComponent.blit(poseStack, 16 + (i * 12), y - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientRageData.get() - (i * 1000)) > 846) {
                    RenderSystem.setShaderTexture(0, RAGE_PARTIAL_11);
                    GuiComponent.blit(poseStack, 16 + (i * 12), y - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientRageData.get() - (i * 1000)) > 769) {
                    RenderSystem.setShaderTexture(0, RAGE_PARTIAL_10);
                    GuiComponent.blit(poseStack, 16 + (i * 12), y - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientRageData.get() - (i * 1000)) > 692) {
                    RenderSystem.setShaderTexture(0, RAGE_PARTIAL_9);
                    GuiComponent.blit(poseStack, 16 + (i * 12), y - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientRageData.get() - (i * 1000)) > 615) {
                    RenderSystem.setShaderTexture(0, RAGE_PARTIAL_8);
                    GuiComponent.blit(poseStack, 16 + (i * 12), y - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientRageData.get() - (i * 1000)) > 538) {
                    RenderSystem.setShaderTexture(0, RAGE_PARTIAL_7);
                    GuiComponent.blit(poseStack, 16 + (i * 12), y - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientRageData.get() - (i * 1000)) > 461) {
                    RenderSystem.setShaderTexture(0, RAGE_PARTIAL_6);
                    GuiComponent.blit(poseStack, 16 + (i * 12), y - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientRageData.get() - (i * 1000)) > 384) {
                    RenderSystem.setShaderTexture(0, RAGE_PARTIAL_5);
                    GuiComponent.blit(poseStack, 16 + (i * 12), y - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientRageData.get() - (i * 1000)) > 307) {
                    RenderSystem.setShaderTexture(0, RAGE_PARTIAL_4);
                    GuiComponent.blit(poseStack, 16 + (i * 12), y - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientRageData.get() - (i * 1000) > 230)) {
                    RenderSystem.setShaderTexture(0, RAGE_PARTIAL_3);
                    GuiComponent.blit(poseStack, 16 + (i * 12), y - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientRageData.get() - (i * 1000) > 153)) {
                    RenderSystem.setShaderTexture(0, RAGE_PARTIAL_2);
                    GuiComponent.blit(poseStack, 16 + (i * 12), y - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientRageData.get() - (i * 1000) > 76)) {
                    RenderSystem.setShaderTexture(0, RAGE_PARTIAL_1);
                    GuiComponent.blit(poseStack, 16 + (i * 12), y - 16, 0, 0, 12, 12, 12, 12);
                }
                break;
            }
        }
    });
}
