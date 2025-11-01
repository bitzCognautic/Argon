package bitz.argon.mixin;

import bitz.argon.ArgonClient;
import bitz.argon.config.ArgonConfig;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    
    @Inject(method = "updateBlockBreakingProgress", at = @At("HEAD"), cancellable = true)
    private void onUpdateBlockBreakingProgress(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        try {
            ArgonConfig config = ArgonClient.getConfig();
            // If block particles are disabled, we still allow the breaking progress
            // The particle spawning itself is handled by ParticleManagerMixin
        } catch (Exception e) {
            // Silently fail
        }
    }
}
