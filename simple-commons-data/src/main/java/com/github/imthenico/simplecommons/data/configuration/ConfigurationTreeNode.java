package com.github.imthenico.simplecommons.data.configuration;

import com.github.imthenico.simplecommons.data.key.SimpleSourceKey;
import com.github.imthenico.simplecommons.data.node.DelegatedNode;
import com.github.imthenico.simplecommons.data.node.ImmutableTreeNode;
import com.github.imthenico.simplecommons.data.node.TreeNode;
import com.github.imthenico.simplecommons.data.mapper.GenericMapper;
import com.github.imthenico.simplecommons.util.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationTreeNode extends DelegatedNode {

    private final ConfigurationTreeNodeSource source;

    public ConfigurationTreeNode(
            TreeNode delegate,
            ConfigurationTreeNodeSource source
    ) {
        super(delegate);
        this.source = Validate.notNull(source);
    }

    public ConfigurationTreeNodeSource getSource() {
        return source;
    }

    @NotNull
    public static ConfigurationTreeNode create(ConfigurationTreeNodeSource source) {
        return new ConfigurationTreeNode(TreeNode.create(), source);
    }

    @NotNull
    public static ConfigurationTreeNode immutable(TreeNode treeNode, ConfigurationTreeNodeSource source) {
        return new ConfigurationTreeNode(new ImmutableTreeNode(treeNode), source);
    }

    @NotNull
    public static ConfigurationTreeNode load(GenericMapper<String> mapper, File file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        StringBuilder stringBuilder = new StringBuilder();

        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        String lines = stringBuilder.toString();

        return new ConfigurationTreeNode(mapper.map(lines, TreeNode.class), new ConfigurationTreeNodeSource(lines, new SimpleSourceKey(file.getName(), "parentPath", file.getParent())));
    }
}