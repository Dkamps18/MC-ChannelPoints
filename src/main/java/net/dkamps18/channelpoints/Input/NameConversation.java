package net.dkamps18.channelpoints.Input;

import net.dkamps18.channelpoints.Main;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class NameConversation extends StringPrompt {

	private Main plugin;

	public NameConversation(Main pl) {
		this.plugin = plugin;
	}

	@Override
	public Prompt acceptInput(ConversationContext con, String answer) {
		this.plugin.menu.nameinput((Player) con.getForWhom(), answer);
		return null;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		return ChatColor.DARK_AQUA + "Please enter the name for the reward in chat up to " + ChatColor.AQUA + "40 " + ChatColor.DARK_AQUA + "characters";
	}
}
