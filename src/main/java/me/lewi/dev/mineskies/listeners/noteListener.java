package me.lewi.dev.mineskies.listeners;


import me.lewi.dev.mineskies.Core;
import me.lewi.dev.mineskies.commands.currency.crystals.CrystalsUtility;
import me.lewi.dev.mineskies.handlers.MongoHandler;
import me.lewi.dev.mineskies.managers.UserManager;
import me.lewi.dev.mineskies.managers.WithdrawManager;
import me.lewi.dev.mineskies.storage.User;
import me.lewi.dev.mineskies.storage.Withdraw;
import me.lewi.dev.mineskies.storage.WithdrawTypes;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class noteListener implements Listener {

    private Core core;

    public noteListener(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (!(e.getItem().getType() == Material.PAPER)) return;
        if ((e.getItem().getItemMeta().getPersistentDataContainer().isEmpty())) return;
        User user = UserManager.getInstance(core).load(e.getPlayer().getUniqueId(), e.getPlayer().getName()).join();
        if (e.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(core, "uuid"), PersistentDataType.STRING)) {
            int amount = e.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(core, "amount"), PersistentDataType.INTEGER);
            if(WithdrawTypes.valueOf(e.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(core, "type"), PersistentDataType.STRING)) == WithdrawTypes.CRYSTALS) {
                user.setCrystals(user.getCrystals() + amount);
                e.getPlayer().sendMessage(ChatColor.GRAY + "You have deposited " + ChatColor.LIGHT_PURPLE + amount + ChatColor.GRAY + " Crystals.");
            } else if(WithdrawTypes.valueOf(e.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(core, "type"), PersistentDataType.STRING)) == WithdrawTypes.MONEY) {
                user.setMoney(user.getMoney() + amount);
                e.getPlayer().sendMessage(ChatColor.GRAY + "You have deposited " + ChatColor.AQUA + "$" + amount );
            } else if (WithdrawTypes.valueOf(e.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(core, "type"), PersistentDataType.STRING)) == WithdrawTypes.MINECOINS) {
                user.setMinecoins(user.getMinecoins() + amount);
                e.getPlayer().sendMessage(ChatColor.GRAY + "You have deposited " + ChatColor.AQUA + amount + ChatColor.GRAY + " Minecoins.");
            }
        } else {
            e.getPlayer().sendMessage(ChatColor.RED + "Invalid Note!");
        }
        e.getPlayer().getInventory().removeItem(e.getItem());
        UserManager.getInstance(core).save(user.save());
    }


}
