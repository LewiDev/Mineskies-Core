package me.lewi.dev.mineskies;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import me.lewi.dev.mineskies.commands.currency.crystals.CrystalCommand;
import me.lewi.dev.mineskies.commands.currency.minecoins.MinecoinCommands;
import me.lewi.dev.mineskies.commands.currency.money.MoneyCommands;
import me.lewi.dev.mineskies.handlers.MongoHandler;
import me.lewi.dev.mineskies.listeners.noteListener;
import me.lewi.dev.mineskies.listeners.onJoinListener;
import me.lewi.dev.mineskies.managers.ConfigManager;
import me.lewi.dev.mineskies.managers.UserManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin {


    private MongoHandler mongoHandler;

    @Getter
    private MongoCollection players;
    @Getter
    private MongoCollection notes;

    @Override
    public void onEnable() {
        ConfigManager.setupConfig(this);
        mongoHandler = new MongoHandler(this);
        mongoHandler.open();
        this.players = mongoHandler.getPlayers();
        this.notes = mongoHandler.getNotes();

        this.getServer().getPluginManager().registerEvents(new noteListener(this), this);
        this.getServer().getPluginManager().registerEvents(new onJoinListener(new UserManager(this)), this);

        this.getCommand("crystals").setExecutor(new CrystalCommand(this));
        this.getCommand("minecoins").setExecutor(new MinecoinCommands(this));
        this.getCommand("balance").setExecutor(new MoneyCommands(this));
    }

    @Override
    public void onDisable() {
        mongoHandler.close();
    }

}
