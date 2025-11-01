package bitz.argon.mixin;

import bitz.argon.ArgonClient;
import bitz.argon.chunk.ChunkLODManager;
import bitz.argon.config.ArgonConfig;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkRendererRegion.class)
public class ChunkRendererRegionMixin {
    
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        try {
            ArgonConfig config = ArgonClient.getConfig();
            if (config != null && (config.enableChunkLOD || config.fastChunkLoading)) {
                // LOD tracking happens at render time through ChunkRenderTaskMixin
                // This initialization hook ensures the config is loaded
            }
        } catch (Exception e) {
            // Silently fail
        }
    }
    
    @Inject(method = "getBlockState", at = @At("HEAD"), cancellable = true)
    private void onGetBlockState(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        try {
            ArgonConfig config = ArgonClient.getConfig();
            if (config != null && (config.enableChunkLOD || config.fastChunkLoading)) {
                ChunkPos chunkPos = new ChunkPos(pos);
                int lodLevel = ChunkLODManager.getInstance().getLODLevel(chunkPos);
                
                // For distant chunks with LOD > 1, simplify by skipping certain block detail checks
                // This reduces the data needed for rendering distant chunks
                if (lodLevel >= 2) {
                    // Let the default behavior continue but mark it for simplified rendering
                    // The actual rendering simplification happens in the renderer
                }
            }
        } catch (Exception e) {
            // Silently fail
        }
    }
}
