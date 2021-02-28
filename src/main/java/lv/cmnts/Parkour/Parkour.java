package lv.cmnts.Parkour;

import lv.cmnts.Parkour.commands.ParkourCommand;
import lv.cmnts.Parkour.events.*;
import lv.cmnts.Parkour.util.BookUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.*;

public class Parkour extends JavaPlugin {
    public static Parkour instance;

    public List<String> finished = new ArrayList<>();
    public HashMap<String, Location> checkpoint = new HashMap<>();
    public TreeMap<String, Integer> finished2 = new TreeMap<>();

    public HashMap <String, Integer> hide = new HashMap<>();

    public int parkour_winners = 0;
    public boolean parkour_started = false;
    public Instant parkour_started_time;
    public boolean autostart = false;

    //-----------------------  Configi  -----------------------------------
    public int gate_minx = getConfig().getInt("parkour.gatemin.x");
    public int gate_miny = getConfig().getInt("parkour.gatemin.y");
    public int gate_minz = getConfig().getInt("parkour.gatemin.z");
    public String gate_minworld = getConfig().getString("parkour.gatemin.world");

    public int gate_maxx = getConfig().getInt("parkour.gatemax.x");
    public int gate_maxy = getConfig().getInt("parkour.gatemax.y");
    public int gate_maxz = getConfig().getInt("parkour.gatemax.z");
    public String gate_maxworld = getConfig().getString("parkour.gatemax.world");

    public int lowestpoint = getConfig().getInt("parkour.lowestpoint");

    public String eventname = getConfig().getString("message.event-name");
    public String reachcheck = getConfig().getString("message.reach-checkpoint");
    public String reachfinish = getConfig().getString("message.reach-finish");

    public String gate_open = getConfig().getString("gateopen");
    public String gate_close = getConfig().getString("gateclose");

    public String tp_pitch = getConfig().getString("tppitch");
    public String tp_yaw = getConfig().getString("tpyaw");
    //------------------------------------------------------------------

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new HungerEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new MoveEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new JoinEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new InteractEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new DropEvent(this), this);

        this.getCommand("parkour").setExecutor(new ParkourCommand(this));
    }

    public void translate(CommandSender player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void doReplace(Location min, Location max, Material replace, Material with) {
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    Block blk = min.getWorld().getBlockAt(new Location(min.getWorld(), x, y, z));
                    if (blk.getType() == replace) {
                        blk.setType(with);
                    }
                }
            }
        }
    }

    public void hidePlayers(Player p) {
        for (Player toHide : Bukkit.getServer().getOnlinePlayers()) {
            if (p != toHide) {
                p.hidePlayer(toHide);
            }
        }
    }

    public void showPlayers(Player p) {
        for (Player toHide : Bukkit.getServer().getOnlinePlayers()) {
            if (p != toHide) {
                p.showPlayer(toHide);
            }
        }
    }

    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        Set<T> keys = new HashSet<T>();
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    public void openBook(Player player, List<String> pages) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle("");
        meta.setAuthor("");
        BookUtil.setPages(meta, pages);
        book.setItemMeta(meta);
        BookUtil.openBook(book, player);
    }

    public static Parkour getInstance() {
        return instance;
    }
}
