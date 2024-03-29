package io.github.crashgamescrmc.UltimateCars;

import org.bukkit.Material;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CarController implements Listener {

	public CarController() {
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

		if (Car.isItemCar(car)) {

			double speed;
			if (!Car.hasMaxSpeed(car)) {
				speed = UltimateCars.getCarDefaultSpeed();
			} else {
				speed = Car.getMaxSpeed(car);
			}

			if (car.getItemMeta().getLore().contains("owner: " + player.getUniqueId())) {

				CarManager.addCar(player, event.getClickedBlock().getLocation(), speed);
				player.getInventory().setItemInMainHand(null);

				player.sendMessage(Values.prefix + Values.car_spawned);
			} else {
				player.sendMessage(Values.prefix + Values.car_does_not_belong_to_you);
			}
		} else {
			player.sendMessage(Values.prefix + Values.error_invalid_vehicle_type);
		}
	}

	@EventHandler
	public void onDestroyMinecart(VehicleDestroyEvent event) {

		if (!Car.isCar((Minecart) event.getVehicle())) {
			return;
		}

		Player owner = Car.getOwner((Minecart) event.getVehicle());

		if (event.getAttacker() == null) {

			owner.sendMessage(Values.prefix + Values.car_destroyed_by_environment);
			return;
		}

		if (event.getAttacker() instanceof Player) {

			Player player = (Player) event.getAttacker();

			if (player.getUniqueId().equals(Car.getOwnerUUID((Minecart) event.getVehicle()))) {

				player.sendMessage(Values.prefix + Values.car_destroyed_by_owner);
			} else {

				event.setCancelled(true);
				return;
			}
		}

		CarManager.stashCar((Minecart) event.getVehicle());
		event.setCancelled(true);
	}

	@EventHandler
	public void onCarMove(VehicleMoveEvent event) {

		Minecart car = (Minecart) event.getVehicle();
		if (!Car.isCar(car)) {
			return;
		}

		Player player = (Player) car.getPassengers().get(0);
		Vector direction = player.getLocation().getDirection();

		direction.setY(0);

		double total = Math.sqrt(direction.getX() * direction.getX() + direction.getZ() * direction.getZ());

		direction.setX(1 / total * direction.getX());
		direction.setZ(1 / total * direction.getZ());

		car.getLocation().setDirection(direction);

		direction.setY(car.getVelocity().getY());

		direction.setX(1 / total * direction.getX() * Car.getSpeed(car));
		direction.setZ(1 / total * direction.getZ() * Car.getSpeed(car));

		car.setVelocity(direction);
	}
}
