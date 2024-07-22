package com.ethamorim.betterwindcharger;

import com.ethamorim.betterwindcharger.command.WindChargerCommand;
import com.ethamorim.betterwindcharger.event.WindChargerEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterWindChargerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        registerCommands();
        registerEvents();
    }

    private void registerCommands() {
        var wcCommand = getCommand("windcharger");
        if (wcCommand != null) wcCommand.setExecutor(new WindChargerCommand(this));
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new WindChargerEvent(this), this);
    }
}
