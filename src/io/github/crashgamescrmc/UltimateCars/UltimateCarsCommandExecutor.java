package io.github.crashgamescrmc.UltimateCars;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UltimateCarsCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (args.length == 0) {
			sender.sendMessage(Values.prefix + "UltimateCars developed by CrashGamesCrMc (version "
					+ UltimateCars.version + ", build " + UltimateCars.build);
			return true;
		} else {
			if (args[0].equalsIgnoreCase("buy")) {
				if (sender instanceof Player) {
					if (!UltimateCars.economy.has((Player) sender, 100)) {
						sender.sendMessage(Values.prefix + Values.error_not_enough_money);
						return true;
					}
					CarManager.buyCar((Player) sender, UltimateCars.getCarPurchaseCost(),
							UltimateCars.getCarDefaultSpeed());
					return true;
				} else {
					sender.sendMessage(Values.prefix + Values.error_not_a_player);
					return true;
				}
			} else {
				sender.sendMessage(Values.prefix + Values.error_subcommand_not_found);
				return true;
			}
		}
	}

}
