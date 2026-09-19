package cat.katie.createtechnicaltweaks.mixin.rendering;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
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

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {
    @Shadow
    @Final
    private Minecraft minecraft;

    protected ClientLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }

    @WrapMethod(method = "levelEvent")
    private void skipCobbleLevelEvents(Player player, int type, BlockPos pos, int data, Operation<Void> original) {
        if (!AllConfigs.client().disableForeignCobbleParticles.get()
                || player == this.minecraft.player
                || type != LevelEvent.PARTICLES_DESTROY_BLOCK
                || data != Block.getId(Blocks.COBBLESTONE.defaultBlockState())
        ) {
            original.call(player, type, pos, data);
        }
    }

    @WrapMethod(method = "destroyBlockProgress")
    private void skipCobbleDestroyProgress(int breakerId, BlockPos pos, int progress, Operation<Void> original) {
        if (!AllConfigs.client().disableForeignCobbleBreakingOverlay.get()
                || breakerId == this.minecraft.player.getId()
                || !this.getBlockState(pos).is(Blocks.COBBLESTONE)
        ) {
            original.call(breakerId, pos, progress);
        }
    }
}
