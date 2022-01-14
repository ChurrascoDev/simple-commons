package com.github.imthenico.simplecommons.data.node;

import java.util.Map;
import java.util.Optional;

public class ImmutableTreeNode extends DelegatedNode {

    public ImmutableTreeNode(TreeNode delegate) {
        super(delegate);
    }

    @Override
    public void set(Map<String, Object> values) {
        throwException();
    }

    @Override
    public Optional<TreeNode> createNode(String path) {
        return throwException();
    }

    @Override
    public TreeNode getOrCreate(String path) {
        return throwException();
    }

    @Override
    public boolean mutable() {
        return false;
    }

    private <T> T throwException() {
        throw new UnsupportedOperationException("Immutable Node");
    }
}