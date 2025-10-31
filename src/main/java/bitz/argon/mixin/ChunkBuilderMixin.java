package bitz.argon.mixin;

import bitz.argon.ArgonClient;
import bitz.argon.config.ArgonConfig;
import net.minecraft.client.render.chunk.ChunkBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkBuilder.class)
public class ChunkBuilderMixin {
    
    @Shadow private int threadCount;
    
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        try {
            ArgonConfig config = ArgonClient.getConfig();
            if (config != null && config.fastChunkLoading) {
                // Increase thread count based on speed setting
                // Speed 1-10 maps to multiplier 1.0x to 3.0x
                float multiplier = 1.0f + (config.chunkLoadingSpeed / 5.0f);
                this.threadCount = Math.max(1, (int)(this.threadCount * multiplier));
            }
        } catch (Exception e) {
            // Silently fail
        }
    }
}
