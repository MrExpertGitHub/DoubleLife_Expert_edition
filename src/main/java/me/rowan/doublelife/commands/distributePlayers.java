package me.rowan.doublelife.commands;

import me.rowan.doublelife.DoubleLife;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class distributePlayers {

    private static Location getRandomLocation(World world){
        WorldBorder worldBorder = world.getWorldBorder();
        double borderSize = worldBorder.getSize();
        Location borderCenter = worldBorder.getCenter();
        Random rand = new Random();
        int randomX = (rand.nextInt(((int) borderSize/2) + ((int) borderSize/2)) - ((int) borderSize/2)) + (int) borderCenter.getX();
        int randomZ = (rand.nextInt(((int) borderSize/2) + ((int) borderSize/2)) - ((int) borderSize/2)) + (int) borderCenter.getZ();
        int highestYPoint = world.getHighestBlockYAt(randomX, randomZ);
        return new Location(world, randomX, highestYPoint, randomZ);
    }

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.RED + "This command may not be executed via the console!");
            return true;
        }
        Player executor = (Player) sender;
        World world = executor.getWorld();

        DoubleLife.plugin.getServer().broadcastMessage(ChatColor.DARK_GREEN + "Spreading all players out within the world border!");

        for (Player participant : Bukkit.getOnlinePlayers()) {
            Location teleportLocation = null;
            do {
                Location possibleLocation = getRandomLocation(world);
                if (possibleLocation.getBlock().getType() != Material.WATER)
                    teleportLocation = possibleLocation;
            } while (teleportLocation == null);
            teleportLocation.setY(teleportLocation.getY() + 1);
            participant.teleport(teleportLocation);
            if (DoubleLife.plugin.getConfig().getBoolean("misc.set-spawn-point-at-distributed-location"))
                participant.setBedSpawnLocation(teleportLocation, true);

            participant.setSaturation(20);
            participant.setExhaustion(0);
            participant.setHealth(20);
            participant.setFoodLevel(20);
        }

        return true;
    }

}
