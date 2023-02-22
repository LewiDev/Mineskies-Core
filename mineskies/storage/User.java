package me.lewi.dev.mineskies.storage;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.UUID;

@Getter
@Setter
public class User {

    private UUID uuid;
    private String name;
    private int minecoins;
    private int money;
    private int crystals;

    public User() {
    }

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public User load(Document document) {
        this.uuid = document.get("uuid", UUID.class);
        this.name = document.getString("name");
        this.minecoins = document.getInteger("minecoins");
        this.money = document.getInteger("money");
        this.crystals = document.getInteger("crystals");
        return this;
    }

    public Document save() {
        Document document = new Document();
        document.put("uuid", this.uuid);
        document.put("name", this.name);
        document.put("minecoins", this.minecoins);
        document.put("money", this.money);
        document.put("crystals", this.crystals);
        return document;
    }

}
