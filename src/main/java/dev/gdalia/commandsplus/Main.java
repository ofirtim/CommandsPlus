package dev.gdalia.commandsplus;

import java.util.*;

import dev.gdalia.commandsplus.runnables.ActionBarVanishTask;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import dev.gdalia.commandsplus.utils.Config;
import dev.gdalia.commandsplus.utils.ListenerAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * MIT License
 * Copyright (c) 2022 Commands+
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @author Gdalia, OfirTIM
 * @since 1.0-SNAPSHOT build number #2
 */

public class Main extends JavaPlugin {

	public static HashMap<UUID, Boolean> booleanHashMap = new HashMap<>();

	@Getter
	@Setter(value = AccessLevel.PRIVATE)
	private static Main instance;
		
    @Getter
    @Setter(value = AccessLevel.PRIVATE)
    private static Config
    	languageConfig, punishmentsConfig, reportsConfig;
    
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(
				" ", " ",
				Message.fixColor("&a&lEnabling CommandsPlus... hold on tight!"));

		setInstance(this);
		saveDefaultConfig();
		
		setLanguageConfig(Config.getConfig("language", null, false));
		Arrays.stream(Message.values()).forEach(message -> getLanguageConfig().addDefault(message.name(), message.getDefaultMessage()));
        getLanguageConfig().saveConfig();

		Bukkit.getConsoleSender().sendMessage(Message.fixColor("&a&lLoaded all default configurations!"));

		setPunishmentsConfig(Config.getConfig("punishments", null, false));
		setReportsConfig(Config.getConfig("reports", null, false));
		Bukkit.getConsoleSender().sendMessage(Message.fixColor("&a&lLoaded info from database!"));


		new ListenerAutoRegistration(this, false).register("dev.gdalia.commandsplus.listeners");
		if (!new CommandAutoRegistration(this, false).register("dev.gdalia.commandsplus.commands")) {
			this.getLogger().warning("Couldn't load all commands properly! please check any exceptions that pop up on console!");
			getPluginLoader().disablePlugin(this);
		} else Bukkit.getConsoleSender().sendMessage("&a&lSuccessfully loaded all commands from default sources!");
		Bukkit.getScheduler().runTaskTimer(this, new ActionBarVanishTask(), 0, 10);

		Bukkit.getConsoleSender().sendMessage(
				Message.fixColor("&7CommandsPlus has been &aEnabled&7."),
				" ",
				" ");
	}
	
	
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(Message.fixColor("&7CommandsPlus has been &cDisabled&7."));
	}
	
	public String getPluginPrefix() {
		return Message.fixColor(getConfig().getString("PREFIX"));
	}
	
	public static class MetadataValues {
		public static FixedMetadataValue godModeData(boolean state) {
			return new FixedMetadataValue(instance, state);
		}
	}
	
	public static class PlayerCollection {
		@Getter
		private static List<UUID>
			vanishPlayers = new ArrayList<>(),
			freezePlayers = new ArrayList<>(),
			staffchatPlayers = new ArrayList<>(),
			buildmodePlayers = new ArrayList<>();
	}
}
