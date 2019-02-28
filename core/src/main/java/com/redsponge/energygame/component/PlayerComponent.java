package com.redsponge.energygame.component;

import com.badlogic.ashley.core.Component;
import com.redsponge.energygame.energy.EnergyManager;

/**
 * Marks an entity as a player.
 */
public class PlayerComponent implements Component {
    public boolean dead;

    /* You have to set it every tick. */
    public EnergyManager energy;
}
