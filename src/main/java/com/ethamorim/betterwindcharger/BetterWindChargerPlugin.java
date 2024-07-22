package com.ethamorim.betterwindcharger;

import com.ethamorim.betterwindcharger.command.WindChargerCommand;
import com.ethamorim.betterwindcharger.event.WindChargerEvent;
import com.ethamorim.betterwindcharger.jedis.JedisInstance;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public final class BetterWindChargerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        JedisInstance.connect();
        JedisInstance.setValue("velocity-factor", 1.0);
        JedisInstance.setValue("explosion-factor", 1.0f);
        JedisInstance.setValue("animation", false);

        registerCommands();
        registerEvents();
    }

    private void registerCommands() {
        var wcCommand = getCommand("windcharger");
        if (wcCommand != null) wcCommand.setExecutor(new WindChargerCommand());
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new WindChargerEvent(this), this);
    }
}
