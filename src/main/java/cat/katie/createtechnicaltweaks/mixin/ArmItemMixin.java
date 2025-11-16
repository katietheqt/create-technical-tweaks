package cat.katie.createtechnicaltweaks.mixin;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ArmItem.class, remap = false)
public class ArmItemMixin {
    @WrapOperation(
            method = "useOn",
            remap = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/mechanicalArm/ArmInteractionPoint;isInteractable(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
                    remap = false
            )
    )
    private boolean placeArmIfSneaking(Level level, BlockPos pos, BlockState state, Operation<Boolean> original,
                                       @Local(argsOnly = true, name = "arg1") UseOnContext ctx) {
        if (AllConfigs.client().placeArmsNormallyWhenShifting.get() && ctx.getPlayer() != null && ctx.getPlayer().isShiftKeyDown()) {
            return false;
        }

        return original.call(level, pos, state);
    }
}
