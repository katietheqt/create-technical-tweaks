package cat.katie.createtechnicaltweaks.mixin;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
    @WrapWithCondition(
            method = {"teleportTo(DDD)V", "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FF)Z"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;teleportPassengers()V"
            )
    )
    private boolean dontTeleportPassengers(Entity instance) {
        assert AllConfigs.server().specification != null;
        if (!AllConfigs.server().specification.isLoaded()) {
            return true;
        }

        return !AllConfigs.server().teleportNoPassengers.get();
    }
}
