package lv.cmnts.Parkour.events;

import lv.cmnts.Parkour.Parkour;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InteractEvent implements Listener {
    public Parkour plugin;

    public HashMap<String, Boolean> player_cooldown = new HashMap<>();

    public InteractEvent(Parkour plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        switch (e.getAction()) {
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK: {
                if (e.getItem() == null) return;
                if (e.getItem().getType() == Material.WATCH) {

                    if (!this.player_cooldown.containsKey(p.getName())) {
                        this.player_cooldown.put(p.getName(), false);
                    }
                    if (!this.player_cooldown.get(p.getName())) {
                        Bukkit.getScheduler().runTaskLater(plugin, () -> player_cooldown.put(p.getName(), false), 20L);
                        if (plugin.hide.get(p.getName()) == 0) {
                            plugin.hide.remove(p.getName());
                            plugin.hide.put(p.getName(), 1);
                            plugin.hidePlayers(p);
                            plugin.translate(p, " &8» &7All players are now &cHIDDEN&7!");
                        } else {
                            plugin.hide.remove(p.getName());
                            plugin.hide.put(p.getName(), 0);
                            plugin.showPlayers(p);
                            plugin.translate(p, " &8» &7All players are now &2VISIBLE&7!");
                        }
                        this.player_cooldown.put(p.getName(), true);
                    }
                    else {
                        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', " &8» &7You have to wait &61 &7second to use this."));
                        e.setCancelled(true);
                    }
                } else if (e.getItem().getType() == Material.BOOK) {
                    List<String> pages = new ArrayList<String>();
                    TextComponent page0 = new TextComponent(ChatColor.GOLD + "   ");
                    page0.addExtra(ChatColor.BOLD + "Parkour Event");
                    page0.addExtra("\n\n");
                    page0.addExtra(ChatColor.BOLD + "1. " + ChatColor.RESET + "To save your\n" +
                                    "checkpoint, you\n" +
                                    "have to step on\n" +
                                    "the gold block!\n\n");
                    page0.addExtra(ChatColor.BOLD + "2. " + ChatColor.RESET + "To hide other\n" +
                                    "players, Right-Click\n" +
                                    "clock item in your\n" +
                                    "inventory!\n\n");
                    page0.addExtra(
                            "Event by:\n" +
                                 ChatColor.DARK_GREEN + ChatColor.BOLD + "Side Event Team!");
                    pages.add(ComponentSerializer.toString(page0));
                    plugin.openBook(e.getPlayer(), pages);
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getWhoClicked().hasPermission("parkour.staff")) {
            if (e.getCurrentItem() == null) e.setCancelled(true);
            else if (e.getCurrentItem().getType() == Material.WATCH) e.setCancelled(true);
            else if (e.getCurrentItem().getType() == Material.BOOK) e.setCancelled(true);
            else if (e.getCurrentItem().getType() == Material.AIR) e.setCancelled(true);
        }
    }
}