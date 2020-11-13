package net.Dkamps18.ChannelPoints;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.Dkamps18.ChannelPoints.DB.Manager;
import net.Dkamps18.ChannelPoints.Handler.*;
import net.Dkamps18.ChannelPoints.Util.DBUtil;
import net.Dkamps18.ChannelPoints.Util.TwitchApiUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class main extends JavaPlugin implements Listener {

	private CommandHandler ch = new CommandHandler(this);
	public PubSubHandler PubSub;
	public Manager db;
	public DBUtil dbu;
	public MenuHandler menu;
	public JsonObject config;
	public TwitchApiUtil tapi = new TwitchApiUtil(this);
	public List<UUID> disablefall = new ArrayList<>();
	public JsonParser parser = new JsonParser();

	public void onEnable() {
		this.config = new Config(this).getConfig();
		new DependencyHandler(this);
		this.db = new Manager(this);
		this.dbu = new DBUtil(this);
		this.PubSub = new PubSubHandler(this);
		this.menu = new MenuHandler(this);
		Bukkit.getServer().getPluginManager().registerEvents(new EventHandler(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(this.menu, this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
			this.PubSub.Ping();
		}, 0l, 240 * 20l);
		System.out.println("[Dkamps18-ChannelPoints] Loaded plugin or something like that");
	}

	public void onDisable() {
		this.PubSub.Disconnect();
	}

	@org.bukkit.event.EventHandler
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return ch.run(sender, cmd, label, args);
	}
}
