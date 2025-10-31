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
import java.util.stream.Collectors;

public class ParticleConfigScreen extends Screen {
    private final Screen parent;
    private final ArgonConfig config;
    private int leftColumn;
    private int rightColumn;
    private TextFieldWidget searchField;
    private String searchQuery = "";
    private int scrollOffset = 0;
    private List<CyclingButtonWidget<Boolean>> particleButtons = new ArrayList<>();
    
    public ParticleConfigScreen(Screen parent) {
        super(Text.literal("Argon Particle Settings"));
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
        searchField.setPlaceholder(Text.literal("Search particles..."));
        searchField.setText(searchQuery);
        searchField.setChangedListener(text -> {
            if (!searchQuery.equals(text)) {
                searchQuery = text;
                scrollOffset = 0;
                buildParticleButtons(76);
            }
        });
        this.addDrawableChild(searchField);
        
        y += 24;
        
        // Global particle toggle
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
        
        // Individual particle toggles - mouse wheel scrolling
        buildParticleButtons(y);
        
        // Done button
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            config.save();
            if (this.client != null) {
                this.client.setScreen(parent);
            }
        }).dimensions(this.width / 2 - 100, this.height - 28, 200, 20).build());
    }
    
    
    private void buildParticleButtons(int startY) {
        // Remove old particle buttons
        particleButtons.forEach(this::remove);
        particleButtons.clear();
        
        List<String> particleTypes = new ArrayList<>(config.particleOptions.keySet());
        particleTypes.sort(String::compareTo);
        
        // Filter by search
        if (!searchQuery.isEmpty()) {
            String query = searchQuery.toLowerCase();
            particleTypes = particleTypes.stream()
                .filter(type -> type.toLowerCase().contains(query) || 
                               ParticleDescriptions.getDescription(type).toLowerCase().contains(query))
                .collect(Collectors.toList());
        }
        
        // Calculate visible range
        int maxRows = (this.height - startY - 35) / 24;
        int maxVisible = maxRows * 2; // 2 columns
        int startIndex = scrollOffset * 2;
        int endIndex = Math.min(startIndex + maxVisible, particleTypes.size());
        
        int y = startY;
        int row = 0;
        
        for (int i = startIndex; i < endIndex; i += 2) {
            String leftParticle = particleTypes.get(i);
            String rightParticle = (i + 1 < endIndex) ? particleTypes.get(i + 1) : null;
            
            // Left column button
            CyclingButtonWidget<Boolean> leftButton = CyclingButtonWidget.onOffBuilder(config.isParticleEnabled(leftParticle))
                .build(leftColumn, y, 150, 20, 
                    Text.literal(formatParticleName(leftParticle)), 
                    (button, value) -> {
                        config.particleOptions.put(leftParticle, value);
                    });
            this.addDrawableChild(leftButton);
            particleButtons.add(leftButton);
            
            // Right column button (if exists)
            if (rightParticle != null) {
                CyclingButtonWidget<Boolean> rightButton = CyclingButtonWidget.onOffBuilder(config.isParticleEnabled(rightParticle))
                    .build(rightColumn, y, 150, 20, 
                        Text.literal(formatParticleName(rightParticle)), 
                        (button, value) -> {
                            config.particleOptions.put(rightParticle, value);
                        });
                this.addDrawableChild(rightButton);
                particleButtons.add(rightButton);
            }
            
            y += 24;
            row++;
        }
    }
    
    private String formatParticleName(String particle) {
        return particle.toLowerCase().replace("_", " ");
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        List<String> particleTypes = new ArrayList<>(config.particleOptions.keySet());
        if (!searchQuery.isEmpty()) {
            String query = searchQuery.toLowerCase();
            particleTypes = particleTypes.stream()
                .filter(type -> type.toLowerCase().contains(query) || 
                               ParticleDescriptions.getDescription(type).toLowerCase().contains(query))
                .collect(Collectors.toList());
        }
        
        int maxRows = (this.height - 84 - 35) / 24;
        int maxVisible = maxRows * 2;
        int maxScroll = Math.max(0, (particleTypes.size() - maxVisible + 1) / 2);
        
        if (verticalAmount > 0 && scrollOffset > 0) {
            scrollOffset--;
            buildParticleButtons(76);
            return true;
        } else if (verticalAmount < 0 && scrollOffset < maxScroll) {
            scrollOffset++;
            buildParticleButtons(76);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
        
        // Show scroll hint
        List<String> particleTypes = new ArrayList<>(config.particleOptions.keySet());
        if (!searchQuery.isEmpty()) {
            String query = searchQuery.toLowerCase();
            particleTypes = particleTypes.stream()
                .filter(type -> type.toLowerCase().contains(query) || 
                               ParticleDescriptions.getDescription(type).toLowerCase().contains(query))
                .collect(Collectors.toList());
        }
        
        int maxRows = (this.height - 84 - 35) / 24;
        int maxVisible = maxRows * 2;
        if (particleTypes.size() > maxVisible) {
            int showing = Math.min(maxVisible, particleTypes.size() - (scrollOffset * 2));
            String hint = String.format("Showing %d-%d of %d particles (use mouse wheel to scroll)", 
                scrollOffset * 2 + 1, scrollOffset * 2 + showing, particleTypes.size());
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(hint), 
                this.width / 2, this.height - 40, 0xAAAAAA);
        }
    }
    
    @Override
    public void close() {
        config.save();
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}
