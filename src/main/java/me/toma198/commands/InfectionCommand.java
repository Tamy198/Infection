package me.toma198.commands;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class InfectionCommand implements CommandExecutor {
    /* just for me to practice my java - later fix would be to make player count an argument
    int player_count;
    public InfectionCommand(int player_count) {
        this.player_count = player_count;
    }
    */

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can execute this command!");
            return true;
        }

        /* Step 1 - randomly assigned imposters
         *
         * 1. Need to know player count (done)
         * 2. Pick an (entered) amount of imposters from the player count (done?)
         * 3. Reveal roles to players (50%)
         * 4. Imposters only know each other
         *
         * Potential problems/challenges:
         * - Randomising the imposters
         * - Only picking the entered amount imposters
         */

        int playerCount = Bukkit.getServer().getOnlinePlayers().size();

        // Checking to see the argument is valid
        int imposterAmount = Integer.parseInt(args[0]);
        if (imposterAmount <= 0 || imposterAmount > playerCount) {
            sender.sendMessage("§cInvalid amount of imposters");
        }

        // Accessing all the players online
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        for (Player player : players) {
            System.out.println(player.getName() + " has been registered");
            player.sendMessage("§bYou have been registered!");
        }

        // Making sure there are no duplicates
        ArrayList<Integer> playerNumberList = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            playerNumberList.add(i);
        }

        // Generating the random imposters
        Random random = new Random();
        for (int i = 0; i < imposterAmount; i++) {
            // Removing the player number if it
            int imposter = playerNumberList.remove(random.nextInt(0, playerCount));
            int count = 0;
            for (Player player : players) {
                if (count == imposter) {
                    player.sendMessage("§cYou are the imposter:)");
                    break;
                } else {
                    player.sendMessage("§aYou are innocent:)");
                }
                count++;
            }
            playerCount--;
        }
        playerCount = Bukkit.getServer().getOnlinePlayers().size();

        return true;
    }
}
