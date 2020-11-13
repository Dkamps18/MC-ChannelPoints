package net.Dkamps18.ChannelPoints.Handler;

import com.google.gson.JsonObject;
import net.Dkamps18.ChannelPoints.Input.DescriptionConversation;
import net.Dkamps18.ChannelPoints.Input.NameConversation;
import net.Dkamps18.ChannelPoints.Types.ChannelPointsReward;
import net.Dkamps18.ChannelPoints.main;
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

import java.sql.ResultSet;
import java.util.*;

public class MenuHandler implements Listener {

	private main plugin;
	private Map<UUID, ChannelPointsReward> editingreward = new HashMap<>();
	private ConversationFactory cf;

	public MenuHandler(main pl) {
		this.plugin = pl;
		this.cf = new ConversationFactory(this.plugin);
	}

	public void openmenu(Player p) {
		if (this.editingreward.containsKey(p.getUniqueId())) {
			if (this.editingreward.get(p.getUniqueId()).id == null) {
				this.opennewrewardmenu(p);
			} else {
				// todo kldjfa
			}
		} else {
			this.plugin.menu.openmainmenu(p);
		}
	}

	private void openmainmenu(Player p) {
		Inventory menu = Bukkit.createInventory(null, 36, "Channel Points Rewards");

		List<ItemStack> itms = new ArrayList<>();
		int pos = 0;
		ResultSet res = this.plugin.dbu.getrewards(p.getUniqueId().toString());
		try {
			while (res.next()) {
				try {
					ItemStack itm = new ItemStack(this.TypeItem(res.getString("type")));
					ItemMeta itmm = itm.getItemMeta();
					itmm.setDisplayName(ChatColor.DARK_AQUA + res.getString("name"));
					List<String> itml = new ArrayList<>();
					itml.add(ChatColor.AQUA + "Price: " + ChatColor.GRAY + res.getString("price"));
					if (res.getString("description") != null) {
						itml.add(ChatColor.AQUA + "Description: " + ChatColor.GRAY + res.getString("description"));
					}
					itml.add(ChatColor.YELLOW + "Sadly due to Java not being able to send a PATCH");
					itml.add(ChatColor.YELLOW + "request its currently not possible to edit a reward");
					itmm.setLore(itml);
					itm.setItemMeta(itmm);
					itms.add(itm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ItemStack cni = new ItemStack(Material.DIAMOND);
		ItemMeta cnim = cni.getItemMeta();
		cnim.setDisplayName(ChatColor.DARK_AQUA + "Create new reward");
		cni.setItemMeta(cnim);
		itms.add(cni);
		for (ItemStack i : itms) {
			menu.setItem(pos, i);
			pos++;
		}
		ItemStack close = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta closem = close.getItemMeta();
		closem.setDisplayName(ChatColor.YELLOW + "Close");
		close.setItemMeta(closem);
		menu.setItem(35, close);
		p.openInventory(menu);
	}

	private void opennewrewardmenu(Player p) {
		Inventory menu = Bukkit.createInventory(null, 45, "Create new reward");

		ChannelPointsReward data;

		if (this.editingreward.containsKey(p.getUniqueId())) {
			data = this.editingreward.get(p.getUniqueId());
		} else {
			data = new ChannelPointsReward();
		}
		this.editingreward.put(p.getUniqueId(), data);
		ItemStack name = new ItemStack(Material.NAME_TAG);
		ItemMeta namem = name.getItemMeta();
		namem.setDisplayName(ChatColor.DARK_AQUA + "Name");
		List<String> namel = new ArrayList<>();
		if (data.name == null) {
			namel.add(ChatColor.GRAY + "Name not configured");
		} else {
			namel.add(ChatColor.GRAY + data.name);
		}
		namem.setLore(namel);
		name.setItemMeta(namem);
		menu.setItem(11, name);

		ItemStack price = new ItemStack(Material.GOLD_INGOT);
		ItemMeta pricem = price.getItemMeta();
		pricem.setDisplayName(ChatColor.DARK_AQUA + "Price");
		List<String> pricel = new ArrayList<>();
		pricel.add(ChatColor.GRAY + data.price.toString());
		pricem.setLore(pricel);
		price.setItemMeta(pricem);
		menu.setItem(13, price);

		ItemStack desc = new ItemStack(Material.BOOK);
		ItemMeta descm = desc.getItemMeta();
		descm.setDisplayName(ChatColor.DARK_AQUA + "Description");
		List<String> descl = new ArrayList<>();
		if (data.description == null) {
			descl.add(ChatColor.GRAY + "Description not configured");
		} else {
			descl.add(ChatColor.GRAY + data.description);
		}
		descm.setLore(descl);
		desc.setItemMeta(descm);
		menu.setItem(15, desc);

		ItemStack type = new ItemStack(Material.EMERALD);
		ItemMeta typem = type.getItemMeta();
		typem.setDisplayName(ChatColor.DARK_AQUA + "Type");
		List<String> typel = new ArrayList<>();
		if (data.type == null) {
			typel.add(ChatColor.GRAY + "Type not configured");
		} else {
			typel.add(ChatColor.GRAY + data.type);
		}
		typem.setLore(typel);
		type.setItemMeta(typem);
		menu.setItem(21, type);

		ItemStack cldwn = new ItemStack(Material.BEDROCK);
		ItemMeta cldwnm = cldwn.getItemMeta();
		cldwnm.setDisplayName(ChatColor.DARK_AQUA + "COMMING SOON");
		List<String> cldwnl = new ArrayList<>();
//		if (data.type == null) {
//			cldwnl.add(ChatColor.GRAY + "");
//		} else {
//			cldwnl.add(ChatColor.GRAY + data.type);
//		}
		cldwnm.setLore(cldwnl);
		cldwn.setItemMeta(cldwnm);
		menu.setItem(23, cldwn);

		ItemStack confirm;
		ItemMeta confirmm;
		if (data.name == null || data.type == null) {
			confirm = new ItemStack(Material.RED_STAINED_GLASS_PANE);
			confirmm = confirm.getItemMeta();
			confirmm.setDisplayName(ChatColor.RED + "Please configure the name and type");
			confirm.setItemMeta(confirmm);
		} else {
			confirm = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
			confirmm = confirm.getItemMeta();
			confirmm.setDisplayName(ChatColor.DARK_GREEN + "Confirm");
			confirm.setItemMeta(confirmm);
		}
		menu.setItem(44, confirm);

		p.openInventory(menu);
	}

	private void openpricemenu(Player p, Integer a) {
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
		itemMeta.setDisplayName(ChatColor.GOLD + String.valueOf(a));
		item.setItemMeta(itemMeta);

		menu.setItem(10, minus100);
		menu.setItem(11, minus10);
		menu.setItem(12, minus1);
		menu.setItem(13, item);
		menu.setItem(14, plus1);
		menu.setItem(15, plus10);
		menu.setItem(16, plus100);


		ItemStack confirm = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		ItemMeta confirmm = confirm.getItemMeta();
		confirmm.setDisplayName(ChatColor.GREEN + "Confirm");
		confirm.setItemMeta(confirmm);
		menu.setItem(35, confirm);

		p.openInventory(menu);
	}

	private void opentypemenu(Player p) {
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
						this.opennewrewardmenu(p);
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
						this.openpricemenu(p, this.editingreward.get(p.getUniqueId()).price);
						return;
					}
					case BOOK: {
						this.cf.withFirstPrompt(new DescriptionConversation(this.plugin)).withLocalEcho(false).buildConversation(p).begin();
						p.closeInventory();
						return;
					}
					case EMERALD: {
						this.opentypemenu(p);
						return;
					}
					case RED_STAINED_GLASS_PANE: {
						p.closeInventory();
					}
					case LIME_STAINED_GLASS_PANE: {
						ResultSet res = this.plugin.dbu.get_user_data(p.getUniqueId().toString());
						try {
							if (res.next()) {
								ChannelPointsReward data = this.editingreward.get(p.getUniqueId());
								String ard = this.plugin.tapi.createreward(res.getString("twitchid"), res.getString("authtoken"), data.name, data.price, data.description);
								JsonObject ad = null;
								if (ard.contains("\"status\":")) {
									switch (this.plugin.parser.parse(ard).getAsJsonObject().get("status").getAsInt()) {
										case 401: {
											p.sendMessage(ChatColor.DARK_RED + "Authentication failed, please update your token using /tcp auth");
											break;
										}
										default: {
											p.sendMessage(ChatColor.DARK_RED + "Reward creation failed please tell the server owner to look in the console");
											System.out.println(ard);
											break;
										}
									}
								} else {
									ad = this.plugin.parser.parse(ard).getAsJsonObject().getAsJsonArray("data").get(0).getAsJsonObject();
								}
								Boolean b = this.plugin.dbu.savereward(ad.get("id").getAsString(), p.getUniqueId().toString(), data.type, data.name, data.price, data.description);
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
				ItemStack itm = e.getClickedInventory().getItem(13);
				ItemMeta itmm = itm.getItemMeta();
				Integer i = Integer.parseInt(ChatColor.stripColor(itmm.getDisplayName()));
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
						itmm.setDisplayName(ChatColor.GOLD + i.toString());
						itm.setItemMeta(itmm);
						return;
					}
					case LIME_STAINED_GLASS_PANE: {
						ChannelPointsReward data = this.editingreward.get(p.getUniqueId());
						data.price = i;
						this.editingreward.put(p.getUniqueId(), data);
						if (data.id == null) {
							this.opennewrewardmenu(p);
						} else {
							// TODO open edit menu
						}
						return;
					}
				}
			}
			case "Reward type": {
				e.setCancelled(true);
				switch (e.getCurrentItem().getType()) {
					case CHORUS_FRUIT: {
						ChannelPointsReward data = this.editingreward.get(p.getUniqueId());
						data.type = "Fly";
						this.editingreward.put(p.getUniqueId(), data);
						if (data.id == null) {
							this.opennewrewardmenu(p);
						} else {
							// TODO open edit menu
						}
						return;
					}
					default: {
						return;
					}
				}
			}
			default: {
				return;
			}
		}
	}


	public void nameinput(Player p, String name) {
		ChannelPointsReward data = this.editingreward.get(p.getUniqueId());
		if (name.length() > 40) {
			data.name = name.substring(0, 39);
		} else {
			data.name = name;
		}
		this.editingreward.put(p.getUniqueId(), data);
		if (data.id == null) {
			this.opennewrewardmenu(p);
		} else {
			// TODO open edit menu
		}
	}

	public void descriptioninput(Player p, String desc) {
		ChannelPointsReward data = this.editingreward.get(p.getUniqueId());
		if (desc.length() > 200) {
			data.description = desc.substring(0, 199);
		} else {
			data.description = desc;
		}
		this.editingreward.put(p.getUniqueId(), data);
		if (data.id == null) {
			this.opennewrewardmenu(p);
		} else {
			// TODO open edit menu
		}
	}

	private Material TypeItem(String i) {
		switch (i.toLowerCase()) {
			case "fly": {
				return Material.CHORUS_FRUIT;
			}
			default: {
				return Material.BEDROCK;
			}
		}
	}
}
