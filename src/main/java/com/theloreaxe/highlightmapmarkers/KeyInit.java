package com.theloreaxe.highlightmapmarkers;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KeyInit {

    public static KeyMapping toggleHighlightKey;

    public static void init() {
        toggleHighlightKey = new KeyMapping(
                "key.highlightmapmarkers.toggle",
                InputConstants.Type.KEYSYM,
                InputConstants.UNKNOWN.getValue(), // No default key
                "key.categories.highlightmapmarkers"
        );

        ClientRegistry.registerKeyBinding(toggleHighlightKey);
    }
}