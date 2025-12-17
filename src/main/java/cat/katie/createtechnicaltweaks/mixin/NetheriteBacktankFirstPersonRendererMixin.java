package cat.katie.createtechnicaltweaks.mixin;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.simibubi.create.content.equipment.armor.NetheriteBacktankFirstPersonRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetheriteBacktankFirstPersonRenderer.class)
public class NetheriteBacktankFirstPersonRendererMixin {
    @Shadow
    private static boolean rendererActive;

    @Inject(
            method = "clientTick",
            at = @At("TAIL")
    )
    private static void disableArmOverlay(CallbackInfo ci) {
        if (!AllConfigs.client().netheriteBacktankArm.get()) {
            rendererActive = false;
        }
    }
}
