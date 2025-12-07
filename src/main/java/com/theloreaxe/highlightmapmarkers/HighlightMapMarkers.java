package com.theloreaxe.highlightmapmarkers;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("highlightmapmarkers")
public class HighlightMapMarkers {

    public static final String MOD_ID = "highlightmapmarkers";

    public HighlightMapMarkers() {
        // 1. Register Config
        ModLoadingContext.get().registerConfig(Type.CLIENT, com.theloreaxe.highlightmapmarkers.ModConfig.CLIENT_SPEC);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // 2. Initialize Keybinds
        KeyInit.init();
    }
}