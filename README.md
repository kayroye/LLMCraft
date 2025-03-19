<div align="center">

# LLMCraft
### LLM to Minecraft Integration Mod (WIP)

A Minecraft Forge mod that allows interaction with open-source large language models directly from within the game. With LLMCraft, players can get real-time AI assistance based on their in-game context and environment.

[Bug Report](https://github.com/kayroye/LLMCraft/issues)

![GitHub stars](https://img.shields.io/github/stars/kayroye/LLMCraft)
![GitHub issues](https://img.shields.io/github/issues/kayroye/LLMCraft)
![GitHub license](https://img.shields.io/github/license/kayroye/LLMCraft)
![Version](https://img.shields.io/badge/version-1.0.0-blue)

</div>

## 📸 Screenshots

*Coming soon*

## ✨ Features

<details>
<summary>🔮 Position Reader Item</summary>

- Reads the player's coordinates, dimension, biome, and time
- Sends this context to a local LLM for processing
- Receives tailored suggestions based on the player's in-game situation
- Displays AI suggestions directly in the Minecraft chat
</details>

<details>
<summary>🤖 LLM Integration</summary>

- Works with locally hosted Ollama models (default: Qwen2.5 32B)
- API integration that works through HTTP requests
- Customizable system prompts to shape the AI assistant's behavior
- Context-aware suggestions based on player's surroundings
</details>

<details>
<summary>🔧 Configuration Options</summary>

- Customize which LLM model to use
- Adjust the system prompt to change how the AI responds
</details>

## 🛠️ Tech Stack

- **Minecraft Forge**: 1.21.4-54.1.3
- **Java**: Version 21
- **LLM Backend**: Ollama API (supports various local models)
- **Networking**: Java HTTP client for API communication

## 🚀 Getting Started

### Prerequisites

- Minecraft: Java Edition 1.21.4
- Forge 54.1.3 or newer
- [Ollama](https://ollama.ai/) installed and running locally
- At least one LLM model pulled in Ollama (default: Qwen2.5 32B)

### Installation

1. Install [Minecraft Forge](https://files.minecraftforge.net/) for version 1.21.4
2. Download the latest LLMCraft mod JAR from the [Releases page](https://github.com/kayroye/LLMCraft/releases)
3. Place the JAR file in your Minecraft `mods` folder
4. Install [Ollama](https://ollama.ai/) on your computer
5. Pull the default model:
```bash
ollama pull qwen2.5:32b
```
6. Start the Ollama server:
```bash
ollama serve
```
7. Launch Minecraft with Forge

### Usage

1. Craft or give yourself the Position Reader item (currently found in the Building Blocks creative tab)
2. Hold the item and right-click to use it
3. The mod will send your current position, dimension, biome, and time of day to the LLM
4. Wait a moment for the response to appear in your Minecraft chat
5. Get contextual suggestions based on your in-game situation!

## 💡 Why LLMCraft?

LLMCraft bridges the gap between Minecraft gameplay and AI assistance. Instead of leaving the game to ask for help or look up information, players can get context-aware suggestions right in their Minecraft experience.

Key benefits:
- **Contextual Awareness**: The AI knows where you are and what's around you
- **Privacy-Focused**: Uses local LLMs instead of sending data to cloud services
- **Customizable**: Choose different models and customize how they respond
- **Immersive**: Get help without breaking game immersion by alt-tabbing

## 🔮 Future Plans

- Add more context to LLM prompts (inventory contents, nearby entities, etc.)
- Create additional items for different types of AI assistance
- Support for server-side integration
- Add configuration GUI for easier setup
- Support for more LLM providers beyond Ollama

## 📄 License

This project is licensed under All Rights Reserved.

## Acknowledgments

- [Minecraft Forge](https://files.minecraftforge.net/) for the modding framework
- [Ollama](https://ollama.ai/) for making local LLMs accessible
