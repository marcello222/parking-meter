package com.fiap.parking.meter.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;


@Configuration
public class MongoConfig {

    @Bean
    CommandLineRunner initDatabase(MongoClient mongoClient) {
        return args -> {
            MongoDatabase database = mongoClient.getDatabase("parking-meter");

            if (collectionNotExists(database, "driver")) {
                database.createCollection("driver");
            }

            if (collectionNotExists(database, "parking")) {
                database.createCollection("parking");
            }

            if (collectionNotExists(database, "payment-method")) {
                database.createCollection("payment-method");
            }

            if (collectionNotExists(database, "vehicle")) {
                database.createCollection("vehicle");
            }
        };
    }

    private boolean collectionNotExists(MongoDatabase database, String collectionName) {
        for (String name : database.listCollectionNames()) {
            if (name.equalsIgnoreCase(collectionName)) {
                return false;
            }
        }
        return true;
    }
}
