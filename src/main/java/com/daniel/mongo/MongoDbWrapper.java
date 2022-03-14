package com.daniel.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.daniel.mongo.recorder.RecordableAction;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class MongoDbWrapper {

    private final MongoClient client;
    private final String database;
    private final String collection;

    public MongoDbWrapper(MongoClient client, String database, String collection) {
        this.client = client;
        this.database = database;
        this.collection = collection;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    public RecordableAction insertMany(final List<Document> docs) {
        return new RecordableAction() {
            @Override
            public String name() {
                return "insertMany";
            }

            @Override
            public void run() {
                MongoDatabase database = client.getDatabase(getDatabase());
                MongoCollection<Document> collection = database.getCollection(getCollection());
                collection.insertMany(docs);
            }
        };
    }

    public List<Document> documentsToInsert(int max) {

        int count = 0;
        List<Document> result = new ArrayList<>(count);

        while (count++ < max) {

            Random r = new Random();
            int loginAttempts = r.nextInt(Integer.MAX_VALUE);

            Document doc = new Document("grant_type", "password")
                            .append("id", randomIp())
                            .append("login_attempts", loginAttempts)
                            .append("failed_logins", r.nextInt(loginAttempts));

            result.add(doc);
        }

        return result;
    }
    
    private String randomIp() {
        Random r = new Random();
        return r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
    }
    
}
