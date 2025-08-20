package cat.katie.createcreativetweaks.mixin.tick;

import cat.katie.createcreativetweaks.features.tick.TickRateManager;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.SleepStatus;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @WrapWithCondition(
            method = "tick",
            at = {
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/server/level/ServerLevel;advanceWeatherCycle()V"
                    ),
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/server/level/ServerLevel;tickTime()V"
                    ),
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/server/level/ServerLevel;runBlockEvents()V"
                    ),
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/server/level/ServerLevel;resetEmptyTime()V"
                    ),
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/level/border/WorldBorder;tick()V"
                    ),
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/entity/raid/Raids;tick()V"
                    ),
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/level/dimension/end/EndDragonFight;tick()V"
                    ),
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/server/level/ServerLevel;tickBlockEntities()V"
                    )
            }
    )
    private boolean dontDoABunchOfStuff(@Coerce Object instance) {
        return TickRateManager.INSTANCE.runsNormally();
    }

    @WrapWithCondition(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/entity/PersistentEntitySectionManager;tick()V"
            )
    )
    private boolean dontUpdateEntitySectionManagerIfDeeplyFrozen(PersistentEntitySectionManager<Entity> instance) {
        return TickRateManager.INSTANCE.runsNormally() || !TickRateManager.INSTANCE.isDeeplyFrozen();
    }

    @WrapWithCondition(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/level/ServerLevel;emptyTime:I",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private boolean dontUpdateEmptyTime(ServerLevel instance, int value) {
        return TickRateManager.INSTANCE.runsNormally();
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/SleepStatus;areEnoughSleeping(I)Z"
            )
    )
    private boolean dontTickSleeping(SleepStatus instance, int requiredSleepPercentage, Operation<Boolean> original) {
        if (TickRateManager.INSTANCE.runsNormally()) {
            return original.call(instance, requiredSleepPercentage);
        }

        return false;
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;isDebug()Z",
                    ordinal = 0
            )
    )
    private boolean dontTickBlocksOrFluids(ServerLevel instance, Operation<Boolean> original) {
        if (TickRateManager.INSTANCE.runsNormally()) {
            return original.call(instance);
        }

        return true; // pretend to be debug to skip block/fluid ticks
    }

    @Inject(
            method = "lambda$tick$6",
            at = @At("HEAD"),
            cancellable = true
    )
    private void dontTickEntities(ProfilerFiller profilerfiller, Entity entity, CallbackInfo ci) {
        if (!TickRateManager.INSTANCE.shouldTickEntity(entity)) {
            ci.cancel();
        }
    }
}
