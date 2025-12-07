package com.theloreaxe.highlightmapmarkers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.Color;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MapMarkerRenderer {

    private static final ResourceLocation TARGET_ENTITY_ID = new ResourceLocation("the_vault", "map_marker_tile_entity");

    // Default ON until toggled
    private static boolean enabled = true;

    // --- KEYBIND LOGIC ---
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyInit.toggleHighlightKey.consumeClick()) {
            enabled = !enabled;
            // Print status to chat
            Minecraft.getInstance().player.displayClientMessage(
                    new TextComponent("Map Markers: " + (enabled ? "ON" : "OFF")), true
            );
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        // Check if enabled
        if (!enabled) return;

        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        PoseStack poseStack = event.getPoseStack();
        Vec3 cameraPos = event.getCamera().getPosition();

        // Get Radius from Config
        int radius = ModConfig.CLIENT.renderRadiusChunks.get();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.lineWidth(1.0f); // CHANGED: Thickness to 1.0f
        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);

        ChunkPos playerChunk = mc.player.chunkPosition();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                LevelChunk chunk = mc.level.getChunk(playerChunk.x + x, playerChunk.z + z);

                for (BlockEntity be : chunk.getBlockEntities().values()) {
                    ResourceLocation typeName = ForgeRegistries.BLOCK_ENTITIES.getKey(be.getType());

                    if (typeName != null && typeName.equals(TARGET_ENTITY_ID)) {

                        BlockPos pos = be.getBlockPos();

                        float r = 1.0f;
                        float g = 1.0f;
                        float b = 1.0f;

                        CompoundTag nbt = be.getUpdateTag();
                        if (nbt.contains("Colour")) {
                            int decimalColor = nbt.getInt("Colour");
                            if (decimalColor != 0) {
                                Color c = new Color(decimalColor);
                                r = c.getRed() / 255.0f;
                                g = c.getGreen() / 255.0f;
                                b = c.getBlue() / 255.0f;
                            }
                        }

                        poseStack.pushPose();
                        poseStack.translate(pos.getX() - cameraPos.x, pos.getY() - cameraPos.y, pos.getZ() - cameraPos.z);

                        LevelRenderer.renderLineBox(
                                poseStack,
                                buffer,
                                new AABB(0, 0, 0, 1, 1, 1).inflate(0.01),
                                r, g, b, 1.0f
                        );

                        poseStack.popPose();
                    }
                }
            }
        }

        tesselator.end();

        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
}