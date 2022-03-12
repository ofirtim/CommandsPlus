package me.gdalia.commandsplus.models;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import lombok.Getter;
import me.gdalia.commandsplus.Main;
import me.gdalia.commandsplus.structs.Punishment;
import me.gdalia.commandsplus.structs.PunishmentType;
import me.gdalia.commandsplus.utils.Config;

/**
 * TODO think about better ideas to make this even more better and cooler to use.
 */
public class Punishments {

	@Getter
	private static final Punishments instance = new Punishments();
	
	@Getter
	private final HashMap<UUID, Punishment> punishments = new HashMap<>();
	
	private final Config pConfig = Main.getPunishmentsConfig();
	
	/**
	 * Finds a punishment inside the database by the following UUID.
	 * This method is working by checking if the punishment is already been saved
	 * to memory, and if it did it'll access the local map and pull them.
	 * if it doesn't, the method will access the database to check if the punishment exists,
	 * and will return the punishment if it exists inside and put it inside the local map too,
	 * if not it will return an empty optional container. 
	 * 
	 * @param uniqueId The uuid of the punishment for instance.
	 * @return An Optional container that is either empty or containing a punishment.
	 */
	public Optional<Punishment> getPunishment(UUID uuid) {
		Optional<Punishment> opt = Optional.empty();
		if (punishments.containsKey(uuid))
			opt = Optional.of(punishments.get(uuid));
		
		ConfigurationSection cs = pConfig.getConfigurationSection(uuid.toString());
		if (cs == null) return opt;
		
		Punishment punishment = new Punishment(
				uuid,
				UUID.fromString(cs.getString(ConfigFields.PunishFields.PUNISHED)),
				Optional.ofNullable(UUID.fromString(cs.getString(ConfigFields.PunishFields.EXECUTER))).orElse(null),
				PunishmentType.valueOf(cs.getString(ConfigFields.PunishFields.TYPE)),
				cs.getString(ConfigFields.PunishFields.REASON));
		
		Optional.of(cs.getLong(ConfigFields.PunishFields.EXPIRY)).ifPresent(expiry -> punishment.setExpiry(Instant.ofEpochMilli(expiry)));
		punishments.put(uuid, punishment);
		
		opt = Optional.of(punishment);
		
		return opt;
		//Gdalia was here
	    //OfirTIM was here
	}
	
	/**
	 * Gets and finds any punishment involved with this player (only as a punished user).
	 * 
	 * @param uuid The unique ID of player/user to find their punishments.
	 * @return a list of punishments that this player has/had, or empty list for instance.
	 */
	public List<Punishment> getHistory(UUID uuid) {
		return pConfig
				.getKeys(false)
				.stream()
				.map(UUID::fromString)
				.map(this::getPunishment)
				.filter(opt -> opt.isPresent())
				.map(Optional::get)
				.toList();
	}
	
	/**
	 * Checks and gets an active punishment that a user/player currently has.
	 * 
	 * @param uuid The user/player unique ID.
	 * @param type The optional types to add into the filtering.
	 * @return An optional container which could be empty or contain a punishment.
	 */	
	public Optional<Punishment> getActivePunishment(UUID uuid, PunishmentType... type) {
		return getHistory(uuid).stream()
				.filter(punishment -> Arrays.asList(type).contains(punishment.getType()))
				.filter(punishment -> punishment.getExpiry() == null || punishment.getExpiry().isAfter(Instant.now()))
				.filter(punishment -> {
					ConfigurationSection cs = pConfig.getConfigurationSection(punishment.getPunishmentUniqueId().toString());
					return !cs.contains(ConfigFields.PunishFields.OVERRIDE) && !cs.contains(ConfigFields.PunishFields.REMOVED_BY);
				}).findFirst();
	}
	
	/**
	 * makes a full deep check if the following user has a punishment of this type.
	 * @param uuid The user/player unique ID.
	 * @param type The type of punishment to be checked.
	 * @return true if the player has/had a punishment of this type.
	 */
	public boolean hasPunishment(UUID uuid, PunishmentType type) {
		return getHistory(uuid).stream()
				.filter(punishment -> punishment.getType() == type).findFirst()
				.isPresent();
	}
	
	/**
	 * Writes into the punishment new information, this is good for
	 * the punishment revoke system when adding written stuff,
	 * or when overriding an existing active punishment.
	 * 
	 * @param uuid The uniqueId of the punishment (NOT THE USER).
	 * @param key the key name to create and write into.
	 * @param value the object to insert.
	 * @param instaSave If the method should save once the key and value being written.
	 */
	public void writeTo(UUID uuid, String key, Object value, boolean instaSave) {
		ConfigurationSection cs = pConfig.getConfigurationSection(uuid.toString());
		cs.set(key, value);
		if (instaSave) pConfig.saveConfig();
	}
	
	/**
	 * same as {@link #writeTo(UUID, String, Object)}, the usage here
	 * is for shortening code calls whenever possible
	 * 
	 * @param punishment The punishment to write into, if existing.
	 * @param key the key name to create and write into.
	 * @param value the object to insert.
	 * @param instaSave If the method should save once the key and value being written.
	 */
	public void writeTo(Punishment punishment, String key, Object value, boolean instaSave) {
		writeTo(punishment.getPunishmentUniqueId(), key, value, instaSave);
	}
}