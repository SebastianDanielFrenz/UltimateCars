package io.github.crashgamescrmc.UltimateCars;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Car {

	public static void setTags(Minecart minecart, UUID owner) {
		minecart.addScoreboardTag("UltimateCars_Vehicle: Car");
		minecart.addScoreboardTag("UltimateCars_Owner: " + owner.toString());
		minecart.addScoreboardTag("UltimateCars_Speed: 0.0");
	}

	public static void setTags(Minecart minecart, OfflinePlayer owner) {
		setTags(minecart, owner.getUniqueId());
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

	public static double getSpeed(Minecart minecart) {
		for (String tag : minecart.getScoreboardTags()) {
			if (tag.startsWith("UltimateCars_Speed: ")) {
				return Double.parseDouble(tag.substring(20));
			}
		}

		return Double.NaN;
	}

	public static void setSpeed(Minecart minecart, double speed) {
		for (String tag : minecart.getScoreboardTags()) {
			if (tag.startsWith("UltimateCars_Speed: ")) {
				minecart.removeScoreboardTag(tag);
				break;
			}
		}

		minecart.addScoreboardTag("UltimateCars_Speed: " + String.valueOf(speed));
	}

}
