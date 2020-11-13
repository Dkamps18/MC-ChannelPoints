package net.Dkamps18.ChannelPoints;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Config {

	private JsonObject config;

	public Config(main pl) {
		File cf = new File(pl.getDataFolder() + "/config.json");
		if (cf.exists()) {
			try (FileReader file = new FileReader(pl.getDataFolder() + "/config.json")) {
				this.config = pl.parser.parse(file).getAsJsonObject();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try (FileWriter file = new FileWriter(pl.getDataFolder() + "/config.json")) {
				String t = "{\n" +
						"\t\"datastore\": \"sqlite\",\n" +
						"\t\"clientid\": \"pls change me\"\n" +
						"}";
				file.write(t);
				file.flush();
				this.config = pl.parser.parse(t).getAsJsonObject();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public JsonObject getConfig() {
		return this.config;
	}
}
