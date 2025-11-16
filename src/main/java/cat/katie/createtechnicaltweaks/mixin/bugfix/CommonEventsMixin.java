package cat.katie.createtechnicaltweaks.mixin.bugfix;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.simibubi.create.foundation.events.CommonEvents;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CommonEvents.class, remap = false)
public class CommonEventsMixin {
    @Inject(
            method = "onServerWorldTick",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void dontDoTickingIfFrozen(LevelTickEvent.Post event, CallbackInfo ci) {
        if (!AllConfigs.common().dontDoTickingIfFrozen.get()) {
            return;
        }

        if (!event.getLevel().tickRateManager().runsNormally()) {
            ci.cancel();
        }
    }
}
