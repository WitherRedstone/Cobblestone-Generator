# 圆石生成机 (Cobblestone Generator) 功能分类文档

[English](#english) | [中文](#中文)

---

# **English**

A mod that adds various cobblestone generators to the game.

## ✨ Features

### Basic Features
- 📦 **9-slot Inventory**: Right-click to extract items
- 🔄 **Auto-output**: Items automatically output to storage blocks above

## 🔧 Cobblestone Generator Types

### Basic Type
- **Vanilla Cobblestone Generator**: Basic cobblestone generation device

### Special Types

#### 💜 Amethyst Cobblestone Generator
- Accelerates amethyst bud growth in a **3x3 area** around it
- ✅ Configurable acceleration rate in config file

#### 🔴 Redstone Cobblestone Generator
- Outputs redstone signals based on configured mode
- **CONTINUOUS Mode**: Produces constant **15-tile** redstone signal
- **INTERVAL Mode**: Produces redstone signals at configured tick intervals

#### 💡 Glowstone Cobblestone Generator
- Emits light level **15**

#### 🌾 Hay Bale Cobblestone Generator
- Provides regeneration effect to nearby players
- ✅ Configurable effect level/duration in config file

#### 🟦 Sculk Cobblestone Generator
- Gradually converts target blocks within specified range to sculk blocks
- ✅ Configurable range/probability/blocks in config file

---

## ⚙️ Configuration

The following settings can be adjusted in the config file:

| Configuration               | Description                                                         |
|-----------------------------|---------------------------------------------------------------------|
| **Output Direction**        | Set the output direction of cobblestone generator                   |
| **Auto Output**             | Enable/disable auto-output function                                 |
| **Global Speed Multiplier** | Global multiplier affecting generation tick                         |
| **Generation Count**        | Number of items generated per cobblestone generator                 |
| **Generation Speed**        | Generation speed for each cobblestone generator                     |
| **Special Functions**       | Exclusive function configuration for special cobblestone generators |

---

## 📋 Version Information

- **Minecraft**: 1.21.1
- **Mod Loader**: NeoForge

---

# **中文**

一个为游戏添加多种圆石生成机的模组。

## ✨ 功能特性

### 基础功能
- 📦 **9 格缓存空间**：右键即可取出物品
- 🔄 **自动输出**：物品会自动向上方有储物空间的方块输出圆石

## 🔧 圆石生成器类型

### 基础型
- **原版矿物基础圆石生成器**：基础的圆石生成设备

### 特殊型

#### 💜 紫水晶圆石生成器
- 加速周围 **3x3 区域**内紫水晶母岩的生长速度
- ✅ 配置文件中可调整加速倍率

#### 🔴 红石圆石生成器
- 根据配置模式产生相应的红石信号输出
- **CONTINUOUS 模式**：产生持续的 **15 格**红石信号
- **INTERVAL 模式**：按照配置文件设定的 tick 产生红石信号

#### 💡 荧石圆石生成器
- 发出 **15 等级**的亮度

#### 🌾 干草块圆石生成器
- 为附近玩家提供生命恢复效果
- ✅ 配置文件中可修改等级/时间

#### 🟦 幽匿圆石生成器
- 将周围指定范围内的目标方块逐渐转化为幽匿方块
- ✅ 配置文件中可调整范围/概率/方块

---

## ⚙️ 配置文件

可在配置文件中调整以下设置：

| 配置项        | 说明              |
|------------|-----------------|
| **输出方向**   | 设置圆石生成机的输出方向    |
| **自动输出**   | 是否启用自动输出功能      |
| **全局速度倍数** | 影响生成 tick 的全局倍率 |
| **生成数量**   | 每个圆石生成器的生成个数    |
| **生成速度**   | 每个圆石生成器的生成速度    |
| **特殊功能**   | 各特殊圆石生成器的专属功能配置 |

---

## 📋 版本信息

- **Minecraft**: 1.21.1
- **模组加载器**: NeoForge