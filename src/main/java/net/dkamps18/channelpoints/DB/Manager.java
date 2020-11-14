package net.dkamps18.channelpoints.DB;

import com.google.gson.JsonObject;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import net.dkamps18.channelpoints.Main;

public class Manager {

	private Main plugin;
	private File dbfile;
	public Connection con;

	public Manager(Main plugin) {
		this.plugin = plugin;
		if (plugin.config.get("datastore").getAsString().equals("sqlite")) {
			this.dbfile = new File(this.plugin.getDataFolder().getAbsolutePath() + "/database.db");
			if (!this.dbfile.exists()) {
				try {
					this.dbfile.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				this.con = DriverManager.getConnection("jdbc:sqlite:" + this.dbfile);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (plugin.config.get("datastore").getAsString().equals("mysql")) {
			JsonObject d = plugin.config.getAsJsonObject("mysql");
			try {
				this.con = DriverManager.getConnection("jdbc:mysql://" + d.get("host").getAsString() + ":" + d.get("port").getAsString() + "/" + d.get("database").getAsString() + "?autoReconnect=true&useSSL=false", d.get("user").getAsString(), d.get("password").getAsString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean update(String q) {
		try {
			this.con.prepareStatement(q).execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
