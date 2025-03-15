package me.rowan.doublelife.listeners;

import me.rowan.doublelife.data.SaveHandler;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;

public class ShareEffects implements Listener {

    @EventHandler
    public void applyEffectToSoulmate(EntityPotionEffectEvent event) {
        if ((event.getEntityType() != EntityType.PLAYER) || event.getCause() == EntityPotionEffectEvent.Cause.PLUGIN)
            return;

        Player player = (Player) event.getEntity();
        if (event.getOldEffect() == null && event.getNewEffect() != null){
            PotionEffect effect = event.getNewEffect();
            Player soulmate = SaveHandler.getSoulmate(player);
            if (soulmate != null)
                soulmate.addPotionEffect(effect);
        } else if (event.getNewEffect() == null && event.getOldEffect() != null){
            PotionEffect effect = event.getOldEffect();
            Player soulmate = SaveHandler.getSoulmate(player);
            if (soulmate != null)
                soulmate.removePotionEffect(effect.getType());
        }

    }

    @EventHandler
    public void removeEffectsAfterConsumable(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() != Material.MILK_BUCKET)
            return;

        Player player = event.getPlayer();
        Player soulmate = SaveHandler.getSoulmate(player);
        if (soulmate != null) {
            for (PotionEffect effect : soulmate.getActivePotionEffects())
                soulmate.removePotionEffect(effect.getType());
        }
    }
}
