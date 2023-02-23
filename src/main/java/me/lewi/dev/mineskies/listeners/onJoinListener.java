package me.lewi.dev.mineskies.listeners;

import me.lewi.dev.mineskies.Core;
import me.lewi.dev.mineskies.managers.UserManager;
import me.lewi.dev.mineskies.storage.User;
import org.bukkit.entity.Player;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class onJoinListener implements Listener {

    private Core core;

    public onJoinListener(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!player.hasPlayedBefore()) {
            User user = new User(player.getUniqueId(), player.getName());
            user.setCrystals(0);
            user.setMoney(0);
            user.setMinecoins(0);
            UserManager.getInstance(core).save(user.save());

        }
    }

}
