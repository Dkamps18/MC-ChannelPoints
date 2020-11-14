package net.dkamps18.channelpoints.Handler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.sql.ResultSet;
import net.dkamps18.channelpoints.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler {

	private Main plugin;
	private TextComponent authmsg;

	public CommandHandler(Main pl) {
		this.plugin = pl;
		this.authmsg = new TextComponent(ChatColor.DARK_AQUA + "Please ");
		TextComponent subComponent = new TextComponent("click here");
		subComponent.setColor(net.md_5.bungee.api.ChatColor.AQUA);
		subComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://channelpoints.minecraft.dkamps18.net"));
		this.authmsg.addExtra(subComponent);
		this.authmsg.addExtra(ChatColor.DARK_AQUA + " to get started!");
	}

	public boolean run(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("dkamps.channelpoints.use")) {
			sender.sendMessage(ChatColor.RED + "No permissions");
		}
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Not enough arguments provided");
		}
		Player p = (Player) sender;
		switch (args[0].toLowerCase()) {
			case "connect": {
				ResultSet res = this.plugin.databaseUtil.getUserData(p.getUniqueId().toString());
				try {
					if (res.next()) {
						this.plugin.pubSubHandler.addStream(p, res.getString("twitchid"), res.getString("authtoken"));
					} else {
						p.sendMessage(ChatColor.RED + "No authentication details found");
						p.sendMessage(this.authmsg);
					}
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			case "configure": {
				this.plugin.menuHandler.openMenu(p);
				return true;
			}
			case "disconnect": {
				this.plugin.pubSubHandler.recieve.remove(p.getUniqueId());
				return true;
			}
			case "auth": {
				if (args.length == 2) {
					String res = this.plugin.twitchApi.validateAuth(args[1]);
					JsonObject data = this.plugin.parser.parse(res).getAsJsonObject();
					if (data.has("status")) {
						if (data.get("status").getAsInt() == 401) {
							sender.sendMessage(ChatColor.RED + "Authentication failure: Invalid access token");
						} else {
							sender.sendMessage(ChatColor.RED + "Twitch is having a hard time trying to validate the provided token. Please try again later");
						}
					} else {
						JsonArray scopes = data.get("scopes").getAsJsonArray();
						if (scopes.contains(new JsonPrimitive("channel:manage:redemptions")) && scopes.contains(new JsonPrimitive("channel:read:redemptions"))) {
							if (this.plugin.databaseUtil.addUserData(p.getUniqueId().toString(), data.get("user_id").getAsString(), args[1])) {
								p.sendMessage(ChatColor.DARK_GREEN + "Successfully authenticated with " + ChatColor.DARK_PURPLE + "Twitch");
							} else {
								p.sendMessage(ChatColor.DARK_RED + "Something went wrong, please try again later.");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "Authentication failure: Required scopes not grated");
						}
					}
				} else {
					p.sendMessage(this.authmsg);
				}
				return true;
			}
			default: {
				return true;
			}
		}
	}
}
