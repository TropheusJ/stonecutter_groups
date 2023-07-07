package io.github.tropheusj.stonecutter_groups;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.resources.ResourceLocation;

public class StonecutterGroups {
    public static final String ID = "stonecutter_groups";
    public static final String NAME = "Stonecutter Groups";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static void init() {
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(ID, path);
    }
}
