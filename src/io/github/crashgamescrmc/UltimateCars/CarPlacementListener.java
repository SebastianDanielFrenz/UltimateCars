package io.github.crashgamescrmc.UltimateCars;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

public class CarPlacementListener implements Listener {

	public CarPlacementListener() {
	}

	@EventHandler
	public void onPlaceMinecart(PlayerInteractEvent event) {

		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.MINECART) {
			return;
		}
		if (event.getClickedBlock().getType() == XMaterial.RAIL.parseMaterial()
				|| event.getClickedBlock().getType() == Material.POWERED_RAIL
				|| event.getClickedBlock().getType() == Material.DETECTOR_RAIL) {
			return;
		}

		Player player = event.getPlayer();

		if (!Utils.hasPermission(player, Values.PERMISSION_PLACE)) {
			return;
		}

		ItemStack car = player.getInventory().getItemInMainHand();

		if (!car.hasItemMeta()) {
			return;
		}
		if (!car.getItemMeta().hasLore()) {
			return;
		}

		if (car.getItemMeta().getLore().contains("Vehicle: Car")) {
			if (car.getItemMeta().getLore().contains("owner: " + player.getUniqueId())) {
				CarManager.addCar(player, event.getClickedBlock().getLocation());
				player.sendMessage(Values.prefix + Values.car_spawned);
			} else {
				player.sendMessage(Values.prefix + Values.car_does_not_belong_to_you);
			}
		} else {
			player.sendMessage(Values.prefix + Values.error_invalid_vehicle_type);
		}
		UltimateCars.saveVehiclesFile();
	}

	@EventHandler
	public void onDestroyMinecart(VehicleDestroyEvent event) {

		JSONObject car = CarManager.getCar(event.getVehicle().getEntityId());

		if (car == null) {
			return;
		}

		Player owner = Bukkit.getPlayer(UUID.fromString((String) car.get("owner")));

		if (event.getAttacker() == null) {

			owner.sendMessage(Values.prefix + Values.car_destroyed_by_environment);
			return;
		}

		if (event.getAttacker() instanceof Player) {

			Player player = (Player) event.getAttacker();

			if (player.getUniqueId().equals(UUID.fromString((String) car.get("owner")))) {

				player.sendMessage(Values.prefix + Values.car_destroyed_by_owner);
			} else {

				event.setCancelled(true);
				return;
			}
		}

		CarManager.stashCar(event.getVehicle().getEntityId());

		UltimateCars.saveVehiclesFile();
	}
}
