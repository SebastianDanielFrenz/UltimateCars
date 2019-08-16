package io.github.crashgamescrmc.UltimateCars;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

public class CarManager {

	@SuppressWarnings("unchecked")
	public static void addCar(OfflinePlayer owner, Location location) {
		JSONObject car = new JSONObject();

		car.put("owner", owner.getUniqueId().toString());

		location.add(0, 1, 0);

		Minecart minecart = location.getWorld().spawn(location, Minecart.class);

		getCars().put(minecart.getEntityId(), car);

	}

	public static void removeCar(int entityID) {
		getCars().remove(entityID);
	}

	public static JSONObject getCar(int entityID) {
		return (JSONObject) getCars().get(entityID);
	}

	public static JSONObject getCars() {
		return (JSONObject) UltimateCars.vehicles.get("cars");
	}

	/**
	 * puts the car in the owner's inventory
	 * 
	 * @param entityID
	 */
	public static void stashCar(int entityID) {

		String rawUUID = (String) getCar(entityID).get("owner");
		Player player = Bukkit.getPlayer(UUID.fromString(rawUUID));

		ItemStack car = new ItemStack(Material.MINECART);
		ItemMeta meta = car.getItemMeta();
		List<String> lore = new ArrayList<String>();

		lore.add("Vehicle: Car");
		lore.add("owner: " + rawUUID);

		meta.setLore(lore);
		car.setItemMeta(meta);

		player.getInventory().addItem(car);

		removeCar(entityID);
	}

	/**
	 * removes the car and puts it in the owner's inventory
	 * 
	 * @param minecart
	 */
	public static void stashCar(Minecart minecart) {
		stashCar(minecart.getEntityId());

		minecart.remove();
	}

	public static void buyCar(Player player) {

		ItemStack car = new ItemStack(Material.MINECART);
		ItemMeta meta = car.getItemMeta();
		List<String> lore = new ArrayList<String>();

		lore.add("Vehicle: Car");
		lore.add("owner: " + player.getUniqueId());

		meta.setLore(lore);
		car.setItemMeta(meta);

		player.getInventory().addItem(car);

		UltimateCars.economy.withdrawPlayer(player, 100);
	}

}
