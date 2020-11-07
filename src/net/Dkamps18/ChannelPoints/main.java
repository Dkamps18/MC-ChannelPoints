package net.Dkamps18.ChannelPoints;

import net.Dkamps18.ChannelPoints.Handler.CommandHandler;
import net.Dkamps18.ChannelPoints.Handler.DependencyHandler;
import net.Dkamps18.ChannelPoints.Handler.PubSubHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener {

	private CommandHandler ch = new CommandHandler(this);

	public void onEnable() {
		new DependencyHandler(this);
		new PubSubHandler(this);
		System.out.print("[Dkamps18-ChannelPoints] Loaded plugin or something like that");
	}

	@EventHandler
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return ch.run(sender, cmd, label, args);
	}
}
