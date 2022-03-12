package me.gdalia.commandsplus.listeners;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.gdalia.commandsplus.Main;
import me.gdalia.commandsplus.structs.Message;
import me.gdalia.commandsplus.structs.PunishmentType;
import me.gdalia.commandsplus.structs.events.PlayerPunishEvent;

public class PlayerPunishListener implements Listener {

	@EventHandler
	public void onPunish(PlayerPunishEvent event) {
		PunishmentType type = event.getPunishment().getType();
		
		if (!List.of(PunishmentType.BAN, PunishmentType.TEMPBAN, PunishmentType.KICK).contains(type)) return;
		
		String typeName = type.name().toLowerCase();
		
		List<String> message = Main.getInstance().getConfig().getStringList(typeName + "-lang." + typeName + "-template");
		
		StringBuilder sb = new StringBuilder();
		
		message.forEach(msg -> sb.append(msg).append("\n"));
		System.out.println(sb.toString());
		event.getPlayer().kickPlayer(Message.fixColor(sb.toString())); 
	}
}