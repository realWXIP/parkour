package lv.cmnts.Parkour.events;

import lv.cmnts.Parkour.Parkour;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;

public class MoveEvent implements Listener {
    public Parkour plugin;

    public MoveEvent(Parkour plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if(e.getAction().equals(Action.PHYSICAL) && e.getClickedBlock().getType() == Material.STONE_PLATE) {
            if (plugin.parkour_started) {
                if (!p.hasPermission("parkour.fix")) {
                    if (!plugin.finished.contains(p.getName()) && plugin.finished.size() < plugin.parkour_winners) {
                        plugin.finished.add(p.getName());
                        plugin.finished2.put(p.getName(), plugin.finished2.size() + 1);
                        final Date myDate = Date.from(plugin.parkour_started_time);
                        final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                        final String formattedDate = formatter.format(myDate);
                        final Date myDate2 = Date.from(Instant.now());
                        final SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
                        final String formattedDate2 = formatter2.format(myDate2);
                        final LocalTime t1 = LocalTime.parse(formattedDate);
                        final LocalTime t2 = LocalTime.parse(formattedDate2);
                        final Duration d = Duration.between(t2, t1);
                        final long hours = d.toHours();
                        final long minutes = d.toMinutes() % 60L;
                        final long secs = d.getSeconds() % 60L;
                        for (final Player pp : Bukkit.getOnlinePlayers()) {
                            plugin.translate(pp, " ");
                            plugin.translate(pp, " &8» &c&l" + p.getName() + "&r&c has finished " + plugin.reachfinish + "! &8[&4" + Math.abs(hours) + "h&4:" + Math.abs(minutes) + "m&4:" + Math.abs(secs) + "s&r&8]");
                            this.plugin.translate(pp, " ");
                        }
                        if (plugin.finished.size() == plugin.parkour_winners) {
                            for (final Player pp : Bukkit.getOnlinePlayers()) {
                                plugin.translate(pp, " ");
                                plugin.translate(pp, " &8» &cWinners:");
                                plugin.finished2.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(k -> plugin.translate(pp, "    &7" + k.getValue() + ". &c" + k.getKey()));
                            }
                            plugin.parkour_started = false;
                            plugin.finished2.clear();
                            plugin.finished.clear();
                            plugin.parkour_started_time = null;
                            //plugin.checkpoint.clear();
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void onFall(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        Location gold = p.getLocation().getBlock().getLocation();

        gold.setX(gold.getX() + 0.5);
        gold.setZ(gold.getZ() + 0.5);

        //if (plugin.parkour_started) {
            if (e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GOLD_BLOCK) {
                if (p.getWorld().equals(plugin.getServer().getWorld("Parkour"))) {
                    if (!Parkour.getKeysByValue(plugin.checkpoint, gold).contains(p.getName())) {
                        Location l = e.getPlayer().getLocation().getBlock().getLocation();

                        l.setX(l.getX() + 0.5);
                        l.setZ(l.getZ() + 0.5);


                        plugin.checkpoint.put(p.getName(), l);
                        plugin.translate(p, plugin.reachcheck);
                    }
                }
            }
        //}

        if (p.getWorld().equals(plugin.getServer().getWorld("Parkour"))) {
            if (plugin.checkpoint.containsKey(p.getName())) {
                //float pjaw = p.getLocation().getYaw();
                //float ppitch = p.getLocation().getPitch();

                if (p.getLocation().getBlockY() <= plugin.lowestpoint) {
                    p.teleport(plugin.checkpoint.get(p.getName()));

                    Location loc =
                            new Location(
                                    p.getLocation().getWorld(),
                                    p.getLocation().getX(),
                                    p.getLocation().getY(),
                                    p.getLocation().getZ(),
                                    Float.parseFloat(plugin.tp_yaw),
                                    Float.parseFloat(plugin.tp_pitch));

                    p.teleport(loc);
                }
            } else {
                if (p.getLocation().getBlockY() <= plugin.lowestpoint) {
                    Location loc = new Location(
                            p.getLocation().getWorld(),
                            -0.5,
                            217,
                            -21.5,
                            Float.parseFloat(plugin.tp_yaw),
                            Float.parseFloat(plugin.tp_pitch));

                    p.teleport(loc);
                }
            }
        }
    }
}
