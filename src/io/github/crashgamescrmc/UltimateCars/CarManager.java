package io.github.crashgamescrmc.UltimateCars;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CarManager {

	public static void addCar(OfflinePlayer owner, Location location, double maxSpeed) {

		location.add(0, 1, 0);

		Minecart minecart = location.getWorld().spawn(location, Minecart.class);

		Car.setTags(minecart, owner, maxSpeed);

	}

	public static void stashCar(Minecart minecart) {

		Player player = Bukkit.getPlayer(Car.getOwnerUUID(minecart));

		ItemStack car = new ItemStack(Material.MINECART);
		ItemMeta meta = car.getItemMeta();
		List<String> lore = new ArrayList<String>();

		lore.add("Vehicle: Car");
		lore.add("owner: " + player.getUniqueId());
		lore.add("speed: " + Car.getMaxSpeed(minecart));

		meta.setLore(lore);
		car.setItemMeta(meta);

		player.getInventory().addItem(car);

		minecart.remove();
	}

	public static void buyCar(Player player, double price, double maxSpeed) {

		ItemStack car = new ItemStack(Material.MINECART);
		ItemMeta meta = car.getItemMeta();
		List<String> lore = new ArrayList<String>();

		lore.add("Vehicle: Car");
		lore.add("owner: " + player.getUniqueId());
		lore.add("speed: " + maxSpeed);

		meta.setLore(lore);
		car.setItemMeta(meta);

		player.getInventory().addItem(car);

		UltimateCars.economy.withdrawPlayer(player, price);
	}

}
