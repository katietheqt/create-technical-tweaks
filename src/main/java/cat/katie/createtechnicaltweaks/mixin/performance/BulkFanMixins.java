package cat.katie.createtechnicaltweaks.mixin.performance;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("unused")
public class BulkFanMixins {
    @Mixin(AllFanProcessingTypes.BlastingType.class)
    public static class NoBlastingSmokeParticles {
        @WrapMethod(method = "spawnProcessingParticles")
        public void spawnProcessingParticles(Level level, Vec3 pos, Operation<Void> original) {
            if (AllConfigs.client().bulkProcessingParticles.get())
                original.call(level, pos);
        }
    }

    @Mixin(AllFanProcessingTypes.HauntingType.class)
    public static class NoHauntingSmokeParticles {
        @WrapMethod(method = "spawnProcessingParticles")
        public void spawnProcessingParticles(Level level, Vec3 pos, Operation<Void> original) {
            if (AllConfigs.client().bulkProcessingParticles.get())
                original.call(level, pos);
        }
    }

    @Mixin(AllFanProcessingTypes.SmokingType.class)
    public static class NoSmokingSmokeParticles {
        @WrapMethod(method = "spawnProcessingParticles")
        public void spawnProcessingParticles(Level level, Vec3 pos, Operation<Void> original) {
            if (AllConfigs.client().bulkProcessingParticles.get())
                original.call(level, pos);
        }
    }

    @Mixin(AllFanProcessingTypes.SplashingType.class)
    public static class NoWashingSmokeParticles {
        @WrapMethod(method = "spawnProcessingParticles")
        public void spawnProcessingParticles(Level level, Vec3 pos, Operation<Void> original) {
            if (AllConfigs.client().bulkProcessingParticles.get())
                original.call(level, pos);
        }
    }
}
