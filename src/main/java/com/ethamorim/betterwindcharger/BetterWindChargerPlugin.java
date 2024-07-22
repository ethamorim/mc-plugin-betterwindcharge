package com.ethamorim.betterwindcharger;

import com.ethamorim.betterwindcharger.command.WindChargerCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public final class BetterWindChargerPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        registerCommands();

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    private void registerCommands() {
        var wcCommand = getCommand("windcharger");
        if (wcCommand != null) wcCommand.setExecutor(new WindChargerCommand(this));
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
            var factorModifier = getConfig().getDouble("windcharger.velocity-factor");
            wc.setVelocity(new Vector(
                    velocity.getX() * factorModifier,
                    velocity.getY() * factorModifier,
                    velocity.getZ() * factorModifier
            ));
        }

    }
}
