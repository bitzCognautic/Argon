package bitz.argon.util;

import bitz.argon.ArgonClient;
import net.fabricmc.loader.api.FabricLoader;

public class CompatibilityHelper {
    
    private static Boolean sodiumLoaded = null;
    private static Boolean lithiumLoaded = null;
    private static Boolean irisLoaded = null;
    private static Boolean starlightLoaded = null;
    
    public static boolean isSodiumLoaded() {
        if (sodiumLoaded == null) {
            sodiumLoaded = FabricLoader.getInstance().isModLoaded("sodium");
            if (sodiumLoaded) {
                ArgonClient.LOGGER.info("Sodium detected - enabling compatibility mode");
            }
        }
        return sodiumLoaded;
    }
    
    public static boolean isLithiumLoaded() {
        if (lithiumLoaded == null) {
            lithiumLoaded = FabricLoader.getInstance().isModLoaded("lithium");
            if (lithiumLoaded) {
                ArgonClient.LOGGER.info("Lithium detected - enabling compatibility mode");
            }
        }
        return lithiumLoaded;
    }
    
    public static boolean isIrisLoaded() {
        if (irisLoaded == null) {
            irisLoaded = FabricLoader.getInstance().isModLoaded("iris");
            if (irisLoaded) {
                ArgonClient.LOGGER.info("Iris detected - enabling compatibility mode");
            }
        }
        return irisLoaded;
    }
    
    public static boolean isStarlightLoaded() {
        if (starlightLoaded == null) {
            starlightLoaded = FabricLoader.getInstance().isModLoaded("starlight");
            if (starlightLoaded) {
                ArgonClient.LOGGER.info("Starlight detected - enabling compatibility mode");
            }
        }
        return starlightLoaded;
    }
    
    public static boolean shouldApplyOptimization(String optimizationType) {
        // Check if we should skip certain optimizations due to mod conflicts
        switch (optimizationType) {
            case "chunk_rendering":
                return !isSodiumLoaded(); // Sodium handles this better
            case "entity_culling":
                return !isSodiumLoaded(); // Sodium has its own entity culling
            case "lighting":
                return !isStarlightLoaded(); // Starlight handles lighting
            default:
                return true;
        }
    }
}
