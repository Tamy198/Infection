package me.toma198.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cOnly players can execute this command!");
            return true;
        }

        // if sender is a player, lasts 10 seconds
        final Player player = (Player) commandSender;
        player.setAllowFlight(true);
        player.setFlying(true);

        return true;
    }
}
