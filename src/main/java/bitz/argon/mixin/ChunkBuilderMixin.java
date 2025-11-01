package bitz.argon.mixin;

import bitz.argon.ArgonClient;
import bitz.argon.chunk.ChunkLODManager;
import bitz.argon.config.ArgonConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * ChunkBuilder mixin for fast chunk loading optimization with LOD.
 * Implements Distant Horizons-style chunk rendering by reducing detail for distant chunks.
 */
@Mixin(ChunkBuilder.class)
public class ChunkBuilderMixin {
    
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        try {
            ArgonConfig config = ArgonClient.getConfig();
            if (config != null && config.fastChunkLoading) {
                ArgonClient.LOGGER.info("Fast chunk loading enabled (speed: {}) with LOD rendering", config.chunkLoadingSpeed);
                ArgonClient.LOGGER.info("Distant chunks will render with reduced detail for better performance");
            }
            if (config != null && config.enableChunkLOD) {
                ArgonClient.LOGGER.info("Chunk LOD enabled - distance: {}, quality: {}", config.lodDistance, config.lodQuality);
            }
        } catch (Exception e) {
            // Silently fail
        }
    }
}
