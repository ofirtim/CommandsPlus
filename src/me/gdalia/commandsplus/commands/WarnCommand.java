package me.gdalia.commandsplus.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.gdalia.commandsplus.models.PunishmentManager;
import me.gdalia.commandsplus.structs.Message;
import me.gdalia.commandsplus.structs.Punishment;
import me.gdalia.commandsplus.structs.PunishmentType;

@me.gdalia.commandsplus.utils.CommandAutoRegistration.Command(value = "warn")
public class WarnCommand implements CommandExecutor{

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}
		
		if (!sender.hasPermission("commandsplus.history")) {
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}
		
		if (args.length <= 1) {
			Message.WARN_ARGUMENTS.sendMessage(sender, true);
			return true;
		}
		
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if (!target.hasPlayedBefore()) {
			Message.INVALID_PLAYER.sendMessage(sender, true);
			return true;
		}
		
		UUID executer = sender instanceof Player requester ? requester.getUniqueId() : null;
		Punishment punishment = new Punishment(UUID.randomUUID(), target.getUniqueId(), executer, PunishmentType.WARN, "");
		
		PunishmentManager.getInstance().invoke(punishment);
		Message.PLAYER_WARN_MESSAGE.sendMessage(sender, true);
		return true;
	}
}
