package me.toma198.infection;

import org.bukkit.plugin.java.JavaPlugin;

public final class Infection extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Hiiii!!!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Byee!!!");
    }
}
