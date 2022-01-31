package com.github.imthenico.simplecommons.data.repository;

import com.github.imthenico.simplecommons.data.key.SimpleSourceKey;
import com.github.imthenico.simplecommons.data.key.SourceKey;
import com.github.imthenico.simplecommons.data.mapper.GenericMapper;
import com.github.imthenico.simplecommons.util.Validate;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

public class MongoRepository<T> extends AbstractRepository<T> {

    private final GenericMapper<String> mapper;
    private final MongoCollection<Document> collection;

    public MongoRepository(
            Executor taskProcessor,
            Class<T> modelClass,
            MongoCollection<Document> collection,
            GenericMapper<String> mapper
    ) {
        super(taskProcessor, modelClass);
        this.mapper = Validate.notNull(mapper);
        this.collection = Validate.notNull(collection);
    }

    @Override
    public void save(T obj, SourceKey key) {
        Map<String, Object> fields = new HashMap<>();

        mapper.toMap(obj).forEach((k, v) -> fields.put(k, v.getValue()));
        collection.replaceOne(Filters.eq("_id", key.getKey()), new Document(fields), new ReplaceOptions().upsert(true));
    }

    @Override
    public int delete(SourceKey key) throws IllegalArgumentException {
        BasicDBObject bson = new BasicDBObject("_id", key.getKey());
        DeleteResult deleteResult = collection.deleteOne(bson);

        return (int) deleteResult.getDeletedCount();
    }

    @Override
    public T usingId(SourceKey key) {
        Document result = collection.find(new BasicDBObject("_id", key.getKey())).first();

        if (result == null)
            return null;

        return mapper.map(result.toJson(), modelClass);
    }

    @Override
    public Set<T> all(){
        Set<T> collected = new HashSet<>();
        FindIterable<Document> allResults = collection.find();

        for (Document document : allResults) {
            collected.add(mapper.map(document.toJson(), modelClass));
        }

        return collected;
    }

    @Override
    public Set<SourceKey> keys() {
        Set<SourceKey> keys = new HashSet<>();

        for (Document document : collection.find().projection(Projections.include("_id"))) {
            keys.add(new SimpleSourceKey(document.get("_id").toString()));
        }

        return keys;
    }
}