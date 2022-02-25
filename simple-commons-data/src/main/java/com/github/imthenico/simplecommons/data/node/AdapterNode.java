package com.github.imthenico.simplecommons.data.node;

import com.github.imthenico.simplecommons.data.mapper.GenericMapper;
import com.github.imthenico.simplecommons.util.Validate;

public class AdapterNode extends DelegatedNode {

    private final GenericMapper<String> mapper;

    public AdapterNode(TreeNode delegate, GenericMapper<String> mapper) {
        super(delegate);
        this.mapper = Validate.notNull(mapper);
    }

    public MappableNodeValue prepareForMap(String path) {
        return new MappableNodeValue(get(path), mapper);
    }

    @Override
    public MappableFindResult find(String targetPath) {
        FindResult findResult = super.find(targetPath);

        return new MappableFindResult(findResult.internalCopy(), mapper);
    }
}