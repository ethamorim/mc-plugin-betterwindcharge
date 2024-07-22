package com.ethamorim.betterwindcharger.event;

import com.ethamorim.betterwindcharger.BetterWindChargerPlugin;
import com.ethamorim.betterwindcharger.jedis.JedisInstance;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public class WindChargerEvent implements Listener {

    BetterWindChargerPlugin main;

    public WindChargerEvent(BetterWindChargerPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        var windCharger = new ItemStack(Material.WIND_CHARGE, 64);
        e.getPlayer().getInventory().addItem(windCharger);
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof Fireball wc) {
            var velocity = wc.getVelocity();
            var factorModifier = JedisInstance.getDouble("velocity-factor");
            wc.setVelocity(new Vector(
                    velocity.getX() * factorModifier,
                    velocity.getY() * factorModifier,
                    velocity.getZ() * factorModifier
            ));
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof WindCharge wc) {
            var location = wc.getLocation();
            var factorModifier = JedisInstance.getFloat("explosion-factor");
            wc.getWorld().createExplosion(
                    location,
                    wc.getYield() * factorModifier,
                    false,
                    false,
                    wc
            );
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        System.out.println("Cause " + e.getCause());
        if (e.getEntity() instanceof Player) {
            e.setDamage(0);
        }
    }
}
