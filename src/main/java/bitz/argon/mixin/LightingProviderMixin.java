package bitz.argon.mixin;

import bitz.argon.ArgonClient;
import bitz.argon.config.ArgonConfig;
import net.minecraft.world.chunk.light.LightingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightingProvider.class)
public class LightingProviderMixin {
    
    private int lightingUpdateCounter = 0;
    
    @Inject(method = "doLightUpdates", at = @At("HEAD"), cancellable = true)
    private void reduceLightingUpdates(CallbackInfoReturnable<Integer> cir) {
        try {
            ArgonConfig config = ArgonClient.getConfig();
            if (config != null && config.reduceLightingUpdates) {
                lightingUpdateCounter++;
                // Skip 1 out of 2 lighting updates for better FPS (reduced from 3/4 to prevent dark patches)
                if (lightingUpdateCounter % 2 != 0) {
                    cir.setReturnValue(0);
                }
            }
        } catch (Exception e) {
            // Silently fail
        }
    }
}
