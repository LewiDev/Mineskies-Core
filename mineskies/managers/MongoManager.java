package me.lewi.dev.mineskies.managers;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.lewi.dev.mineskies.Core;

import me.lewi.dev.mineskies.storage.User;
import org.bson.Document;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MongoManager {

    private Core core;

    public MongoManager(Core core) {
        this.core = core;
    }

    public CompletableFuture<User> load(UUID uuid, String name) {
            return CompletableFuture.supplyAsync(() -> {
                Document document = (Document) core.getPlayers().find(Filters.eq("uuid", uuid)).first();
                if (document == null) return new User(uuid, name);
                User user = new User().load(document);
                return user;
            });
    }

    public void save(User user) {
        CompletableFuture.runAsync(() -> {
            core.getPlayers().replaceOne(Filters.eq("uuid", user.getUuid()), user.save(), new ReplaceOptions().upsert(true));
        });
    }

    public void delete(User user) {
        CompletableFuture.runAsync(() -> {
            core.getPlayers().deleteOne(Filters.eq("uuid", user.getUuid()));
        });
    }



}
