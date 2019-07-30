package me.lkd70.sethomeextra;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

public class SetHomeExtra extends JavaPlugin {

    private File file = new File(getDataFolder(), "Homes.yml");
    YamlConfiguration homes = YamlConfiguration.loadConfiguration(file);
    private FileConfiguration config = getConfig();
    private static final String prefixError = ChatColor.DARK_RED + "[" + ChatColor.RED + "*" + ChatColor.DARK_RED + "] " + ChatColor.GRAY;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, @NotNull String[] args) {
        SetHomeExtraUtils utils = new SetHomeExtraUtils(this);

        if (command.getName().equals("sethome")) {
            if (sender instanceof ConsoleCommandSender) {
                getLogger().log(Level.WARNING, "This command is for players only");
            } else if (sender instanceof Player) {
                Player player = (Player) sender;
                utils.setHome(player);
                if (config.getBoolean("debug")) getLogger().log(Level.INFO, player.getDisplayName() + " has set their home at: " + utils.getHomeLocation(player).toString());
                String strFormatted = Objects.requireNonNull(config.getString("sethome-message")).replace("%player%", player.getDisplayName());
                if (config.getBoolean("show-sethome-message")) player.sendMessage(ChatColor.translateAlternateColorCodes('&', strFormatted));
            } else {
                sender.sendMessage(prefixError + "An error occurred whilst trying to set your home.");
            }
        } else if (command.getName().equals("home")) {
            if (sender instanceof ConsoleCommandSender) {
                getLogger().log(Level.WARNING, "This command is for players only");
            } else if (sender instanceof Player) {
                Player player = (Player) sender;
                String strFormatted = Objects.requireNonNull(config.getString("teleport-message"))
                        .replace("%time%", Integer.toString(config.getInt("teleport-delay")))
                        .replace("%player%", player.getDisplayName());
                if (config.getBoolean("show-teleport-message")) player.sendMessage(ChatColor.translateAlternateColorCodes('&', strFormatted));
                Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
                    if (config.getBoolean("play-warp-sound")) player.playSound(utils.getHomeLocation(player), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                    utils.sendHome(player);
                    if (config.getBoolean("show-home-message")) player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("home-message"))
                            .replace("%player%", player.getDisplayName())));
                    if (config.getBoolean("debug")) getLogger().log(Level.INFO, player.getDisplayName() + " has been sent to their home at: " + utils.getHomeLocation(player).toString());
                }, 20 * config.getInt("teleport-delay"));
            }
        }
        return false;
    }

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("sethome")).setExecutor(this);
        Objects.requireNonNull(getCommand("home")).setExecutor(this);
        getServer().getPluginManager().registerEvents(new SetHomeExtraEvents(this), this);

        config.options().copyDefaults(true);
        saveDefaultConfig();

        try {
            config.save(getDataFolder() + File.separator + "config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!file.exists()) saveHomesFile();

    }

    void saveHomesFile() {
        try {
            homes.save(file);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not save homes file.\nHere is the stack trace:");
            e.printStackTrace();
        }
    }
}
