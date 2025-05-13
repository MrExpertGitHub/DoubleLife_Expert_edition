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
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class DoubleLife extends JavaPlugin {

    public static JavaPlugin plugin;

    public static ConfigHandler ConfigHandler;

    public static Collection<NamespacedKey> recipeKeys;

    int configVersion = 3;

    public void createRecipes(){

        if (plugin.getConfig().getBoolean("recipes.craftable-saddle")) {
            ItemStack saddle = new ItemStack(Material.SADDLE, 1);
            NamespacedKey saddleKey = new NamespacedKey(plugin, "saddle");
            ShapedRecipe saddleRecipe = new ShapedRecipe(saddleKey, saddle);
            saddleRecipe.shape("LLL", " X ", "X X");
            saddleRecipe.setIngredient('X', Material.LEATHER);
            Bukkit.addRecipe(saddleRecipe);
            recipeKeys.add(saddleKey);
        }

        if (plugin.getConfig().getBoolean("recipes.craftable-name-tag")) {
            ItemStack nameTag = new ItemStack(Material.NAME_TAG, 1);
            NamespacedKey nameTagKey = new NamespacedKey(plugin, "name_tag");
            ShapedRecipe nameTagRecipe = new ShapedRecipe(nameTagKey, nameTag);
            nameTagRecipe.shape("XXX", " S ", " P ");
            nameTagRecipe.setIngredient('S', Material.STRING);
            nameTagRecipe.setIngredient('P', Material.PAPER);
            Bukkit.addRecipe(nameTagRecipe);
            recipeKeys.add(nameTagKey);
        }

        if (plugin.getConfig().getBoolean("recipes.paper-tnt")) {
            ItemStack TNT = new ItemStack(Material.TNT, 1);
            NamespacedKey TNTKey = new NamespacedKey(plugin, "paper_tnt");
            ShapedRecipe TNTRecipe = new ShapedRecipe(TNTKey, TNT);
            TNTRecipe.shape("PSP", "SGS", "PSP");
            TNTRecipe.setIngredient('P', Material.PAPER);
            TNTRecipe.setIngredient('S', Material.SAND);
            TNTRecipe.setIngredient('G', Material.GUNPOWDER);
            Bukkit.addRecipe(TNTRecipe);
            recipeKeys.add(TNTKey);
        }

        if (plugin.getConfig().getBoolean("recipes.craftable-spore-blossom")) {
            ItemStack sporeBlossom = new ItemStack(Material.SPORE_BLOSSOM, 1);
            NamespacedKey sporeBlossomKey = new NamespacedKey(plugin, "spore_blossom");
            ShapedRecipe sporeBlossomRecipe = new ShapedRecipe(sporeBlossomKey, sporeBlossom);
            sporeBlossomRecipe.shape("  M", " L ", "XXX");
            sporeBlossomRecipe.setIngredient('M', Material.MOSS_BLOCK);
            sporeBlossomRecipe.setIngredient('L', Material.LILAC);
            Bukkit.addRecipe(sporeBlossomRecipe);
            recipeKeys.add(sporeBlossomKey);
        }

        if (plugin.getConfig().getBoolean("recipes.craftable-experience-bottle")) {
            ItemStack experienceBottle = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
            NamespacedKey experienceBottleKey = new NamespacedKey(plugin, "experience-bottle");
            ShapedRecipe experienceBottleRecipe = new ShapedRecipe(experienceBottleKey, experienceBottle);
            experienceBottleRecipe.shape(" G ", "GLG", " G ");
            experienceBottleRecipe.setIngredient('G', Material.GLASS_PANE);
            experienceBottleRecipe.setIngredient('L', Material.LAPIS_LAZULI);
            Bukkit.addRecipe(experienceBottleRecipe);
            recipeKeys.add(experienceBottleKey);
        }

        if (plugin.getConfig().getBoolean("recipes.craftable-sculk-sensor")) {
            ItemStack sculkSensor = new ItemStack(Material.SCULK_SENSOR, 1);
            NamespacedKey sculkSensorKey = new NamespacedKey(plugin, "sculk-sensor");
            ShapedRecipe sculkSensorRecipe = new ShapedRecipe(sculkSensorKey, sculkSensor);
            sculkSensorRecipe.shape("   ", "DHD", "OOO");
            sculkSensorRecipe.setIngredient('H', Material.HAY_BLOCK);
            sculkSensorRecipe.setIngredient('D', Material.DIAMOND);
            sculkSensorRecipe.setIngredient('O', Material.OBSIDIAN);
            Bukkit.addRecipe(sculkSensorRecipe);
            recipeKeys.add(sculkSensorKey);
        }


        for (Player player : Bukkit.getOnlinePlayers())
            for (NamespacedKey key : recipeKeys)
                player.discoverRecipe(key);

    }

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
        createRecipes();

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

    public class mainTabCompleter implements TabCompleter {

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
