package net.Dkamps18.ChannelPoints;

import net.Dkamps18.ChannelPoints.Handler.DependencyHandler;
import net.Dkamps18.ChannelPoints.Handler.PubSubHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener {

	public void onEnable() {
		new DependencyHandler(this);
		new PubSubHandler(this);
		System.out.print("[Dkamps18-ChannelPoints] Loaded plugin or something like that");
	}
}
