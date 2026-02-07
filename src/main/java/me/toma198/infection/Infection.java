package me.toma198.infection;

import me.toma198.commands.FlyCommand;
import me.toma198.commands.InfectionCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * What I need:
 * 1. Randomly assign imposters (yet to be tested)
 * 2. Design the infection mechanic (if a death is within 20 blocks of imposter, conversion begins)
 * 3. Design an objective (bingo, timelimit, imposter win condition)
 * 4. Remove nametags (can be done with commands)
 * 5. If both imposters die without infecting anyone, they choose one person to pass it on to
 *
 * Bingo and imposter win condition can be done manually if needed
 */

public final class Infection extends JavaPlugin implements Listener {
    int player_count;

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("My plugin has started!!");
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("infection").setExecutor(new InfectionCommand());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        player_count++;
        System.out.println("Player count is " + player_count);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        player_count--;
        System.out.println("Player count is " + player_count);
    }

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

    /*
    @EventHandler
    public void onCommandExecuted(PlayerCommandSendEvent event) {
        getCommand("fly").setExecutor(new InfectionCommand(player_count));
    }
    */

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("My plugin has stopped!!");
    }
}
