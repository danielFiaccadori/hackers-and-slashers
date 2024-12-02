package net.dndats.hackersandslashers.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

// KEYBINDING REGISTERER
public class Keybinds {

    // Created keybindings
    public static final KeyMapping PARRY = new KeyMapping(
            "key.hackersandslashers.parry",
            InputConstants.Type.MOUSE,
            InputConstants.MOUSE_BUTTON_RIGHT,
            "key.categories.hackersandslashers"
    );

    // Register the keybindings
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(PARRY);
    }

}
