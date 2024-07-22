package com.ethamorim.betterwindcharger;

import com.ethamorim.betterwindcharger.command.WindChargerCommand;
import com.ethamorim.betterwindcharger.event.WindChargerEvent;
import com.ethamorim.betterwindcharger.jedis.JedisInstance;
import com.ethamorim.betterwindcharger.util.ConfigKeys;
import com.ethamorim.betterwindcharger.util.PowerWindCharger;
import com.ethamorim.betterwindcharger.util.VelocityWindCharger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterWindChargerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        JedisInstance.connect();
        JedisInstance.setValue(
                ConfigKeys.VELOCITY_FACTOR.toString(),
                VelocityWindCharger.DEFAULT.getValue());
        JedisInstance.setValue(
                ConfigKeys.EXPLOSION_FACTOR.toString(),
                PowerWindCharger.DEFAULT.getValue());
        JedisInstance.setValue(
                ConfigKeys.ANIMATION.toString(),
                false);

        registerCommands();
        registerEvents();
    }

    private void registerCommands() {
        var wcCommand = getCommand("windcharger");
        if (wcCommand != null) {
            wcCommand.setExecutor(new WindChargerCommand());
        }
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new WindChargerEvent(this), this);
    }
}
