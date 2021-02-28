package lv.cmnts.Parkour.events;

import lv.cmnts.Parkour.Parkour;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerEvent implements Listener {
    public Parkour plugin;

    public HungerEvent(final Parkour plugin) { this.plugin = plugin; }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();

        p.setFoodLevel(20);
        e.setCancelled(true);
    }
}
