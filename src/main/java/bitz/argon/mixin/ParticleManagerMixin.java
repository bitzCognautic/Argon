package bitz.argon.mixin;

import bitz.argon.ArgonClient;
import bitz.argon.config.ArgonConfig;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    
    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void onAddParticle(Particle particle, CallbackInfo ci) {
        try {
            ArgonConfig config = ArgonClient.getConfig();
            if (config != null && config.disableAllParticles) {
                ci.cancel();
            }
        } catch (Exception e) {
            // Silently fail to avoid crashes
        }
    }
    
    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    private void onAddParticleEffect(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir) {
        try {
            ArgonConfig config = ArgonClient.getConfig();
            if (config == null) {
                return;
            }
            
            // Disable all particles if configured
            if (config.disableAllParticles) {
                cir.setReturnValue(null);
                return;
            }
            
            // Check individual particle type
            if (parameters != null) {
                String particleId = Registries.PARTICLE_TYPE.getId(parameters.getType()).getPath();
                // Convert to uppercase to match config keys
                String particleKey = particleId.toUpperCase();
                
                // Special handling for BLOCK and BLOCK_MARKER particles (block breaking/placing)
                // These use BlockStateParticleEffect which wraps the actual particle type
                if (parameters instanceof BlockStateParticleEffect) {
                    // For block particles, check both BLOCK and BLOCK_MARKER
                    if (particleKey.equals("BLOCK") || particleKey.equals("BLOCK_MARKER")) {
                        if (!config.isParticleEnabled(particleKey)) {
                            cir.setReturnValue(null);
                            return;
                        }
                    }
                }
                
                if (!config.isParticleEnabled(particleKey)) {
                    cir.setReturnValue(null);
                }
            }
        } catch (Exception e) {
            // Silently fail to avoid crashes
        }
    }
}
