package bitz.argon.mixin;

import bitz.argon.ArgonClient;
import bitz.argon.config.ArgonConfig;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    
    @Shadow public abstract double getX();
    @Shadow public abstract double getY();
    @Shadow public abstract double getZ();
    
    private int tickSkipCounter = 0;
    
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        ArgonConfig config = ArgonClient.getConfig();
        
        if (config.entityTickingOptimization) {
            Entity entity = (Entity) (Object) this;
            
            // Skip ticking for distant entities, but only for very far entities to avoid choppy animations
            if (entity.getEntityWorld() != null && entity.getEntityWorld().isClient()) {
                var client = net.minecraft.client.MinecraftClient.getInstance();
                if (client != null && client.player != null && client.world != null && entity != client.player) {
                    double distance = client.player.squaredDistanceTo(getX(), getY(), getZ());
                    
                    // Reduced culling to prevent choppy animations - only cull very distant entities
                    if (config.aggressiveEntityCulling) {
                        // Only skip ticks for entities beyond render distance
                        if (distance > 16384.0) { // 128 blocks - beyond typical render distance
                            tickSkipCounter++;
                            if (tickSkipCounter % 4 != 0) { // Tick every 4th tick
                                ci.cancel();
                                return;
                            }
                        } else if (distance > 6400.0) { // 80 blocks
                            tickSkipCounter++;
                            if (tickSkipCounter % 2 != 0) { // Tick every 2nd tick
                                ci.cancel();
                                return;
                            }
                        }
                    } else {
                        // Normal culling - only for extremely distant entities
                        if (distance > 25600.0) { // 160 blocks - way beyond normal render distance
                            tickSkipCounter++;
                            if (tickSkipCounter % 3 != 0) {
                                ci.cancel();
                                return;
                            }
                        }
                    }
                    tickSkipCounter = 0;
                }
            }
        }
    }
}
