package com.github.imthenico.simplecommons.data.node.value;

import com.github.imthenico.simplecommons.data.node.NodeValue;
import com.github.imthenico.simplecommons.data.node.NodeValueList;
import com.github.imthenico.simplecommons.util.list.SimpleCustomList;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SimpleNodeValueList extends SimpleCustomList<NodeValue> implements NodeValueList {

    public SimpleNodeValueList(
            List<NodeValue> list
    ) {
        super(LinkedList::new, list);
    }

    public SimpleNodeValueList(NodeValue[] values) {
        this(Arrays.asList(values));
    }
}