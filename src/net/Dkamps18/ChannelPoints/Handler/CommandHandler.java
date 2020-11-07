package net.Dkamps18.ChannelPoints.Handler;

import net.Dkamps18.ChannelPoints.main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandHandler {

	private main plugin;

	public CommandHandler(main pl) {
		this.plugin = pl;
	}

	public boolean run(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("Dkamps18.ChannelPoints.Use")) {
			sender.sendMessage(ChatColor.RED + "No permissions");
		}
		if(args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Not enough arguments provided");
		}
		switch (args[0].toLowerCase()) {
			case "connect": {

			}
			case "disconnect": {
			}
			default: {
				return true;
			}
		}
	}
}
