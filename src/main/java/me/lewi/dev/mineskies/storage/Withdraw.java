package me.lewi.dev.mineskies.storage;

import org.bson.Document;

import java.util.UUID;

public class Withdraw {

    private UUID uuid;
    private int withdrawAmount;
    private WithdrawTypes type;

    public Withdraw(UUID uuid, int withdrawAmount, WithdrawTypes type) {
        this.uuid = uuid;
        this.withdrawAmount = withdrawAmount;
        this.type = type;
    }

    public Withdraw() {

    }

    public Withdraw load(Document document) {
        this.uuid = document.get("uuid", UUID.class);
        this.withdrawAmount = document.get("withdrawAmount", Integer.class);
        this.type = WithdrawTypes.valueOf(document.get("type", String.class));
        return this;
    }

    public Document save() {
        Document document = new Document();
        document.put("uuid", this.uuid);
        document.put("withdrawAmount", this.withdrawAmount);
        document.put("type", this.type.name());
        return document;
    }

}
