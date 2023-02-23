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

    private static UserManager instance;

    private static MongoCollection<Document> players;

    private UserManager() {
    }

    public static UserManager getInstance(Core core) {
        if(instance == null) {
            instance = new UserManager();
            players = core.getNotes();
            return instance;

        }
        return instance;

    }

    public CompletableFuture<User> load(UUID uuid, String name) {
            return CompletableFuture.supplyAsync(() -> {
                Document document = (Document) players.find(Filters.eq("uuid", uuid)).first();
                if (document == null) return new User(uuid, name);
                User user = new User().load(document);
                return user;
            });
    }

    public void save(Document document) {
        CompletableFuture.runAsync(() -> {
            players.replaceOne(Filters.eq("uuid", document.get("uuid", UUID.class)), document, new ReplaceOptions().upsert(true));
        });
    }

    public void delete(User user) {
        CompletableFuture.runAsync(() -> {
            players.deleteOne(Filters.eq("uuid", user.getUuid()));
        });
    }



}
