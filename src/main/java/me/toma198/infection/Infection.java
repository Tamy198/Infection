package me.toma198.infection;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * What I need:
 * 1. Randomly assign imposters
 * 2. Design the infection mechanic (if a death is within 20 blocks of imposter, conversion begins)
 * 3. Design an objective (bingo, timelimit, imposter win condition)
 * 4. Remove nametags (can be done with commands)
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

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("My plugin has stopped!!");
    }
}
