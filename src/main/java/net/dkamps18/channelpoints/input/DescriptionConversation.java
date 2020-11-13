package net.dkamps18.channelpoints.input;

import net.dkamps18.channelpoints.Main;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class DescriptionConversation extends StringPrompt {

	private Main plugin;

	public DescriptionConversation(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public Prompt acceptInput(ConversationContext con, String answer) {
		this.plugin.menu.descriptioninput((Player) con.getForWhom(), answer);
		return null;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		return ChatColor.DARK_AQUA + "Please enter the description for the reward in chat up to " + ChatColor.AQUA + "200 " + ChatColor.DARK_AQUA + "characters";
	}
}