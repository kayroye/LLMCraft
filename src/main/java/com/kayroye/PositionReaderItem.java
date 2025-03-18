package com.kayroye;

import net.minecraft.world.item.Item;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;


public class PositionReaderItem extends Item {
    public PositionReaderItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide) {
            double playerX = player.getX();
            double playerY = player.getY();
            double playerZ = player.getZ();
            String message = String.format("[LLMCraft] Position: %.1f, %.1f, %.1f", playerX, playerY, playerZ);
            Minecraft.getInstance().gui.getChat().addMessage(Component.literal(message));

            // Try to write position to file
            try {
                Files.writeString(Paths.get("position.txt"), message);
                Minecraft.getInstance().gui.getChat().addMessage(Component.literal("[LLMCraft] Position saved to position.txt"));
            } catch (IOException e) {
                Minecraft.getInstance().gui.getChat().addMessage(Component.literal("[LLMCraft] Error saving position: " + e.getMessage()));
            }
        }
        return InteractionResult.SUCCESS;
    }

    
}
