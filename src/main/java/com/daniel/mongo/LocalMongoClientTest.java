package com.daniel.mongo;

import com.daniel.mongo.recorder.RecordableAction;
import com.daniel.mongo.recorder.Recorder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class LocalMongoClientTest {

    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "mongodb://localhost:27017";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDbWrapper wrapper = new MongoDbWrapper(mongoClient, "loginstatistics", "ip_based", false);

//            List<Document> documents = wrapper.documentsToInsert(1000000);
//            RecordableAction insertAction = wrapper.insertMany(documents);
//            Recorder.record(insertAction);

            RecordableAction queryAction = wrapper.queryCollection();

            int count = 10;
            for (int i = 0; i < count; i++) {
                Recorder.record(queryAction);
            }
            Recorder.print();
        }
    }

}
