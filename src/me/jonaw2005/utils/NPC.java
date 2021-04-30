package me.jonaw2005.utils;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.minecraft.server.v1_16_R3.WorldServer;

public class NPC {
	
	public static List<EntityPlayer> npcs = new ArrayList<EntityPlayer>();
	
	public static void createNPC(Player player, String name) {
		Location loc = player.getLocation();
		
		
		// gathering all required data
		
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer world = ((CraftWorld) Bukkit.getServer().getWorld(player.getWorld().getName())).getHandle();
		
		GameProfile profile = new GameProfile(UUID.randomUUID(), name);
		
		// setting up the game profile
		
		String[] content = getSkin(player, name);
		profile.getProperties().put("textures", new Property("textures", content[0], content[1]));
		
		// creating the EntityPlayer --> the NPC
		
		EntityPlayer npc = new EntityPlayer(server, world, profile, new PlayerInteractManager(world));
		
		// setting the NPC's Location
		
		npc.setLocation(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
				player.getLocation().getYaw(), player.getLocation().getPitch());
		
		// adding the npc to the world by sending packets
		
		addNPCPacket(npc);
		
		npcs.add(npc);
		
	}
	
	
	private static String[] getSkin(Player player, String name) {
		
		try {
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+name);
			InputStreamReader ir = new InputStreamReader(url.openStream());
			String uuid = new JsonParser().parse(ir).getAsJsonObject().get("id").getAsString();
			
			URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid+"?unsigned=false");
			InputStreamReader ir2 = new InputStreamReader(url2.openStream());
			JsonObject property = new JsonParser().parse(ir2).getAsJsonObject()
					.get("properties").getAsJsonArray().get(0).getAsJsonObject();
			
			String texture = property.get("value").getAsString();
			String signature = property.get("signature").getAsString();
		
			return new String[] {texture,signature};
			
		}catch(Exception e) {
			
			EntityPlayer p = ((CraftPlayer) player).getHandle();
			GameProfile gp = p.getProfile();
			Property property = gp.getProperties().get("texture").iterator().next();
			String texture = property.getValue();
			String signature = property.getSignature();
			
			return new String[] {texture, signature};
			
			
		}
		
		
	}
	
	public static void addNPCPacket(EntityPlayer npc) {
		for(Player player : Bukkit.getOnlinePlayers()){
			PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
			connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
			connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
			connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
		}
	}
	
	public static void addJoinPacket(Player player) {
		for(EntityPlayer npc : npcs){
			PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
			connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
			connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
			connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
		}
	}
	
	public static void removeNPCPacket(EntityPlayer npc) {
		for(Player player : Bukkit.getOnlinePlayers()){
			PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
			
			connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
			connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
			
			
		}
	}
	
	public static void removeAllNpcs() {
		npcs.forEach(npc ->{
			removeNPCPacket(npc);
		});
		
		npcs.clear();
	}
	
	
	
	
	

}
