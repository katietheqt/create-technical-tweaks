package cat.katie.createtechnicaltweaks.mixin.bugfix;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.simibubi.create.foundation.events.ClientEvents;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ClientEvents.class, remap = false)
public class ClientEventsMixin {
    @WrapWithCondition(
            method = "onTick",
            at = {
                    @At(
                            value = "INVOKE",
                            target = "Lcom/simibubi/create/content/contraptions/ContraptionHandler;tick(Lnet/minecraft/world/level/Level;)V"
                    ),
                    @At(
                            value = "INVOKE",
                            target = "Lcom/simibubi/create/content/contraptions/minecart/CouplingPhysics;tick(Lnet/minecraft/world/level/Level;)V"
                    )
            }
    )
    private static boolean dontDoTickingIfFrozen(Level level) {
        return !AllConfigs.common().dontDoTickingIfFrozen.get() || level.tickRateManager().runsNormally();
    }
}
