package net.dkamps18.channelpoints.handler;

import com.google.gson.JsonObject;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.dkamps18.channelpoints.Main;
import net.dkamps18.channelpoints.input.DescriptionConversation;
import net.dkamps18.channelpoints.input.NameConversation;
import net.dkamps18.channelpoints.types.ChannelPointsReward;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuHandler implements Listener {

	private Main plugin;
	private Map<UUID, ChannelPointsReward> editingRewardMap = new HashMap<>();
	private ConversationFactory cf;

	public MenuHandler(Main plugin) {
		this.plugin = plugin;
		this.cf = new ConversationFactory(this.plugin);
	}

	public void openMenu(Player p) {
		if (this.editingRewardMap.containsKey(p.getUniqueId())) {
			if (this.editingRewardMap.get(p.getUniqueId()).id == null) {
				this.openNewRewardMenu(p);
			} else {
				// TODO: ???
			}
		} else {
			this.plugin.menuHandler.openMainMenu(p);
		}
	}

	private void openMainMenu(Player p) {
		Inventory menu = Bukkit.createInventory(null, 36, "Channel Points Rewards");

		List<ItemStack> items = new ArrayList<>();
		int pos = 0;
		ResultSet res = this.plugin.databaseUtil.getRewards(p.getUniqueId().toString());
		try {
			while (res.next()) {
				try {
					ItemStack item = new ItemStack(this.TypeItem(res.getString("type")));
					ItemMeta itemMeta = item.getItemMeta();
					itemMeta.setDisplayName(ChatColor.DARK_AQUA + res.getString("name"));
					List<String> itemLore = new ArrayList<>();
					itemLore.add(ChatColor.AQUA + "Price: " + ChatColor.GRAY + res.getString("price"));
					if (res.getString("description") != null) {
						itemLore.add(ChatColor.AQUA + "Description: " + ChatColor.GRAY + res.getString("description"));
					}
					itemLore.add(ChatColor.YELLOW + "Due to technical restrictions, you cannot edit this reward.");
					itemMeta.setLore(itemLore);
					item.setItemMeta(itemMeta);
					items.add(item);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ItemStack addRewardItem = new ItemStack(Material.DIAMOND);
		ItemMeta addRewardItemItemMeta = addRewardItem.getItemMeta();
		addRewardItemItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Create new reward");
		addRewardItem.setItemMeta(addRewardItemItemMeta);
		items.add(addRewardItem);
		for (ItemStack i : items) {
			menu.setItem(pos, i);
			pos++;
		}
		ItemStack closeItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta closeItemMeta = closeItem.getItemMeta();
		closeItemMeta.setDisplayName(ChatColor.YELLOW + "Close");
		closeItem.setItemMeta(closeItemMeta);
		menu.setItem(35, closeItem);
		p.openInventory(menu);
	}

	private void openNewRewardMenu(Player p) {
		Inventory menu = Bukkit.createInventory(null, 45, "Create new reward");

		ChannelPointsReward data;

		if (this.editingRewardMap.containsKey(p.getUniqueId())) {
			data = this.editingRewardMap.get(p.getUniqueId());
		} else {
			data = new ChannelPointsReward();
		}
		this.editingRewardMap.put(p.getUniqueId(), data);
		ItemStack nameItem = new ItemStack(Material.NAME_TAG);
		ItemMeta nameItemMeta = nameItem.getItemMeta();
		nameItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Name");
		List<String> nameLore = new ArrayList<>();
		if (data.name == null) {
			nameLore.add(ChatColor.GRAY + "Name not configured");
		} else {
			nameLore.add(ChatColor.GRAY + data.name);
		}
		nameItemMeta.setLore(nameLore);
		nameItem.setItemMeta(nameItemMeta);
		menu.setItem(11, nameItem);

		ItemStack priceItem = new ItemStack(Material.GOLD_INGOT);
		ItemMeta priceItemMeta = priceItem.getItemMeta();
		priceItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Price");
		List<String> priceLore = new ArrayList<>();
		priceLore.add(ChatColor.GRAY + data.price.toString());
		priceItemMeta.setLore(priceLore);
		priceItem.setItemMeta(priceItemMeta);
		menu.setItem(13, priceItem);

		ItemStack descriptionItem = new ItemStack(Material.BOOK);
		ItemMeta descriptionItemMeta = descriptionItem.getItemMeta();
		descriptionItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Description");
		List<String> descriptionLore = new ArrayList<>();
		if (data.description == null) {
			descriptionLore.add(ChatColor.GRAY + "Description not configured");
		} else {
			descriptionLore.add(ChatColor.GRAY + data.description);
		}
		descriptionItemMeta.setLore(descriptionLore);
		descriptionItem.setItemMeta(descriptionItemMeta);
		menu.setItem(15, descriptionItem);

		ItemStack typeItem = new ItemStack(Material.EMERALD);
		ItemMeta typeItemMeta = typeItem.getItemMeta();
		typeItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Type");
		List<String> typeLore = new ArrayList<>();
		if (data.type == null) {
			typeLore.add(ChatColor.GRAY + "Type not configured");
		} else {
			typeLore.add(ChatColor.GRAY + data.type);
		}
		typeItemMeta.setLore(typeLore);
		typeItem.setItemMeta(typeItemMeta);
		menu.setItem(21, typeItem);

		ItemStack cldwn = new ItemStack(Material.BEDROCK);
		ItemMeta cldwnm = cldwn.getItemMeta();
		cldwnm.setDisplayName(ChatColor.DARK_AQUA + "COMING SOON");
		List<String> cldwnl = new ArrayList<>();
//		if (data.type == null) {
//			cldwnl.add(ChatColor.GRAY + "");
//		} else {
//			cldwnl.add(ChatColor.GRAY + data.type);
//		}
		cldwnm.setLore(cldwnl);
		cldwn.setItemMeta(cldwnm);
		menu.setItem(23, cldwn);

		ItemStack confirmItem;
		ItemMeta confirmItemMeta;
		if (data.name == null || data.type == null) {
			confirmItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
			confirmItemMeta = confirmItem.getItemMeta();
			confirmItemMeta.setDisplayName(ChatColor.RED + "Please configure the name and type");
		} else {
			confirmItem = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
			confirmItemMeta = confirmItem.getItemMeta();
			confirmItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Confirm");
		}
		confirmItem.setItemMeta(confirmItemMeta);
		menu.setItem(44, confirmItem);

		p.openInventory(menu);
	}

	private void openPriceMenu(Player p, Integer customAmount) {
		Inventory menu = Bukkit.createInventory(null, 36, "Set reward price");

		ItemStack minus100 = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta minus100Meta = minus100.getItemMeta();
		minus100Meta.setDisplayName(ChatColor.RED + "-100");
		minus100.setItemMeta(minus100Meta);

		ItemStack minus10 = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta minus10Meta = minus10.getItemMeta();
		minus10Meta.setDisplayName(ChatColor.RED + "-10");
		minus10.setItemMeta(minus10Meta);

		ItemStack minus1 = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta minus1Meta = minus1.getItemMeta();
		minus1Meta.setDisplayName(ChatColor.RED + "-1");
		minus1.setItemMeta(minus1Meta);

		ItemStack plus1 = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta plus1Meta = plus1.getItemMeta();
		plus1Meta.setDisplayName(ChatColor.GREEN + "+1");
		plus1.setItemMeta(plus1Meta);

		ItemStack plus10 = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta plus10Meta = plus10.getItemMeta();
		plus10Meta.setDisplayName(ChatColor.GREEN + "+10");
		plus10.setItemMeta(plus10Meta);

		ItemStack plus100 = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta plus100Meta = plus100.getItemMeta();
		plus100Meta.setDisplayName(ChatColor.GREEN + "+100");
		plus100.setItemMeta(plus100Meta);

		ItemStack item = new ItemStack(Material.GOLD_INGOT);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.GOLD + Integer.toString(customAmount));
		item.setItemMeta(itemMeta);

		menu.setItem(10, minus100);
		menu.setItem(11, minus10);
		menu.setItem(12, minus1);
		menu.setItem(13, item);
		menu.setItem(14, plus1);
		menu.setItem(15, plus10);
		menu.setItem(16, plus100);


		ItemStack confirmItem = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		ItemMeta confirmItemMeta = confirmItem.getItemMeta();
		confirmItemMeta.setDisplayName(ChatColor.GREEN + "Confirm");
		confirmItem.setItemMeta(confirmItemMeta);
		menu.setItem(35, confirmItem);

		p.openInventory(menu);
	}

	private void openTypeMenu(Player p) {
		Inventory menu = Bukkit.createInventory(null, 36, "Reward type");
		ItemStack tfly = new ItemStack(Material.CHORUS_FRUIT);
		ItemMeta tflym = tfly.getItemMeta();
		tflym.setDisplayName(ChatColor.DARK_AQUA + "Fly");
		tfly.setItemMeta(tflym);
		List<String> tflyl = new ArrayList<>();
		tflyl.add(ChatColor.GRAY + "Sends you flying into the air");
		tflym.setLore(tflyl);
		menu.setItem(0, tfly);
		p.openInventory(menu);
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().getType() == Material.BEDROCK) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		switch (e.getView().getTitle()) {
			case "Channel Points Rewards": {
				e.setCancelled(true);
				switch (e.getCurrentItem().getType()) {
					case DIAMOND: {
						this.openNewRewardMenu(p);
						return;
					}
					case RED_STAINED_GLASS_PANE: {
						p.closeInventory();
					}
					default: {
						return;
					}
				}
			}
			case "Create new reward": {
				e.setCancelled(true);
				switch (e.getCurrentItem().getType()) {
					case NAME_TAG: {
						this.cf.withFirstPrompt(new NameConversation(this.plugin)).withLocalEcho(false).buildConversation(p).begin();
						p.closeInventory();
						return;
					}
					case GOLD_INGOT: {
						this.openPriceMenu(p, this.editingRewardMap.get(p.getUniqueId()).price);
						return;
					}
					case BOOK: {
						this.cf.withFirstPrompt(new DescriptionConversation(this.plugin)).withLocalEcho(false).buildConversation(p).begin();
						p.closeInventory();
						return;
					}
					case EMERALD: {
						this.openTypeMenu(p);
						return;
					}
					case RED_STAINED_GLASS_PANE: {
						p.closeInventory();
					}
					case LIME_STAINED_GLASS_PANE: {
						ResultSet res = this.plugin.databaseUtil.getUserData(p.getUniqueId().toString());
						try {
							if (res.next()) {
								ChannelPointsReward data = this.editingRewardMap.get(p.getUniqueId());
								String ard = this.plugin.twitchApi.createReward(res.getString("twitchid"), res.getString("authtoken"), data.name, data.price, data.description);
								JsonObject ad = null;
								if (ard.contains("\"status\":")) {
									if (this.plugin.parser.parse(ard).getAsJsonObject().get("status").getAsInt() == 401) {
										p.sendMessage(ChatColor.DARK_RED + "Authentication failed, please update your token using /tcp auth");
									} else {
										p.sendMessage(ChatColor.DARK_RED + "Reward creation failed please tell the server owner to look in the console");
									}
								} else {
									ad = this.plugin.parser.parse(ard).getAsJsonObject().getAsJsonArray("data").get(0).getAsJsonObject();
								}
								boolean b = this.plugin.databaseUtil.saveReward(ad.get("id").getAsString(), p.getUniqueId().toString(), data.type, data.name, data.price, data.description);
								if (b) {
									p.sendMessage(ChatColor.DARK_GREEN + "Reward created");
								} else {
									p.sendMessage("Failed creating reward");
								}

							} else {
								p.sendMessage(ChatColor.RED + "No authentication details found");
							}
							p.closeInventory();
							return;
						} catch (Exception ex) {
							ex.printStackTrace();
							return;
						}
					}
					default: {
						return;
					}
				}
			}
			case "Set reward price": {
				e.setCancelled(true);
				ItemStack item = e.getClickedInventory().getItem(13);
				ItemMeta itemMeta = item.getItemMeta();
				int i = Integer.parseInt(ChatColor.stripColor(itemMeta.getDisplayName()));
				switch (e.getCurrentItem().getType()) {
					case RED_STAINED_GLASS_PANE:
					case GREEN_STAINED_GLASS_PANE: {
						switch (e.getSlot()) {
							case 10:
								i -= 100;
								break;
							case 11:
								i -= 10;
								break;
							case 12:
								i -= 1;
								break;
							case 14:
								i += 1;
								break;
							case 15:
								i += 10;
								break;
							case 16:
								i += 100;
								break;
						}
						if (i < 1) {
							i = 1;
						}
						itemMeta.setDisplayName(ChatColor.GOLD + Integer.toString(i));
						item.setItemMeta(itemMeta);
						return;
					}
					case LIME_STAINED_GLASS_PANE: {
						ChannelPointsReward data = this.editingRewardMap.get(p.getUniqueId());
						data.price = i;
						this.editingRewardMap.put(p.getUniqueId(), data);
						if (data.id == null) {
							this.openNewRewardMenu(p);
						} else {
							// TODO open edit menu
						}
						return;
					}
				}
			}
			case "Reward type": {
				e.setCancelled(true);
				if (e.getCurrentItem().getType() == Material.CHORUS_FRUIT) {
					ChannelPointsReward data = this.editingRewardMap.get(p.getUniqueId());
					data.type = "Fly";
					this.editingRewardMap.put(p.getUniqueId(), data);
					if (data.id == null) {
						this.openNewRewardMenu(p);
					} else {
						// TODO open edit menu
					}
				}
			}
		}
	}


	public void nameInput(Player p, String name) {
		ChannelPointsReward data = this.editingRewardMap.get(p.getUniqueId());
		if (name.length() > 40) {
			data.name = name.substring(0, 39);
		} else {
			data.name = name;
		}
		this.editingRewardMap.put(p.getUniqueId(), data);
		if (data.id == null) {
			this.openNewRewardMenu(p);
		} else {
			// TODO open edit menu
		}
	}

	public void descriptionInput(Player p, String desc) {
		ChannelPointsReward data = this.editingRewardMap.get(p.getUniqueId());
		if (desc.length() > 200) {
			data.description = desc.substring(0, 199);
		} else {
			data.description = desc;
		}
		this.editingRewardMap.put(p.getUniqueId(), data);
		if (data.id == null) {
			this.openNewRewardMenu(p);
		} else {
			// TODO open edit menu
		}
	}

	private Material TypeItem(String i) {
		if ("fly".equals(i.toLowerCase())) {
			return Material.CHORUS_FRUIT;
		}
		return Material.BEDROCK;
	}
}
