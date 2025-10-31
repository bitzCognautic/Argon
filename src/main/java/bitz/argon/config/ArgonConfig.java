package bitz.argon.config;

import bitz.argon.ArgonClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ArgonConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("argon.json");
    
    // Performance optimizations
    public boolean entityTickingOptimization = true;
    public boolean chunkUpdateOptimization = true;
    public boolean memoryOptimization = true;
    public boolean reduceLightingUpdates = true;
    public boolean optimizeExplosions = true;
    public boolean fastMath = true;
    
    // Particle settings
    public boolean disableAllParticles = false;
    public Map<String, Boolean> particleOptions = createDefaultParticleOptions();
    
    // Advanced optimizations
    public boolean aggressiveEntityCulling = false;
    public boolean reducedExplosionParticles = true;
    public int maxParticlesPerFrame = 4000;
    
    // Fast chunk loading
    public boolean fastChunkLoading = false;
    public int chunkLoadingSpeed = 5; // 1-10 scale, 5 is default
    
    // Chunk LOD (Level of Detail) settings
    public boolean enableChunkLOD = false;
    public int lodDistance = 16; // Distance in chunks where LOD starts (8-32)
    public int lodQuality = 2; // Quality reduction factor (1-4, higher = lower quality)
    
    private static Map<String, Boolean> createDefaultParticleOptions() {
        Map<String, Boolean> particles = new HashMap<>();
        
        // All Minecraft 1.21 particles (alphabetically sorted)
        particles.put("ANGRY_VILLAGER", true);
        particles.put("ASH", true);
        particles.put("BLOCK", true);
        particles.put("BLOCK_MARKER", true);
        particles.put("BUBBLE", true);
        particles.put("BUBBLE_COLUMN_UP", true);
        particles.put("BUBBLE_POP", true);
        particles.put("CAMPFIRE_COSY_SMOKE", true);
        particles.put("CAMPFIRE_SIGNAL_SMOKE", true);
        particles.put("CHERRY_LEAVES", true);
        particles.put("CLOUD", true);
        particles.put("COMPOSTER", true);
        particles.put("CRIMSON_SPORE", true);
        particles.put("CRIT", true);
        particles.put("CURRENT_DOWN", true);
        particles.put("DAMAGE_INDICATOR", true);
        particles.put("DOLPHIN", true);
        particles.put("DRAGON_BREATH", true);
        particles.put("DRIPPING_DRIPSTONE_LAVA", true);
        particles.put("DRIPPING_DRIPSTONE_WATER", true);
        particles.put("DRIPPING_HONEY", true);
        particles.put("DRIPPING_LAVA", true);
        particles.put("DRIPPING_OBSIDIAN_TEAR", true);
        particles.put("DRIPPING_WATER", true);
        particles.put("DUST", true);
        particles.put("DUST_COLOR_TRANSITION", true);
        particles.put("DUST_PILLAR", true);
        particles.put("DUST_PLUME", true);
        particles.put("EFFECT", true);
        particles.put("EGG_CRACK", true);
        particles.put("ELDER_GUARDIAN", true);
        particles.put("ELECTRIC_SPARK", true);
        particles.put("ENCHANT", true);
        particles.put("ENCHANTED_HIT", true);
        particles.put("END_ROD", true);
        particles.put("ENTITY_EFFECT", true);
        particles.put("EXPLOSION", true);
        particles.put("EXPLOSION_EMITTER", true);
        particles.put("FALLING_DRIPSTONE_LAVA", true);
        particles.put("FALLING_DRIPSTONE_WATER", true);
        particles.put("FALLING_DUST", true);
        particles.put("FALLING_HONEY", true);
        particles.put("FALLING_LAVA", true);
        particles.put("FALLING_NECTAR", true);
        particles.put("FALLING_OBSIDIAN_TEAR", true);
        particles.put("FALLING_SPORE_BLOSSOM", true);
        particles.put("FALLING_WATER", true);
        particles.put("FIREWORK", true);
        particles.put("FISHING", true);
        particles.put("FLAME", true);
        particles.put("FLASH", true);
        particles.put("GLOW", true);
        particles.put("GLOW_SQUID_INK", true);
        particles.put("GUST", true);
        particles.put("GUST_EMITTER_LARGE", true);
        particles.put("GUST_EMITTER_SMALL", true);
        particles.put("HAPPY_VILLAGER", true);
        particles.put("HEART", true);
        particles.put("INFESTED", true);
        particles.put("INSTANT_EFFECT", true);
        particles.put("ITEM", true);
        particles.put("ITEM_COBWEB", true);
        particles.put("ITEM_SLIME", true);
        particles.put("ITEM_SNOWBALL", true);
        particles.put("LANDING_HONEY", true);
        particles.put("LANDING_LAVA", true);
        particles.put("LANDING_OBSIDIAN_TEAR", true);
        particles.put("LARGE_SMOKE", true);
        particles.put("LAVA", true);
        particles.put("MYCELIUM", true);
        particles.put("NAUTILUS", true);
        particles.put("NOTE", true);
        particles.put("OMINOUS_SPAWNING", true);
        particles.put("PALE_OAK_LEAVES", true);
        particles.put("POOF", true);
        particles.put("PORTAL", true);
        particles.put("RAID_OMEN", true);
        particles.put("RAIN", true);
        particles.put("REVERSE_PORTAL", true);
        particles.put("SCRAPE", true);
        particles.put("SCULK_CHARGE", true);
        particles.put("SCULK_CHARGE_POP", true);
        particles.put("SCULK_SOUL", true);
        particles.put("SHRIEK", true);
        particles.put("SMALL_FLAME", true);
        particles.put("SMALL_GUST", true);
        particles.put("SMOKE", true);
        particles.put("SNEEZE", true);
        particles.put("SNOWFLAKE", true);
        particles.put("SONIC_BOOM", true);
        particles.put("SOUL", true);
        particles.put("SOUL_FIRE_FLAME", true);
        particles.put("SPIT", true);
        particles.put("SPLASH", true);
        particles.put("SPORE_BLOSSOM_AIR", true);
        particles.put("SQUID_INK", true);
        particles.put("SWEEP_ATTACK", true);
        particles.put("TINTED_LEAVES", true);
        particles.put("TOTEM_OF_UNDYING", true);
        particles.put("TRAIL", true);
        particles.put("TRIAL_OMEN", true);
        particles.put("TRIAL_SPAWNER_DETECTION", true);
        particles.put("TRIAL_SPAWNER_DETECTION_OMINOUS", true);
        particles.put("UNDERWATER", true);
        particles.put("VAULT_CONNECTION", true);
        particles.put("VIBRATION", true);
        particles.put("WARPED_SPORE", true);
        particles.put("WAX_OFF", true);
        particles.put("WAX_ON", true);
        particles.put("WHITE_ASH", true);
        particles.put("WHITE_SMOKE", true);
        particles.put("WITCH", true);
        
        return particles;
    }
    
    public static ArgonConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                ArgonConfig config = GSON.fromJson(json, ArgonConfig.class);
                
                // Ensure particle options is initialized
                if (config.particleOptions == null) {
                    config.particleOptions = createDefaultParticleOptions();
                } else {
                    // Merge with defaults to add any new particles
                    Map<String, Boolean> defaults = createDefaultParticleOptions();
                    for (Map.Entry<String, Boolean> entry : defaults.entrySet()) {
                        config.particleOptions.putIfAbsent(entry.getKey(), entry.getValue());
                    }
                }
                
                ArgonClient.LOGGER.info("Loaded configuration from {} with {} particles", CONFIG_PATH, config.particleOptions.size());
                return config;
            } catch (Exception e) {
                ArgonClient.LOGGER.error("Failed to load config, using defaults", e);
            }
        }
        
        ArgonConfig config = new ArgonConfig();
        config.save();
        return config;
    }
    
    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            String json = GSON.toJson(this);
            Files.writeString(CONFIG_PATH, json);
            ArgonClient.LOGGER.info("Saved configuration to {}", CONFIG_PATH);
        } catch (IOException e) {
            ArgonClient.LOGGER.error("Failed to save config", e);
        }
    }
    
    public boolean isParticleEnabled(String particleType) {
        return particleOptions.getOrDefault(particleType, true);
    }
}
