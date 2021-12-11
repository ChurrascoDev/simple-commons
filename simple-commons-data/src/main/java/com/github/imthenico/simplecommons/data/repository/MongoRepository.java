package com.github.imthenico.simplecommons.data.repository;

import com.github.imthenico.simplecommons.data.repository.exception.SerializationException;
import com.github.imthenico.simplecommons.util.Validate;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.logging.Level;

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
    public void save(T obj, String key) {
        try {
            Map<String, Object> fields = mapper.serializeFields(obj);

            collection.replaceOne(Filters.eq("_id", key), new Document(fields), new ReplaceOptions().upsert(true));
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String key) {
        BasicDBObject bson = new BasicDBObject("_id", key);
        DeleteResult deleteResult = collection.deleteOne(bson);

        if (deleteResult.getDeletedCount() <= 0) {
            REPOSITORY_LOGGER.log(Level.WARNING, String.format("unable to delete: no registry found ('%s')", key));
        }
    }

    @Override
    public T usingId(String key) {
        Document result = collection.find(new BasicDBObject("_id", key)).first();

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
    public Set<String> keys() {
        Set<String> keys = new HashSet<>();

        for (Document document : collection.find().projection(Projections.include("_id"))) {
            keys.add(document.get("_id").toString());
        }

        return keys;
    }
}