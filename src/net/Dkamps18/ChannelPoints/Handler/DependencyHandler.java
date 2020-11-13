package net.Dkamps18.ChannelPoints.Handler;

import net.Dkamps18.ChannelPoints.main;
import org.bukkit.craftbukkit.Main;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;

public class DependencyHandler {

	private main plugin;

	public DependencyHandler(main pl) {
		this.plugin = pl;
		try {
			File lf = new File(this.plugin.getDataFolder() + "/libs");
			if (!lf.exists()) {
				lf.mkdirs();
			}
			File wsf = new File(lf + "/Java-WebSocket-1.5.1.jar");
			if (!wsf.exists()) {
				this.Download("Java-WebSocket-1.5.1", wsf);
			}
			File sf4f = new File(lf + "/slf4j-api-1.7.26.jar");
			if (!sf4f.exists()) {
				this.Download("slf4j-api-1.7.26", sf4f);
			}
			File sflf = new File(lf + "/slf4j-nop-1.7.26.jar");
			if (!sflf.exists()) {
				this.Download("slf4j-nop-1.7.26", sflf);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void Download(String lib, File loc) {
		System.out.println("[Dkamps18-ChannelPoints] Downloading dependency " + lib);
		URL url;
		URLConnection con;
		DataInputStream dis;
		FileOutputStream fos;
		byte[] fileData;
		try {
			url = new URL("https://cdn.dannyhisstuff.com/dkm18/prod/mccp/dl/" + lib + ".jar");
			con = url.openConnection();
			dis = new DataInputStream(con.getInputStream());
			fileData = new byte[con.getContentLength()];
			for (int q = 0; q < fileData.length; q++) {
				fileData[q] = dis.readByte();
			}
			dis.close(); // close the data input stream
			fos = new FileOutputStream(loc);
			fos.write(fileData);
			fos.close();
			System.out.println("[Dkamps18-ChannelPoints] Downloaded " + lib);
		} catch (Exception e) {
			System.out.println("[Dkamps18-ChannelPoints] Failed Downloading " + lib);
			System.out.println(e);
			e.printStackTrace();
		}
	}
}