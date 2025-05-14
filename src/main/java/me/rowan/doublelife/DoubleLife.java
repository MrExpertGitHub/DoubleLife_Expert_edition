package me.rowan.doublelife;

import me.rowan.doublelife.commands.*;
import me.rowan.doublelife.data.ConfigHandler;
import me.rowan.doublelife.data.SaveHandler;
import me.rowan.doublelife.listeners.BlockBannedItems;
import me.rowan.doublelife.listeners.ChatFormat;
import me.rowan.doublelife.listeners.PairHealth;
import me.rowan.doublelife.listeners.ShareEffects;
import me.rowan.doublelife.scoreboard.TeamHandler;
import me.rowan.doublelife.util.commandArguments;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class DoubleLife extends JavaPlugin {

    public static JavaPlugin plugin;

    public static ConfigHandler ConfigHandler;

    public static Collection<NamespacedKey> recipeKeys;

    int configVersion = 3;



    @Override
    public void onEnable() {
        plugin = this;

        int pluginId = 16819;
        new Metrics(plugin, pluginId);

        ConfigHandler = new ConfigHandler();
        SaveHandler.construct();

        plugin.getCommand("doublelife").setExecutor(new mainCommandExecutor());
        plugin.getCommand("doublelife").setTabCompleter(new mainTabCompleter());

        Bukkit.getPluginManager().registerEvents(new TeamHandler(), plugin);
        Bukkit.getPluginManager().registerEvents(new PairHealth(), plugin);
        if (plugin.getConfig().getBoolean("soulmates.link-effects")) {
            Bukkit.getPluginManager().registerEvents(new ShareEffects(), plugin);
        }
        Bukkit.getPluginManager().registerEvents(new BlockBannedItems(), plugin);
        Bukkit.getPluginManager().registerEvents(new ChatFormat(), plugin);

        BlockBannedItems.startKillVillagersLoop();

        recipeKeys = new ArrayList<>();
        Recipes.createRecipes();


        /*new UpdateChecker(plugin, 106141).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("The plugin is up to date!");
            } else {
                getLogger().severe("There is a newer version available!  Running: " + this.getDescription().getVersion() + "  Newest: " + version);
                getLogger().severe("You may download it here: https://modrinth.com/plugin/double-life");
            }
        });*/

        if (getConfig().getInt("config-version") != configVersion) {
            getLogger().warning("Your configuration file is " + (this.configVersion - getConfig().getInt("config-version")) + " version(s) behind!");
            getLogger().warning("To be able to access the newly added settings, please delete the current config.yml file and restart/reload the server!");
        }
    }

    public class mainCommandExecutor implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (args.length < 1)
                return false;

            if (args[0].equalsIgnoreCase("setup"))
                return setup.onCommand(sender, command, label, args);
            else if (args[0].equalsIgnoreCase("randomizepairs"))
                return randomizePairs.onCommand(sender, command, label, args);
            else if (args[0].equalsIgnoreCase("pair"))
                return pair.onCommand(sender, command, label, args);
            else if (args[0].equalsIgnoreCase("unpair"))
                return unpair.onCommand(sender, command, label, args);
            else if (args[0].equalsIgnoreCase("setLives"))
                return setLives.onCommand(sender, command, label, args);
            else if (args[0].equalsIgnoreCase("distributeplayers"))
                return distributePlayers.onCommand(sender, command, label, args);
            else if (args[0].equalsIgnoreCase("help"))
                return help.onCommand(sender, command, label, args);
            else if (args[0].equalsIgnoreCase("reload"))
                return reload.onCommand(sender, command, label, args);
            else if (args[0].equalsIgnoreCase("config"))
                return config.onCommand(sender, command, label, args);

            return false;
        }

    }

    public static class mainTabCompleter implements TabCompleter {

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

            if (command.getName().equalsIgnoreCase("doublelife") && sender.hasPermission("doublelife.admin")){
                if (args.length == 1)
                    return commandArguments.getAdministrativeCommands();

                if (args[0].equalsIgnoreCase("pair"))
                    return pair.getAppropriateArguments(args);
                else if (args[0].equalsIgnoreCase("unpair"))
                    return unpair.getAppropriateArguments(args);
                else if (args[0].equalsIgnoreCase("setLives"))
                    return setLives.getAppropriateArguments(args);
                else if (args[0].equalsIgnoreCase("config"))
                    return config.getAppropriateArguments(args);
            }

            return new ArrayList<>();
        }

    }

}
