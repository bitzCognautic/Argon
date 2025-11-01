package bitz.argon.mixin;

import bitz.argon.ArgonClient;
import bitz.argon.config.ArgonConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    
    @Inject(method = "spawnBreakParticles", at = @At("HEAD"), cancellable = true)
    private static void onSpawnBreakParticles(World world, PlayerEntity player, BlockPos pos, BlockState state, CallbackInfo ci) {
        try {
            ArgonConfig config = ArgonClient.getConfig();
            if (config != null && (!config.isParticleEnabled("BLOCK") || config.disableAllParticles)) {
                ci.cancel();
            }
        } catch (Exception e) {
            // Silently fail
        }
    }
}
