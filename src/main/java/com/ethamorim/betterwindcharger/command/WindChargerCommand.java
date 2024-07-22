package com.ethamorim.betterwindcharger.command;

import com.ethamorim.betterwindcharger.BetterWindChargerPlugin;
import com.ethamorim.betterwindcharger.jedis.JedisInstance;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WindChargerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            if (args.length < 2) {
                return false;
            }

            var operation = args[0];
            if (operation.equals("set")) {
                var property = args[1];
                var value = args[2];
                if (property.equals("velocity")) {
                    return setWindChargerVelocity(player, value);
                }
            } else if (operation.equals("give")) {
                return giveAmount(player, args[1]);
            } else {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean setWindChargerVelocity(Player player, String value) {
        var factor = "velocity-factor";
        switch (value) {
            case "static" -> {
                player.sendMessage("Wind Charger is now static");
                JedisInstance.setValue(factor, 0.0);
            }
            case "slow" -> {
                player.sendMessage("Wind Charger's velocity is now slow");
                JedisInstance.setValue(factor, 0.05);
            }
            case "default" -> {
                player.sendMessage("Wind Charger's velocity is back to default");
                JedisInstance.setValue(factor, 1.0);
            }
            case "fast" -> {
                player.sendMessage("Wind Charger's velocity is now fast");
                JedisInstance.setValue(factor, 2.0);
            }
            case "lightning" -> {
                player.sendMessage(ChatColor.GOLD + "Wind Charger's velocity is now LIGHTNING FAST!!");
                JedisInstance.setValue(factor, 8.0);
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    private boolean giveAmount(Player player, String amountArg) {
        try {
            var amount = Integer.parseInt(amountArg);
            player.getInventory().addItem(new ItemStack(Material.WIND_CHARGE, amount));
            return true;
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Not a valid number");
            return false;
        }
    }
}
