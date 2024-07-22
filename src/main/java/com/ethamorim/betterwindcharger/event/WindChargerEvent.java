package com.ethamorim.betterwindcharger.event;

import com.ethamorim.betterwindcharger.BetterWindChargerPlugin;
import com.ethamorim.betterwindcharger.jedis.JedisInstance;
import com.ethamorim.betterwindcharger.util.ConfigKeys;
import com.ethamorim.betterwindcharger.util.PowerWindCharger;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

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
            var factorModifier = JedisInstance.getDouble(ConfigKeys.VELOCITY_FACTOR.toString());
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
            var factorModifier = JedisInstance.getFloat(ConfigKeys.EXPLOSION_FACTOR.toString());
            wc.setYield(factorModifier);

            var world = wc.getWorld();
            world.createExplosion(
                    location,
                    factorModifier,
                    false,
                    false,
                    wc
            );

            var random = new Random();
            if (factorModifier == PowerWindCharger.MEDIUM.getValue()) {
                world.spawnParticle(
                        Particle.GUST,
                        location,
                        random.nextInt(2),
                        random.nextInt(2),
                        random.nextInt(2),
                        random.nextInt(2)
                );
            } else if (factorModifier == PowerWindCharger.HIGH.getValue()) {
                world.spawnParticle(
                        Particle.EXPLOSION,
                        location,
                        random.nextInt(5),
                        random.nextInt(5),
                        random.nextInt(5),
                        random.nextInt(5)
                );
            } else if (factorModifier == PowerWindCharger.HUGE.getValue()) {
                world.spawnParticle(
                        Particle.EXPLOSION_EMITTER,
                        location,
                        random.nextInt(10),
                        random.nextInt(10),
                        random.nextInt(10),
                        random.nextInt(10)
                );
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
            e.setDamage(0);
        }
    }
}
