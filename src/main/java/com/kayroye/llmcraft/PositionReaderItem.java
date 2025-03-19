package com.kayroye.llmcraft;

import net.minecraft.world.item.Item;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
import java.net.URL;
import java.net.URI;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.core.BlockPos;

public class PositionReaderItem extends Item {
    public PositionReaderItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if(level.isClientSide) {
            // Get the player co-ordinates
            double playerX = player.getX();
            double playerY = player.getY();
            double playerZ = player.getZ();
            // Convert the player's position to a BlockPos
            BlockPos playerPos = new BlockPos((int)playerX, (int)playerY, (int)playerZ);
            // Get the dimension from the level
            String dimension = level.dimension().location().toString();
            // Get the player's biome
            String biome = level.getBiome(playerPos).toString();
            // Get the time of day
            String timeOfDay = String.valueOf(level.getDayTime() % 24000);
            // Format the message
            String message = String.format("Position: X: %.1f, Y: %.1f, Z: %.1f in Dimension: %s, Biome: %s, Time of Day: %s", playerX, playerY, playerZ, dimension, biome, timeOfDay);
            
            // Run HTTP request in a separate thread to avoid blocking the game
            new Thread(() -> {
                try {
                    Minecraft.getInstance().execute(() -> {
                        Minecraft.getInstance().gui.getChat().addMessage(
                            Component.literal("Asking LLM for suggestions...")
                        );
                    });
                    
                    String model = "qwen2.5:32b";
                    String prompt = "The player's position is " + message +
                        ".";
                    
                    String systemPrompt = "You are an AI assistant in Minecraft. Consider the X, Y, Z coordinates and the dimension. Suggest a safe and logical next action " +
                        ". Avoid nonsensical actions and be concise. Your message will be displayed to the player in chat.";
                    
                    // Set up the connection to the Ollama API (default port is 11434)
                    URL url = URI.create("http://127.0.0.1:11434/api/generate").toURL();
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(30000);
                    
                    // Create JSON request body
                    // This was the only way to properly construct the request body in JSON (that i could find)
                    StringBuilder requestBodyBuilder = new StringBuilder();
                    requestBodyBuilder.append("{");
                    requestBodyBuilder.append("\"model\": \"").append(model).append("\",");
                    requestBodyBuilder.append("\"prompt\": \"").append(escapeJson(prompt)).append("\",");
                    requestBodyBuilder.append("\"system\": \"").append(escapeJson(systemPrompt)).append("\",");
                    requestBodyBuilder.append("\"stream\": false");
                    requestBodyBuilder.append("}");
                    
                    String requestBody = requestBodyBuilder.toString();
                    
                    // Send the request
                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
                    
                    // Get the response
                    int responseCode = connection.getResponseCode();
                    
                    // Handle the response
                    if (responseCode >= 200 && responseCode < 300) {
                        // Read the successful response
                        StringBuilder responseBuilder = new StringBuilder();
                        try (BufferedReader br = new BufferedReader(
                                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                responseBuilder.append(line);
                            }
                        }
                        
                        final String responseBody = responseBuilder.toString();
                        
                        Minecraft.getInstance().execute(() -> {
                            try {
                                // Extract just the response field
                                if (responseBody.contains("\"response\":")) {
                                    int startIdx = responseBody.indexOf("\"response\":") + "\"response\":".length();
                                    int endIdx = responseBody.indexOf("\"", startIdx + 1);
                                    if (startIdx > 0 && endIdx > startIdx) {
                                        String llmResponse = responseBody.substring(startIdx + 1, endIdx);
                                        
                                        Minecraft.getInstance().gui.getChat().addMessage(
                                            Component.literal("LLM Suggestion: " + llmResponse)
                                        );
                                    }
                                }
                            } catch (Exception e) {
                                Minecraft.getInstance().gui.getChat().addMessage(
                                    Component.literal("Error processing LLM response")
                                );
                            }
                        });
                    } else {
                        Minecraft.getInstance().execute(() -> {
                            Minecraft.getInstance().gui.getChat().addMessage(
                                Component.literal("Failed to get a response from LLM")
                            );
                        });
                    }
                    
                    connection.disconnect();
                    
                } catch (Exception e) {
                    Minecraft.getInstance().execute(() -> {
                        Minecraft.getInstance().gui.getChat().addMessage(
                            Component.literal("Error connecting to LLM service")
                        );
                    });
                }
            }).start();
        }
        return InteractionResult.SUCCESS;
    }

    // Helper method to properly escape JSON strings
    private static String escapeJson(String input) {
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
