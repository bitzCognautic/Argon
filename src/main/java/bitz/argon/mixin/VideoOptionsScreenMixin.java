package bitz.argon.mixin;

import bitz.argon.config.ArgonConfigScreenTabbed;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
public abstract class VideoOptionsScreenMixin extends GameOptionsScreen {
    
    public VideoOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }
    
    @Inject(method = "addOptions", at = @At("RETURN"))
    private void addArgonButton(CallbackInfo ci) {
        try {
            // Add Argon Settings button at the bottom of the button list
            this.body.addWidgetEntry(
                ButtonWidget.builder(
                    Text.literal("Argon Settings..."),
                    button -> {
                        if (this.client != null) {
                            this.client.setScreen(new ArgonConfigScreenTabbed(this));
                        }
                    })
                    .width(310)
                    .build(),
                null
            );
        } catch (Exception e) {
            // Silently fail to avoid crashes
        }
    }
}
