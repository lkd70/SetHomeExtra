package me.lkd70.sethomeextra;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

class SetHomeExtraUtils {

    private SetHomeExtra instance;

    SetHomeExtraUtils(SetHomeExtra instance) {
        this.instance = instance;
    }

    void setHome(Player player) {
        instance.homes.set("Homes." + player.getUniqueId().toString() + ".X", player.getLocation().getX());
        instance.homes.set("Homes." + player.getUniqueId().toString() + ".Y", player.getLocation().getY());
        instance.homes.set("Homes." + player.getUniqueId().toString() + ".Z", player.getLocation().getZ());
        instance.homes.set("Homes." + player.getUniqueId().toString() + ".Yaw", player.getLocation().getYaw());
        instance.homes.set("Homes." + player.getUniqueId().toString() + ".Pitch", player.getLocation().getPitch());
        instance.homes.set("Homes." + player.getUniqueId().toString() + ".World", player.getLocation().getWorld().getName());
        instance.saveHomesFile();
    }

    void sendHome(Player player) {
        player.teleport(getHomeLocation(player));
    }

    Location getHomeLocation(Player player) {
        return new Location(
                Bukkit.getWorld(Objects.requireNonNull(instance.homes.getString("Homes." + player.getUniqueId().toString() + ".World"))),
                instance.homes.getDouble("Homes." + player.getUniqueId().toString() + ".X"),
                instance.homes.getDouble("Homes." + player.getUniqueId().toString() + ".Y"),
                instance.homes.getDouble("Homes." + player.getUniqueId().toString() + ".Z"),
                instance.homes.getLong("Homes." + player.getUniqueId().toString() + ".Yaw"),
                instance.homes.getLong("Homes." + player.getUniqueId().toString() + ".Pitch")
        );
    }

    boolean homeIsNull(Player player) {
        return instance.homes.getString("Homes." + player.getUniqueId()) == null;
    }
}
