package cat.katie.createtechnicaltweaks.mixin;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointHandler;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ArmInteractionPointHandler.class, remap = false)
public class ArmInteractionPointHandlerMixin {
    @Inject(
            method = "rightClickingBlocksSelectsThem",
            cancellable = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/mechanicalArm/ArmInteractionPointHandler;getSelected(Lnet/minecraft/core/BlockPos;)Lcom/simibubi/create/content/kinetics/mechanicalArm/ArmInteractionPoint;"
            )
    )
    private static void dontTriggerOnShiftClick(PlayerInteractEvent.RightClickBlock event, CallbackInfo ci, @Local(name = "player") Player player) {
        if (AllConfigs.client().placeArmsNormallyWhenShifting.get() && player.isShiftKeyDown()) {
            ci.cancel();
        }
    }
}
