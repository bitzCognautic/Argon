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
            
            // Optimize movement ticking for distant entities, but preserve smooth animations
            if (entity.getEntityWorld() != null && entity.getEntityWorld().isClient()) {
                var client = net.minecraft.client.MinecraftClient.getInstance();
                if (client != null && client.player != null && client.world != null && entity != client.player) {
                    double distance = client.player.squaredDistanceTo(entity.getX(), entity.getY(), entity.getZ());
                    
                    // Reduced movement culling to prevent choppy animations
                    if (config.aggressiveEntityCulling) {
                        // Only skip movement for entities beyond render distance
                        if (distance > 16384.0) { // 128 blocks - beyond typical render distance
                            if (entity.age % 4 != 0) { // Update every 4th tick
                                ci.cancel();
                                return;
                            }
                        } else if (distance > 6400.0) { // 80 blocks
                            if (entity.age % 2 != 0) { // Update every 2nd tick
                                ci.cancel();
                                return;
                            }
                        }
                    } else {
                        // Normal culling - only for extremely distant entities
                        if (distance > 25600.0) { // 160 blocks - way beyond normal render distance
                            if (entity.age % 3 != 0) {
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
