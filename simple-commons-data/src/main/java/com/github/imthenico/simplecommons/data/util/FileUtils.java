package com.github.imthenico.simplecommons.data.util;

import java.io.*;

public interface FileUtils {

    static String readTextFile(File file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }

    static void setTextContent(File file, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
            writer.flush();
        }
    }
}