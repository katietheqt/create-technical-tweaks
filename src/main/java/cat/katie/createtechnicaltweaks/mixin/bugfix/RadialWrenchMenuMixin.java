package cat.katie.createtechnicaltweaks.mixin.bugfix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.contraptions.wrench.RadialWrenchMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = RadialWrenchMenu.class, remap = false)
public class RadialWrenchMenuMixin {
    // radial wrench menu assumes all rendered blocks have block entities, causes NPEs in rendering

    @WrapOperation(
            method = "renderRadialSectors",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/BlockEntity;getLevel()Lnet/minecraft/world/level/Level;",
                    remap = true
            )
    )
    private Level nullCheckBlockEntity(BlockEntity instance, Operation<Level> original) {
        if (instance != null) {
            return original.call(instance);
        }

        return null;
    }

    @WrapOperation(
            method = "renderRadialSectors",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/BlockEntity;setLevel(Lnet/minecraft/world/level/Level;)V",
                    remap = true
            )
    )
    private void nullCheckBlockEntity(BlockEntity instance, Level level, Operation<Void> original) {
        if (instance != null) {
            original.call(instance, level);
        }
    }
}
