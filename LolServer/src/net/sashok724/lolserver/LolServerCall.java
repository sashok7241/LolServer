package net.sashok724.lolserver;

public interface LolServerCall
{
	public void onErrorLaunching();
	public void onSuccessLaunching();
	public void onPlayerConnected(String ip);
}