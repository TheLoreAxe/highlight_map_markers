package com.theloreaxe.highlightmapmarkers;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {
    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static class Client {
        public final ForgeConfigSpec.IntValue renderRadiusChunks;

        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            renderRadiusChunks = builder
                    .comment("The radius (in chunks) to search for map markers. Default is 3.")
                    .defineInRange("renderRadiusChunks", 3, 1, 32);
            builder.pop();
        }
    }
}