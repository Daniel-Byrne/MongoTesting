package com.daniel.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.daniel.mongo.recorder.RecordableAction;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongoDbWrapper {

    private final MongoClient client;
    private final boolean hardcodedScore;
    private final String database;
    private final String collection;

    public MongoDbWrapper(MongoClient client, String database, String collection, boolean hardcodedScore) {
        this.client = client;
        this.database = database;
        this.collection = collection;
        this.hardcodedScore = hardcodedScore;
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

    public RecordableAction queryCollection() {
        return new RecordableAction() {
            @Override
            public String name() {
                return "queryCollection";
            }

            @Override
            public void run() {
                final Bson grantTypeFilter = Filters.eq("grant_type", "password");
                final Bson scoreFilter;

                if (hardcodedScore) {
                    scoreFilter = Filters.gte("score", Float.valueOf(0.51f));
                } else {
                    scoreFilter = Filters.expr(Document.parse(" { $gte: [ { $divide: [ '$failed_logins', '$login_attempts' ] }, 0.51 ] } "));
                }

                final Bson filter = Filters.and(grantTypeFilter, scoreFilter);

                MongoDatabase database = client.getDatabase(getDatabase());
                MongoCollection<Document> collection = database.getCollection(getCollection());

                final AtomicInteger ai = new AtomicInteger(0);
                final Consumer<Document> extractBlock = new Consumer<Document>() {
                    @Override
                    public void accept(final Document document) {
                        ai.getAndIncrement();
                    }
                };

                final FindIterable<Document> result = collection.find(filter);
                result.forEach(extractBlock);

                System.out.println("AtomicInteger: " + ai);
            }
        };
    }

    public List<Document> documentsToInsert(int max) {

        int count = 0;
        List<Document> result = new ArrayList<>(count);

        while (count++ < max) {

            Random r = new Random();
            Integer loginAttempts = Integer.valueOf(r.nextInt(Integer.MAX_VALUE));
            Integer failedAttempts = Integer.valueOf(r.nextInt(loginAttempts.intValue()));
            Float score = Float.valueOf((float) failedAttempts.intValue() / loginAttempts.intValue());

            Document doc = new Document("grant_type", "password")
                    .append("id", randomIp())
                    .append("login_attempts", loginAttempts)
                    .append("failed_logins", failedAttempts)
                    .append("score", score);

            result.add(doc);
        }

        return result;
    }

    private String randomIp() {
        Random r = new Random();
        return r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
    }

}
