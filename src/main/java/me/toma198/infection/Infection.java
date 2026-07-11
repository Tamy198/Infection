package me.toma198.infection;

import me.toma198.commands.FlyCommand;
import me.toma198.commands.InfectionCommand;
import me.toma198.commands.NameTagsOffCommand;
import me.toma198.commands.NameTagsOnCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

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

        // So there's only one command object for each command
        InfectionCommand infectionCommand = new InfectionCommand(this);
        NameTagsOnCommand nameTagsOnCommand = new NameTagsOnCommand(this);
        NameTagsOffCommand nameTagsOffCommand = new NameTagsOffCommand(this);

        // Register commands and listeners
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(infectionCommand, this);
        getServer().getPluginManager().registerEvents(nameTagsOnCommand, this);
        getServer().getPluginManager().registerEvents(nameTagsOffCommand, this);
        Objects.requireNonNull(getCommand("fly")).setExecutor(new FlyCommand());
        Objects.requireNonNull(getCommand("infection")).setExecutor(infectionCommand);
        Objects.requireNonNull(getCommand("nameTagsOn")).setExecutor(nameTagsOnCommand);
        Objects.requireNonNull(getCommand("nameTagsOff")).setExecutor(nameTagsOffCommand);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        player_count++;
        System.out.println("Player count is " + player_count);
    }

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
