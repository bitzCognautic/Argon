package bitz.argon.config;

import bitz.argon.ArgonClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.minecraft.screen.ScreenTexts;

public class ArgonVideoSettingsScreen extends Screen {
    private final Screen parent;
    private final GameOptions gameOptions;
    private final ArgonConfig config;
    private int leftColumn;
    private int rightColumn;
    
    // Tab system
    private enum Tab {
        VANILLA("Vanilla"),
        ARGON("Argon"),
        PARTICLES("Particles");
        
        private final String name;
        
        Tab(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
    }
    
    private Tab currentTab = Tab.VANILLA;
    
    public ArgonVideoSettingsScreen(Screen parent, GameOptions gameOptions) {
        super(Text.literal("Video Settings"));
        this.parent = parent;
        this.gameOptions = gameOptions;
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
        int tabStartX = this.width / 2 - (tabWidth * 3) / 2;
        
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
            case VANILLA:
                buildVanillaSettings(contentY);
                break;
            case ARGON:
                buildArgonSettings(contentY);
                break;
            case PARTICLES:
                buildParticleSettings(contentY);
                break;
        }
        
        // Done button
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            config.save();
            if (this.client != null) {
                this.client.options.write();
                this.client.setScreen(parent);
            }
        }).dimensions(this.width / 2 - 100, this.height - 28, 200, 20).build());
    }
    
    private void buildVanillaSettings(int startY) {
        int y = startY;
        
        // Graphics mode
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Graphics: " + gameOptions.getGraphicsMode().getValue()),
            button -> {
                net.minecraft.client.option.GraphicsMode current = gameOptions.getGraphicsMode().getValue();
                net.minecraft.client.option.GraphicsMode[] modes = net.minecraft.client.option.GraphicsMode.values();
                int nextIndex = (current.ordinal() + 1) % modes.length;
                gameOptions.getGraphicsMode().setValue(modes[nextIndex]);
                button.setMessage(Text.literal("Graphics: " + modes[nextIndex]));
            })
            .dimensions(leftColumn, y, 150, 20)
            .build());
        
        // Render distance
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Render Distance: " + gameOptions.getViewDistance().getValue()),
            button -> {
                int current = gameOptions.getViewDistance().getValue();
                int next = current >= 32 ? 2 : current + 2;
                gameOptions.getViewDistance().setValue(next);
                button.setMessage(Text.literal("Render Distance: " + next));
            })
            .dimensions(rightColumn, y, 150, 20)
            .build());
        
        y += 24;
        
        // VSync
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(gameOptions.getEnableVsync().getValue())
            .build(leftColumn, y, 150, 20, Text.literal("VSync"), (button, value) -> {
                gameOptions.getEnableVsync().setValue(value);
            }));
        
        // Max FPS
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Max FPS: " + gameOptions.getMaxFps().getValue()),
            button -> {
                int current = gameOptions.getMaxFps().getValue();
                int next = current >= 260 ? 10 : current + 10;
                gameOptions.getMaxFps().setValue(next);
                button.setMessage(Text.literal("Max FPS: " + next));
            })
            .dimensions(rightColumn, y, 150, 20)
            .build());
        
        y += 24;
        
        // Fullscreen
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(gameOptions.getFullscreen().getValue())
            .build(leftColumn, y, 150, 20, Text.literal("Fullscreen"), (button, value) -> {
                gameOptions.getFullscreen().setValue(value);
            }));
        
        // Brightness
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Brightness: " + (int)(gameOptions.getGamma().getValue() * 100) + "%"),
            button -> {
                double current = gameOptions.getGamma().getValue();
                double next = current >= 1.0 ? 0.0 : current + 0.25;
                gameOptions.getGamma().setValue(next);
                button.setMessage(Text.literal("Brightness: " + (int)(next * 100) + "%"));
            })
            .dimensions(rightColumn, y, 150, 20)
            .build());
        
        y += 24;
        
        // GUI Scale
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("GUI Scale: " + (gameOptions.getGuiScale().getValue() == 0 ? "Auto" : gameOptions.getGuiScale().getValue())),
            button -> {
                int current = gameOptions.getGuiScale().getValue();
                int next = current >= 4 ? 0 : current + 1;
                gameOptions.getGuiScale().setValue(next);
                button.setMessage(Text.literal("GUI Scale: " + (next == 0 ? "Auto" : next)));
            })
            .dimensions(leftColumn, y, 150, 20)
            .build());
        
        // Entity Shadows
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(gameOptions.getEntityShadows().getValue())
            .build(rightColumn, y, 150, 20, Text.literal("Entity Shadows"), (button, value) -> {
                gameOptions.getEntityShadows().setValue(value);
            }));
        
        y += 24;
        
        // View Bobbing
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(gameOptions.getBobView().getValue())
            .build(leftColumn, y, 150, 20, Text.literal("View Bobbing"), (button, value) -> {
                gameOptions.getBobView().setValue(value);
            }));
    }
    
    private void buildArgonSettings(int startY) {
        int y = startY;
        
        // Performance settings
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
        
        y += 30;
        
        // Chunk LOD Settings
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.enableChunkLOD)
            .build(leftColumn, y, 310, 20, Text.literal("Enable Chunk LOD (Better FPS)"), (button, value) -> {
                config.enableChunkLOD = value;
            }));
        
        y += 24;
        
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("LOD Distance: " + config.lodDistance),
            button -> {
                config.lodDistance += 4;
                if (config.lodDistance > 32) {
                    config.lodDistance = 8;
                }
                button.setMessage(Text.literal("LOD Distance: " + config.lodDistance));
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
        int tabStartX = this.width / 2 - (tabWidth * 3) / 2;
        int currentTabIndex = currentTab.ordinal();
        int highlightX = tabStartX + (currentTabIndex * (tabWidth + 5));
        
        // Draw underline for active tab
        context.fill(highlightX, tabY + 21, highlightX + tabWidth, tabY + 23, 0xFFFFFFFF);
    }
    
    @Override
    public void close() {
        config.save();
        if (this.client != null) {
            this.client.options.write();
            this.client.setScreen(parent);
        }
    }
}
