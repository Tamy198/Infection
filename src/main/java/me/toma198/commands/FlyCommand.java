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

        // makes player fly
        // CURRENTLY DISABLED
        final Player player = (Player) commandSender;
        player.setAllowFlight(false);
        player.setFlying(false);

        return true;
    }
}
