package me.rowan.doublelife.listeners;

import me.rowan.doublelife.DoubleLife;
import me.rowan.doublelife.data.SaveHandler;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PairHealth implements Listener {

    List<UUID> justHadALifeRemoved = new ArrayList<>();
    List<UUID> soulmateDiedWhileOffline = new ArrayList<>();


    Integer redLiveStartsIn = DoubleLife.plugin.getConfig().getInt("lives.red-live-start-in");
    Integer yellowLiveStartsIn = DoubleLife.plugin.getConfig().getInt("lives.yellow-live-start-in");
    Integer limeLiveStartsIn = DoubleLife.plugin.getConfig().getInt("lives.lime-live-start-in");
    Integer maxLives = DoubleLife.plugin.getConfig().getInt("lives.max-lives");

    @EventHandler(priority = EventPriority.NORMAL)
    public void removeLifeOnDeath(PlayerDeathEvent event){
        Player playerWhoDied = event.getEntity();
        Player soulmate = SaveHandler.getSoulmate(playerWhoDied);

        if (soulmateDiedWhileOffline.contains(playerWhoDied.getUniqueId())) {
            Bukkit.getScheduler().runTaskLater(DoubleLife.plugin, () -> soulmateDiedWhileOffline.remove(playerWhoDied.getUniqueId()), 5);
            event.setDeathMessage(ChatColor.BOLD + playerWhoDied.getDisplayName() + " was killed because their soulmate died while they were offline!");
            return;
        }

        if (soulmate != null) {
            if (soulmate == playerWhoDied.getKiller()) {
                event.setDeathMessage(event.getDeathMessage() + ChatColor.RED + ChatColor.BOLD + " Friendly fire!");
            }
        }

        if (justHadALifeRemoved.contains(playerWhoDied.getUniqueId()))
            return;
        justHadALifeRemoved.add(playerWhoDied.getUniqueId());
        if (soulmate != null && !justHadALifeRemoved.contains(soulmate.getUniqueId())) {
            justHadALifeRemoved.add(soulmate.getUniqueId());
        } else if (soulmate == null && DoubleLife.plugin.getConfig().getBoolean("soulmates.kill-soulmate-on-join-if-offline-during-death")) {
            OfflinePlayer offlineSoulmate = SaveHandler.getOfflineSoulmate(playerWhoDied);
            if ((offlineSoulmate != null) && !soulmateDiedWhileOffline.contains(offlineSoulmate.getUniqueId()))
                soulmateDiedWhileOffline.add(offlineSoulmate.getUniqueId());
        }

        Bukkit.getScheduler().runTaskLater(DoubleLife.plugin, () -> {
            justHadALifeRemoved.remove(playerWhoDied.getUniqueId());
            if (soulmate != null)
                justHadALifeRemoved.remove(soulmate.getUniqueId());
        }, 20);

        int currentLivesAmount = SaveHandler.getPairLivesAmount(playerWhoDied);

        if (currentLivesAmount - 1 == 0) {
            if (soulmate != null) {
                soulmate.setHealth(0);
            }
            Bukkit.getScheduler().runTaskLater(DoubleLife.plugin, () -> {
                playerWhoDied.sendTitle(ChatColor.BOLD + "" + ChatColor.RED + "You run out of lives!", null, 10, 100, 10);
                if (soulmate == null) {
                    if (DoubleLife.plugin.getConfig().getBoolean("soulmates.broadcast-color-change")) {
                    Bukkit.broadcastMessage(ChatColor.GRAY + playerWhoDied.getDisplayName() + ChatColor.RESET + " run out of lives!");
                    }
                } else {
                    soulmate.sendTitle(ChatColor.BOLD + "" + ChatColor.RED + "You run out of lives!", null, 10, 100, 10);
                    if (DoubleLife.plugin.getConfig().getBoolean("soulmates.broadcast-color-change")) {
                        Bukkit.broadcastMessage(ChatColor.GRAY + playerWhoDied.getDisplayName() + ChatColor.RESET + " and " + ChatColor.GRAY + soulmate.getDisplayName() + ChatColor.RESET + " run out of lives!");
                    }
                }
            }, 20);


            if (DoubleLife.plugin.getConfig().getBoolean("misc.global-explosion-sound-on-final-death")){
                for (Player player : Bukkit.getOnlinePlayers())
                    player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
            }
        }
        if (currentLivesAmount - 1 == redLiveStartsIn) {
            if (DoubleLife.plugin.getConfig().getBoolean("soulmates.broadcast-color-change")) {
                Bukkit.getScheduler().runTaskLater(DoubleLife.plugin, () -> {
                    if (soulmate == null) {
                        Bukkit.broadcastMessage(ChatColor.RED + playerWhoDied.getDisplayName() + ChatColor.RESET + " joined the " + ChatColor.BOLD + ChatColor.RED + "REDS" + ChatColor.RESET + "!");
                    } else {
                        Bukkit.broadcastMessage(ChatColor.RED + playerWhoDied.getPlayerListName() + ChatColor.RESET + " and " + ChatColor.RED + soulmate.getDisplayName() + ChatColor.RESET + " joined the " + ChatColor.BOLD + ChatColor.RED + "REDS" + ChatColor.RESET + "!");
                    }
                }, 5);
            }

        } else if (currentLivesAmount - 1 == yellowLiveStartsIn) {
            if (DoubleLife.plugin.getConfig().getBoolean("soulmates.broadcast-color-change")) {
                Bukkit.getScheduler().runTaskLater(DoubleLife.plugin, () -> {
                    if (soulmate == null) {
                        Bukkit.broadcastMessage(ChatColor.YELLOW + playerWhoDied.getDisplayName() + ChatColor.RESET + " turned " + ChatColor.BOLD + ChatColor.YELLOW + "YELLOW" + ChatColor.RESET + "!");
                    } else {
                        Bukkit.broadcastMessage(ChatColor.YELLOW + playerWhoDied.getDisplayName() + ChatColor.RESET + " and " + ChatColor.YELLOW + soulmate.getDisplayName() + ChatColor.RESET + " turned " + ChatColor.BOLD + ChatColor.YELLOW + "YELLOW" + ChatColor.RESET + "!");
                    }
                }, 5);
            }

        } else if (currentLivesAmount - 1 == limeLiveStartsIn) {
            if (DoubleLife.plugin.getConfig().getBoolean("soulmates.broadcast-color-change")) {
                Bukkit.getScheduler().runTaskLater(DoubleLife.plugin, () -> {
                    if (soulmate == null) {
                        Bukkit.broadcastMessage(ChatColor.GREEN + playerWhoDied.getDisplayName() + ChatColor.RESET + " doesn't have 6 lives anymore!");
                    } else {
                        Bukkit.broadcastMessage(ChatColor.GREEN + playerWhoDied.getDisplayName() + ChatColor.RESET + " and " + ChatColor.GREEN + soulmate.getDisplayName() + ChatColor.RESET + " doesn't have 6 lives anymore!");
                    }
                }, 5);
            }
        }

        SaveHandler.setPairLivesAmount(playerWhoDied, (currentLivesAmount - 1));
        SaveHandler.setPairHealth(playerWhoDied, 20.0);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void killBackup(PlayerDeathEvent event){
        Player playerWhoDied = event.getEntity();
        if (soulmateDiedWhileOffline.contains(playerWhoDied.getUniqueId()))
            return;

        Player soulmate = SaveHandler.getSoulmate(playerWhoDied);
        Bukkit.getScheduler().runTaskLater(DoubleLife.plugin, () -> {
            if ((soulmate != null) && !soulmate.isDead() && (DoubleLife.plugin.getConfig().getBoolean("soulmates.link-hearths"))){
                soulmate.setHealth(0);
            }
        }, 10);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void damageEvent(EntityDamageEvent event){
        if (DoubleLife.plugin.getConfig().getBoolean("soulmates.link-hearths")) {
            if (event.getEntity().getType() != EntityType.PLAYER || event.getCause() == EntityDamageEvent.DamageCause.CUSTOM)
                return;

            Player damagedPlayer = (Player) event.getEntity();
            if (soulmateDiedWhileOffline.contains(damagedPlayer.getUniqueId()))
                return;

            double finalDamage = event.getFinalDamage();
            double currentHealth = SaveHandler.getPairHealth(damagedPlayer);

            Player soulmate = SaveHandler.getSoulmate(damagedPlayer);
            double healthToSet = currentHealth - finalDamage;

            SaveHandler.setPairHealth(damagedPlayer, healthToSet);
            if ((soulmate != null) && !soulmate.isDead()) {
                double maxHealth = soulmate.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                if (healthToSet >= maxHealth)
                    healthToSet = maxHealth;
                else if (healthToSet <= 0.0)
                    healthToSet = 0.0;

                soulmate.damage(0.01);
                soulmate.setHealth(healthToSet);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void regainHealthEvent(EntityRegainHealthEvent event){
        if (DoubleLife.plugin.getConfig().getBoolean("soulmates.link-hearths")) {
            if (event.getEntity().getType() != EntityType.PLAYER || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.CUSTOM)
                return;

            Player healedPlayer = (Player) event.getEntity();
            double healAmount = event.getAmount();
            double currentHealth = SaveHandler.getPairHealth(healedPlayer);
            double healthToSet = currentHealth + healAmount;

            Player soulmate = SaveHandler.getSoulmate(healedPlayer);
            SaveHandler.setPairHealth(healedPlayer, healthToSet);
            if (soulmate != null && !soulmate.isDead()) {
                double maxHealth = soulmate.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                if (healthToSet >= maxHealth)
                    healthToSet = maxHealth;
                else if (healthToSet <= 0.0)
                    healthToSet = 0.0;

                soulmate.setHealth(healthToSet);
            }
        }
    }

    @EventHandler
    public void killIfSoulmateDiedWhileOffline(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if (soulmateDiedWhileOffline.contains(player.getUniqueId())){
            if (player.getGameMode() == GameMode.SPECTATOR){
                soulmateDiedWhileOffline.remove(player.getUniqueId());
                return;
            }
            Bukkit.getScheduler().runTaskLater(DoubleLife.plugin, () -> {
                if (player.isOnline())
                    player.setHealth(0);
            }, 100);
        }
    }

    @EventHandler
    public void synchronizeHealthAfterJoining(PlayerJoinEvent event){
        Player player = event.getPlayer();

        double pairHealth = SaveHandler.getPairHealth(player);
        if (pairHealth > 0)
            player.setHealth(pairHealth);
    }

}
