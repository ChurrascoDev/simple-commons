package com.github.imthenico.simplecommons.bukkit.util;

import org.bukkit.ChatColor;

import java.util.List;

public interface TextColorApplier {

    static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    static String[] color(String[] text) {
        for (int i = 0; i < text.length; i++) {
            text[i] = color(text[i]);
        }

        return text;
    }

    static List<String> color(List<String> text) {
        text.replaceAll(TextColorApplier::color);

        return text;
    }
}