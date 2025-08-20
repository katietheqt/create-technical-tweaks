package cat.katie.createcreativetweaks.mixin.tick;

import cat.katie.createcreativetweaks.features.tick.TickRateManager;
import cat.katie.createcreativetweaks.mixin.accessors.ChunkMapAccessor;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Mixin(ServerChunkCache.class)
public class ServerChunkCacheMixin {
    @Shadow @Final public ChunkMap chunkMap;

    @Shadow @Final public ServerLevel level;

    @Inject(
            method = "tickChunks",
            at = @At("HEAD"),
            cancellable = true
    )
    private void dontTickChunks(CallbackInfo ci) {
        if (TickRateManager.INSTANCE.runsNormally()) {
            return;
        }

        List<ChunkHolder> chunks = Lists.newArrayList(((ChunkMapAccessor)this.chunkMap).cct$getChunks());
        Collections.shuffle(chunks);

        this.level.getProfiler().push("broadcast");
        for (ChunkHolder holder : chunks) {
            Optional<LevelChunk> optional = holder.getTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK).left();

            if (optional.isPresent()) {
                holder.broadcastChanges(optional.get());
            }
        }
        this.level.getProfiler().pop();

        ci.cancel();
    }
}
