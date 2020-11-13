package net.Dkamps18.ChannelPoints.Input;

import net.Dkamps18.ChannelPoints.main;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class NameConversation extends StringPrompt {

	private main plugin;

	public NameConversation(main pl) {
		this.plugin = pl;
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
