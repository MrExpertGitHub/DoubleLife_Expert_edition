package me.rowan.doublelife.commands;

import me.rowan.doublelife.data.SaveHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class setLives {

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3)
            return false;

        Player playerArgument = Bukkit.getPlayer(args[1]);
        if (playerArgument == null)
            return false;

        int livesToSet;
        try {
            livesToSet = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            return false;
        }

        boolean successful = SaveHandler.setPairLivesAmount(playerArgument, livesToSet);
        if (!successful) {
            sender.sendMessage(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "That player either doesn't have a soulmate, or the lives input was invalid!");
            return true;
        }

        sender.sendMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + playerArgument.getName() + " and their soulmate now have " + livesToSet + " lives!");
        return true;
    }

    public static List<String> getAppropriateArguments(String[] args) {
        List<String> arguments = new ArrayList<>();

        if (args.length == 2){
            Bukkit.getOnlinePlayers().forEach(player -> {
                arguments.add(player.getName());
            });
        } else if (args.length == 3){
            arguments.add("number");
        }

        return arguments;
    }

}
