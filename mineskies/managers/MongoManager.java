package me.lewi.dev.mineskies.managers;

import com.mongodb.client.model.Filters;
import me.lewi.dev.mineskies.Core;

import me.lewi.dev.mineskies.storage.User;
import org.bson.Document;

import java.util.UUID;

public class MongoManager {

    private Core core;

    public MongoManager(Core core) {
        this.core = core;
    }

    public User load(UUID uuid, String name) {
        Document document = (Document) core.getPlayers().find(Filters.eq("uuid", uuid)).first();
        if (document == null) return new User(uuid, name);
        User user = new User().load(document);
        return user;
    }

    public void save(User user) {
        if(core.getPlayers().find(Filters.eq("uuid", user.getUuid())).first() == null) {
            core.getPlayers().replaceOne(Filters.eq("uuid", user.getUuid()), user.save());
            return;
        }
        core.getPlayers().insertOne(user.save());

    }

    public void delete(User user) {
        core.getPlayers().deleteOne(Filters.eq("uuid", user.getUuid()));
    }



}
