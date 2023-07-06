package io.github.tropheusj.example_mod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod {
    public static final String ID = "example_mod";
    public static final String NAME = "Example Mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static void init() {
        LOGGER.info(NAME + " loaded!");
    }
}
