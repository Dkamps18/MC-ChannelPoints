package net.dkamps18.channelpoints.Handler;

import com.google.gson.JsonObject;
import java.net.URI;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.dkamps18.channelpoints.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class PubSubHandler {

	private Main plugin;
	private WebSocketClient con = null;
	private List<String> openStreams = new ArrayList<>();
	public Map<UUID, String> recieve = new HashMap<>();
	public Map<String, Player> auth = new HashMap<>();

	public PubSubHandler(Main plugin) {
		this.plugin = plugin;
		try {
			this.con = new WebSocketClient(new URI("wss://pubsub-edge.twitch.tv")) {
				@Override
				public void onMessage(String message) {
					JsonObject md = PubSubHandler.this.plugin.parser.parse(message).getAsJsonObject();
					switch (md.get("type").getAsString()) {
						case "MESSAGE": {
							JsonObject d = PubSubHandler.this.plugin.parser.parse(md.get("data").getAsJsonObject().get("message").getAsString()).getAsJsonObject().getAsJsonObject("data");
							String rid = d.getAsJsonObject("redemption").getAsJsonObject("reward").get("id").getAsString();
							if (!PubSubHandler.this.recieve.containsValue(d.getAsJsonObject("redemption").get("channel_id").getAsString())) {
								return;
							}
							ResultSet res = PubSubHandler.this.plugin.databaseUtil.getRewardOwner(rid);
							if (res == null) {
								return;
							}
							try {
								if (res.next()) {
									Player p = Bukkit.getPlayer(UUID.fromString(res.getString("owner")));
									Location loc = p.getLocation();
									p.setVelocity(new Vector(loc.getDirection().getX() * 80, loc.getY() * 40, loc.getDirection().getZ() * 80));
									PubSubHandler.this.plugin.disablefall.add(p.getUniqueId());
									ResultSet dbr = PubSubHandler.this.plugin.databaseUtil.getUserData(p.getUniqueId().toString());
									try {
										if (dbr.next()) {
											// See this awesome code by Marenthyu to make the patch requests proxy through https://github.com/Dkamps18/MC-ChannelPoints/issues/1
											PubSubHandler.this.plugin.tapi.acceptreward(rid, d.getAsJsonObject("redemption").get("id").getAsString(), dbr.getString("twitchid"), dbr.getString("authtoken"));
										} else {
											System.out.println("SOMETHING WEIRD HAPPENED");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									return;
								}
							} catch (Exception e) {
								e.printStackTrace();
								return;
							}
							break;
						}
						case "RESPONSE": {
							if (md.get("error").getAsString().equals("")) {
								Player p = PubSubHandler.this.auth.get(md.get("nonce").getAsString());
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2Successfully connected to &5Twitch"));
								PubSubHandler.this.auth.remove(md.get("nonce").getAsString());
							} else {
								Player p = PubSubHandler.this.auth.get(md.get("nonce").getAsString());
								if ("ERR_BADAUTH".equals(md.get("error").getAsString())) {
									p.sendMessage(ChatColor.DARK_RED + "Invalid authentication");
								} else {
									p.sendMessage(ChatColor.DARK_RED + "A unknown error occurred");
								}
								PubSubHandler.this.auth.remove(md.get("nonce").getAsString());
								PubSubHandler.this.recieve.remove(p.getUniqueId());
							}
							break;
						}
						case "PONG": {
							break;
						}
						case "Reconnect": {
							for (Map.Entry<UUID, String> entry : PubSubHandler.this.recieve.entrySet()) {
								Bukkit.getPlayer(entry.getKey()).sendMessage(ChatColor.DARK_RED + "Sadly we've lost the connection to " + ChatColor.DARK_PURPLE + "Twitch");
							}
							break;
						}
						default: {
							System.out.println("Received unknown message");
							break;
						}
					}
				}

				@Override
				public void onOpen(ServerHandshake handshake) {
					System.out.println("Connected to pubsub");
				}

				@Override
				public void onClose(int code, String reason, boolean remote) {
					for (Map.Entry<UUID, String> entry : PubSubHandler.this.recieve.entrySet()) {
						Bukkit.getPlayer(entry.getKey()).sendMessage(ChatColor.DARK_RED + "Sadly we've lost the connection to " + ChatColor.DARK_PURPLE + "Twitch");
					}
					System.out.println("Disconnected to pubsub");
				}

				@Override
				public void onError(Exception ex) {
					ex.printStackTrace();
				}
			};
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean addStream(Player p, String id, String auth) {
		if (!this.con.isOpen()) {
			this.con.connect();
			while (!this.con.isOpen()) {
				try {
					TimeUnit.SECONDS.sleep(1l);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		String uuid = UUID.randomUUID().toString();
		if (this.openStreams.contains(id)) {
			return true;
		}
		this.auth.put(uuid, p);
		this.con.send("{\"type\":\"LISTEN\",\"nonce\":\"" + uuid + "\",\"data\":{\"topics\":[\"channel-points-channel-v1." + id + "\"],\"auth_token\":\"" + auth + "\"}}");
		this.openStreams.add(id);
		this.recieve.put(p.getUniqueId(), id);
		return true;
	}

	public void disconnect() {
		this.con.close();
	}

	public void ping() {
		if (this.con.isOpen()) {
			this.con.send("{\"type\":\"PING\"}");
		}
	}

}
