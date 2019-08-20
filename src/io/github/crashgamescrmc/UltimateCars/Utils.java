package io.github.crashgamescrmc.UltimateCars;

import org.bukkit.command.CommandSender;

public class Utils {

	public static boolean hasPermission(CommandSender sender, String[] permissions) {
		for (String perm : permissions) {
			if (sender.hasPermission(perm)) {
				return true;
			}
		}

		permissionDenied(sender, permissions);

		return false;
	}

	public static void permissionDenied(CommandSender sender, String[] permissions) {
		sender.sendMessage(Values.prefix + Values.error_permission_denied);
		for (String perm : permissions) {
			sender.sendMessage(Values.prefix + " - §e" + perm);
		}
	}

	public static boolean isSmaller(String version, long build, String ref, long ref_build) {

		String[] versionArray = version.split(".");
		String[] refArray = ref.split(".");

		for (int i = 0; i < versionArray.length; i++) {
			if (Integer.parseInt(versionArray[i]) > Integer.parseInt(refArray[i])) {
				return false;
			} else if (Integer.parseInt(versionArray[i]) < Integer.parseInt(refArray[i])) {
				return true;
			}
		}
		return false;
	}

}
