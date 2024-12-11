package net.dndats.hackersandslashers.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

// KEYBINDING REGISTERER
public class Keybinds {

    // Created keybindings
    public static final KeyMapping PARRY = new KeyMapping(
            "key.hackersandslashers.parry",
            InputConstants.Type.MOUSE,
            InputConstants.MOUSE_BUTTON_RIGHT,
            "key.categories.hackersandslashers"
    );

}
