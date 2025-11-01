package bitz.argon;

import bitz.argon.config.ArgonConfig;
import bitz.argon.util.ZoomManager;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArgonClient implements ClientModInitializer {
    public static final String MOD_ID = "argon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    private static ArgonConfig config;
    
    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Argon Performance Mod");
        
        // Load configuration
        config = ArgonConfig.load();
        
        // Initialize zoom feature
        ZoomManager.getInstance().initialize();
        
        LOGGER.info("Argon initialized successfully");
        LOGGER.info("Entity Ticking Optimization: {}", config.entityTickingOptimization);
        LOGGER.info("Aggressive Entity Culling: {}", config.aggressiveEntityCulling);
        LOGGER.info("Chunk Update Optimization: {}", config.chunkUpdateOptimization);
        LOGGER.info("Reduce Lighting Updates: {}", config.reduceLightingUpdates);
        LOGGER.info("Chunk LOD Enabled: {}", config.enableChunkLOD);
        LOGGER.info("Zoom Feature Enabled: {}", config.enableZoom);
    }
    
    public static ArgonConfig getConfig() {
        if (config == null) {
            config = ArgonConfig.load();
        }
        return config;
    }
}
