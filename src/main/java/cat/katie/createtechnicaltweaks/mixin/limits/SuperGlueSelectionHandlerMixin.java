package cat.katie.createtechnicaltweaks.mixin.limits;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import cat.katie.createtechnicaltweaks.util.CTTLang;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.content.contraptions.glue.SuperGlueSelectionHandler;
import net.createmod.catnip.outliner.Outline;
import net.createmod.catnip.platform.services.NetworkHelper;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SuperGlueSelectionHandler.class, remap = false)
public class SuperGlueSelectionHandlerMixin {
    @Shadow private SuperGlueEntity selected;
    @Shadow @Final private static int FAIL;
    @Shadow @Final private static int HIGHLIGHT;
    @Unique
    private double ctt$selectedRange = 0d;

    @Unique
    private double ctt$trueMaxRange;

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;",
                    remap = true
            )
    )
    private AABB increaseGlueSearchRange(AABB instance, double x, double y, double z, Operation<AABB> original) {
        double bonus = 0;

        if (AllConfigs.client().extraGluePunchingRange.get()) {
            bonus = 30d;
        }

        if (AllConfigs.client().alwaysShowHoveredGlue.get()) {
            bonus = 1000d;
        }

        return original.call(instance, x + bonus, y + bonus, z + bonus);
    }

    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getAttributeValue(Lnet/minecraft/core/Holder;)D",
                    remap = true
            )
    )
    private double increaseGlueTraceRange(double range) {
        if (AllConfigs.client().extraGluePunchingRange.get()) {
            range = Math.max(range, 31d);
        }

        ctt$trueMaxRange = range;

        if (AllConfigs.client().alwaysShowHoveredGlue.get()) {
            range = Math.max(range, 1001d);
        }

        return range;
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;distanceToSqr(Lnet/minecraft/world/phys/Vec3;)D",
                    remap = true
            )
    )
    private double rememberGlueRangeLocally(Vec3 instance, Vec3 vec, Operation<Double> original,
                                     @Share("squareDistance") LocalDoubleRef squareDistanceRef) {
        double squareDistance = original.call(instance, vec);
        squareDistanceRef.set(squareDistance);
        return squareDistance;
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/simibubi/create/content/contraptions/glue/SuperGlueSelectionHandler;selected:Lcom/simibubi/create/content/contraptions/glue/SuperGlueEntity;",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void rememberGlueRangeInField(CallbackInfo ci, @Share("squareDistance") LocalDoubleRef squareDistanceRef) {
        ctt$selectedRange = Math.sqrt(squareDistanceRef.get());
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/BlockPos;closerThan(Lnet/minecraft/core/Vec3i;D)Z"
            )
    )
    private boolean allowUnlimitedGlueBoxes(BlockPos instance, Vec3i vec3i, double v, Operation<Boolean> original) {
        if (AllConfigs.server().unlimitedGlueBoxes.get()) {
            return true;
        }

        return original.call(instance, vec3i, v);
    }

    @Unique
    private boolean ctt$glueBoxTooFarToAttack(LocalPlayer player) {
        return selected.distanceTo(player) > 32d || ctt$selectedRange > ctt$trueMaxRange;
    }

    @WrapWithCondition(
            method = "onMouseInput",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/createmod/catnip/platform/services/NetworkHelper;sendToServer(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;)V",
                    ordinal = 0
            )
    )
    private boolean recheckGlueRangeBeforeSending(NetworkHelper instance, CustomPacketPayload customPacketPayload, @Local(name = "player") LocalPlayer player) {
        return selected != null && !ctt$glueBoxTooFarToAttack(player);
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Minecraft;hitResult:Lnet/minecraft/world/phys/HitResult;",
                    opcode = Opcodes.GETFIELD,
                    remap = true
            )
    )
    private void showTooFarToPunchMessage(CallbackInfo ci, @Local(name = "player") LocalPlayer player) {
        // server won't let you punch glue boxes further than 32 blocks away
        if (selected != null && ctt$glueBoxTooFarToAttack(player)) {
            CTTLang.translate("super_glue.too_far_to_punch")
                    .color(FAIL)
                    .sendStatus(player);
        }
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/createmod/catnip/outliner/Outline$OutlineParams;colored(I)Lnet/createmod/catnip/outliner/Outline$OutlineParams;",
                    ordinal = 0
            )
    )
    private Outline.OutlineParams showTooFarBoxesInADifferentColor(Outline.OutlineParams instance, int color,
                                                                   Operation<Outline.OutlineParams> original,
                                                                   @Local(name = "player") LocalPlayer player) {
        if (color == HIGHLIGHT /* only true if current box == selected */ && ctt$glueBoxTooFarToAttack(player)) {
            color = FAIL;
        }

        return original.call(instance, color);
    }
}
