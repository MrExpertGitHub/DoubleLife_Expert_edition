package me.rowan.doublelife;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;


public class Recipes {

    public static void createRecipes(){

        if (DoubleLife.plugin.getConfig().getBoolean("recipes.craftable-saddle")) {
            ItemStack saddle = new ItemStack(Material.SADDLE, 1);
            NamespacedKey saddleKey = new NamespacedKey(DoubleLife.plugin, "saddle");
            ShapedRecipe saddleRecipe = new ShapedRecipe(saddleKey, saddle);
            saddleRecipe.shape("LLL", "S S", "I I");
            saddleRecipe.setIngredient('L', Material.LEATHER);
            saddleRecipe.setIngredient('I', Material.IRON_INGOT);
            saddleRecipe.setIngredient('S', Material.STRING);
            Bukkit.addRecipe(saddleRecipe);
            DoubleLife.recipeKeys.add(saddleKey);
        }

        if (DoubleLife.plugin.getConfig().getBoolean("recipes.craftable-name-tag")) {
            ItemStack nameTag = new ItemStack(Material.NAME_TAG, 1);
            NamespacedKey nameTagKey = new NamespacedKey(DoubleLife.plugin, "name_tag");
            ShapedRecipe nameTagRecipe = new ShapedRecipe(nameTagKey, nameTag);
            nameTagRecipe.shape("XXX", " S ", " P ");
            nameTagRecipe.setIngredient('S', Material.STRING);
            nameTagRecipe.setIngredient('P', Material.PAPER);
            Bukkit.addRecipe(nameTagRecipe);
            DoubleLife.recipeKeys.add(nameTagKey);
        }

        if (DoubleLife.plugin.getConfig().getBoolean("recipes.paper-tnt")) {
            ItemStack TNT = new ItemStack(Material.TNT, 1);
            NamespacedKey TNTKey = new NamespacedKey(DoubleLife.plugin, "paper_tnt");
            ShapedRecipe TNTRecipe = new ShapedRecipe(TNTKey, TNT);
            TNTRecipe.shape("PSP", "SGS", "PSP");
            TNTRecipe.setIngredient('P', Material.PAPER);
            TNTRecipe.setIngredient('S', Material.SAND);
            TNTRecipe.setIngredient('G', Material.GUNPOWDER);
            Bukkit.addRecipe(TNTRecipe);
            DoubleLife.recipeKeys.add(TNTKey);
        }

        if (DoubleLife.plugin.getConfig().getBoolean("recipes.craftable-spore-blossom")) {
            ItemStack sporeBlossom = new ItemStack(Material.SPORE_BLOSSOM, 1);
            NamespacedKey sporeBlossomKey = new NamespacedKey(DoubleLife.plugin, "spore_blossom");
            ShapedRecipe sporeBlossomRecipe = new ShapedRecipe(sporeBlossomKey, sporeBlossom);
            sporeBlossomRecipe.shape("  M", " L ", "XXX");
            sporeBlossomRecipe.setIngredient('M', Material.MOSS_BLOCK);
            sporeBlossomRecipe.setIngredient('L', Material.LILAC);
            Bukkit.addRecipe(sporeBlossomRecipe);
            DoubleLife.recipeKeys.add(sporeBlossomKey);
        }

        if (DoubleLife.plugin.getConfig().getBoolean("recipes.craftable-experience-bottle")) {
            ItemStack experienceBottle = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
            NamespacedKey experienceBottleKey = new NamespacedKey(DoubleLife.plugin, "experience-bottle");
            ShapedRecipe experienceBottleRecipe = new ShapedRecipe(experienceBottleKey, experienceBottle);
            experienceBottleRecipe.shape(" G ", "GLG", " G ");
            experienceBottleRecipe.setIngredient('G', Material.GLASS_PANE);
            experienceBottleRecipe.setIngredient('L', Material.LAPIS_LAZULI);
            Bukkit.addRecipe(experienceBottleRecipe);
            DoubleLife.recipeKeys.add(experienceBottleKey);
        }

        if (DoubleLife.plugin.getConfig().getBoolean("recipes.craftable-sculk-sensor")) {
            ItemStack sculkSensor = new ItemStack(Material.SCULK_SENSOR, 1);
            NamespacedKey sculkSensorKey = new NamespacedKey(DoubleLife.plugin, "sculk-sensor");
            ShapedRecipe sculkSensorRecipe = new ShapedRecipe(sculkSensorKey, sculkSensor);
            sculkSensorRecipe.shape("   ", "DHD", "OOO");
            sculkSensorRecipe.setIngredient('H', Material.HAY_BLOCK);
            sculkSensorRecipe.setIngredient('D', Material.DIAMOND);
            sculkSensorRecipe.setIngredient('O', Material.OBSIDIAN);
            Bukkit.addRecipe(sculkSensorRecipe);
            DoubleLife.recipeKeys.add(sculkSensorKey);
        }

        if (DoubleLife.plugin.getConfig().getBoolean("recipes.craftable-slime")) {
            ItemStack slimeBall = new ItemStack(Material.SLIME_BALL, 1);
            NamespacedKey slimeBallKey = new NamespacedKey(DoubleLife.plugin, "slime-ball");
            ShapelessRecipe slimeBallRecipe = new ShapelessRecipe(slimeBallKey, slimeBall);
            slimeBallRecipe.addIngredient(Material.LIME_DYE);
            slimeBallRecipe.addIngredient(Material.LIME_DYE);
            slimeBallRecipe.addIngredient(Material.LIME_DYE);
            slimeBallRecipe.addIngredient(Material.LIME_DYE);
            Bukkit.addRecipe(slimeBallRecipe);
            DoubleLife.recipeKeys.add(slimeBallKey);
        }

        if (DoubleLife.plugin.getConfig().getBoolean("recipes.convert-gravel-and-sand")) {

            ItemStack sand = new ItemStack(Material.SAND, 8);
            NamespacedKey sandKey = new NamespacedKey(DoubleLife.plugin, "sand");
            ShapedRecipe sandRecipe = new ShapedRecipe(sandKey, sand);
            sandRecipe.shape("GGG", "GDG", "GGG");
            sandRecipe.setIngredient('G', Material.GRAVEL);
            sandRecipe.setIngredient('D', Material.DIAMOND);
            Bukkit.addRecipe(sandRecipe);
            DoubleLife.recipeKeys.add(sandKey);

            ItemStack gravel = new ItemStack(Material.GRAVEL, 8);
            NamespacedKey gravelKey = new NamespacedKey(DoubleLife.plugin, "gravel");
            ShapedRecipe gravelRecipe = new ShapedRecipe(gravelKey, gravel);
            gravelRecipe.shape("SSS", "SDS", "SSS");
            gravelRecipe.setIngredient('S', Material.SAND);
            gravelRecipe.setIngredient('D', Material.DIAMOND);
            Bukkit.addRecipe(gravelRecipe);
            DoubleLife.recipeKeys.add(gravelKey);
        }

        for (Player player : Bukkit.getOnlinePlayers())
            for (NamespacedKey key : DoubleLife.recipeKeys)
                player.discoverRecipe(key);

    }
}
