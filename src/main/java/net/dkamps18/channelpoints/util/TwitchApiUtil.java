package net.dkamps18.channelpoints.util;

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import net.dkamps18.channelpoints.Main;

public class TwitchApiUtil {

	private Main plugin;

	public TwitchApiUtil(Main plugin) {
		this.plugin = plugin;
	}

	public String validateAuth(String t) {
		Map<String, String> h = new HashMap<>();
		h.put("Authorization", "OAuth " + t);
		return this.get("https://id.twitch.tv/oauth2/validate", h);
	}

	public String createReward(String id, String a, String n, Integer p, String d) {
		JsonObject b = new JsonObject();
		b.addProperty("title", n);
		b.addProperty("cost", p);
		if (d != null) {
			b.addProperty("prompt", d);
		}
		Map<String, String> h = new HashMap<>();
		h.put("Authorization", "Bearer " + a);
		h.put("Client-ID", this.plugin.config.get("clientid").getAsString());
		h.put("Content-Type", "application/json");
		return this.post("https://api.twitch.tv/helix/channel_points/custom_rewards?broadcaster_id=" + id, h, b.toString());
	}

	public void acceptReward(String rid, String red, String id, String a) {
		Map<String, String> h = new HashMap<>();
		h.put("Authorization", "Bearer " + a);
		h.put("Client-ID", this.plugin.config.get("clientid").getAsString());
		h.put("Content-Type", "application/json");
		this.post("https://dkamps.marenthyu.de/helix/channel_points/custom_rewards/redemptions?broadcaster_id=" + id + "&reward_id=" + rid + "&id=" + red, h, "{\"status\": \"FULFILLED\"}");
	}

	private String get(String target, Map<String, String> headers) {
		String stuff = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(target);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String str = in.readLine();
			in.close();
			if (str != null) {
				stuff = str;
			}
		} catch (IOException e) {
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			try {
				String str = in.readLine();
				in.close();
				if (str != null) {
					stuff = str;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return stuff;
	}

	private String post(String target, Map<String, String> headers, String body) {
		String stuff = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(target);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}
			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = body.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder everything = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				everything.append(line);
			}
			in.close();
			stuff = everything.toString();
		} catch (IOException e) {
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			try {
				String str = in.readLine();
				in.close();
				if (str != null) {
					stuff = str;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return stuff;
	}

	private void patch(String target, Map<String, String> headers, String body) {
		HttpURLConnection connection;
		try {
			URL url = new URL(target);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}
			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = body.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder everything = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				everything.append(line);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String delete(String target, Map<String, String> headers, String body) {
		String stuff = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(target);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(false);
			connection.setRequestMethod("DELETE");
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}
			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = body.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder everything = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				everything.append(line);
			}
			in.close();
			stuff = everything.toString();
		} catch (java.io.IOException e) {
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			try {
				String str = in.readLine();
				in.close();
				if (str != null) {
					stuff = str;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return stuff;
	}
}
