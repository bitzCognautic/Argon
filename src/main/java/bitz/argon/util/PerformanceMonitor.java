package bitz.argon.util;

import bitz.argon.ArgonClient;

public class PerformanceMonitor {
    
    private static long lastLogTime = 0;
    private static int frameCount = 0;
    private static long totalFrameTime = 0;
    
    public static void recordFrame(long frameTime) {
        frameCount++;
        totalFrameTime += frameTime;
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastLogTime > 60000) { // Log every minute
            double avgFrameTime = totalFrameTime / (double) frameCount;
            double avgFps = 1000.0 / avgFrameTime;
            
            ArgonClient.LOGGER.debug("Performance: Avg FPS: {}, Avg Frame Time: {}ms", 
                String.format("%.2f", avgFps), 
                String.format("%.2f", avgFrameTime));
            
            // Reset counters
            frameCount = 0;
            totalFrameTime = 0;
            lastLogTime = currentTime;
        }
    }
    
    public static void logMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        
        ArgonClient.LOGGER.debug("Memory: Used: {}MB, Free: {}MB, Total: {}MB, Max: {}MB",
            usedMemory, freeMemory, totalMemory, maxMemory);
    }
}
