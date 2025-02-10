package org.example.easymanage.DAO.ConnectionsDB;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnector {
    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    private static final String DATABASE = "EasyManage";

    public static MongoDatabase getDatabase() {
        MongoClient mongoClient = MongoClients.create("mongodb://" + HOST + ":" + PORT);
        return mongoClient.getDatabase(DATABASE);
    }
}
