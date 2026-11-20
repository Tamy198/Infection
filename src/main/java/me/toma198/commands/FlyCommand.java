package me.toma198.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command,
                             @NonNull String s, String @NonNull [] strings) {

        if(!(commandSender instanceof Player player)) {
            commandSender.sendMessage("§cOnly players can execute this command!");
            return true;
        }

        // makes player fly
        // ** CURRENTLY DISABLED **
        player.setAllowFlight(false);
        player.setFlying(false);

        return true;
    }
}
