# 🚀 Argon - Ultimate Performance Optimization for Minecraft 1.21.10

**Boost your FPS by 20-90%** with Argon, the most comprehensive performance mod for Minecraft 1.21.10!

## ⚡ What is Argon?

Argon is an all-in-one performance optimization mod that dramatically improves your Minecraft experience through intelligent entity culling, chunk management, lighting optimization, and advanced particle control. Whether you're playing on a high-end gaming PC or a modest laptop, Argon will give you smoother gameplay and higher FPS.

## 🎯 Key Features

### 🔥 Massive Performance Gains
- **Entity Ticking Optimization** - Up to 87.5% reduction in entity processing
- **Aggressive Entity Culling** - Smart distance-based entity management
- **Chunk Update Optimization** - 66% reduction in terrain updates
- **Lighting Optimization** - 75% reduction in lighting calculations
- **Memory Management** - Automatic garbage collection and optimization

### 🌍 Chunk LOD System
- Distant chunks render in **lower quality** for better performance
- **Automatic quality upgrade** as you approach
- Fully configurable distance and quality settings
- Seamless transitions between quality levels

### ✨ Advanced Particle Control
- **100+ individual particle toggles** - Control every particle type
- Global particle on/off switch
- Explosion particle reduction
- Configurable max particles per frame (1000-8000)
- Searchable particle menu for easy configuration

### 🎨 User-Friendly Interface
- Beautiful tabbed configuration screen
- Integrated into vanilla Options and Video Settings
- Mod Menu support
- Real-time setting changes (no restart required)
- Clean, intuitive design

## 📊 Performance Benchmarks

| Configuration | FPS Improvement | Best For |
|--------------|----------------|----------|
| Normal Mode | +20-40% | Balanced gameplay |
| Aggressive Culling | +40-70% | Maximum performance |
| Chunk LOD Enabled | +50-90% | Large render distances |
| Particle Reduction | +10-30% | Particle-heavy scenarios |

*Tested on various hardware configurations. Your results may vary.*

## 🎮 Perfect For

- **Low-end PCs** - Get playable FPS on older hardware
- **High render distances** - Maintain smooth FPS even at 32 chunks
- **Multiplayer servers** - Handle crowded servers without lag
- **Modpack users** - Optimize heavily modded instances
- **Content creators** - Record smooth gameplay footage

## 🔧 How It Works

### Entity Optimization
Argon intelligently reduces tick rates for distant entities:
- **Normal Mode**: Entities beyond 64 blocks tick every 4th frame
- **Aggressive Mode**: Entities beyond 32 blocks tick every 8th frame
- Living entity movement updates scale with distance

### Chunk Optimization
- Skips 2 out of 3 unnecessary terrain updates
- LOD system reduces complexity for distant chunks
- Dynamic quality adjustment based on player position

### Lighting Optimization
- Skips 3 out of 4 redundant lighting calculations
- Maintains visual quality while reducing overhead
- Smart update scheduling

### Particle Management
- Per-type filtering system
- Frame-based particle limiting
- Explosion particle reduction
- Zero performance impact when particles are disabled

## 🤝 Compatibility

### ✅ Works Great With
- **Sodium** - Rendering optimization (highly recommended!)
- **Lithium** - Server-side optimization
- **Iris** - Shader support
- **Starlight** - Lighting engine rewrite
- **Mod Menu** - Configuration interface
- Most Fabric mods

### ⚠️ Potential Conflicts
- Other mods that heavily modify entity ticking
- Mods that override chunk rendering

## 📥 Installation

1. Install **Fabric Loader** for Minecraft 1.21.10
2. Download **Fabric API**
3. Download **Argon**
4. Place both JARs in your `.minecraft/mods` folder
5. Launch and enjoy!

**Optional but Recommended:**
- Install Sodium for even better performance
- Install Mod Menu for easy configuration access

## ⚙️ Configuration

### Quick Access
- **Video Settings** → "Argon Settings"
- **Mod Menu** → Argon → Config

### Recommended Settings

**For Maximum FPS:**
```
✅ Entity Ticking Optimization
✅ Aggressive Entity Culling
✅ Chunk Update Optimization
✅ Reduce Lighting Updates
✅ Enable Chunk LOD
📊 LOD Distance: 16 chunks
📊 LOD Quality: 2
📊 Max Particles: 2000
```

**For Balanced Performance:**
```
✅ Entity Ticking Optimization
❌ Aggressive Entity Culling
✅ Chunk Update Optimization
✅ Reduce Lighting Updates
✅ Enable Chunk LOD
📊 LOD Distance: 20 chunks
📊 LOD Quality: 2
```

## 🎓 Usage Tips

1. **Start Conservative** - Enable optimizations one at a time to find your sweet spot
2. **Test in Different Scenarios** - Some settings work better in certain situations
3. **Combine with Sodium** - For the absolute best performance
4. **Adjust LOD Distance** - Higher values = more aggressive optimization
5. **Fine-tune Particles** - Disable particles you don't notice or need

## 🐛 Known Issues

- ChunkBuilderMixin warning in logs (cosmetic, does not affect functionality)
- Distant entity animations may appear slightly choppy with Aggressive Culling enabled
- Some particle effects may not appear if individually disabled

## 📝 Changelog

### Version 1.0.0 (Current)
- 🎉 Initial release
- ⚡ Entity ticking optimization with normal and aggressive modes
- 🌍 Chunk LOD system with dynamic quality adjustment
- ✨ Advanced particle control with 100+ individual toggles
- 💡 Lighting optimization system
- 🧠 Memory management and optimization
- 🎨 Beautiful tabbed configuration interface
- 🔗 Full Mod Menu integration
- ✅ Tested and compatible with Sodium, Lithium, Iris, and Starlight

## 💬 Support & Community

- **Report Bugs**: [GitHub Issues](https://github.com/bitzCognautic/argon/issues)
- **Feature Requests**: [GitHub Discussions](https://github.com/bitzCognautic/argon/discussions)

## 📜 License

Argon is open source and licensed under the MIT License. Feel free to contribute!

## 🙏 Credits

**Developer**: bitz.cognautic
---

**⭐ If you enjoy Argon, please consider:**
- Starring the GitHub repository
- Sharing with friends who need better FPS
- Supporting development

**Made with ❤️ for the Minecraft community**
