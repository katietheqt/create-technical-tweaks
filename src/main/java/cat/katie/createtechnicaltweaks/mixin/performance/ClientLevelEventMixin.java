package cat.katie.createtechnicaltweaks.mixin.performance;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelEventMixin extends Level {
    @Shadow
    @Final
    private Minecraft minecraft;

    protected ClientLevelEventMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }

    @Inject(method = "levelEvent", at = @At("HEAD"), cancellable = true)
    private void noCobbleParticles(Player player, int type, BlockPos pos, int data, CallbackInfo ci) {
        if (AllConfigs.client().disableForeignCobbleParticles.get()
                && player != this.minecraft.player
                && type == LevelEvent.PARTICLES_DESTROY_BLOCK
                && data == Block.getId(Blocks.COBBLESTONE.defaultBlockState())
        ) {
            ci.cancel();
        }

    }

    @Inject(method = "destroyBlockProgress", at = @At("HEAD"), cancellable = true)
    private void noCobbleBreakProgress(int breakerId, BlockPos pos, int progress, CallbackInfo ci) {
        assert this.minecraft.player != null;
        if (AllConfigs.client().disableForeignCobbleBreakingIndicator.get()
                && breakerId != this.minecraft.player.getId()
                && this.getBlockState(pos).is(Blocks.COBBLESTONE)
        ) {
            ci.cancel();
        }
    }
}