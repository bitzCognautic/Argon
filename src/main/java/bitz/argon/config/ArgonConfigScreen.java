package bitz.argon.config;

import bitz.argon.ArgonClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.screen.ScreenTexts;

import java.util.ArrayList;
import java.util.List;

public class ArgonConfigScreen extends Screen {
    private final Screen parent;
    private final ArgonConfig config;
    private int leftColumn;
    private int rightColumn;
    private TextFieldWidget searchField;
    private String searchQuery = "";
    private List<ButtonWidget> allButtons = new ArrayList<>();
    
    public ArgonConfigScreen(Screen parent) {
        super(Text.literal("Argon Configuration"));
        this.parent = parent;
        this.config = ArgonClient.getConfig();
    }
    
    @Override
    protected void init() {
        super.init();
        
        leftColumn = this.width / 2 - 155;
        rightColumn = this.width / 2 + 5;
        int y = 30;
        
        // Search field
        searchField = new TextFieldWidget(this.textRenderer, leftColumn, y, 310, 20, Text.literal("Search"));
        searchField.setPlaceholder(Text.literal("Search settings..."));
        searchField.setText(searchQuery);
        searchField.setChangedListener(text -> {
            searchQuery = text.toLowerCase();
            updateButtonVisibility();
        });
        this.addDrawableChild(searchField);
        
        y += 30;
        
        // Performance Optimizations
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.entityTickingOptimization)
            .build(leftColumn, y, 150, 20, Text.literal("Entity Ticking Opt"), (button, value) -> {
                config.entityTickingOptimization = value;
            }));
        
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.aggressiveEntityCulling)
            .build(rightColumn, y, 150, 20, Text.literal("Aggressive Culling"), (button, value) -> {
                config.aggressiveEntityCulling = value;
            }));
        
        y += 24;
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.chunkUpdateOptimization)
            .build(leftColumn, y, 150, 20, Text.literal("Chunk Updates Opt"), (button, value) -> {
                config.chunkUpdateOptimization = value;
            }));
        
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.memoryOptimization)
            .build(rightColumn, y, 150, 20, Text.literal("Memory Optimization"), (button, value) -> {
                config.memoryOptimization = value;
            }));
        
        y += 24;
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.reduceLightingUpdates)
            .build(leftColumn, y, 150, 20, Text.literal("Reduce Lighting"), (button, value) -> {
                config.reduceLightingUpdates = value;
            }));
        
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.optimizeExplosions)
            .build(rightColumn, y, 150, 20, Text.literal("Optimize Explosions"), (button, value) -> {
                config.optimizeExplosions = value;
            }));
        
        // HUD Display Settings
        y += 30;
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.enableChunkLOD)
            .build(leftColumn, y, 150, 20, Text.literal("Chunk LOD"), (button, value) -> {
                config.enableChunkLOD = value;
            }));
        
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.fastChunkLoading)
            .build(rightColumn, y, 150, 20, Text.literal("Fast Chunk Load"), (button, value) -> {
                config.fastChunkLoading = value;
            }));
        
        // Particle Settings
        y += 30;
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.disableAllParticles)
            .build(leftColumn, y, 150, 20, Text.literal("Disable All Particles"), (button, value) -> {
                config.disableAllParticles = value;
            }));
        
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Particle Settings..."),
            button -> {
                if (this.client != null) {
                    this.client.setScreen(new ParticleConfigScreen(this));
                }
            })
            .dimensions(rightColumn, y, 150, 20)
            .build());
        
        // Done button
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            config.save();
            if (this.client != null) {
                this.client.setScreen(parent);
            }
        }).dimensions(this.width / 2 - 100, this.height - 28, 200, 20).build());
    }
    
    private void updateButtonVisibility() {
        if (searchQuery.isEmpty()) {
            // Show all buttons
            for (ButtonWidget button : allButtons) {
                button.visible = true;
            }
        } else {
            // Filter buttons by search query
            for (ButtonWidget button : allButtons) {
                String buttonText = button.getMessage().getString().toLowerCase();
                button.visible = buttonText.contains(searchQuery);
            }
        }
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
    }
    
    @Override
    public void close() {
        config.save();
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}
