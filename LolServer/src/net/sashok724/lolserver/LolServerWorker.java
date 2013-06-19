package net.sashok724.lolserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class LolServerWorker implements Runnable
{
	private LolServerCall callback;
	public int[] mc_version = null;
	public String mood = null, kick = null;
	public int port = 25565, nt_version = 61, current_players = 0, maximum_players = 0;
	private static final char dot = '.', nil = 0;
	
	public LolServerWorker(LolServerCall call)
	{
		callback = call;
	}
	
	@Override public void run()
	{
		try(ServerSocket server = new ServerSocket())
		{
			server.setPerformancePreferences(1, 2, 0);
			server.bind(new InetSocketAddress(port));
			callback.onSuccessLaunching();
			while(server.isBound())
			{
				try(Socket socket = server.accept(); DataInputStream input = new DataInputStream(socket.getInputStream()); DataOutputStream output = new DataOutputStream(socket.getOutputStream()))
				{
					String text = kick;
					socket.setSoTimeout(5000);
					callback.onPlayerConnected(socket.getRemoteSocketAddress().toString());
					if(input.read() == 0xFE && input.read() == 0x01)
					{
						text = ((char)167) + "1" + nil + nt_version + nil + mc_version[0] + dot + mc_version[1] + dot + mc_version[2] + nil + mood + nil + current_players + nil + maximum_players;
					}
					output.write(0xFF);
					output.writeShort(text.length());
					for(char c : text.toCharArray())
					{
						output.writeShort(c);
					}
				} catch(Exception e)
				{
					continue;
				}
			}
		} catch(IOException e)
		{
			callback.onErrorLaunching();
		}
	}
}