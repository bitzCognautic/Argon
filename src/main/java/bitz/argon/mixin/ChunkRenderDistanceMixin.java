package bitz.argon.mixin;

import bitz.argon.ArgonClient;
import bitz.argon.chunk.ChunkLODManager;
import bitz.argon.config.ArgonConfig;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkBuilder.class)
public class ChunkRenderDistanceMixin {
    
    @Inject(method = "rebuild", at = @At("HEAD"))
    private void onRebuild(CallbackInfo ci) {
        try {
            ArgonConfig config = ArgonClient.getConfig();
            if (config != null && config.enableChunkLOD) {
                // Update LOD levels periodically during chunk rebuilds
                ChunkLODManager.getInstance().updateAllLODLevels();
            }
        } catch (Exception e) {
            // Silently fail to prevent crashes
        }
    }
}
