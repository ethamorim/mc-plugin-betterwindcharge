package com.ethamorim.betterwindcharger.command;

import com.ethamorim.betterwindcharger.BetterWindChargerPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class WindChargerCommand implements CommandExecutor {

    BetterWindChargerPlugin main;
    FileConfiguration configuration;

    public WindChargerCommand(BetterWindChargerPlugin main) {
        this.main = main;
        this.configuration = main.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            if (args.length < 3) {
                return false;
            }

            var property = args[1];
            var value = args[2];
            if (property.equals("velocity")) {
                return setWindChargerVelocity(player, value);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean setWindChargerVelocity(Player player, String value) {
        var factor = "windcharger.velocity-factor";
        switch (value) {
            case "static" -> {
                player.sendMessage("Wind Charger is now static");
                configuration.set(factor, 0.0);
            }
            case "slow" -> {
                player.sendMessage("Wind Charger's velocity is now slow");
                configuration.set(factor, 0.05);
            }
            case "default" -> {
                player.sendMessage("Wind Charger's velocity is back to default");
                configuration.set(factor, 1.0);
            }
            case "fast" -> {
                player.sendMessage("Wind Charger's velocity is now fast");
                configuration.set(factor, 2.0);
            }
            case "lightning" -> {
                player.sendMessage(ChatColor.GOLD + "Wind Charger's velocity is now LIGHTNING FAST!!");
                configuration.set(factor, 8.0);
            }
            default -> {
                return false;
            }
        }
        main.saveConfig();
        return true;
    }
}
