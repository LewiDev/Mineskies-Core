package me.lewi.dev.mineskies.listeners;

import me.lewi.dev.mineskies.managers.MongoManager;
import me.lewi.dev.mineskies.storage.User;
import org.bukkit.entity.Player;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class onJoinListener implements Listener {

    private MongoManager mongoManager;

    public onJoinListener(MongoManager mongoManager) {
        this.mongoManager = mongoManager;
    }

    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(player.hasPlayedBefore()) {
            User user = new User(player.getUniqueId(), player.getName());
            user.setCrystals(0);
            user.setMoney(0);
            user.setMinecoins(0);
            user.save();

            mongoManager.save(user);

        }
    }

}
