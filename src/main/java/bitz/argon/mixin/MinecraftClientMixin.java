package bitz.argon.mixin;

import bitz.argon.ArgonClient;
import bitz.argon.chunk.ChunkLODManager;
import bitz.argon.config.ArgonConfig;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    
    private int memoryCleanupCounter = 0;
    private int lodUpdateCounter = 0;
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ArgonConfig config = ArgonClient.getConfig();
        
        // Memory optimization - run GC periodically
        if (config.memoryOptimization) {
            memoryCleanupCounter++;
            if (memoryCleanupCounter >= 6000) { // Every 5 minutes (6000 ticks)
                System.gc();
                memoryCleanupCounter = 0;
            }
        }
        
        // Update chunk LOD levels periodically
        if (config.enableChunkLOD) {
            lodUpdateCounter++;
            if (lodUpdateCounter >= 20) { // Every second (20 ticks)
                ChunkLODManager.getInstance().updateAllLODLevels();
                lodUpdateCounter = 0;
            }
        }
    }
}
