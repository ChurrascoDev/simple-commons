package com.github.imthenico.simplecommons.bukkit.configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.imthenico.simplecommons.bukkit.util.TextColorApplier.color;

public class Configuration extends YamlConfiguration {

    private final Plugin plugin;
    private final File file;
    private final String resourceFileName;

    public Configuration(Plugin plugin, String filename, String fileExtension, File folder, String resourceFolder) {
        this.plugin = plugin;
        String fileName = filename + (filename.endsWith(fileExtension) ? "" : fileExtension);
        this.file = new File(folder, fileName);
        this.resourceFileName = !Objects.equals(resourceFolder, "") ? file.getName() + File.separator + resourceFolder : file.getName();
        this.createFile();
    }

    public Configuration(Plugin plugin, File file, String resourceFolder) {
        this(plugin, file.getName(), ".yml", file.getParentFile(), resourceFolder);
    }

    public Configuration(Plugin plugin, String fileName, String fileExtension, String resourceFolder) {
        this(plugin, fileName, fileExtension, plugin.getDataFolder(), resourceFolder);
    }

    public Configuration(Plugin plugin, String fileName, String fileExtension) {
        this(plugin, fileName, fileExtension, plugin.getDataFolder(), "");
    }

    public Configuration(Plugin plugin, String fileName) {
        this(plugin, fileName, ".yml", plugin.getDataFolder(), "");
    }

    public Configuration(Plugin plugin, File file) {
        this(plugin, file, "");
    }

    private void createFile() {
        try {
            if (!file.exists()) {
                if (this.plugin.getResource(resourceFileName) != null) {
                    this.plugin.saveResource(resourceFileName, false);
                } else {
                    this.save(file);
                }
                this.load(file);
                return;
            }
            this.load(file);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getString(String path) {
        String s = super.getString(path);
        return s != null ? color(s) : null;
    }

    @Override
    public String getString(String path, String def) {
        String s = super.getString(path, def);

        return s != null ? color(s) : def;
    }

    @Override
    public int getInt(String path, int def) {
        int a = super.getInt(path, def);
        return a > 0 ? a : def;
    }

    public List<String> getStringList(String path, String... def) {
        List<String> list = super.getStringList(path);

        if (list.isEmpty()) {
            list = Arrays.asList(color(def));
        } else {
            color(list);
        }

        return list;
    }

    public void setMap(Map<String, Object> objectMap) {
        objectMap.forEach(this::set);
    }

    public File getFile() {
        return file;
    }
}