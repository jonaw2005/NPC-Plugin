package me.jonaw2005.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.jonaw2005.utils.NPC;

public class Remove implements CommandExecutor{

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		NPC.removeAllNpcs();
		
		return true;
	}
}
