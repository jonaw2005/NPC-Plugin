package me.jonaw2005.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.jonaw2005.utils.NPC;

public class JoinEvent implements Listener{
	
	
	@EventHandler
	
	public void onPlayerJoin(PlayerJoinEvent e) {
		
		Player player = e.getPlayer();
		
		NPC.addJoinPacket(player);
		
	}

}
