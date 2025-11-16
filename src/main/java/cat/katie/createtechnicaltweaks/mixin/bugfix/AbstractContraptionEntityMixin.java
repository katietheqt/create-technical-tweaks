package cat.katie.createtechnicaltweaks.mixin.bugfix;


import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AbstractContraptionEntity.class, remap = false)
public class AbstractContraptionEntityMixin {
    @WrapWithCondition(
            method = "saveWithoutId",
            remap = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;setPosRaw(DDD)V"
            )
    )
    private boolean dontMoveEntitiesOnSave(Entity instance, double x, double y, double z) {
        return !AllConfigs.common().dontMoveEntitiesOnSave.get();
    }
}
