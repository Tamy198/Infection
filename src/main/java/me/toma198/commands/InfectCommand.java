package me.toma198.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jspecify.annotations.NonNull;

public class InfectCommand extends InfectionCommand implements CommandExecutor, Listener {
    public InfectCommand() {
        super();
    }

    // ** HOW DO I SEND THE DEAD IMPOSTER AND INNOCENT LIST TO THIS CLASS (Done?) **
    // 1. command (infect) which only this player can access (done?)
    // 2. check if valid player is the argument (done?)
    // 3. run the conversion method on them (done?)
    // 4. time limit (done?)

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command,
                             @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player imposter)) {
            sender.sendMessage("§cOnly players can execute this command!");
            return true;
        }

        // Iterate over innocent players to check if they can be converted
        for (Player player : innocentList) {
            String name = player.getDisplayName();
            if (name.equals(args[0])) {
                // Remove the ability for all to send this command and convert
                // the innocent to imposter
                remove_perms(imposter);
                conversion(player);
                return true;
            } else {
                // Tell player the command failed and the correct usage of the command
                sender.sendMessage("§cMust input an alive innocent player");
                sender.sendMessage("§cUsage: /" + label + " <InnocentPlayerUsername>");
            }
        }

        return true;
    }
}
