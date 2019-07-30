package me.lkd70.sethomeextra;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Objects;
import java.util.logging.Level;

public class SetHomeExtraEvents implements Listener {
    private SetHomeExtra plugin;

    SetHomeExtraEvents(SetHomeExtra plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        SetHomeExtraUtils utils = new SetHomeExtraUtils(plugin);

        if (event.getPlayer().getBedSpawnLocation() != null) {
            if (plugin.getConfig().getBoolean("debug")) {
                plugin.getLogger().log(Level.WARNING, "Player " + event.getPlayer().getName() + " already has a bed location. Not teleporting to 'set home'.");
            }
        } else if (!utils.homeIsNull(event.getPlayer())) {
            event.setRespawnLocation(new Location(Bukkit.getWorld(Objects.requireNonNull(plugin.homes.getString("Homes." + event.getPlayer().getUniqueId().toString() + ".World"))),
                    plugin.homes.getDouble("Homes." + event.getPlayer().getUniqueId().toString() + ".X"),
                    plugin.homes.getDouble("Homes." + event.getPlayer().getUniqueId().toString() + ".Y"),
                    plugin.homes.getDouble("Homes." + event.getPlayer().getUniqueId().toString() + ".Z"),
                    plugin.homes.getLong("Homes." + event.getPlayer().getUniqueId().toString() + ".Yaw"),
                    plugin.homes.getLong("Homes." + event.getPlayer().getUniqueId().toString() + ".Pitch"))
            );
        }
    }
}
