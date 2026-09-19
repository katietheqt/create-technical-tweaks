package cat.katie.createtechnicaltweaks.mixin;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContraptionEntity.class)
public abstract class AbstractContraptionEntityMixin extends Entity {
    public AbstractContraptionEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "setPos",
            require = 0, // Incompatibility with Aeronautics
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;setPos(DDD)V",
                    shift = At.Shift.AFTER
            )
    )
    private void disableContraptionInterpolation(double x, double y, double z, CallbackInfo ci) {
        if (level().isClientSide && AllConfigs.client().disableContraptionInterpolation.get()) {
            setOldPosAndRot();
        }
    }
}
