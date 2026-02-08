package me.toma198.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

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
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command,
                             @NonNull String label, @NonNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can execute this command!");
            return true;
        }

        /* Step 1 - randomly assigned imposters (working?)
         *
         * 1. Need to know player count (done)
         * 2. Pick an (entered) amount of imposters from the player count (done)
         * 3. Reveal roles to players (done)
         * 4. Imposters only know each other (done)
         * 5. Sounds (not yet)
         *
         * Potential problems/challenges:
         * - Randomising the imposters
         * - Only picking the entered amount imposters
         */

        // inform of command usage if they typed no arguments
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /" + label + " <imposterAmount>");
            return true;
        }

        // check if the argument is numeric
        for (Character c : args[0].toCharArray()) {
            if (!Character.isDigit(c)) {
                sender.sendMessage("§cImposter amount must be a positive integer");
                sender.sendMessage("§cUsage: /" + label + " <imposterAmount>");
                return true;
            }
        }

        int playerCount = Bukkit.getServer().getOnlinePlayers().size();

        // Checking to see the argument is valid
        int imposterAmount = Integer.parseInt(args[0]);
        if (imposterAmount <= 0 || imposterAmount > playerCount) {
            sender.sendMessage("§cInvalid amount of imposters, must be between 1 and the playerCount");
            return true;
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
        System.out.println(playerNumberList);

        // Imposter list
        ArrayList<Player> imposterList = new ArrayList<>();

        // Generating the random imposters
        Random random = new Random();
        for (int i = 0; i < imposterAmount; i++) {
            // Removing the player number from player list to ensure no duplicates
            int imposter = playerNumberList.remove(random.nextInt(0, playerCount));

            // Countdown (could make it sync later - like the last java series episode clocks)
            String title;
            String subtitle = "";
            for (int j = 3; j > 0; j--) {
                title = ChatColor.GOLD + Integer.toString(j);
                for (Player player : players) {
                    player.sendTitle(title, subtitle, 1, 6, 2);
                    player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 50.0f, 50.0f);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // Tell the player they're the imposter
            String imposterMessage = ChatColor.RED + "You are the imposter:)";
            String innocent = ChatColor.GREEN + "You are NOT the imposter:)";
            int count = 0;
            for (Player player : players) {
                if (count == imposter) {
                    player.sendTitle(imposterMessage, subtitle, 10, 70, 20);
                    //player.sendMessage("§cYou are the imposter:)");
                    player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 100.0f, 100.0f);
                    imposterList.add(player);
                } else {
                    player.sendTitle(innocent, subtitle, 10, 70, 20);
                    //player.sendMessage("§aYou are innocent:)");
                    System.out.println(player.getName() + " is innocent");
                }
                count++;
            }
            playerCount--;
        }
        playerCount = Bukkit.getServer().getOnlinePlayers().size();

        // Reveal the imposters to one another
        String imposterReveal = "§cThe imposters are: ";
        for (Player imposter : imposterList) {
            imposterReveal += imposter.getName() + " ";
        }
        for (Player player : imposterList) {
            player.sendMessage(imposterReveal);
        }

        return true;
    }
}
