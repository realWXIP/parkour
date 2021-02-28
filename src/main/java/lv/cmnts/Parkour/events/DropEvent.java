package lv.cmnts.Parkour.events;

import lv.cmnts.Parkour.Parkour;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropEvent implements Listener {
    public Parkour plugin;

    public DropEvent(Parkour plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();

        if (e.getItemDrop().getItemStack().getType() == Material.WATCH) e.setCancelled(true);
        else if (e.getItemDrop().getItemStack().getType() == Material.BOOK) e.setCancelled(true);
    }
}
