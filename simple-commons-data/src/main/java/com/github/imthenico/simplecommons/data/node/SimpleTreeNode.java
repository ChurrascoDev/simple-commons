package com.github.imthenico.simplecommons.data.node;

import com.github.imthenico.simplecommons.data.node.value.SimpleNodeValue;
import com.github.imthenico.simplecommons.data.node.value.SimpleNodeValueList;
import com.github.imthenico.simplecommons.util.Validate;
import com.github.imthenico.simplecommons.value.*;

import java.util.*;

@SuppressWarnings("deprecation")
public class SimpleTreeNode implements TreeNode {

    private final Map<String, NodeValue> valueMap;
    private final TreeNode parent;
    private final TreeNode root;

    public SimpleTreeNode(
            TreeNode parent,
            TreeNode root
    ) {
        this.parent = parent;
        this.root = root;
        this.valueMap = new LinkedHashMap<>();
    }

    @Override
    public FindResult find(String targetPath) {
        NodeValue found = get(targetPath);

        if (found.isNull())
            return new FindResultImpl(Collections.emptyList());

        if (found.getAsArray().isPresent())
            return new FindResultImpl(found.getAsArray().get());

        return new FindResultImpl(Collections.singletonList(found));
    }

    @Override
    public FindResult all() {
        return new FindResultImpl(Collections.singletonList(new SimpleNodeValue(this)));
    }

    @Override
    public NodeValue get(String path) {
        int i = path.lastIndexOf(".");

        NodeValue value = null;
        if (i != -1) {
            String lastPathKey = path.substring(i + 1);
            String nodePath = path.substring(0, i);

            Optional<TreeNode> possibleNode = get(nodePath).getAsNode();

            if (possibleNode.isPresent()) {
                TreeNode node = possibleNode.get();

                value = node.get(lastPathKey);
            }
        } else {
            value = valueMap.get(path);
        }

        return Validate.defIfNull(value, SimpleNodeValue.EMPTY);
    }

    @Override
    public String getString(String path) {
        return find(path).getOrDefault(0, null);
    }

    @Override
    public int getInt(String path) {
        return find(path).getOrDefault(0, 0);
    }

    @Override
    public int getDouble(String path) {
        return find(path).getOrDefault(0, 0);
    }

    @Override
    public long getLong(String path) {
        return find(path).getOrDefault(0, 0);
    }

    @Override
    public byte getByte(String path) {
        return find(path).getOrDefault(0, (byte) 0);
    }

    @Override
    public float getFloat(String path) {
        return find(path).getOrDefault(0, 0);
    }

    @Override
    public boolean getBoolean(String path) {
        return find(path).getOrDefault(0, false);
    }

    @Override
    public <T> List<T> getList(String path) {
        return find(path).toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(String path, Object value) {
        int i = path.lastIndexOf(".");

        if (i != -1) {
            String lastPathKey = path.substring(i + 1);
            String nodePath = path.substring(0, i);

            getOrCreate(nodePath).set(lastPathKey, value);
        } else {
            if (value instanceof Map) {
                TreeNode node = getOrCreate(path);

                node.set((Map<String, Object>) value);
                return;
            }

            NodeValue nodeValue = null;

            if (value != null) {
                nodeValue = interceptValue(value);
            }

            internalValueCreation(path, nodeValue);
        }
    }

    @Override
    public void set(Map<String, Object> values) {
        values.forEach(this::set);
    }

    @Override
    public Optional<TreeNode> getNode(String path) {
        return get(path).getAsNode();
    }

    @Override
    public Optional<TreeNode> createNode(String path) {
        int i = path.lastIndexOf(".");

        if (i != -1) {
            String lastPathKey = path.substring(i + 1);
            String nodePath = path.substring(0, i);
            TreeNode temp = this;

            Optional<TreeNode> possibleNode = temp.createNode(nodePath);

            if (possibleNode.isPresent()) {
                temp = possibleNode.get();
            }

            return temp != this ? temp.createNode(lastPathKey) : Optional.empty();
        } else {
            if (getNode(path).isPresent()) {
                return Optional.empty();
            }

            TreeNode node = new SimpleTreeNode(this, root);
            internalValueCreation(path, new SimpleNodeValue(node));

            return Optional.of(node);
        }
    }

    @Override
    public TreeNode getOrCreate(String path) {
        Optional<TreeNode> possibleNode = getNode(path);

        if (!possibleNode.isPresent()) {
            possibleNode = createNode(path);
        }

        return possibleNode.orElse(this);
    }

    @Override
    public TreeNode parent() {
        return parent;
    }

    @Override
    public TreeNode root() {
        return Validate.defIfNull(root, this);
    }

    @Override
    public boolean mutable() {
        return true;
    }

    @Override
    public Map<String, NodeValue> simple() {
        return Collections.unmodifiableMap(valueMap);
    }

    @Override
    public Set<String> keys() {
        return Collections.unmodifiableSet(valueMap.keySet());
    }

    private void internalValueCreation(String key, NodeValue value) {
        valueMap.put(key, value);
    }

    @SuppressWarnings("unchecked")
    protected NodeValueList interceptCollection(Collection<?> obj) {
        List<NodeValue> nodeValues = new ArrayList<>(obj.size());

        for (Object o : obj) {
            NodeValue nodeValue;
            if (o instanceof Map) {
                TreeNode treeNode = TreeNode.create();

                treeNode.set((Map<String, Object>) o);
                nodeValue = new SimpleNodeValue(treeNode);
            } else {
                nodeValue = interceptValue(o);
            }

            nodeValues.add(nodeValue);
        }

        return new SimpleNodeValueList(nodeValues);
    }

    protected NodeValue interceptValue(Object o) {
        if (o instanceof AbstractValue)
            return new SimpleNodeValue(((AbstractValue) o).getValue());

        if (o instanceof Collection<?>) {
            return new SimpleNodeValue(interceptCollection((Collection<?>) o));
        } else if (o.getClass().isArray()) {
            return new SimpleNodeValue(interceptCollection(Arrays.asList((Object[]) o)));
        } else if (o instanceof TreeNode) {
            return new SimpleNodeValue(TreeNode.copy((TreeNode) o));
        } else {
            return new SimpleNodeValue(new SimpleAbstractValue(o));
        }
    }
}