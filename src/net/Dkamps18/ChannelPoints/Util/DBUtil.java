package net.Dkamps18.ChannelPoints.Util;

import net.Dkamps18.ChannelPoints.main;

import java.sql.ResultSet;

public class DBUtil {

	private main plugin;

	public DBUtil(main pl) {
		this.plugin = pl;
		this.init();
	}

	private void init() {
		this.plugin.db.update("CREATE TABLE IF NOT EXISTS `tcp_user_data` (" +
				"`uuid` VARCHAR(36) NOT NULL," +
				"`twitchid` VARCHAR(11) NOT NULL," +
				"`authtoken` VARCHAR(30) NOT NULL," +
				"PRIMARY KEY (`uuid`)" +
				");"
		);
		this.plugin.db.update("CREATE TABLE IF NOT EXISTS `tcp_rewards` (" +
				"`id` VARCHAR(36) NOT NULL," +
				"`owner` VARCHAR(36) NOT NULL," +
				"`type` VARCHAR(50) NOT NULL," +
				"`name` VARCHAR(40) NOT NULL," +
				"`price` INT(11) NOT NULL," +
				"`description` VARCHAR(200) NOT NULL," +
				"PRIMARY KEY (`id`)" +
				");"
		);
	}

	public ResultSet getrewardowner(String id) {
		try {
			return this.plugin.db.con.prepareStatement("SELECT `owner` FROM `tcp_rewards` WHERE `id` = '" + id + "';").executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ResultSet getrewards(String id) {
		try {
			return this.plugin.db.con.prepareStatement("SELECT `id`, `type`, `name`, `price`, `description` FROM `tcp_rewards` WHERE `owner` = '" + id + "';").executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean add_user_data(String uuid, String id, String auth) {
		return this.plugin.db.update("REPLACE INTO `tcp_user_data` (`uuid`, `twitchid`, `authtoken`) VALUES ('" + uuid + "','" + id + "','" + auth + "');");
	}

	public ResultSet get_user_data(String uuid) {
		try {
			return this.plugin.db.con.prepareStatement("SELECT `twitchid`, `authtoken` FROM `tcp_user_data` WHERE `uuid` = '" + uuid + "';").executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean savereward(String i, String o, String t, String n, Integer p, String d) {
		return this.plugin.db.update("INSERT INTO `tcp_rewards` (`id`,`owner`,`type`,`name`,`price`,`description`) VALUES ('" + i + "','" + o + "','" + t + "', '" + n + "', '" + p + "', '" + d + "');");
	}
}
