package com.ethamorim.pluginenxtest.command;

import com.ethamorim.pluginenxtest.jedis.JedisInstance;
import com.ethamorim.pluginenxtest.key.ConfigKeys;
import com.ethamorim.pluginenxtest.key.PowerWindCharge;
import com.ethamorim.pluginenxtest.key.VelocityWindCharge;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WindChargeCommand implements CommandExecutor {

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
                } else if (property.equals("trailing_particles")) {
                    return setWindChargerTrailingParticles(player, value);
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
        if (value.equals(VelocityWindCharge.STATIC.toString())) {
            message = "Wind Charge's is now static";
            modifier = VelocityWindCharge.STATIC.getValue();
        } else if (value.equals(VelocityWindCharge.SLOW.toString())) {
            message = "Wind Charge's velocity is now slow";
            modifier = VelocityWindCharge.SLOW.getValue();
        } else if (value.equals(VelocityWindCharge.DEFAULT.toString())) {
            message = "Wind Charge's velocity is back to default";
            modifier = VelocityWindCharge.DEFAULT.getValue();
        } else if (value.equals(VelocityWindCharge.FAST.toString())) {
            message = "Wind Charge's velocity is now fast";
            modifier = VelocityWindCharge.FAST.getValue();
        } else if (value.equals(VelocityWindCharge.LIGHTNING.toString())) {
            message = ChatColor.GOLD + "Wind Charge's velocity is now LIGHTNING FAST!!";
            modifier = VelocityWindCharge.LIGHTNING.getValue();
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
        if (value.equals(PowerWindCharge.DEFAULT.toString())) {
            message = "Wind Charge's explosion power is back to default";
            modifier = PowerWindCharge.DEFAULT.getValue();
        } else if (value.equals(PowerWindCharge.MEDIUM.toString())) {
            message = "Wind Charge's explosion power is now medium";
            modifier = PowerWindCharge.MEDIUM.getValue();
        } else if (value.equals(PowerWindCharge.HIGH.toString())) {
            message = "Wind Charge's explosion power is now high";
            modifier = PowerWindCharge.HIGH.getValue();
        } else if (value.equals(PowerWindCharge.HUGE.toString())) {
            message = ChatColor.GOLD + "Wind Charge's explosion power is now HUGE!!";
            modifier = PowerWindCharge.HUGE.getValue();
        } else {
            return false;
        }
        player.sendMessage(message);
        JedisInstance.setValue("explosion-factor", modifier);
        return true;
    }

    private boolean setWindChargerTrailingParticles(Player player, String value) {
        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            return false;
        }

        var bool = Boolean.parseBoolean(value);
        JedisInstance.setValue(ConfigKeys.TRAILING_PARTICLES.toString(), bool);

        String message = bool
                ? "Wind Charge's trailing particles is turned on"
                : "Wind Charge's trailing particles is turned off";
        player.sendMessage(message);
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
