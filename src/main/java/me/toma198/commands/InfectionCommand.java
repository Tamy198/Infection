package me.toma198.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
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

    // A scheduler for asynchronous waiting so the server doesn't sleep
    //BukkitScheduler scheduler = getServer().getScheduler();

    // Accessing all the players online
    ArrayList<Player> players = new ArrayList<>();

    // Imposter and innocent list
    ArrayList<Player> imposterList = new ArrayList<>();
    ArrayList<Player> innocentList = new ArrayList<>();

    // How many people have been infected during the game
    int infected = 0;

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

        // Countdown
        String title;
        String subtitle = "";
        for (int j = 3; j > 0; j--) {
            title = ChatColor.GOLD + Integer.toString(j);
            for (Player player : players) {
                player.sendTitle(title, subtitle, 1, 6, 2);
                player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED,
                        10000f, 1f);
            }

            // pause for 1 second
            /*
            new BukkitRunnable() {
                @Override
                public void run() {
                    System.out.println("Executed after delay!");
                }
            }.runTaskLater(plugin, 20L);

             */

            // pause for 1 second
            Bukkit.getScheduler().runTaskLater(plugin, () -> System.out.println("""
                    Executed after delay! (or do I have to wait until \
                    after this pause?"""), 20L);

            /*
            scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    System.out.println("Waited 1 second?");
                    for (Player player : players) {
                        player.sendTitle(title, subtitle, 1, 6, 2);
                        player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED,
                                10000f, 1f);
                    }
                }
                }, 0L, 20L);
                */

            /*
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
             */
        }


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

        // Reveal the imposters to one another
        String imposterReveal = "§cThe imposters are: ";
        for (Player imposter : imposterList) {
            imposterReveal += imposter.getName() + " ";
        }

        // Inform imposters of their dubiousness
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
            //System.out.println(player.getName() + " is innocent");
        }

        // Wait for dramatic effect
        //scheduler.scheduleSyncDelayedTask(plugin, () ->
                //System.out.println("Waited 4 second?"), 80L);

        // Wait for dramatic effect
        // pause for 4 second (can anything else happen during this pause?)
        /*
        new BukkitRunnable() {
            @Override
            public void run() {
                System.out.println("Executed after delay!");
            }
        }.runTaskLater(plugin, 20L * 4);
         */

        // Wait for dramatic effect
        // pause for 4 second (can anything else happen during this pause?)
        Bukkit.getScheduler().runTaskLater(plugin, () ->
                System.out.println("Executed after delay! (or do I have to wait until " +
                "after this pause?"), 600L);

        /*
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
         */

        // Then reveal message sent to imposters
        for (Player player : imposterList) {
            player.sendMessage(imposterReveal);
        }

        return true;
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

    // DOESNT REGISTER DEATH?
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
        System.out.println("ImposterDeath didn't run");
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
        }
    }

    public void innocentDeath(Player p) {
        /*3. Check if an imposter is near (or if the imposter damaged them)
        * and then begin the conversion
        * 4. Design the conversion
        */

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
            }
        }

        // if within 30 blocks, conversion
        if (activateConversion) {
            conversion(p);
        }

        // else set innocent to spectator
        p.setGameMode(GameMode.SPECTATOR);
    }

    public void conversion(Player p) {
        // Might be a bit flawed - will this send them back and will the effects apply?
        //p.setRespawnLocation(p.getLocation());

        // During the conversion, the player will be invincible and immobile
        p.setHealth(20);
        p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,
                30, 255, false, true));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,
                30, 255, false, true));
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                30, 255, false, true));
        p.setSneaking(true);

        // Applies a freezing effect to the player
        p.setFreezeTicks(p.getMaxFreezeTicks());

        // Set to infected and join the imposter team
        imposterList.add(p);
        innocentList.remove(p);
        System.out.println("Conversion");
        System.out.println(imposterList);
        System.out.println(innocentList);
        infected++;

        // Pause for 30 second
        /*
        new BukkitRunnable() {
            @Override
            public void run() {
                System.out.println("Executed after 30 seconds!");
                p.setFreezeTicks(0);
                p.removePotionEffect(PotionEffectType.SLOWNESS);
                p.removePotionEffect(PotionEffectType.RESISTANCE);
                p.removePotionEffect(PotionEffectType.REGENERATION);
                p.setSneaking(false);
            }
        }.runTaskLater(plugin, 20L * 30);
        */

        // Conversion lasts 30 seconds
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            System.out.println("Executed after 30 seconds!");
            p.setFreezeTicks(0);
            p.removePotionEffect(PotionEffectType.SLOWNESS);
            p.removePotionEffect(PotionEffectType.RESISTANCE);
            p.removePotionEffect(PotionEffectType.REGENERATION);
            p.setSneaking(false);
            System.out.println("Waited 30 second?");
        }, 600L);

        /*
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
         */
    }
}
