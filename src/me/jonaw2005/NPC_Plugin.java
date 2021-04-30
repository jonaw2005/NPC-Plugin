package me.jonaw2005;

import org.bukkit.plugin.java.JavaPlugin;

import me.jonaw2005.commands.Remove;
import me.jonaw2005.commands.Spawn;
import me.jonaw2005.events.JoinEvent;

public class NPC_Plugin extends JavaPlugin{
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new JoinEvent(), this);
		getCommand("spawn").setExecutor(new Spawn());
		getCommand("remove").setExecutor(new Remove());
	}
	
}
