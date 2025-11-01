package bitz.argon.mixin;

import bitz.argon.util.ZoomManager;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    
    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        ZoomManager zoomManager = ZoomManager.getInstance();
        
        // If zooming and scroll is detected, handle it for zoom adjustment
        if (zoomManager.isZooming() && vertical != 0) {
            zoomManager.handleScroll(vertical);
            ci.cancel(); // Prevent default hotbar scrolling while zooming
        }
    }
}
