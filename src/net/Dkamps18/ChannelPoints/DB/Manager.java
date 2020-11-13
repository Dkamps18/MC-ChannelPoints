package net.Dkamps18.ChannelPoints.DB;

import com.google.gson.JsonObject;
import net.Dkamps18.ChannelPoints.main;

import java.io.File;
import java.sql.DriverManager;

public class Manager {
	private main plugin;
	private File dbfile;
	public java.sql.Connection con;

	public Manager(main pl) {
		this.plugin = pl;
		if (pl.config.get("datastore").getAsString().equals("sqlite")) {
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

		} else if (pl.config.get("datastore").getAsString().equals("mysql")) {
			JsonObject d = pl.config.getAsJsonObject("mysql");
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
