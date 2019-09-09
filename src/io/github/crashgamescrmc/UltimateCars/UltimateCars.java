package io.github.crashgamescrmc.UltimateCars;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Minecart;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import net.milkbowl.vault.economy.Economy;

import net.minecraft.server.v1_14_R1.PacketPlayInSteerVehicle;

public class UltimateCars extends JavaPlugin {

	public static JSONObject config = new JSONObject();
	public static UltimateCars plugin;
	public static Economy economy;
	public static ProtocolManager protocolManager;

	public static final String version = "0.0.3";
	public static final long build = 2;

	@Override
	public void onEnable() {
		plugin = this;

		// setup protocolLib

		protocolManager = ProtocolLibrary.getProtocolManager();
		protocolManager.addPacketListener(
				new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.STEER_VEHICLE) {
					// @Override
					public void onPacketReceiving(PacketEvent event) {
						if (event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE) {

							PacketPlayInSteerVehicle steering = (PacketPlayInSteerVehicle) event.getPacket()
									.getHandle();

							if (event.getPlayer().getVehicle() instanceof Minecart) {

								Minecart minecart = (Minecart) event.getPlayer().getVehicle();

								if (!Car.isCar(minecart)) {
									return;
								}

								if (minecart.getPassengers().size() == 0) {
									return;
								}

								if (steering.c() > 0 && Car.getSpeed(minecart) >= Car.getMaxSpeed(minecart)) {
									Car.setSpeed(minecart, Car.getMaxSpeed(minecart));
								} else if (steering.c() < 0 && Car.getSpeed(minecart) <= -Car.getMaxSpeed(minecart)) {
									Car.setSpeed(minecart, -Car.getMaxSpeed(minecart));
								} else {
									double acc_ticks = 20 * 5;
									Car.setSpeed(minecart, Car.getSpeed(minecart) + steering.c() / acc_ticks);
								}
							}
						}
					}
				});

		if (!setupEconomey()) {
			getLogger().severe("No economy found!");
			Bukkit.shutdown();
		}
		loadConfigFile();

		getServer().getPluginManager().registerEvents(new CarController(), this);

		getCommand("cars").setExecutor(new UltimateCarsCommandExecutor());

		getLogger().info("Enabled UltimateCars!");
	}

	public static void saveConfigFile() {
		try {
			FileWriter fw = new FileWriter("plugins/UltimateCars/config.json");
			fw.write(config.toJSONString());
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public static void loadConfigFile() {
		File dir = new File("plugins/UltimateCars");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		try {
			FileReader fw = new FileReader("plugins/UltimateCars/config.json");
			JSONParser parser = new JSONParser();
			try {
				config = (JSONObject) parser.parse(fw);
				fw.close();

				if (Utils.isSmaller((String) config.get("version"), (long) config.get("build"), version, build)) {
					JSONObject car = new JSONObject();

					car.put("default_speed", 5.0);
					car.put("upgrade_speed", 2.5);
					car.put("upgrade_base_cost", 10);
					car.put("upgrade_multiplier_cost", 1.5);
					car.put("purchase_cost", 100);
					car.put("max_speed", 100.0);

					config.put("car", car);

					plugin.getLogger().info("Adapted config for " + version + "_" + build);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			resetConfigData();
			plugin.getLogger().info("config.json not found, generating...");
			saveConfigFile();
		}

	}

	@SuppressWarnings("unchecked")
	public static void resetConfigData() {
		config.clear();

		JSONObject car = new JSONObject();

		car.put("default_speed", 5.0);
		car.put("upgrade_speed", 2.5);
		car.put("upgrade_base_cost", 10);
		car.put("upgrade_multiplier_cost", 1.5);
		car.put("purchase_cost", 100);
		car.put("max_speed", 100.0);

		config.put("car", car);

		config.put("version", version);
		config.put("build", build);
	}

	private boolean setupEconomey() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return economy != null;
	}

	public static JSONObject getCarConfig() {
		return (JSONObject) config.get("car");
	}

	public static double getCarDefaultSpeed() {
		return (double) getCarConfig().get("default_speed");
	}

	@SuppressWarnings("unchecked")
	public static void setCarDefaultSpeed(double speed) {
		getCarConfig().put("default_speed", speed);
	}

	public static double getCarUpgradeSpeed() {
		return (double) getCarConfig().get("upgrade_speed");
	}

	@SuppressWarnings("unchecked")
	public static void setCarUpgradeSpeed(double speed) {
		getCarConfig().put("upgrade_speed", speed);
	}

	public static double getCarUpgradeBaseCost() {
		return (double) getCarConfig().get("upgrade_base_cost");
	}

	@SuppressWarnings("unchecked")
	public static void setCarUpgradeBaseCost(double cost) {
		getCarConfig().put("upgrade_base_cost", cost);
	}

	public static double getCarUpgradeMultiplierCost() {
		return (double) getCarConfig().get("upgrade_multiplier_cost");
	}

	@SuppressWarnings("unchecked")
	public static void setCarUpgradeMultiplierCost(double cost) {
		getCarConfig().put("upgrade_multiplier_cost", cost);
	}

	public static double getCarPurchaseCost() {
		return (double) getCarConfig().get("purchase_cost");
	}

	@SuppressWarnings("unchecked")
	public static void setCarPurchaseCost(double cost) {
		getCarConfig().put("purchase_cost", cost);
	}

	public static double getCarMaxSpeed() {
		return (double) getCarConfig().get("max_speed");
	}

	@SuppressWarnings("unchecked")
	public static void setCarMaxSpeed(double speed) {
		getCarConfig().put("max_speed", speed);
	}

}
