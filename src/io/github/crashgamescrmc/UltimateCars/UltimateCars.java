package io.github.crashgamescrmc.UltimateCars;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
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
	public static final long build = 1;

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

								// assuming only players can drive cars

								Player player = (Player) minecart.getPassengers().get(0);
								Vector direction = player.getLocation().getDirection();

								direction.setY(0);
								double total = Math.sqrt(
										direction.getX() * direction.getX() + direction.getZ() * direction.getZ());

								direction.setX(1 / total * direction.getX());
								direction.setZ(1 / total * direction.getZ());

								minecart.setVelocity(direction.multiply(10 * steering.c()));
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
					car.put("speed", 10);
					config.put("car", car);
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

}
