package net.Dkamps18.ChannelPoints.Handler;

import net.Dkamps18.ChannelPoints.main;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EventHandler implements Listener {

	private main plugin;

	public EventHandler(main pl) {
		this.plugin = pl;
		System.out.println("hi");
	}

	@org.bukkit.event.EventHandler
	public void onDamageEvent(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
				if (this.plugin.disablefall.contains(e.getEntity().getUniqueId())) {
					e.setCancelled(true);
					this.plugin.disablefall.remove(e.getEntity().getUniqueId());
				}
			} else {
				return;
			}
		} else {
			return;
		}
	}
}
