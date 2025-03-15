package me.rowan.doublelife.scoreboard;

import me.rowan.doublelife.DoubleLife;
import me.rowan.doublelife.data.SaveHandler;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Set;
import java.util.UUID;

public class TeamHandler implements Listener {

    Team extraLivesTeam;
    Team threeLivesTeam;
    Team twoLivesTeam;
    Team oneLifeTeam;
    Team spectatorTeam;

    public static Scoreboard scoreboard;

    public TeamHandler() {
        if (Bukkit.getScoreboardManager() != null) {
            scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        }
        else
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[DoubleLife] Please restart the server for the plugin to create the required teams properly!");

        if (scoreboard.getTeams().toArray().length > 0) {
            for (Team team : scoreboard.getTeams())
                team.unregister();
        }

        extraLivesTeam = scoreboard.registerNewTeam("extraLives");
        threeLivesTeam = scoreboard.registerNewTeam("threeLives");
        twoLivesTeam = scoreboard.registerNewTeam("twoLives");
        oneLifeTeam = scoreboard.registerNewTeam("oneLife");
        spectatorTeam = scoreboard.registerNewTeam("spectator");
        extraLivesTeam.setColor(ChatColor.DARK_GREEN);
        extraLivesTeam.setCanSeeFriendlyInvisibles(false);
        threeLivesTeam.setColor(ChatColor.GREEN);
        threeLivesTeam.setCanSeeFriendlyInvisibles(false);
        twoLivesTeam.setColor(ChatColor.YELLOW);
        twoLivesTeam.setCanSeeFriendlyInvisibles(false);
        oneLifeTeam.setColor(ChatColor.RED);
        oneLifeTeam.setCanSeeFriendlyInvisibles(false);
        spectatorTeam.setColor(ChatColor.GRAY);

        Set<Team> teamSet = scoreboard.getTeams();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setPlayerListName(null);
            boolean onTeam = false;
            for (Team team : teamSet){
                if (team.hasEntry(player.getName()))
                    onTeam = true;
            }
            String pair = SaveHandler.getPair(player);
            if (pair != null && !onTeam)
                changePlayerTeamAccordingly(pair, SaveHandler.getPairLivesAmount(player));
        }
    }

    public static void modifyTeamAndGameMode(Player player, Integer livesAmount) {
        if (player != null){
            if (livesAmount == 0) {
                player.setGameMode(GameMode.SPECTATOR);
                scoreboard.getTeam("spectator").addEntry(player.getName());
                if (DoubleLife.plugin.getConfig().getBoolean("misc.ban-players-upon-losing")) {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "You've been banned because the server owner has configured the server to do so after you lose all of your lives!", null, "DoubleLife");
                    player.kickPlayer("You've been banned because you're out of lives!");
                }
            } else if (livesAmount == 1) {
                player.setGameMode(GameMode.SURVIVAL);
                scoreboard.getTeam("oneLife").addEntry(player.getName());
            } else if (livesAmount == 2) {
                player.setGameMode(GameMode.SURVIVAL);
                scoreboard.getTeam("twoLives").addEntry(player.getName());
            } else if (livesAmount == 3) {
                player.setGameMode(GameMode.SURVIVAL);
                scoreboard.getTeam("threeLives").addEntry(player.getName());
            } else if (livesAmount >= 4) {
                player.setGameMode(GameMode.SURVIVAL);
                scoreboard.getTeam("extraLives").addEntry(player.getName());
            } else if (livesAmount == -1) { // for if the player's pair has been removed
                for (Team team : scoreboard.getTeams()) {
                    if (team.hasEntry(player.getName()))
                        team.removeEntry(player.getName());
                }
                player.setGameMode(GameMode.SURVIVAL);
            }
        }
    }

    public static void changePlayerTeamAccordingly(String pair, Integer newLivesAmount) {
        String[] splitMessage = pair.split(" : ");
        Player playerOne = Bukkit.getPlayer(UUID.fromString(splitMessage[0]));
        Player playerTwo = Bukkit.getPlayer(UUID.fromString(splitMessage[1]));
        modifyTeamAndGameMode(playerOne, newLivesAmount);
        modifyTeamAndGameMode(playerTwo, newLivesAmount);
    }

    @EventHandler
    public void updatePlayerTeamOnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int amountOfLives = SaveHandler.getPairLivesAmount(player);
        if (amountOfLives == -1)
            return;
        modifyTeamAndGameMode(player, amountOfLives);
    }

}
