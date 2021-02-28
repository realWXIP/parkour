package lv.cmnts.Parkour.commands;

import lv.cmnts.Parkour.Parkour;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.Instant;

import static org.bukkit.Bukkit.getServer;

public class ParkourCommand implements CommandExecutor {
    public Parkour plugin;

    public ParkourCommand(final Parkour plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        World minw = Bukkit.getWorld("Parkour");
        World maxw = Bukkit.getWorld("Parkour");

        Location min = new Location(minw, plugin.gate_minx, plugin.gate_miny, plugin.gate_minz);
        Location max = new Location(maxw, plugin.gate_maxx, plugin.gate_maxy, plugin.gate_maxz);

        Material mato = Material.getMaterial(plugin.gate_open);
        Material matc = Material.getMaterial(plugin.gate_close);

        if (args.length == 0) {
            plugin.translate(sender, " &8» &7Incomplete command!");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start": {
                if (sender.hasPermission("parkour.admin")) {
                    if (!plugin.parkour_started) {
                        if (args.length == 2) {

                            plugin.parkour_started = true;
                            plugin.parkour_winners = Integer.parseInt(args[1]);
                            plugin.parkour_started_time = Instant.now();

                            plugin.doReplace(min, max, matc, mato);

                            for (Player i : Bukkit.getOnlinePlayers()) {

                                if (!i.hasPermission("parkour.bypass")) {
                                    getServer().dispatchCommand(Bukkit.getConsoleSender(), "spawn " + i.getName());
                                }

                                plugin.translate(i, "");
                                plugin.translate(i, " &4» &c" + plugin.eventname + " event has started!");
                                plugin.translate(i, "");
                            }
                            return true;
                        } else {
                            plugin.translate(sender, " &8» &7Incomplete command!");
                            return true;
                        }
                    } else {
                        plugin.translate(sender, " &8» &7Parkour event has already started!");
                        return true;
                    }
                } else {
                    plugin.translate(sender, " &8» &7You don't have permission to execute this command!");
                    return true;
                }
            }

            case "autostart": {
                if (sender.hasPermission("parkour.admin")) {
                    if (!plugin.parkour_started) {
                        if (args.length == 3) {

                            plugin.parkour_started = true;
                            plugin.parkour_winners = Integer.parseInt(args[1]);
                            int time = Integer.parseInt(args[2]);


                            for (Player p : Bukkit.getOnlinePlayers()) {
                                plugin.translate(p, "");
                                plugin.translate(p, " &4» &c" + plugin.eventname + " is going to start in " + start(Integer.parseInt(args[1])/60) + "!");
                                plugin.translate(p, "");
                            }

                            new BukkitRunnable() {
                                int sec = time*60;

                                @Override
                                public void run() {
                                    if (sec <= 0) {

                                        plugin.doReplace(min, max, matc, mato);

                                        plugin.parkour_started_time = Instant.now();

                                        for (Player p : Bukkit.getOnlinePlayers()) {

                                            if (!p.hasPermission("parkour.bypass")) {
                                                getServer().dispatchCommand(Bukkit.getConsoleSender(), "spawn " + p.getName());
                                            }

                                            plugin.translate(p, "");
                                            plugin.translate(p, " &4» &c" + plugin.eventname + " event has started!");
                                            plugin.translate(p, "");
                                        }

                                        this.cancel();
                                    }

                                    if (sec == (time*60*0.75)) {
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            plugin.translate(p, "");
                                            plugin.translate(p, " &4» &c" + plugin.eventname + " is going to start in " + start(sec/60) + "!");
                                            plugin.translate(p, "");
                                        }
                                    }

                                    if (sec == (time*60*0.5)) {
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            plugin.translate(p, "");
                                            plugin.translate(p, " &4» &c" + plugin.eventname + " is going to start in " + start(sec/60) + "!");
                                            plugin.translate(p, "");
                                        }
                                    }

                                    if (sec == (time*60*0.25)) {
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            plugin.translate(p, "");
                                            plugin.translate(p, " &4» &c" + plugin.eventname + " is going to start in " + start(sec/60) + "!");
                                            plugin.translate(p, "");
                                        }
                                    }
                                    sec--;
                                }
                            }.runTaskTimer(plugin, 0L, 20L);
                            return true;
                        } else {
                            plugin.translate(sender, " &8» &7Incomplete command!");
                            return true;
                        }
                    } else {
                        plugin.translate(sender, " &8» &7Parkour event has already started!");
                        return true;
                    }
                } else {
                    plugin.translate(sender, " &8» &7You don't have permission to execute this command!");
                    return true;
                }
            }

            case "gate": {
                if (sender.hasPermission("parkour.gate")) {
                    if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("open")) {
                            plugin.doReplace(min, max, matc, mato);
                            plugin.translate(sender, " &8» &cGate is now open!");
                        }
                        if (args[1].equalsIgnoreCase("close")) {
                            plugin.doReplace(min, max, mato, matc);
                            plugin.translate(sender, " &8» &cGate is closed now!");
                        }
                    } else plugin.translate(sender, " &8» &7Incomplete command!");
                    return true;
                }
            }
        }
        return true;
    }

    public String start(int time) {
        if (time == 1) return time + " minute";
        else return time + " minutes";
    }
}
