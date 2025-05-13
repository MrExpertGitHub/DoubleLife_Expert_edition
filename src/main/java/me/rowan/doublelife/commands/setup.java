package me.rowan.doublelife.commands;

import me.rowan.doublelife.DoubleLife;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class setup {

    public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender)
            return true;

        if ((args.length != 2) || !args[1].equalsIgnoreCase("confirm")){
            sender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Are you sure? " + ChatColor.RESET + ChatColor.GRAY + "This command should only be ran once, while setting up the server! Please run '/dl setup confirm' to execute the command!");
            return true;
        }

        Player player = (Player) sender;
        World world = player.getWorld();

        world.setGameRule(GameRule.RANDOM_TICK_SPEED, DoubleLife.plugin.getConfig().getInt("gamerules.random-tick-speed"));
        world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, DoubleLife.plugin.getConfig().getBoolean("gamerules.spectators-generate-chunks"));
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, DoubleLife.plugin.getConfig().getBoolean("gamerules.do-weather-cycle"));
        world.setGameRule(GameRule.DO_INSOMNIA, DoubleLife.plugin.getConfig().getBoolean("gamerules.do-insomnia"));
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, DoubleLife.plugin.getConfig().getBoolean("gamerules.announce-advancements"));
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, DoubleLife.plugin.getConfig().getInt("gamerules.players-sleeping-percentage"));
        world.setGameRule(GameRule.SPAWN_RADIUS, DoubleLife.plugin.getConfig().getInt("gamerules.spawn-radius"));
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, DoubleLife.plugin.getConfig().getBoolean("gamerules.do-day-cycles"));

        world.setSpawnLocation(player.getLocation());
        world.getWorldBorder().setCenter(player.getLocation());
        world.getWorldBorder().setSize(DoubleLife.plugin.getConfig().getDouble("misc.border-size"));

        player.sendMessage(ChatColor.DARK_GREEN + "The world has been set up!");
        return true;
    }

}
