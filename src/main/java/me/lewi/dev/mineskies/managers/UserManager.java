package me.lewi.dev.mineskies.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.lewi.dev.mineskies.Core;

import me.lewi.dev.mineskies.storage.User;
import org.bson.Document;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserManager {

    private static MongoCollection players;

    public UserManager(Core core) {
        this.players = core.getPlayers();
    }

    public static CompletableFuture<User> load(UUID uuid, String name) {
            return CompletableFuture.supplyAsync(() -> {
                Document document = (Document) players.find(Filters.eq("uuid", uuid)).first();
                if (document == null) return new User(uuid, name);
                User user = new User().load(document);
                return user;
            });
    }

    public static void save(Document document) {
        CompletableFuture.runAsync(() -> {
            players.replaceOne(Filters.eq("uuid", document.get("uuid", UUID.class)), document, new ReplaceOptions().upsert(true));
        });
    }

    public static void delete(User user) {
        CompletableFuture.runAsync(() -> {
            players.deleteOne(Filters.eq("uuid", user.getUuid()));
        });
    }



}
