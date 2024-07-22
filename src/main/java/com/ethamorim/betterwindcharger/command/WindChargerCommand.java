package com.ethamorim.betterwindcharger.command;

import com.ethamorim.betterwindcharger.jedis.JedisInstance;
import com.ethamorim.betterwindcharger.util.PowerWindCharger;
import com.ethamorim.betterwindcharger.util.VelocityWindCharger;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.*;
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
                } else if (property.equals("power")) {
                    return setWindChargerExplosionPower(player, value);
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
        String message;
        double modifier;
        if (value.equals(VelocityWindCharger.STATIC.toString())) {
            message = "Wind Charger's is now static";
            modifier = VelocityWindCharger.STATIC.getValue();
        } else if (value.equals(VelocityWindCharger.SLOW.toString())) {
            message = "Wind Charger's velocity is now slow";
            modifier = VelocityWindCharger.SLOW.getValue();
        } else if (value.equals(VelocityWindCharger.DEFAULT.toString())) {
            message = "Wind Charger's velocity is back to default";
            modifier = VelocityWindCharger.DEFAULT.getValue();
        } else if (value.equals(VelocityWindCharger.FAST.toString())) {
            message = "Wind Charger's velocity is now fast";
            modifier = VelocityWindCharger.FAST.getValue();
        } else if (value.equals(VelocityWindCharger.LIGHTNING.toString())) {
            message = ChatColor.GOLD + "Wind Charger's velocity is now LIGHTNING FAST!!";
            modifier = VelocityWindCharger.LIGHTNING.getValue();
        } else {
            return false;
        }
        player.sendMessage(message);
        JedisInstance.setValue("velocity-factor", modifier);
        return true;
    }

    private boolean setWindChargerExplosionPower(Player player, String value) {
        String message;
        float modifier;
        if (value.equals(PowerWindCharger.DEFAULT.toString())) {
            message = "Wind Charger's explosion power is back to default";
            modifier = PowerWindCharger.DEFAULT.getValue();
        } else if (value.equals(PowerWindCharger.MEDIUM.toString())) {
            message = "Wind Charger's explosion power is now medium";
            modifier = PowerWindCharger.MEDIUM.getValue();
        } else if (value.equals(PowerWindCharger.HIGH.toString())) {
            message = "Wind Charger's explosion power is now high";
            modifier = PowerWindCharger.HIGH.getValue();
        } else if (value.equals(PowerWindCharger.HUGE.toString())) {
            message = ChatColor.GOLD + "Wind charger's explosion power is now HUGE!!";
            modifier = PowerWindCharger.HUGE.getValue();
        } else {
            return false;
        }
        player.sendMessage(message);
        JedisInstance.setValue("explosion-factor", modifier);
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
