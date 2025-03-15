package me.rowan.doublelife.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class help {

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(
                ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "Double Life Help: (Page 1/1)" + ChatColor.RESET +
                        ChatColor.GREEN + "\n\n/dl help <page> - " +
                        ChatColor.GRAY + "Shows this list." +
                        ChatColor.GREEN + "\n\n/dl reload - " +
                        ChatColor.GRAY + "Reloads the configuration file so that you can apply changed settings without having to restart or reload the entire server!" +
                        ChatColor.GREEN + "\n\n/dl config <setting> <value> - " +
                        ChatColor.GRAY + "Allows you to make changes to the config.yml file from within the game! (Reloads Automatically)" +
                        ChatColor.GREEN + "\n\n/dl setup - " +
                        ChatColor.GRAY + "Changes the gamerules to the configured values (config.yml) and sets the world's spawn at your current location." +
                        ChatColor.GREEN + "\n\n/dl randomizepairs - " +
                        ChatColor.GRAY + "Assigns every player to a random soulmate." +
                        ChatColor.GREEN + "\n\n/dl pair <player1> <player2> <lives> - " +
                        ChatColor.GRAY + "Pairs the two specified players together and gives them the specified amount of lives." +
                        ChatColor.GREEN + "\n\n/dl unpair <player> - " +
                        ChatColor.GRAY + "Disbands the pair of the specified player." +
                        ChatColor.GREEN + "\n\n/dl setLives <player> <lives> - " +
                        ChatColor.GRAY + "Sets the amount of lives the specified player has to the given value (1-3)."
        );

        return true;
    }

}
