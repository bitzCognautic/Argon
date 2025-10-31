package bitz.argon.mixin;

import bitz.argon.ArgonClient;
import bitz.argon.config.ArgonConfig;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    
    @Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
    private void onTickMovement(CallbackInfo ci) {
        ArgonConfig config = ArgonClient.getConfig();
        
        if (config.entityTickingOptimization) {
            LivingEntity entity = (LivingEntity) (Object) this;
            
            // Optimize movement ticking for distant entities
            if (entity.getEntityWorld() != null && entity.getEntityWorld().isClient()) {
                var client = net.minecraft.client.MinecraftClient.getInstance();
                if (client != null && client.player != null && client.world != null && entity != client.player) {
                    double distance = client.player.squaredDistanceTo(entity.getX(), entity.getY(), entity.getZ());
                    
                    // More aggressive movement culling for better FPS
                    if (config.aggressiveEntityCulling) {
                        // Very aggressive - skip most distant entity movement
                        if (distance > 1024.0) { // 32 blocks
                            if (entity.age % 10 != 0) { // Update every 10th tick
                                ci.cancel();
                                return;
                            }
                        } else if (distance > 256.0) { // 16 blocks
                            if (entity.age % 5 != 0) { // Update every 5th tick
                                ci.cancel();
                                return;
                            }
                        }
                    } else {
                        // Normal culling
                        if (distance > 6400.0) { // 80 blocks
                            if (entity.age % 5 != 0) {
                                ci.cancel();
                                return;
                            }
                        } else if (distance > 3200.0) { // 56 blocks
                            if (entity.age % 3 != 0) { // Update every 3rd tick
                                ci.cancel();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
