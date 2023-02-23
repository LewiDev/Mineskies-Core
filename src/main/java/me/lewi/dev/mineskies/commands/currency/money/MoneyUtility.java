package me.lewi.dev.mineskies.commands.currency.money;

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

public class MoneyUtility {

    public static void addMoney(Core core, Player player, int amount) {
        CompletableFuture<User> user = UserManager.getInstance(core).load(player.getUniqueId(), player.getName());
        user.thenAccept((u) -> {
            int newMoney = u.getMoney() + amount;
            u.setMoney(newMoney);
            UserManager.getInstance(core).save(u.save());
        });
    }

    public static void removeMoney(Core core, Player player, int amount) {
        CompletableFuture<User> user = UserManager.getInstance(core).load(player.getUniqueId(), player.getName());
        user.thenAccept((u) -> {
            int newMoney = u.getMoney() - amount;
            u.setMoney(newMoney);
            UserManager.getInstance(core).save(u.save());
        });
    }

    public static int getMoney(Core core, Player player) {
        CompletableFuture<User> user = UserManager.getInstance(core).load(player.getUniqueId(), player.getName());
        return user.thenApply((u) -> u.getMoney()).join();
    }

    public static void resetMoney(Core core, Player player) {
        CompletableFuture<User> user = UserManager.getInstance(core).load(player.getUniqueId(), player.getName());
        user.thenAccept((u) -> {
            u.setMoney(0);
            UserManager.getInstance(core).save(u.save());
        });
    }

    public static ItemStack withdrawMoney(Core core, Player player, int amount) {
        int money = getMoney(core, player);
        if(money < amount) return null;
        UUID uuid = UUID.randomUUID();

        Withdraw withdraw = new Withdraw(uuid, amount, WithdrawTypes.MONEY);
        WithdrawManager.getInstance(core).save(withdraw.save());

        removeMoney(core, player, amount);

        ItemStack note = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = note.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Money Note");
        meta.getPersistentDataContainer().set(new NamespacedKey(core, "uuid"), PersistentDataType.STRING, uuid.toString());
        meta.getPersistentDataContainer().set(new NamespacedKey(core, "type"), PersistentDataType.STRING, WithdrawTypes.MONEY.name());
        meta.getPersistentDataContainer().set(new NamespacedKey(core, "amount"), PersistentDataType.INTEGER, amount);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Amount: " + ChatColor.GREEN + amount);
        lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Withdrawn by: " + ChatColor.GREEN + player.getName());
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        note.setItemMeta(meta);
        return note;
    }


}
