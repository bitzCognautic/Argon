package bitz.argon.config;

import bitz.argon.ArgonClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.screen.ScreenTexts;

public class ArgonConfigScreenTabbed extends Screen {
    private final Screen parent;
    private final ArgonConfig config;
    private int leftColumn;
    private int rightColumn;
    
    // Tab system
    private enum Tab {
        PERFORMANCE("Performance"),
        PARTICLES("Particles");
        
        private final String name;
        
        Tab(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
    }
    
    private Tab currentTab = Tab.PERFORMANCE;
    
    public ArgonConfigScreenTabbed(Screen parent) {
        super(Text.literal("Argon Settings"));
        this.parent = parent;
        this.config = ArgonClient.getConfig();
    }
    
    @Override
    protected void init() {
        super.init();
        
        leftColumn = this.width / 2 - 155;
        rightColumn = this.width / 2 + 5;
        
        // Tab buttons at the top
        int tabWidth = 100;
        int tabY = 30;
        int tabStartX = this.width / 2 - (tabWidth * 2) / 2;
        
        for (int i = 0; i < Tab.values().length; i++) {
            Tab tab = Tab.values()[i];
            int tabX = tabStartX + (i * (tabWidth + 5));
            
            this.addDrawableChild(ButtonWidget.builder(
                Text.literal(tab.getName()),
                button -> {
                    currentTab = tab;
                    this.clearAndInit();
                })
                .dimensions(tabX, tabY, tabWidth, 20)
                .build());
        }
        
        // Content area starts below tabs
        int contentY = 60;
        
        // Render content based on selected tab
        switch (currentTab) {
            case PERFORMANCE:
                buildPerformanceSettings(contentY);
                break;
            case PARTICLES:
                buildParticleSettings(contentY);
                break;
        }
        
        // Done button
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            config.save();
            if (this.client != null) {
                this.client.setScreen(parent);
            }
        }).dimensions(this.width / 2 - 100, this.height - 28, 200, 20).build());
    }
    
    private void buildPerformanceSettings(int startY) {
        int y = startY;
        
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
            .build(leftColumn, y, 150, 20, Text.literal("Chunk Update Opt"), (button, value) -> {
                config.chunkUpdateOptimization = value;
            }));
        
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.memoryOptimization)
            .build(rightColumn, y, 150, 20, Text.literal("Memory Opt"), (button, value) -> {
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
        
        y += 24;
        
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.fastMath)
            .build(leftColumn, y, 150, 20, Text.literal("Fast Math"), (button, value) -> {
                config.fastMath = value;
            }));
        
        y += 24;
        
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.fastChunkLoading)
            .build(leftColumn, y, 150, 20, Text.literal("Fast Chunk Loading"), (button, value) -> {
                config.fastChunkLoading = value;
            }));
        
        // Chunk loading speed slider (1-10)
        y += 24;
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Chunk Speed: " + config.chunkLoadingSpeed),
            button -> {
                config.chunkLoadingSpeed++;
                if (config.chunkLoadingSpeed > 10) {
                    config.chunkLoadingSpeed = 1;
                }
                button.setMessage(Text.literal("Chunk Speed: " + config.chunkLoadingSpeed));
            })
            .dimensions(leftColumn, y, 150, 20)
            .build());
        
        // Chunk LOD settings
        y += 30;
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.enableChunkLOD)
            .build(leftColumn, y, 310, 20, Text.literal("Enable Chunk LOD (Distant = Low Quality)"), (button, value) -> {
                config.enableChunkLOD = value;
            }));
        
        y += 24;
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("LOD Distance: " + config.lodDistance + " chunks"),
            button -> {
                config.lodDistance += 4;
                if (config.lodDistance > 32) {
                    config.lodDistance = 8;
                }
                button.setMessage(Text.literal("LOD Distance: " + config.lodDistance + " chunks"));
            })
            .dimensions(leftColumn, y, 150, 20)
            .build());
        
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("LOD Quality: " + config.lodQuality),
            button -> {
                config.lodQuality++;
                if (config.lodQuality > 4) {
                    config.lodQuality = 1;
                }
                button.setMessage(Text.literal("LOD Quality: " + config.lodQuality));
            })
            .dimensions(rightColumn, y, 150, 20)
            .build());
    }
    
    private void buildParticleSettings(int startY) {
        int y = startY;
        
        // Quick particle controls
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(!config.disableAllParticles)
            .build(leftColumn, y, 310, 20, Text.literal("Enable Particles"), (button, value) -> {
                config.disableAllParticles = !value;
            }));
        
        y += 24;
        
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.reducedExplosionParticles)
            .build(leftColumn, y, 150, 20, Text.literal("Reduce Explosions"), (button, value) -> {
                config.reducedExplosionParticles = value;
            }));
        
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Max: " + config.maxParticlesPerFrame),
            button -> {
                if (config.maxParticlesPerFrame >= 8000) config.maxParticlesPerFrame = 1000;
                else config.maxParticlesPerFrame += 1000;
                button.setMessage(Text.literal("Max: " + config.maxParticlesPerFrame));
            })
            .dimensions(rightColumn, y, 150, 20)
            .build());
        
        y += 30;
        
        // Button to open detailed particle settings
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Individual Particle Settings..."),
            button -> {
                if (this.client != null) {
                    this.client.setScreen(new ParticleConfigScreen(this));
                }
            })
            .dimensions(leftColumn, y, 310, 20)
            .build());
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
        
        // Draw tab indicator
        int tabWidth = 100;
        int tabY = 30;
        int tabStartX = this.width / 2 - (tabWidth * 2) / 2;
        int currentTabIndex = currentTab.ordinal();
        int highlightX = tabStartX + (currentTabIndex * (tabWidth + 5));
        
        // Draw underline for active tab
        context.fill(highlightX, tabY + 21, highlightX + tabWidth, tabY + 23, 0xFFFFFFFF);
    }
    
    @Override
    public void close() {
        config.save();
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}
