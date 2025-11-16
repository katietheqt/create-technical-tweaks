package cat.katie.createtechnicaltweaks.mixin.limits;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.contraptions.glue.SuperGlueSelectionPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = SuperGlueSelectionPacket.class, remap = false)
public class SuperGlueSelectionPacketMixin {
    @WrapOperation(
            method = "handle",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/BlockPos;closerThan(Lnet/minecraft/core/Vec3i;D)Z",
                    remap = true
            )
    )
    private boolean modifyMaxGlueBoxRange(BlockPos instance, Vec3i vec3i, double v, Operation<Boolean> original) {
        if (AllConfigs.server().unlimitedGlueBoxes.get()) {
            return true;
        }

        return original.call(instance, vec3i, v);
    }
}
