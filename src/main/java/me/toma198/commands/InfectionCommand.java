package me.toma198.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NonNull;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class InfectionCommand implements CommandExecutor, Listener {
    /* just for me to practice my java - later fix would be to make player count an argument
    int player_count;
    public InfectionCommand(int player_count) {
        this.player_count = player_count;
    }
    */
    Plugin plugin;

    public InfectionCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    // Accessing all the players online
    // ** SHOULD I JUST DO THIS BY CALLING BUKKIT.ONLINEPLAYERS EVERYTIME **
    // ** RATHER THAN STORING **
    // ** AND USING UUID RATHER THAN PLAYER OBJECTS **
    ArrayList<Player> players = new ArrayList<>();

    // Imposter and innocent list
    ArrayList<Player> imposterList = new ArrayList<>();
    ArrayList<Player> innocentList = new ArrayList<>();

    // How many people have been infected during the game
    int infected = 0;

    // The list of people who should be converted
    ArrayList<Player> conversionList = new ArrayList<>();

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command,
                             @NonNull String label, @NonNull String @NonNull [] args) {
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
         * 5. Sounds (done)
         *
         * Potential problems/challenges:
         * - Randomizing the imposters
         * - Only picking the entered amount imposters
         */

        players = new ArrayList<>(getServer().getOnlinePlayers());

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

        int playerCount = getServer().getOnlinePlayers().size();

        // Checking to see the argument is valid
        int imposterAmount = Integer.parseInt(args[0]);
        if (imposterAmount <= 0 || imposterAmount > playerCount) {
            sender.sendMessage("§cInvalid amount of imposters, must be between 1 and the playerCount");
            return true;
        }

        // Assign the imposters by randomly shuffling the player list and then the dividing
        // the first part to the imposter team and the remainder to the innocent team
        // SMTH TO TEST: DOES THIS MEAN THE FIRST NAMES ON THE TAB ARE IMPOSTERS??
        // ** THE FIRST PERSON TO JOIN SEEMS TO BE THE IMPOSTER WHEN COMMAND IS FIRST RUN?? **
        // ** ACTUALLY NOT ALWAYS MAYBE IT WAS THE 50/50 CHANCE **
        // ** NEED TO TEST
        Collections.shuffle(players);
        this.imposterList = new ArrayList<>(players.subList(0, imposterAmount));
        this.innocentList = new ArrayList<>(players.subList(imposterAmount, players.size()));
        for (Player player : players) {
            System.out.println(player.getName() + " has been registered");
            player.sendMessage("§bYou have been registered!");
        }

        // Making sure there are no duplicates
        /*
        ArrayList<Integer> playerNumberList = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            playerNumberList.add(i);
        }
        */

        // Generating the random imposters
        /*
        Random random = new Random();
        int count;
        for (inti = 0; i < imposterAmount; i++) {
            // Removing the player number from player list to ensure no duplicates
            int imposter = playerNumberList.remove(random.nextInt(playerNumberList.size()));

            // Assign imposters
            count = 0;
            for (Player player : players) {
                if (count == imposter) {
                    imposterList.add(player);
                    innocentList.remove(player);
                }
                count++;
            }
        }
        */

        // Countdown
        String title3 = ChatColor.GOLD + Integer.toString(3);
        String title2 = ChatColor.GOLD + Integer.toString(2);
        String title1 = ChatColor.GOLD + Integer.toString(1);

        // no delay
        printTitle(title3, players);
        System.out.println("Executed after delay!");

        // 1-second delay
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            printTitle(title2, players);
            System.out.println("Executed after delay!");
        }, 20L);

        // 2-second delay
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            printTitle(title1, players);
            System.out.println("Executed after delay!");
        }, 40L);

        // Wait an additional second to reveal roles
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            // Inform imposters of their dubiousness
            String subtitle = "";
            String imposterMessage = ChatColor.RED + "You are the imposter:)";
            for (Player player : imposterList) {
                player.sendTitle(imposterMessage, subtitle, 10, 70, 20);
                player.sendMessage("§cYou are the imposter, infect the innocents:)");
                player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 10000f, 1f);
            }

            // Inform innocents of their innocence
            String innocent = ChatColor.GREEN + "You are NOT the imposter:)";
            for (Player player : innocentList) {
                player.sendTitle(innocent, subtitle, 10, 70, 20);
                player.sendMessage("§aYou are innocent:)");
                player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0, 10000f, 1f);
            }
        }, 60L);

        // Wait 4 seconds - from the additional 4 from earlier - for dramatic effect
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            // Reveal the imposters to one another
            StringBuilder imposterReveal = new StringBuilder("§cThe imposters are: ");
            int imposterCount = 0;
            for (Player imposter : imposterList) {
                imposterCount++;
                if (imposterCount == imposterList.size()) {
                    imposterReveal.append(imposter.getName());
                } else {
                    imposterReveal.append(imposter.getName()).append(", ");
                }
            }

            // Then send the reveal message to imposters
            for (Player player : imposterList) {
                player.sendMessage(imposterReveal.toString());
            }
            System.out.println("Executed after 4 second delay!");
        }, 140L);

        System.out.println("Printing imposter and innocent list");
        System.out.println(imposterList);
        System.out.println(innocentList);

        return true;
    }

    // Print a title with default configurations
    void printTitle(String title, ArrayList<Player> players) {
        String subtitle = "";
        for (Player player : players) {
            player.sendTitle(title, subtitle, 1, 6, 2);
            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_CLUSTER_BREAK,
                    10000f, 1f);
            player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED,
                    10000f, 1f);
        }
    }

    /* * Step 2. Design the infection mechanic
     * (if a death is within 30 blocks of imposter, conversion begins)
     *
     * 1. Upon a death event, check if a player is an imposter or innocent (done?)
     * 2. If an imposter, turn into spectate (done?)
     * 3. If an innocent, check if an imposter is near (or if the imposter damaged them)
     * and then begin the conversion (done?)
     * 4. Design the conversion (done?)
     * 5. Imposters wrath (yet to implement)
     */

    /** BUGS:
     * 1. Countdown (fixed)
     * 2. Respawning (need game rule not fixed instant)
     * 3. Invincible after conversion (fixed)
     * 4. No effects or freezing after death
     * 5. Dead player doesn't sync with the world after conversion (fixed)
     * 6. Joining order effect who becomes imposter?
     */

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player dead = e.getEntity();
        System.out.println(dead.getName() + " has died!");
        System.out.println(dead);
        System.out.println(imposterList);

        boolean imposter = false;
        // see if the death is an imposter
        for (Player player : imposterList) {
            if (player.equals(dead)) {
                // imposter death
                imposter = true;
                System.out.println("HII!!!, should run imposterDeath");
            }
        }

        // END the imposter
        if (imposter) {
            imposterDeath(dead);
            return;
        }

        // innocent death or conversion
        System.out.println("HII!!!, should run innocentDeath and conversion");
        if (innocentList.contains(dead)) {
            innocentDeath(dead);
        }
    }

    public void imposterDeath(Player p) {
        p.setGameMode(GameMode.SPECTATOR);
        imposterList.remove(p);
        System.out.println("Player " + p.getName() + " has died!");
        System.out.println(imposterList);

        if (infected == 0 && imposterList.isEmpty()) {
            // allow them to choose a player to inherit the infection (imposter's wrath)
            p.sendMessage(ChatColor.RED + "Choose an imposter");
            // **TO IMPLEMENT**
            // **Also need to implement conditions for when it runs (no visible attempt)**
        } else if (imposterList.isEmpty()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.GREEN + "Innocents have won!");
            }
        }
    }

    public void innocentDeath(Player p) {
        // Imposter coordinates
        double impX;
        double impY;
        double impZ;

        // Innocent coordinates
        double X = p.getLocation().getX();
        double Y = p.getLocation().getY();
        double Z = p.getLocation().getZ();

        // Absolute distance squared of imposter to dead player in each dimension
        double absX;
        double absY;
        double absZ;

        double distFromImp;

        // Calculate distance of the player who died to the imposter
        boolean activateConversion = false;
        for (Player player : imposterList) {
            impX = player.getLocation().getX();
            impY = player.getLocation().getY();
            impZ = player.getLocation().getZ();

            absX = Math.pow((X - impX), 2);
            absY = Math.pow((Y - impY), 2);
            absZ = Math.pow((Z - impZ), 2);

            distFromImp = Math.sqrt(absX + absY + absZ);

            // if within 30 blocks, conversion conditions are satisfied
            if (distFromImp <= 30) {
                activateConversion = true;
                conversionList.add(p);
            }
        }

        // else set innocent to spectator
        if (!activateConversion) {
            p.setGameMode(GameMode.SPECTATOR);
        }

        // Activate a respawn event a tick later
        // ** DOESN'T WORK?? **
        p.setRespawnLocation(p.getLocation());
        // ** NEED GAME RULE?? **
        Bukkit.getScheduler().runTaskLater(plugin, () -> p.spigot().respawn(), 1L);
    }

    // Run conversion if necessary upon respawn
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();

        if (conversionList.contains(p)) {
            conversionList.remove(p);
            conversion(p);
        }
    }

    public void conversion(Player p) {
        // During the conversion, the player will be invincible and immobile
        // ** NO EFFECTS **
        /*
        p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,
                600, 255, false, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,
                600, 255, false, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                600, 255, false, false));
         */

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,
                    600, 255, true, true));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,
                    600, 255, true, true));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                    600, 255, true, true));
        }, 1L);

        // Applies a freezing effect to the player
        // ** ONLY LASTS FOR A LIMITED WINDOW **
        p.playSound(p.getLocation(), Sound.ENTITY_CREAKING_FREEZE,
                10000f, 1f);
        /*boolean freezing = true;
        while (freezing) {
            p.setFreezeTicks(p.getMaxFreezeTicks());
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                final freezing = false;
                System.out.println("Waited 30 second?");
            }, 600L);
        }*/

        // Apply freezing effect for 30 seconds
        p.setFreezeTicks(p.getMaxFreezeTicks());
        for (int i = 0; i < 30; i++) {
            Bukkit.getScheduler().runTaskLater(plugin, () ->
                    p.setFreezeTicks(p.getMaxFreezeTicks()), 1L);
        }

        // Set to infected and join the imposter team
        imposterList.add(p);
        innocentList.remove(p);
        System.out.println("Conversion");
        System.out.println(imposterList);
        System.out.println(innocentList);
        infected++;

        // Conversion lasts 30 seconds
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            System.out.println("Executed after 30 seconds!");
            p.removePotionEffect(PotionEffectType.SLOWNESS);
            p.removePotionEffect(PotionEffectType.RESISTANCE);
            p.removePotionEffect(PotionEffectType.REGENERATION);
            System.out.println("Waited 30 second?");
        }, 600L);
    }
}
