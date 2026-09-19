package cat.katie.createtechnicaltweaks.mixin.rendering;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AirCurrent.class)
public class AirCurrentMixin {
    @WrapWithCondition(
            method = { "tickAffectedEntities", "lambda$tickAffectedHandlers$0" },
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType;spawnProcessingParticles(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/Vec3;)V")
    )
    private boolean dontSpawnEntityProcessingParticles(FanProcessingType instance, Level level, Vec3 vec3) {
        return !AllConfigs.client().disableProcessingParticles.get();
    }
}
