package com.fiap.parking.meter.config.mongo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.util.Arrays;
import java.util.List;


@Configuration
public class MongoConfig {


    @Bean
    CommandLineRunner initDatabase(MongoClient mongoClient) {
        return args -> {
            MongoDatabase database = mongoClient.getDatabase("parking-meter");
            List<String> collections = Arrays.asList("driver", "parking", "payment-method", "vehicle");

            collections.stream().forEach(collectionName -> {
                if (collectionNotExists(database, collectionName)) {
                    database.createCollection(collectionName);
                }
            });
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
