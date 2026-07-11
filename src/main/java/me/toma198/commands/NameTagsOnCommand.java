package me.toma198.commands;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public class NameTagsOnCommand implements CommandExecutor, Listener {
    Plugin plugin;
    public NameTagsOnCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command,
                             @NonNull String label, String @NonNull [] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can execute this command!");
            return true;
        }

        Scoreboard board = new Scoreboard() {
            @Override
            public Objective registerNewObjective(String s, String s1) {
                return null;
            }

            @Override
            public Objective registerNewObjective(String s, String s1, String s2) {
                return null;
            }

            @Override
            public Objective registerNewObjective(String s, String s1, String s2, RenderType renderType) {
                return null;
            }

            @Override
            public Objective registerNewObjective(String s, Criteria criteria, String s1) {
                return null;
            }

            @Override
            public Objective registerNewObjective(String s, Criteria criteria, String s1, RenderType renderType) {
                return null;
            }

            @Override
            public Objective getObjective(String s) {
                return null;
            }

            @Override
            public Set<Objective> getObjectivesByCriteria(String s) {
                return Set.of();
            }

            @Override
            public Set<Objective> getObjectivesByCriteria(Criteria criteria) {
                return Set.of();
            }

            @Override
            public Set<Objective> getObjectives() {
                return Set.of();
            }

            @Override
            public Objective getObjective(DisplaySlot displaySlot) {
                return null;
            }

            @Override
            public Set<Score> getScores(OfflinePlayer offlinePlayer) {
                return Set.of();
            }

            @Override
            public Set<Score> getScores(String s) {
                return Set.of();
            }

            @Override
            public void resetScores(OfflinePlayer offlinePlayer) {

            }

            @Override
            public void resetScores(String s) {

            }

            @Override
            public Team getPlayerTeam(OfflinePlayer offlinePlayer) {
                return null;
            }

            @Override
            public Team getEntryTeam(String s) {
                return null;
            }

            @Override
            public Team getTeam(String s) {
                return null;
            }

            @Override
            public Set<Team> getTeams() {
                return Set.of();
            }

            @Override
            public Team registerNewTeam(String s) {
                return null;
            }

            @Override
            public Set<OfflinePlayer> getPlayers() {
                return Set.of();
            }

            @Override
            public Set<String> getEntries() {
                return Set.of();
            }

            @Override
            public void clearSlot(DisplaySlot displaySlot) {

            }
        };
        Team hidden = board.getTeam("hidden");
        if (hidden == null) {
            hidden = board.registerNewTeam("hidden");
        }

        hidden.setOption(
                Team.Option.NAME_TAG_VISIBILITY,
                Team.OptionStatus.ALWAYS
        );

        for (Player player : Bukkit.getOnlinePlayers()) {
            hidden.addEntry(player.getName());
        }

        return true;
    }
}
