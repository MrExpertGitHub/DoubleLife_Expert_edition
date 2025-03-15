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

    @EventHandler(priority = EventPriority.NORMAL)
    public void removeLifeOnDeath(PlayerDeathEvent event){
        Player playerWhoDied = event.getEntity();
        if (soulmateDiedWhileOffline.contains(playerWhoDied.getUniqueId())) {
            Bukkit.getScheduler().runTaskLater(DoubleLife.plugin, () -> {
                soulmateDiedWhileOffline.remove(playerWhoDied.getUniqueId());
            }, 5);
            event.setDeathMessage(ChatColor.BOLD + playerWhoDied.getPlayerListName() + " was killed because their soulmate died while they were offline!");
            return;
        }

        Player soulmate = SaveHandler.getSoulmate(playerWhoDied);
        if (justHadALifeRemoved.contains(playerWhoDied.getUniqueId()))
            return;
        justHadALifeRemoved.add(playerWhoDied.getUniqueId());
        if (soulmate != null && !justHadALifeRemoved.contains(soulmate.getUniqueId())) {
            justHadALifeRemoved.add(soulmate.getUniqueId());
        } else if (soulmate == null && DoubleLife.plugin.getConfig().getBoolean("misc.kill-soulmate-on-join-if-offline-during-death")) {
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
        SaveHandler.setPairLivesAmount(playerWhoDied, (currentLivesAmount - 1));

        if (currentLivesAmount - 1 == 0 && DoubleLife.plugin.getConfig().getBoolean("misc.global-explosion-sound-on-final-death")){
            for (Player player : Bukkit.getOnlinePlayers())
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
        }
        SaveHandler.setPairHealth(playerWhoDied, 20.0);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void killBackup(PlayerDeathEvent event){
        Player playerWhoDied = event.getEntity();
        if (soulmateDiedWhileOffline.contains(playerWhoDied.getUniqueId()))
            return;

        Player soulmate = SaveHandler.getSoulmate(playerWhoDied);
        Bukkit.getScheduler().runTaskLater(DoubleLife.plugin, () -> {
            if ((soulmate != null) && !soulmate.isDead()){
                soulmate.setHealth(0);
            }
        }, 10);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void damageEvent(EntityDamageEvent event){

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

    @EventHandler(priority = EventPriority.LOWEST)
    public void regainHealthEvent(EntityRegainHealthEvent event){
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
