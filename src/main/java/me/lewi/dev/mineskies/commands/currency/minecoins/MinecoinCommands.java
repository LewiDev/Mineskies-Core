package me.lewi.dev.mineskies.commands.currency.minecoins;

import me.lewi.dev.mineskies.Core;
import me.lewi.dev.mineskies.commands.currency.crystals.CrystalsUtility;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MinecoinCommands implements CommandExecutor {

    private Core core;

    public MinecoinCommands(Core core) {
        this.core = core;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(ChatColor.GRAY + "You have " + ChatColor.AQUA + MinecoinUtility.getMinecoins(core, player) + ChatColor.GRAY + " minecoins.");

        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("withdraw")) {
                int amount = Integer.parseInt(args[1]);
                player.getInventory().addItem(MinecoinUtility.withdrawMinecoins(core, player, amount));
                player.sendMessage(ChatColor.GRAY + "You have Withdrawn " + ChatColor.AQUA + amount + ChatColor.GRAY + " minecoins.");
            } else {
                player.sendMessage("usage");
            }
        } else {
            player.sendMessage("usage");
        }

        return false;
    }
}
