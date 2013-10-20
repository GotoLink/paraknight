package core;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class SoundHandler {
	public final static String FOLDER = "paraknight:";

	@ForgeSubscribe
	public void onSound(SoundLoadEvent event) {
		event.manager.addSound(FOLDER + "clank.ogg");
		event.manager.addSound(FOLDER + "ignition.ogg");
		event.manager.addSound(FOLDER + "lawnmower.ogg");
		event.manager.addSound(FOLDER + "purr.ogg");
		event.manager.addSound(FOLDER + "steam.ogg");
	}
}
