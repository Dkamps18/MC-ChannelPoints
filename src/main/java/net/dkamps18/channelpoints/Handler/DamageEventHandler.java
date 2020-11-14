package net.dkamps18.channelpoints.Handler;

import net.dkamps18.channelpoints.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageEventHandler implements Listener {

	private Main plugin;

	public DamageEventHandler(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onDamageEvent(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
				if (this.plugin.disablefall.contains(e.getEntity().getUniqueId())) {
					e.setCancelled(true);
					this.plugin.disablefall.remove(e.getEntity().getUniqueId());
				}
			}
		}
	}
}
