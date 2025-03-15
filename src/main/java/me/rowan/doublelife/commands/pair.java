package me.rowan.doublelife.commands;

import me.rowan.doublelife.data.SaveHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class pair {

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3)
            return false;

        Player playerOne = Bukkit.getPlayer(args[1]);
        Player playerTwo = Bukkit.getPlayer(args[2]);
        int lives = 3;
        if (args.length == 4) {
            try {
                lives = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.DARK_RED + "Invalid lives input!");
                return true;
            }
        }

        if (playerOne == playerTwo) {
            sender.sendMessage(ChatColor.DARK_RED + "You can't pair someone to themselves!");
            return true;
        }

        if (playerOne == null || playerTwo == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Invalid player arguments!");
            return true;
        }

        if (SaveHandler.getPair(playerOne) != null || SaveHandler.getPair(playerTwo) != null) {
            sender.sendMessage(ChatColor.DARK_RED + "One of those players is already paired!");
            return true;
        }

        SaveHandler.createPair(playerOne.getUniqueId(), playerTwo.getUniqueId(), lives);
        sender.sendMessage(ChatColor.DARK_GREEN + "You've successfully paired " + playerOne.getName() + " with " + playerTwo.getName() + "!");

        return true;
    }

    public static List<String> getAppropriateArguments(String[] args) {
        List<String> arguments = new ArrayList<>();

        if (args.length == 2 || args.length == 3) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                arguments.add(player.getName());
            });
        } else if (args.length == 4) {
            arguments.add("1");
            arguments.add("2");
            arguments.add("3");
        }

        return arguments;
    }

}
