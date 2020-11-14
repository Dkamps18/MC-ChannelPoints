package net.dkamps18.channelpoints.Util;

import java.sql.ResultSet;
import net.dkamps18.channelpoints.Main;

public class DBUtil {

	private Main plugin;

	public DBUtil(Main plugin) {
		this.plugin = plugin;
		this.init();
	}

	private void init() {
		this.plugin.manager.update("CREATE TABLE IF NOT EXISTS `tcp_user_data` (" +
				"`uuid` VARCHAR(36) NOT NULL," +
				"`twitchid` VARCHAR(11) NOT NULL," +
				"`authtoken` VARCHAR(30) NOT NULL," +
				"PRIMARY KEY (`uuid`)" +
				");"
		);
		this.plugin.manager.update("CREATE TABLE IF NOT EXISTS `tcp_rewards` (" +
				"`id` VARCHAR(36) NOT NULL," +
				"`owner` VARCHAR(36) NOT NULL," +
				"`type` VARCHAR(50) NOT NULL," +
				"`name` INT(45) NOT NULL," +
				"`price` INT(11) NOT NULL," +
				"`description` VARCHAR(200) NOT NULL," +
				"PRIMARY KEY (`id`)" +
				");"
		);
	}

	public ResultSet getRewardOwner(String id) {
		try {
			return this.plugin.manager.con.prepareStatement("SELECT `owner` FROM `tcp_rewards` WHERE `id` = '" + id + "';").executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ResultSet getRewards(String id) {
		try {
			return this.plugin.manager.con.prepareStatement("SELECT `id`, `type`, `name`, `price`, `description` FROM `tcp_rewards` WHERE `owner` = '" + id + "';").executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean addUserData(String uuid, String id, String auth) {
		return this.plugin.manager.update("REPLACE INTO `tcp_user_data` (`uuid`, `twitchid`, `authtoken`) VALUES ('" + uuid + "','" + id + "','" + auth + "');");
	}

	public ResultSet getUserData(String uuid) {
		try {
			return this.plugin.manager.con.prepareStatement("SELECT `twitchid`, `authtoken` FROM `tcp_user_data` WHERE `uuid` = '" + uuid + "';").executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean saveReward(String i, String o, String t, String n, Integer p, String d) {
		return this.plugin.manager.update("INSERT INTO `tcp_rewards` (`id`,`owner`,`type`,`name`,`price`,`description`) VALUES ('" + i + "','" + o + "','" + t + "', '" + n + "', '" + p + "', '" + d + "');");
	}

}
