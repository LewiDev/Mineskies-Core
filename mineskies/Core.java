package me.lewi.dev.mineskies;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.lewi.dev.mineskies.handlers.MongoHandler;
import me.lewi.dev.mineskies.listeners.onJoinListener;
import me.lewi.dev.mineskies.managers.ConfigManager;
import me.lewi.dev.mineskies.managers.MongoManager;
import org.bson.Document;
import org.bukkit.plugin.java.JavaPlugin;

import javax.print.Doc;
import java.util.UUID;

public final class Core extends JavaPlugin {


    private MongoHandler mongoHandler;

    @Getter
    private MongoCollection players;

    @Override
    public void onEnable() {
        ConfigManager.setupConfig(this);
        mongoHandler = new MongoHandler(this);
        mongoHandler.open();
        this.players = mongoHandler.getPlayers();

        this.getServer().getPluginManager().registerEvents(new onJoinListener(new MongoManager(this)), this);
    }

    @Override
    public void onDisable() {
        mongoHandler.close();
    }

}
