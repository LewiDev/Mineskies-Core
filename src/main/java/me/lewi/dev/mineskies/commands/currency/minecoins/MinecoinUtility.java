package me.lewi.dev.mineskies.commands.currency.minecoins;

import me.lewi.dev.mineskies.Core;
import me.lewi.dev.mineskies.managers.UserManager;
import me.lewi.dev.mineskies.managers.WithdrawManager;
import me.lewi.dev.mineskies.storage.User;
import me.lewi.dev.mineskies.storage.Withdraw;
import me.lewi.dev.mineskies.storage.WithdrawTypes;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MinecoinUtility {

    public static void addMinecoins(Player player, int amount) {
        CompletableFuture<User> user = UserManager.load(player.getUniqueId(), player.getName());
        user.thenAccept((u) -> {
            int newcoins = u.getMinecoins() + amount;
            u.setMinecoins(newcoins);
            UserManager.save(u.save());
        });
    }

    public static void removeMinecoins(Player player, int amount) {
        CompletableFuture<User> user = UserManager.load(player.getUniqueId(), player.getName());
        user.thenAccept((u) -> {
            int newcoins = u.getMinecoins() - amount;
            u.setMinecoins(newcoins);
            UserManager.save(u.save());
        });
    }

    public static int getMinecoins(Player player) {
        CompletableFuture<User> user = UserManager.load(player.getUniqueId(), player.getName());
        return user.thenApply((u) -> u.getMinecoins()).join();
    }

    public static void resetMinecoins(Player player) {
        CompletableFuture<User> user = UserManager.load(player.getUniqueId(), player.getName());
        user.thenAccept((u) -> {
            u.setMinecoins(0);
            UserManager.save(u.save());
        });
    }

    public static ItemStack withdrawMinecoins(Core core, Player player, int amount) {
        int minecoins = getMinecoins(player);
        if(minecoins < amount) return null;
        UUID uuid = UUID.randomUUID();

        Withdraw withdraw = new Withdraw(uuid, amount, WithdrawTypes.MINECOINS);
        WithdrawManager.save(withdraw.save());

        removeMinecoins(player, amount);

        ItemStack note = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = note.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Minecoin Note");
        meta.getPersistentDataContainer().set(new NamespacedKey(core, "uuid"), PersistentDataType.STRING, uuid.toString());
        meta.getPersistentDataContainer().set(new NamespacedKey(core, "type"), PersistentDataType.STRING, WithdrawTypes.MINECOINS.name());
        meta.getPersistentDataContainer().set(new NamespacedKey(core, "amount"), PersistentDataType.INTEGER, amount);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Amount: " + ChatColor.AQUA + amount);
        lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Withdrawn by: " + ChatColor.AQUA + player.getName());
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        note.setItemMeta(meta);
        return note;
    }

}
