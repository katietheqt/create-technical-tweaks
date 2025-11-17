package cat.katie.createtechnicaltweaks.mixin.contraption_order;

import cat.katie.createtechnicaltweaks.features.contraption_debug.ContraptionDebug;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;renderDebug(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/Camera;)V"
            )
    )
    private void onRenderDebug(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera,
                               GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix,
                               Matrix4f projectionMatrix, CallbackInfo ci,
                               @Local MultiBufferSource.BufferSource buffers, @Local PoseStack poseStack,
                               @Local(ordinal = 0) float partialTick
    ) {
        try {
            ContraptionDebug.INSTANCE.renderOverlay(poseStack, buffers, camera, partialTick);
        } catch (Exception e) {
            ContraptionDebug.LOGGER.error("exception thrown during overlay rendering", e);
        }
    }
}
