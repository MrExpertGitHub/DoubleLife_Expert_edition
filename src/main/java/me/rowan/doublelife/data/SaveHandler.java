package me.rowan.doublelife.data;

import me.rowan.doublelife.DoubleLife;
import me.rowan.doublelife.scoreboard.TeamHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SaveHandler {

    public static File saveFile;
    public static YamlConfiguration saveYaml;

    public static void construct() {
        saveFile = new File(DoubleLife.plugin.getDataFolder(), "save.yml");
        saveYaml = YamlConfiguration.loadConfiguration(saveFile);
        if (!saveFile.exists()){
            saveYaml.set("soulmates", new ArrayList<String>());
            List<String> comments = new ArrayList<>();
            comments.add("Save Format:");
            comments.add("firstPlayerUUID : secondPlayerUUID : Health : Lives");
            saveYaml.setComments("soulmates", comments);
            save();
        }
    }

    public static void save() {
        try {
            saveYaml.save(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearFile() {
        saveYaml.set("soulmates", new ArrayList<String>());
        save();
    }

    public static void createPair(UUID firstUUID, UUID secondUUID, Integer livesAmount){

        Player playerOne = Bukkit.getPlayer(firstUUID);
        Player playerTwo = Bukkit.getPlayer(secondUUID);
        if (playerOne != null && playerTwo != null) {
            playerOne.setHealth(20.0);
            playerTwo.setHealth(20.0);
        }

        String finalStringToSave = "";
        finalStringToSave = finalStringToSave + firstUUID + " : " + secondUUID + " : 20.0 : " + livesAmount;

        ArrayList<String> savedPairs = (ArrayList<String>) saveYaml.get("soulmates");
        savedPairs.add(finalStringToSave);
        saveYaml.set("soulmates", savedPairs);

        TeamHandler.changePlayerTeamAccordingly(finalStringToSave, livesAmount);
        save();
    }

    public static void deletePair(Player pairMember){
        String pair = getPair(pairMember);

        ArrayList<String> savedPairs = (ArrayList<String>) saveYaml.get("soulmates");
        savedPairs.remove(pair);
        saveYaml.set("soulmates", savedPairs);
        save();

        TeamHandler.changePlayerTeamAccordingly(pair, -1);
    }

    public static String getPair(Player pairMember) {
        ArrayList<String> savedPairs = (ArrayList<String>) saveYaml.get("soulmates");
        String playerUUID = pairMember.getUniqueId().toString();
        for(String pair : savedPairs){
            String[] splitString = pair.split(" : ");
            String firstUUID = splitString[0];
            String secondUUID = splitString[1];
            if(firstUUID.equals(playerUUID) || secondUUID.equals(playerUUID))
                return pair;
        }
        return null;
    }

    public static Player getSoulmate(Player player) {
        String pair = getPair(player);
        if (pair == null)
            return null;

        String[] splitMessage = pair.split(" : ");
        Player soulmate;
        if (splitMessage[0].equals(player.getUniqueId().toString()))
            soulmate = Bukkit.getPlayer(UUID.fromString(splitMessage[1]));
        else if (splitMessage[1].equals(player.getUniqueId().toString()))
            soulmate = Bukkit.getPlayer(UUID.fromString(splitMessage[0]));
        else
            return null;

        return soulmate;
    }

    public static OfflinePlayer getOfflineSoulmate(Player player) {
        String pair = getPair(player);
        if (pair == null)
            return null;

        String[] splitMessage = pair.split(" : ");
        OfflinePlayer offlineSoulmate;
        if (splitMessage[0].equals(player.getUniqueId().toString()))
            offlineSoulmate = Bukkit.getOfflinePlayer(UUID.fromString(splitMessage[1]));
        else if (splitMessage[1].equals(player.getUniqueId().toString()))
            offlineSoulmate = Bukkit.getOfflinePlayer(UUID.fromString(splitMessage[0]));
        else
            return null;

        return offlineSoulmate;
    }

    public static int getPairLivesAmount(Player pairMember) {
        String pair = getPair(pairMember);
        if (pair != null){
            return Integer.parseInt(pair.split(" : ")[3]);
        }

        return -1;
    }

    public static boolean setPairLivesAmount(Player pairMember, Integer amount) {
        if (amount > DoubleLife.plugin.getConfig().getInt("misc.max-lives") || amount < 0)
            return false;

        int currentAmountOfLives = getPairLivesAmount(pairMember);
        if (currentAmountOfLives == -1)
            return false;

        ArrayList<String> savedPairs = (ArrayList<String>) saveYaml.get("soulmates");
        String pair = getPair(pairMember);

        String[] splitMessage = pair.split(" : ");
        String newPair = splitMessage[0] + " : " + splitMessage[1] + " : " + splitMessage[2] + " : " + amount;

        savedPairs.remove(pair);
        savedPairs.add(newPair);
        save();

        TeamHandler.changePlayerTeamAccordingly(newPair, amount);
        return true;
    }

    public static double getPairHealth(Player pairMember) {
        String pair = getPair(pairMember);
        if (pair != null){
            return Double.parseDouble(pair.split(" : ")[2]);
        }

        return -1;
    }

    public static void setPairHealth(Player pairMember, Double healthToSet){
        ArrayList<String> savedPairs = (ArrayList<String>) saveYaml.get("soulmates");
        String pair = getPair(pairMember);
        if (pair == null)
            return;

        if (healthToSet <= 0.0)
            healthToSet = 0.0;
        else if (healthToSet >= 20.0)
            healthToSet = 20.0;

        String[] splitMessage = pair.split(" : ");
        String newPair = splitMessage[0] + " : " + splitMessage[1] + " : " + healthToSet + " : " + splitMessage[3];

        savedPairs.remove(pair);
        savedPairs.add(newPair);
        save();
    }

}
