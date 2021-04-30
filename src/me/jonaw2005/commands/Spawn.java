package me.jonaw2005.commands;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jonaw2005.utils.NPC;
import net.md_5.bungee.api.ChatColor;

public class Spawn implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			Bukkit.getLogger().log(Level.WARNING, "You have to be a Player to use this Command!");
			return false;
		}else {
			
			Player player = (Player) sender;
			Location loc = player.getLocation();
			
			// creating the npc
			
			if(args.length == 1) {
				NPC.createNPC(player, args[0]);
				return true;
			}else {
				player.sendMessage(ChatColor.RED+"Usage: /spawn NAME");
				return true;
			}
			
		}
		
	}
	
	
	

}
