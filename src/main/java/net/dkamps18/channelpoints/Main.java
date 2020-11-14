package net.dkamps18.channelpoints;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.dkamps18.channelpoints.DB.Manager;
import net.dkamps18.channelpoints.Handler.CommandHandler;
import net.dkamps18.channelpoints.Handler.DamageEventHandler;
import net.dkamps18.channelpoints.Handler.MenuHandler;
import net.dkamps18.channelpoints.Handler.PubSubHandler;
import net.dkamps18.channelpoints.Util.DBUtil;
import net.dkamps18.channelpoints.Util.TwitchApiUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	private CommandHandler commandHandler = new CommandHandler(this);
	public PubSubHandler pubSubHandler;
	public Manager manager;
	public DBUtil databaseUtil;
	public MenuHandler menuHandler;
	public JsonObject config;
	public TwitchApiUtil twitchApi = new TwitchApiUtil(this);
	public List<UUID> disablefall = new ArrayList<>();
	public JsonParser parser = new JsonParser();

	public void onEnable() {
		this.config = new Config(this).getConfig();
		this.manager = new Manager(this);
		this.databaseUtil = new DBUtil(this);
		this.pubSubHandler = new PubSubHandler(this);
		this.menuHandler = new MenuHandler(this);
		Bukkit.getServer().getPluginManager().registerEvents(new DamageEventHandler(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(this.menuHandler, this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			this.pubSubHandler.ping();
		}, 0l, 240 * 20l);
		getLogger().info("ChannelPoints by Dkamps18 loaded successfully or something like that");
	}

	public void onDisable() {
		this.pubSubHandler.disconnect();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return commandHandler.run(sender, cmd, label, args);
	}

}
