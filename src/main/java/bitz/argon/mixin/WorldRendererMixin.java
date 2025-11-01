package bitz.argon.mixin;

import bitz.argon.ArgonClient;
import bitz.argon.chunk.ChunkLODManager;
import bitz.argon.config.ArgonConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    
    @Inject(method = "scheduleTerrainUpdate", at = @At("HEAD"), cancellable = true)
    private void optimizeTerrainUpdates(CallbackInfo ci) {
        ArgonConfig config = ArgonClient.getConfig();
        
        // Fast chunk loading takes priority - don't throttle if enabled
        if (config.fastChunkLoading) {
            // Don't cancel - allow all terrain updates for faster loading
            return;
        }
        
        if (config.chunkUpdateOptimization) {
            // More aggressive chunk update throttling for better FPS
            // Skip 2 out of 3 terrain updates
            if (System.currentTimeMillis() % 3 != 0) {
                ci.cancel();
                return;
            }
        }
        
        // Apply LOD-based update throttling
        if (config.enableChunkLOD) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.player != null) {
                // Additional throttling for LOD - skip even more updates
                if (System.currentTimeMillis() % 5 != 0) {
                    ci.cancel();
                }
            }
        }
    }
}
