package cat.katie.createtechnicaltweaks.mixin;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {
    @Shadow
    @Final
    private Entity entity;

    @Shadow
    @Final
    private ServerLevel level;

    @ModifyExpressionValue(
            method = "sendChanges",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/Entity;hasImpulse:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean alwaysSyncMinecarts(boolean original) {
        if (this.level.isClientSide || !AllConfigs.server().syncMinecartsEveryTick.get()) {
            return original;
        }

        return this.entity instanceof AbstractMinecart;
    }

    @ModifyExpressionValue(
            method = "sendChanges",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/phys/Vec3;lengthSqr()D"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/network/protocol/game/VecDeltaCodec;encodeX(Lnet/minecraft/world/phys/Vec3;)J"
                    )
            ),
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/level/ServerEntity;tickCount:I",
                    opcode = Opcodes.GETFIELD
            )
    )
    private int alwaysSyncMinecartPosition(int original) {
        if (this.level.isClientSide) {
            return original;
        }

        if (AllConfigs.server().syncMinecartsEveryTick.get() && entity instanceof AbstractMinecart) {
            return 0;
        }

        return original;
    }
}
