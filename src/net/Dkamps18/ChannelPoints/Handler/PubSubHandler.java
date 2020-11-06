package net.Dkamps18.ChannelPoints.Handler;

import net.Dkamps18.ChannelPoints.main;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class PubSubHandler {

	private main plugin;
	private WebSocketClient con = null;

	public PubSubHandler(main pl) {
		this.plugin = pl;
		try {
			this.con = new WebSocketClient(new URI("wss://irc-ws.chat.twitch.tv:443")) {
				@Override
				public void onMessage(String message) {
					System.out.println(message);
				}

				@Override
				public void onOpen(ServerHandshake handshake) {
					System.out.println("Connected");
				}

				@Override
				public void onClose(int code, String reason, boolean remote) {
					System.out.println("Disconnected");
				}

				@Override
				public void onError(Exception ex) {
					ex.printStackTrace();
				}
			};
		} catch (Exception e) {
			e.printStackTrace();
		}
		con.connect();
	}
}
