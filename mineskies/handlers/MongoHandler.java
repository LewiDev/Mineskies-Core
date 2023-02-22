package me.lewi.dev.mineskies.handlers;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.lewi.dev.mineskies.Core;
import me.lewi.dev.mineskies.managers.ConfigManager;
import org.bson.Document;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoHandler {

    private final Core core;
    private final ConnectionString uri;
    private MongoClient client;
    @Getter
    private MongoCollection<Document> players;

    public MongoHandler(Core core) {
        this.core = core;
        this.uri = new ConnectionString(ConfigManager.getConnectionString());
    }

    public void open() {
        Logger.getLogger("com.mongodb").setLevel(Level.ALL);
        this.client = MongoClients.create(this.uri);
        this.players = client.getDatabase("core").getCollection("players");
        core.getLogger().info(String.format("[Mongo] Successfully connected to %s database", uri.getDatabase()));
    }

    public void close() {
        this.client.close();
        core.getLogger().info(String.format("[Mongo] Successfully disconnected from %s database", uri.getDatabase()));
    }
}