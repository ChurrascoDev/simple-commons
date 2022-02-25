package com.github.imthenico.simplecommons.data.node;

import com.github.imthenico.simplecommons.util.Validate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DelegatedNode implements TreeNode {

    protected final TreeNode delegate;

    public DelegatedNode(TreeNode delegate) {
        this.delegate = Validate.notNull(delegate);
    }

    @Override
    public FindResult find(String targetPath) {
        return delegate.find(targetPath);
    }

    @Override
    public FindResult all() {
        return delegate.all();
    }

    @Override
    public NodeValue get(String path) {
        return delegate.get(path);
    }

    @Override
    public String getString(String path) {
        return delegate.getString(path);
    }

    @Override
    public int getInt(String path) {
        return delegate.getInt(path);
    }

    @Override
    public int getDouble(String path) {
        return delegate.getDouble(path);
    }

    @Override
    public long getLong(String path) {
        return delegate.getLong(path);
    }

    @Override
    public byte getByte(String path) {
        return delegate.getByte(path);
    }

    @Override
    public float getFloat(String path) {
        return delegate.getFloat(path);
    }

    @Override
    public boolean getBoolean(String path) {
        return delegate.getBoolean(path);
    }

    @Override
    public <T> List<T> getList(String path) {
        return delegate.getList(path);
    }

    @Override
    public void set(String path, Object value) {
        delegate.set(path, value);
    }

    @Override
    public void set(Map<String, Object> values) {
        delegate.set(values);
    }

    @Override
    public Optional<TreeNode> getNode(String path) {
        return delegate.getNode(path);
    }

    @Override
    public Optional<TreeNode> createNode(String path) {
        return delegate.createNode(path);
    }

    @Override
    public TreeNode getOrCreate(String path) {
        return delegate.getOrCreate(path);
    }

    @Override
    public TreeNode parent() {
        return delegate.parent();
    }

    @Override
    public TreeNode root() {
        return delegate.root();
    }

    @Override
    public boolean mutable() {
        return delegate.mutable();
    }

    @Override
    public Map<String, NodeValue> simple() {
        return delegate.simple();
    }

    @Override
    public Set<String> keys() {
        return delegate.keys();
    }
}