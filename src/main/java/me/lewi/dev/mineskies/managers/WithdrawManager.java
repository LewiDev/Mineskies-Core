package me.lewi.dev.mineskies.managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.lewi.dev.mineskies.Core;

import me.lewi.dev.mineskies.storage.User;
import me.lewi.dev.mineskies.storage.Withdraw;
import me.lewi.dev.mineskies.storage.WithdrawTypes;
import org.bson.Document;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class WithdrawManager {

    private static MongoCollection notes;

    public WithdrawManager(Core core) {
        this.notes = core.getNotes();
    }

    public static CompletableFuture<Withdraw> load(UUID uuid, int amount, WithdrawTypes type) {
        return CompletableFuture.supplyAsync(() -> {
            Document document = (Document) notes.find(Filters.eq("uuid", uuid)).first();
            if (document == null) return new Withdraw(uuid, amount, type);
            Withdraw withdraw = new Withdraw().load(document);
            return withdraw;
        });
    }

    public static void save(Document document) {
        CompletableFuture.runAsync(() -> {
            notes.replaceOne(Filters.eq("uuid", document.get("uuid", UUID.class)), document, new ReplaceOptions().upsert(true));
        });
    }

    public static void delete(User user) {
        CompletableFuture.runAsync(() -> {
            notes.deleteOne(Filters.eq("uuid", user.getUuid()));
        });
    }



}
