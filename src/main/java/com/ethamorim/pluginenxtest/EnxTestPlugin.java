package com.ethamorim.pluginenxtest;

import com.ethamorim.pluginenxtest.command.WindChargeCommand;
import com.ethamorim.pluginenxtest.event.WindChargeEvent;
import com.ethamorim.pluginenxtest.jedis.JedisInstance;
import com.ethamorim.pluginenxtest.key.ConfigKeys;
import com.ethamorim.pluginenxtest.key.PowerWindCharge;
import com.ethamorim.pluginenxtest.key.VelocityWindCharge;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public final class EnxTestPlugin extends JavaPlugin {

    private final HashMap<UUID, Projectile> windChargers = new HashMap<>();

    @Override
    public void onEnable() {
        JedisInstance.connect();
        JedisInstance.setValue(
                ConfigKeys.VELOCITY_FACTOR.toString(),
                VelocityWindCharge.DEFAULT.getValue());
        JedisInstance.setValue(
                ConfigKeys.EXPLOSION_FACTOR.toString(),
                PowerWindCharge.DEFAULT.getValue());
        JedisInstance.setValue(
                ConfigKeys.TRAILING_PARTICLES.toString(),
                false);

        registerCommands();
        registerEvents();
        registerTrailingParticles();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    private void registerCommands() {
        var wcCommand = getCommand("windcharger");
        if (wcCommand != null) {
            wcCommand.setExecutor(new WindChargeCommand());
        }
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new WindChargeEvent(this), this);
    }

    private void registerTrailingParticles() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (JedisInstance.getBoolean(ConfigKeys.TRAILING_PARTICLES.toString())) {
                for (UUID id : windChargers.keySet()) {
                    var projectile = windChargers.get(id);
                    var location = projectile.getLocation();
                    projectile.getWorld().spawnParticle(Particle.FIREWORK, location, new Random().nextInt(5));
                }
            }
        }, 0, 1);
    }

    public void addProjectile(Projectile projectile) {
        windChargers.put(projectile.getUniqueId(), projectile);
    }

    public void removeProjectile(UUID uuid) {
        windChargers.remove(uuid);
    }
}
