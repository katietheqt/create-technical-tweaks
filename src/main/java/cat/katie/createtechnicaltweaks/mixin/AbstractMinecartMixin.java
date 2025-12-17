package cat.katie.createtechnicaltweaks.mixin;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends VehicleEntity {
    public AbstractMinecartMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/Level;isClientSide:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean forceServersideBehaviour(boolean original) {
        if (AllConfigs.client().accurateClientMinecarts.get()) {
            return false;
        }

        return original;
    }

    @Inject(
            method = "lerpTo",
            at = @At("HEAD"),
            cancellable = true
    )
    private void disableInterpolation(double x, double y, double z, float yRot, float xRot, int steps, CallbackInfo ci) {
        if (AllConfigs.client().disableMinecartInterpolation.get()) {
            super.lerpTo(x, y, z, yRot, xRot, steps);
            ci.cancel();
        }
    }
}
