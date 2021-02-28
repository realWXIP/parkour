package lv.cmnts.Parkour.events;

import lv.cmnts.Parkour.Parkour;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class JoinEvent implements Listener {
    public Parkour plugin;

    public JoinEvent(Parkour plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        //Player hideris

        p.getInventory().clear();

        ItemStack item = new ItemStack(Material.WATCH, 1);

        ItemMeta m = item.getItemMeta();

        m.setDisplayName(ChatColor.DARK_AQUA + "Player hider");
        m.setLore(Collections.singletonList(ChatColor.GRAY + "Right click to toggle other player visibility!"));
        item.setItemMeta(m);

        if (!p.hasPermission("parkour.staff")) {
            p.getInventory().setItem(6, item);
        }
        //TODO: Pabeigt grāmatas lapas.

        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = book.getItemMeta();

        bookMeta.setDisplayName(ChatColor.GOLD + "Information");
        bookMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Right click to read information about the event!"));

        book.setItemMeta(bookMeta);

        if (!p.hasPermission("parkour.staff")) {
            p.getInventory().setItem(2, book); //2
        }

        if (!plugin.hide.containsKey(p.getName())) plugin.hide.put(p.getName(), 0);

        for (Player pll : Bukkit.getOnlinePlayers()) {
            if (plugin.hide.containsKey(pll.getName())) {
                if (plugin.hide.get(pll.getName()) == 1) {
                    for (Player p2 : Bukkit.getOnlinePlayers()) {
                        pll.hidePlayer(p2);
                    }
                }
            }
        }
    }
}
