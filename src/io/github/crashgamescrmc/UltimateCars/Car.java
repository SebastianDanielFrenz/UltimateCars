package io.github.crashgamescrmc.UltimateCars;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Car {

	public static void setTags(Minecart minecart, UUID owner, double maxSpeed) {
		minecart.addScoreboardTag("UltimateCars_Vehicle: Car");
		minecart.addScoreboardTag("UltimateCars_Owner: " + owner.toString());
		minecart.addScoreboardTag("UltimateCars_Speed: " + 0.0);
		minecart.addScoreboardTag("UltimateCars_MaxSpeed: " + maxSpeed);
	}

	public static void setTags(Minecart minecart, OfflinePlayer owner, double maxSpeed) {
		setTags(minecart, owner.getUniqueId(), maxSpeed);
	}

	public static UUID getOwnerUUID(Minecart minecart) {
		Set<String> tags = minecart.getScoreboardTags();
		for (String tag : tags) {
			if (tag.startsWith("UltimateCars_Owner: ")) {
				return UUID.fromString(tag.substring(20));
			}
		}
		return null;
	}

	public static Player getOwner(Minecart minecart) {
		return Bukkit.getPlayer(getOwnerUUID(minecart));
	}

	public static boolean isCar(Minecart minecart) {
		Set<String> tags = minecart.getScoreboardTags();
		for (String tag : tags) {
			if (tag.equals("UltimateCars_Vehicle: Car")) {
				return true;
			}
		}
		return false;
	}

	public static boolean isItemCar(ItemStack item) {
		if (item.getItemMeta().getLore().contains("Vehicle: Car")) {
			return true;
		}
		return false;
	}

	public static double getSpeed(Minecart car) {
		String speed = getSpeedRaw(car);
		if (speed == null) {
			return Double.NaN;
		}
		return Double.parseDouble(speed);
	}

	public static String getSpeedRaw(Minecart car) {
		for (String tag : car.getScoreboardTags()) {
			if (tag.startsWith("UltimateCars_Speed: ")) {
				return tag.substring(20);
			}
		}
		return null;
	}

	public static void setSpeed(Minecart car, double speed) {
		car.removeScoreboardTag("UltimateCars_Speed: " + getSpeedRaw(car));

		car.addScoreboardTag("UltimateCars_Speed: " + speed);
	}

	public static String getMaxSpeedRaw(Minecart car) {
		for (String tag : car.getScoreboardTags()) {
			if (tag.startsWith("UltimateCars_MaxSpeed: ")) {
				return tag.substring(23);
			}
		}
		return null;
	}

	public static double getMaxSpeed(Minecart car) {
		String speed = getMaxSpeedRaw(car);
		if (speed == null) {
			return Double.NaN;
		} else {
			return Double.parseDouble(speed);
		}
	}

	public static void setMaxSpeed(Minecart car, double speed) {
		car.removeScoreboardTag("UltimateCars_MaxSpeed: " + getMaxSpeed(car));

		car.addScoreboardTag("UltimateCars_MaxSpeed: " + speed);
	}

	public static double getMaxSpeed(ItemStack car) {
		for (String line : car.getItemMeta().getLore()) {
			if (line.startsWith("speed: ")) {
				return Double.parseDouble(line.substring(7));
			}
		}
		return Double.NaN;
	}

	public static boolean hasMaxSpeed(ItemStack car) {
		return getMaxSpeed(car) != Double.NaN;
	}

}
