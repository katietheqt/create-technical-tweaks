package cat.katie.createtechnicaltweaks.mixin.bugfix;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.world.TickRateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(KineticBlockEntity.class)
public class KineticBlockEntityMixin {
    @Redirect(
            method = "getSpeed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/TickRateManager;isFrozen()Z"
            )
    )
    private boolean kineticsShouldWorkWhileStepping(TickRateManager instance) {
        return !instance.runsNormally();
    }
}
