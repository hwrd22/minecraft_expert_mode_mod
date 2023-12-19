package com.hwrd22.hwrd22expertmode.client;

import com.hwrd22.hwrd22expertmode.ExpertMode;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.overlay.ExtendedGui;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;
import org.jetbrains.annotations.NotNull;

public class AdrenalineHudOverlay {
    public static final ResourceLocation FILLED_ADRENALINE = new ResourceLocation(ExpertMode.MODID, "textures/adrenaline/adrenaline_full.png");
    public static final ResourceLocation ADRENALINE_PARTIAL_1 = new ResourceLocation(ExpertMode.MODID, "textures/adrenaline/adrenaline_partial_1.png");
    public static final ResourceLocation ADRENALINE_PARTIAL_2 = new ResourceLocation(ExpertMode.MODID, "textures/adrenaline/adrenaline_partial_2.png");
    public static final ResourceLocation ADRENALINE_PARTIAL_3 = new ResourceLocation(ExpertMode.MODID, "textures/adrenaline/adrenaline_partial_3.png");
    public static final ResourceLocation ADRENALINE_PARTIAL_4 = new ResourceLocation(ExpertMode.MODID, "textures/adrenaline/adrenaline_partial_4.png");
    public static final ResourceLocation ADRENALINE_PARTIAL_5 = new ResourceLocation(ExpertMode.MODID, "textures/adrenaline/adrenaline_partial_5.png");
    public static final ResourceLocation ADRENALINE_PARTIAL_6 = new ResourceLocation(ExpertMode.MODID, "textures/adrenaline/adrenaline_partial_6.png");
    public static final ResourceLocation ADRENALINE_PARTIAL_7 = new ResourceLocation(ExpertMode.MODID, "textures/adrenaline/adrenaline_partial_7.png");
    public static final ResourceLocation ADRENALINE_PARTIAL_8 = new ResourceLocation(ExpertMode.MODID, "textures/adrenaline/adrenaline_partial_8.png");
    public static final ResourceLocation ADRENALINE_PARTIAL_9 = new ResourceLocation(ExpertMode.MODID, "textures/adrenaline/adrenaline_partial_9.png");
    public static final ResourceLocation ADRENALINE_PARTIAL_10 = new ResourceLocation(ExpertMode.MODID, "textures/adrenaline/adrenaline_partial_10.png");
    public static final ResourceLocation ADRENALINE_PARTIAL_11 = new ResourceLocation(ExpertMode.MODID, "textures/adrenaline/adrenaline_partial_11.png");
    public static final ResourceLocation ADRENALINE_PARTIAL_12 = new ResourceLocation(ExpertMode.MODID, "textures/adrenaline/adrenaline_partial_12.png");
    public static final ResourceLocation EMPTY_ADRENALINE = new ResourceLocation(ExpertMode.MODID, "textures/adrenaline/adrenaline_empty.png");

    public static final IGuiOverlay HUD_ADRENALINE = ((gui, poseStack, partialTick, screenWidth, screenHeight) -> {

//        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.setShaderTexture(0, EMPTY_ADRENALINE);
        for (int i = 0; i < 10; i++) {
            poseStack.blit(EMPTY_ADRENALINE, screenWidth - 28 - (i * 12), screenHeight - 16, 0, 0, 12, 12, 12, 12);
        }
//        RenderSystem.setShaderTexture(0, FILLED_ADRENALINE);
        for (int i = 0; i < 10; i++) {
            if ((ClientAdrenalineData.get() / 60) > i) {
                poseStack.blit(FILLED_ADRENALINE, screenWidth - 28 - (i * 12), screenHeight - 16, 0, 0, 12, 12, 12, 12);
            }
            else {
                if ((ClientAdrenalineData.get() - (i * 60)) > 55) {
//                    RenderSystem.setShaderTexture(0, ADRENALINE_PARTIAL_12);
                    poseStack.blit(ADRENALINE_PARTIAL_12, screenWidth - 28 - (i * 12), screenHeight - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientAdrenalineData.get() - (i * 60)) > 50) {
//                    RenderSystem.setShaderTexture(0, ADRENALINE_PARTIAL_11);
                    poseStack.blit(ADRENALINE_PARTIAL_11, screenWidth - 28 - (i * 12), screenHeight - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientAdrenalineData.get() - (i * 60)) > 46) {
//                    RenderSystem.setShaderTexture(0, ADRENALINE_PARTIAL_10);
                    poseStack.blit(ADRENALINE_PARTIAL_10, screenWidth - 28 - (i * 12), screenHeight - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientAdrenalineData.get() - (i * 60)) > 41) {
//                    RenderSystem.setShaderTexture(0, ADRENALINE_PARTIAL_9);
                    poseStack.blit(ADRENALINE_PARTIAL_9, screenWidth - 28 - (i * 12), screenHeight - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientAdrenalineData.get() - (i * 60)) > 36) {
//                    RenderSystem.setShaderTexture(0, ADRENALINE_PARTIAL_8);
                    poseStack.blit(ADRENALINE_PARTIAL_8, screenWidth - 28 - (i * 12), screenHeight - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientAdrenalineData.get() - (i * 60)) > 32) {
//                    RenderSystem.setShaderTexture(0, ADRENALINE_PARTIAL_7);
                    poseStack.blit(ADRENALINE_PARTIAL_7, screenWidth - 28 - (i * 12), screenHeight - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientAdrenalineData.get() - (i * 60)) > 27) {
//                    RenderSystem.setShaderTexture(0, ADRENALINE_PARTIAL_6);
                    poseStack.blit(ADRENALINE_PARTIAL_6, screenWidth - 28 - (i * 12), screenHeight - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientAdrenalineData.get() - (i * 60)) > 23) {
//                    RenderSystem.setShaderTexture(0, ADRENALINE_PARTIAL_5);
                    poseStack.blit(ADRENALINE_PARTIAL_5, screenWidth - 28 - (i * 12), screenHeight - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientAdrenalineData.get() - (i * 60)) > 18) {
//                    RenderSystem.setShaderTexture(0, ADRENALINE_PARTIAL_4);
                    poseStack.blit(ADRENALINE_PARTIAL_4, screenWidth - 28 - (i * 12), screenHeight - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientAdrenalineData.get() - (i * 60) > 13)) {
//                    RenderSystem.setShaderTexture(0, ADRENALINE_PARTIAL_3);
                    poseStack.blit(ADRENALINE_PARTIAL_3, screenWidth - 28 - (i * 12), screenHeight - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientAdrenalineData.get() - (i * 60) > 9)) {
//                    RenderSystem.setShaderTexture(0, ADRENALINE_PARTIAL_2);
                    poseStack.blit(ADRENALINE_PARTIAL_2, screenWidth - 28 - (i * 12), screenHeight - 16, 0, 0, 12, 12, 12, 12);
                }
                else if ((ClientAdrenalineData.get() - (i * 60) > 4)) {
//                    RenderSystem.setShaderTexture(0, ADRENALINE_PARTIAL_1);
                    poseStack.blit(ADRENALINE_PARTIAL_1, screenWidth - 28 - (i * 12), screenHeight - 16, 0, 0, 12, 12, 12, 12);
                }
                break;
            }
        }
    });
}
