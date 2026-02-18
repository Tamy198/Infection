package me.toma198.infection;

import me.toma198.commands.FlyCommand;
import me.toma198.commands.InfectionCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * What I need:
 * 1. Randomly assign imposters (not fully tested)
 * 2. Design the infection mechanic (if a death is within 30 blocks of imposter, conversion begins)
 * 3. Design an objective (bingo, timelimit, imposter win condition)
 * 4. Remove nametags (can be done with commands)
 * 5. If both imposters die without infecting anyone, they choose one person to pass it on to
 * (part of step 2)
 *
 * Bingo and imposter win condition can be done manually if needed
 */

public final class Infection extends JavaPlugin implements Listener {
    int player_count;

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("My plugin has started!!");

        // So there's only one InfectionCommand object
        InfectionCommand infectionCommand = new InfectionCommand(this);

        // Register commands and listeners
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(infectionCommand, this);
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("infection").setExecutor(infectionCommand);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        player_count++;
        System.out.println("Player count is " + player_count);
    }

    /*
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        System.out.println("A player has died");
    }
     */

    /* Step 1 - randomly assigned imposters
    *
    * 1. Need to know player count (done)
    * 2. Pick an (entered) amount of imposters from the player count - command is probably best way
    * 3. Reveal roles to players
    * 4. Imposters only know each other
    *
    * Potential problems/challenges:
    * - Randomising the imposters
    * - Only picking the entered amount imposters
    */

    /* * Step 2. Design the infection mechanic
     * (if a death is within 30 blocks of imposter, conversion begins)
     *
     * 1. Upon a death event, check if a player is an imposter or innocent
     * 2. If an imposter, turn into spectate
     * 3. If an innocent, check if an imposter is near (or if the imposter damaged them)
     * and then begin the conversion
     * 4. Design the conversion
     */

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        player_count--;
        System.out.println("Player count is " + player_count);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("My plugin has stopped!!");
    }
}
