package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;

@CommandAutoRegistration.Command(value = "tphere")
public class TphereCommand implements CommandExecutor {

	/**
	 * /tphere {user}
	 * LABEL ARG0
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}

		if (!Permission.PERMISSION_TPHERE.hasPermission(sender)) {
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}

		Player player = (Player) sender;

		if (args.length == 0) {
			Message.DESCRIBE_PLAYER.sendMessage(sender, true);
			return true;
		}
		
		Player target = Bukkit.getPlayerExact(args[0]);
		if (target == null) {
			Message.INVALID_PLAYER.sendMessage(sender, true);
			return true;
		}
		
		target.teleport(player);
		Message.TPHERE.sendFormattedMessage(player, true, target.getName());
		
		return false;
	}

}
