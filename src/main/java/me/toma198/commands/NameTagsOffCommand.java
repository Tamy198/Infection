package me.toma198.commands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;
import org.jspecify.annotations.NonNull;

public class NameTagsOffCommand implements CommandExecutor, Listener {
    Plugin plugin;
    public NameTagsOffCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command,
                             @NonNull String label, String @NonNull [] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can execute this command!");
            return true;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            disableNametag(player);
        }
        return true;
    }

    // diable nametags for a player
    public void disableNametag(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        Scoreboard scoreboard = manager.getMainScoreboard();

        // Find or create a team for hidden nametags
        Team team = scoreboard.getTeam("hide_nametags");
        if (team == null) {
            team = scoreboard.registerNewTeam("hide_nametags");
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }

        // Add the player to the team
        team.addEntry(player.getName());

        // Make sure the player sees the updated scoreboard
        player.setScoreboard(scoreboard);
    }
}
