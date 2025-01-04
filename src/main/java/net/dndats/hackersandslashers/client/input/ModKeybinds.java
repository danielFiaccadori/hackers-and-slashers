package net.dndats.hackersandslashers.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class ModKeybinds {

    /**
     * This class creates the instances of keybinds
     */

    public static final KeyMapping PARRY = new KeyMapping(
            "key.hackersandslashers.parry",
            InputConstants.Type.MOUSE,
            InputConstants.MOUSE_BUTTON_RIGHT,
            "key.categories.hackersandslashers"
    );

}
