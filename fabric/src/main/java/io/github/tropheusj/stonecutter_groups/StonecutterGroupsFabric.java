package io.github.tropheusj.stonecutter_groups;

import net.fabricmc.api.ModInitializer;

public class StonecutterGroupsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        StonecutterGroups.init();
    }
}
