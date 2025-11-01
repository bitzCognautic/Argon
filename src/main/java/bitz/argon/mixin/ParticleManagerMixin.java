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

@Mixin(value = ParticleManager.class, priority = 999)
public class ParticleManagerMixin {
    
    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void onAddParticle(Particle particle, CallbackInfo ci) {
        try {
            ArgonConfig config = ArgonClient.getConfig();
            if (config != null && config.disableAllParticles) {
                ci.cancel();
                return;
            }
            
            // Check for block particles by examining particle class name
            if (particle != null && config != null) {
                String className = particle.getClass().getSimpleName().toLowerCase();
                if (className.contains("block") && !config.isParticleEnabled("BLOCK")) {
                    ci.cancel();
                }
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
                
                // Special handling for BlockStateParticleEffect (block breaking/cracking particles)
                if (parameters instanceof BlockStateParticleEffect) {
                    // Use "BLOCK" key for all block-based particles (breaking, falling, etc.)
                    particleKey = "BLOCK";
                }
                
                // Check if this particle type is enabled in config
                if (!config.isParticleEnabled(particleKey)) {
                    cir.setReturnValue(null);
                    return;
                }
            }
        } catch (Exception e) {
            // Silently fail to avoid crashes
        }
    }
}
