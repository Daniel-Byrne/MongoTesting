package com.daniel.mongo;

import org.bson.Document;
import java.util.List;

import com.daniel.mongo.recorder.RecordableAction;
import com.daniel.mongo.recorder.Recorder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class LocalMongoClientTest {

    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "mongodb://localhost:27017";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDbWrapper wrapper = new MongoDbWrapper(mongoClient, "loginstatistics", "ip_based");

            List<Document> documents = wrapper.documentsToInsert(1000000);
            RecordableAction action = wrapper.insertMany(documents);
            Recorder.record(action);
        }
    }

}
