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

    Team greenLivesTeam;
    Team limeLivesTeam;
    Team yellowLivesTeam;
    Team redLifeTeam;
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

        greenLivesTeam = scoreboard.registerNewTeam("greenLives");
        limeLivesTeam = scoreboard.registerNewTeam("limeLives");
        yellowLivesTeam = scoreboard.registerNewTeam("yellowLives");
        redLifeTeam = scoreboard.registerNewTeam("redLifeTeam");
        spectatorTeam = scoreboard.registerNewTeam("spectator");
        greenLivesTeam.setColor(ChatColor.DARK_GREEN);
        greenLivesTeam.setCanSeeFriendlyInvisibles(false);
        limeLivesTeam.setColor(ChatColor.GREEN);
        limeLivesTeam.setCanSeeFriendlyInvisibles(false);
        yellowLivesTeam.setColor(ChatColor.YELLOW);
        yellowLivesTeam.setCanSeeFriendlyInvisibles(false);
        redLifeTeam.setColor(ChatColor.RED);
        redLifeTeam.setCanSeeFriendlyInvisibles(false);
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
            if (livesAmount == -1) { // for if the player's pair has been removed
                for (Team team : scoreboard.getTeams()) {
                    if (team.hasEntry(player.getName()))
                        team.removeEntry(player.getName());
                }
            }
            else if (livesAmount == 0) {
                player.setGameMode(GameMode.SPECTATOR);
                scoreboard.getTeam("spectator").addEntry(player.getName());
                if (DoubleLife.plugin.getConfig().getBoolean("misc.ban-players-upon-losing")) {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "You've been banned because the server owner has configured the server to do so after you lose all of your lives!", null, "DoubleLife");
                    player.kickPlayer("You've been banned because you're out of lives!");
                }
            } else if (0 < livesAmount && livesAmount <= DoubleLife.plugin.getConfig().getInt("lives.red-live-start-in")) {
                player.setGameMode(GameMode.SURVIVAL);
                scoreboard.getTeam("redLifeTeam").addEntry(player.getName());
            } else if (livesAmount <= DoubleLife.plugin.getConfig().getInt("lives.yellow-live-start-in")) {
                player.setGameMode(GameMode.SURVIVAL);
                scoreboard.getTeam("yellowLives").addEntry(player.getName());
                player.setGameMode(GameMode.SURVIVAL);
            } else if (livesAmount <= DoubleLife.plugin.getConfig().getInt("lives.lime-live-start-in")) {
                player.setGameMode(GameMode.SURVIVAL);
                scoreboard.getTeam("limeLives").addEntry(player.getName());
            } else if (livesAmount > DoubleLife.plugin.getConfig().getInt("lives.lime-live-start-in")) {
                player.setGameMode(GameMode.SURVIVAL);
                scoreboard.getTeam("greenLives").addEntry(player.getName());
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
