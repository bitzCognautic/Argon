package bitz.argon.mixin;

import bitz.argon.ArgonClient;
import bitz.argon.chunk.ChunkLODManager;
import bitz.argon.config.ArgonConfig;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkRendererRegion.class)
public class ChunkRendererRegionMixin {
    
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        try {
            ArgonConfig config = ArgonClient.getConfig();
            if (config != null && config.enableChunkLOD) {
                // Track this chunk region for LOD management
                ChunkRendererRegion region = (ChunkRendererRegion) (Object) this;
                // LOD tracking will be handled by ChunkLODManager
            }
        } catch (Exception e) {
            // Silently fail
        }
    }
}
