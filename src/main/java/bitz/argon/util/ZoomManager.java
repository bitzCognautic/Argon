package bitz.argon.util;

import bitz.argon.ArgonClient;
import bitz.argon.config.ArgonConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class ZoomManager {
    private static final ZoomManager INSTANCE = new ZoomManager();
    private static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(Identifier.of("argon", "controls"));
    
    private KeyBinding zoomKey;
    private boolean isZooming = false;
    private double currentZoomProgress = 0.0;
    private double targetZoomProgress = 0.0;
    private double dynamicZoomLevel = 4.0; // Dynamic zoom level adjusted by scroll wheel
    
    private ZoomManager() {}
    
    public static ZoomManager getInstance() {
        return INSTANCE;
    }
    
    public void initialize() {
        // Initialize dynamic zoom level from config
        ArgonConfig config = ArgonClient.getConfig();
        dynamicZoomLevel = config.zoomLevel;
        
        // Register zoom keybinding (default: C key)
        zoomKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.argon.zoom",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            CATEGORY
        ));
        
        // Register tick event to handle zoom
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) {
                return;
            }
            
            ArgonConfig tickConfig = ArgonClient.getConfig();
            if (!tickConfig.enableZoom) {
                isZooming = false;
                currentZoomProgress = 0.0;
                targetZoomProgress = 0.0;
                return;
            }
            
            // Check if zoom key is pressed
            boolean shouldZoom = zoomKey.isPressed();
            
            if (shouldZoom != isZooming) {
                isZooming = shouldZoom;
                targetZoomProgress = shouldZoom ? 1.0 : 0.0;
            }
            
            // Smooth zoom transition
            if (tickConfig.smoothZoom) {
                double zoomSpeed = 0.15; // Adjust for smoother/faster zoom
                if (currentZoomProgress < targetZoomProgress) {
                    currentZoomProgress = Math.min(targetZoomProgress, currentZoomProgress + zoomSpeed);
                } else if (currentZoomProgress > targetZoomProgress) {
                    currentZoomProgress = Math.max(targetZoomProgress, currentZoomProgress - zoomSpeed);
                }
            } else {
                currentZoomProgress = targetZoomProgress;
            }
        });
    }
    
    /**
     * Handle scroll wheel input for dynamic zoom adjustment
     * @param scrollDelta The scroll wheel delta (positive = scroll up, negative = scroll down)
     */
    public void handleScroll(double scrollDelta) {
        if (isZooming && scrollDelta != 0) {
            // Adjust zoom level: scroll up = zoom in more, scroll down = zoom out
            double adjustment = scrollDelta * 0.5; // Adjust sensitivity
            dynamicZoomLevel = Math.max(1.0, Math.min(10.0, dynamicZoomLevel + adjustment));
        }
    }
    
    /**
     * Reset dynamic zoom level to config default
     */
    public void resetDynamicZoom() {
        ArgonConfig config = ArgonClient.getConfig();
        dynamicZoomLevel = config.zoomLevel;
    }
    
    /**
     * Get the current FOV multiplier based on zoom state
     * @param baseFov The base FOV value
     * @return The modified FOV value
     */
    public double getZoomedFov(double baseFov) {
        ArgonConfig config = ArgonClient.getConfig();
        if (!config.enableZoom || currentZoomProgress <= 0.0) {
            return baseFov;
        }
        
        // Calculate zoom divisor using dynamic zoom level (higher zoom level = smaller FOV)
        double zoomDivisor = 1.0 + (dynamicZoomLevel - 1.0) * currentZoomProgress;
        return baseFov / zoomDivisor;
    }
    
    /**
     * Check if currently zooming
     */
    public boolean isZooming() {
        return isZooming;
    }
    
    /**
     * Get current zoom progress (0.0 to 1.0)
     */
    public double getZoomProgress() {
        return currentZoomProgress;
    }
}
