package bitz.argon.chunk;

import bitz.argon.ArgonClient;
import bitz.argon.config.ArgonConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages Level of Detail (LOD) for chunks based on distance from player.
 * Distant chunks are rendered with reduced quality to improve performance.
 */
public class ChunkLODManager {
    private static final ChunkLODManager INSTANCE = new ChunkLODManager();
    
    // Track LOD level for each chunk (0 = full quality, 1-4 = reduced quality)
    private final Map<ChunkPos, Integer> chunkLODLevels = new ConcurrentHashMap<>();
    
    private ChunkLODManager() {}
    
    public static ChunkLODManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * Calculate the LOD level for a chunk based on distance from player
     * @param chunkPos The chunk position
     * @return LOD level (0 = full quality, 1+ = reduced quality)
     */
    public int calculateLODLevel(ChunkPos chunkPos) {
        ArgonConfig config = ArgonClient.getConfig();
        if (!config.enableChunkLOD) {
            return 0; // Full quality if LOD is disabled
        }
        
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return 0;
        }
        
        BlockPos playerPos = client.player.getBlockPos();
        ChunkPos playerChunk = new ChunkPos(playerPos);
        
        // Calculate distance in chunks
        int dx = Math.abs(chunkPos.x - playerChunk.x);
        int dz = Math.abs(chunkPos.z - playerChunk.z);
        int distance = Math.max(dx, dz);
        
        // Determine LOD level based on distance
        if (distance < config.lodDistance / 2) {
            return 0; // Full quality for nearby chunks
        } else if (distance < config.lodDistance) {
            return 1; // Medium quality
        } else if (distance < config.lodDistance * 1.5) {
            return Math.min(2, config.lodQuality); // Lower quality
        } else {
            return Math.min(3, config.lodQuality); // Lowest quality for very distant chunks
        }
    }
    
    /**
     * Get the current LOD level for a chunk
     */
    public int getLODLevel(ChunkPos chunkPos) {
        return chunkLODLevels.getOrDefault(chunkPos, 0);
    }
    
    /**
     * Update LOD level for a chunk
     */
    public void updateLODLevel(ChunkPos chunkPos, int lodLevel) {
        chunkLODLevels.put(chunkPos, lodLevel);
    }
    
    /**
     * Update all chunk LOD levels based on current player position
     */
    public void updateAllLODLevels() {
        ArgonConfig config = ArgonClient.getConfig();
        if (!config.enableChunkLOD) {
            chunkLODLevels.clear();
            return;
        }
        
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) {
            return;
        }
        
        // Update LOD levels for all tracked chunks
        chunkLODLevels.keySet().forEach(chunkPos -> {
            int newLevel = calculateLODLevel(chunkPos);
            chunkLODLevels.put(chunkPos, newLevel);
        });
    }
    
    /**
     * Remove a chunk from tracking
     */
    public void removeChunk(ChunkPos chunkPos) {
        chunkLODLevels.remove(chunkPos);
    }
    
    /**
     * Clear all tracked chunks
     */
    public void clear() {
        chunkLODLevels.clear();
    }
    
    /**
     * Check if a chunk should use simplified rendering
     */
    public boolean shouldUseSimplifiedRendering(ChunkPos chunkPos) {
        return getLODLevel(chunkPos) > 0;
    }
    
    /**
     * Get the render distance multiplier based on LOD level
     * Higher LOD = lower render distance for that chunk
     */
    public float getRenderDistanceMultiplier(int lodLevel) {
        switch (lodLevel) {
            case 0: return 1.0f;  // Full quality
            case 1: return 0.75f; // 75% render distance
            case 2: return 0.5f;  // 50% render distance
            case 3: return 0.25f; // 25% render distance
            default: return 1.0f;
        }
    }
}
