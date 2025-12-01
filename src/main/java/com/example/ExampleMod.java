package com.example;

import java.util.logging.Logger;

public class ExampleMod {
    public static final String MOD_ID = "modid";
    public static final Logger LOGGER = Logger.getLogger(MOD_ID);

    public static void initialize() {
        LOGGER.info("Hello world!");
    }
}
