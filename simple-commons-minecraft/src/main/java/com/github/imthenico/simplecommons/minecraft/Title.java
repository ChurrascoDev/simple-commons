package com.github.imthenico.simplecommons.minecraft;

import com.github.imthenico.simplecommons.util.Validate;

import java.util.HashMap;
import java.util.Map;

public class Title {

    private final String title;
    private final String subTitle;
    private final int fadeIn, stay, fadeOut;

    public Title(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> objectMap = new HashMap<>();

        objectMap.put("title", Validate.defIfNull(title, ""));
        objectMap.put("subtitle", Validate.defIfNull(subTitle, ""));
        objectMap.put("fadeIn", fadeIn);
        objectMap.put("stay", stay);
        objectMap.put("fadeOut", fadeOut);

        return objectMap;
    }

    public static Title deserialize(Map<String, Object> objectMap) {
        return new Title(
                (String) objectMap.get("title"),
                (String) objectMap.get("subTitle"),
                (int) objectMap.get("fadeIn"),
                (int) objectMap.get("stay"),
                (int) objectMap.get("fadeOut")
        );
    }
}