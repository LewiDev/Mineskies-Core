package me.lewi.dev.mineskies.commands.currency.crystals;

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

public class CrystalsUtility {

    public static void addCrystals(Core core, Player player, int amount) {
        CompletableFuture<User> user = UserManager.getInstance(core).load(player.getUniqueId(), player.getName());
        user.thenAccept((u) -> {
            int newcrystals = u.getCrystals() + amount;
            u.setCrystals(newcrystals);
            UserManager.getInstance(core).save(u.save());
        });
    }

    public static void removeCrystals(Core core, Player player, int amount) {
        CompletableFuture<User> user = UserManager.getInstance(core).load(player.getUniqueId(), player.getName());
        user.thenAccept((u) -> {
            int newcrystals = u.getCrystals() - amount;
            u.setCrystals(newcrystals);
            UserManager.getInstance(core).save(u.save());
        });
    }

    public static int getCrystals(Core core, Player player) {
        CompletableFuture<User> user = UserManager.getInstance(core).load(player.getUniqueId(), player.getName());
        return user.thenApply((u) -> u.getCrystals()).join();
    }

    public static void resetCrystals(Core core, Player player) {
        CompletableFuture<User> user = UserManager.getInstance(core).load(player.getUniqueId(), player.getName());
        user.thenAccept((u) -> {
            u.setCrystals(0);
            UserManager.getInstance(core).save(u.save());
        });
    }

    public static ItemStack withdrawCrystals(Core core, Player player, int amount) {
        int crystals = getCrystals(core, player);
        if(crystals < amount) return null;
        UUID uuid = UUID.randomUUID();

        Withdraw withdraw = new Withdraw(uuid, amount, WithdrawTypes.CRYSTALS);
        WithdrawManager.getInstance(core).save(withdraw.save());

        removeCrystals(core, player, amount);

        ItemStack note = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = note.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Crystal Note");
        meta.getPersistentDataContainer().set(new NamespacedKey(core, "uuid"), PersistentDataType.STRING, uuid.toString());
        meta.getPersistentDataContainer().set(new NamespacedKey(core, "type"), PersistentDataType.STRING, WithdrawTypes.CRYSTALS.name());
        meta.getPersistentDataContainer().set(new NamespacedKey(core, "amount"), PersistentDataType.INTEGER, amount);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Amount: " + ChatColor.LIGHT_PURPLE + amount);
        lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Withdrawn by: " + ChatColor.LIGHT_PURPLE + player.getName());
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        note.setItemMeta(meta);
        return note;
    }

}
