package mrriegel.dingding;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

	public static Configuration config;

	public static boolean blinkingText,inChat;
	public static int textDuration;
	public static float volume;

	public static void refreshConfig(File file) {
		config = new Configuration(file);
		config.load();

		blinkingText = config.getBoolean("blinkingText", Configuration.CATEGORY_CLIENT, true, "If true text is blinking.");
		textDuration = config.getInt("textDuration", Configuration.CATEGORY_CLIENT, 5, 1, 100, "Duration of text (in seconds)");
		volume = config.getFloat("volume", Configuration.CATEGORY_CLIENT, 0.8f, 0.0f, 1.0f, "Volume of Sound");
		inChat = config.getBoolean("inChat", Configuration.CATEGORY_CLIENT, false, "If true text will appear in chat too.");

		if (config.hasChanged()) {
			config.save();
		}
	}

}
