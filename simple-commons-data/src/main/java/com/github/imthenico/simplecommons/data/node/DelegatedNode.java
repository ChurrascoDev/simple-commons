package com.github.imthenico.simplecommons.data.node;

import com.github.imthenico.simplecommons.util.Validate;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DelegatedNode implements TreeNode {

    protected final TreeNode delegate;

    public DelegatedNode(TreeNode delegate) {
        this.delegate = Validate.notNull(delegate);
    }

    @Override
    public NodeValue get(String path) {
        return delegate.get(path);
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