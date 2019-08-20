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

	public static Vector Drag(double density, Vector speed, double coefficient, double area) {
		return speed.multiply(0.5 * density * coefficient * area).multiply(speed);
	}

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
			if (car.getItemMeta().getLore().contains("owner: " + player.getUniqueId())) {

				CarManager.addCar(player, event.getClickedBlock().getLocation());
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
	public void onVehicleMovement(VehicleMoveEvent event) {

		Minecart minecart = (Minecart) event.getVehicle();

		if (!Car.isCar(minecart)) {
			return;
		}

		Vector velocity = minecart.getVelocity();

		Vector drag_force = Drag(1.225, velocity, 1, 0.5);

		double mass = 500;

		Vector drag_acceleration = drag_force.divide(new Vector(mass, mass, mass));

		velocity.subtract(drag_acceleration);

		minecart.setVelocity(velocity);
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
	}
}
