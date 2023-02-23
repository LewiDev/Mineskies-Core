package me.lewi.dev.mineskies.commands.currency.crystals;

import me.lewi.dev.mineskies.Core;
import me.lewi.dev.mineskies.managers.UserManager;
import me.lewi.dev.mineskies.storage.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class CrystalCommand implements CommandExecutor {

    private Core core;

    public CrystalCommand(Core core) {
        this.core = core;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(ChatColor.GRAY + "You have " + ChatColor.LIGHT_PURPLE + CrystalsUtility.getCrystals(core, player) + ChatColor.GRAY + " crystals.");

        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("withdraw")) {
                int amount = Integer.parseInt(args[1]);
                player.getInventory().addItem(CrystalsUtility.withdrawCrystals(core, player, amount));
                player.sendMessage(ChatColor.GRAY + "You have Withdrawn " + ChatColor.LIGHT_PURPLE + amount + ChatColor.GRAY + " crystals.");
            } else {
                player.sendMessage("usage");
            }
        } else {
            player.sendMessage("usage");
        }

        return false;
    }
}
