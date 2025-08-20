package cat.katie.createcreativetweaks.mixin.tick;

import cat.katie.createcreativetweaks.features.tick.TickRateManager;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @WrapWithCondition(
            method = "tickServer",
            at = {
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraftforge/event/ForgeEventFactory;onPreServerTick(Ljava/util/function/BooleanSupplier;Lnet/minecraft/server/MinecraftServer;)V",
                            remap = false
                    ),
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraftforge/event/ForgeEventFactory;onPostServerTick(Ljava/util/function/BooleanSupplier;Lnet/minecraft/server/MinecraftServer;)V",
                            remap = false
                    )
            }
    )
    private static boolean dontFireServerTickEvents(BooleanSupplier haveTime, MinecraftServer server) {
        return TickRateManager.INSTANCE.runsNormally();
    }

    @WrapWithCondition(
            method = "tickChildren",
            at = {
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraftforge/event/ForgeEventFactory;onPreLevelTick(Lnet/minecraft/world/level/Level;Ljava/util/function/BooleanSupplier;)V",
                            remap = false
                    ),
                    @At(
                            value = "INVOKE",
                            target = "Lnet/minecraftforge/event/ForgeEventFactory;onPostLevelTick(Lnet/minecraft/world/level/Level;Ljava/util/function/BooleanSupplier;)V",
                            remap = false
                    )
            }
    )
    private static boolean dontFireLevelTickEvents(Level level, BooleanSupplier haveTime) {
        return TickRateManager.INSTANCE.runsNormally();
    }

    @WrapWithCondition(
            method = "tickChildren",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/ServerFunctionManager;tick()V"
            )
    )
    private static boolean dontTickFunctions(ServerFunctionManager instance) {
        return TickRateManager.INSTANCE.runsNormally();
    }

    @WrapWithCondition(
            method = "tickChildren",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE_STRING",
                            target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V",
                            args = "ldc=server gui refresh"
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Runnable;run()V",
                    remap = false,
                    id = "server gui refresh",
                    ordinal = 0
            )
    )
    private static boolean dontTickGuis(Runnable instance) {
        return TickRateManager.INSTANCE.runsNormally();
    }

    @Inject(
            method = "tickServer",
            at = @At("TAIL")
    )
    private void onEndOfTick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        TickRateManager.INSTANCE.onTickEnd();
    }
}
