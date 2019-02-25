package com.redsponge.energygame.components;

import com.badlogic.ashley.core.Component;

public class EnemyComponent implements Component {

    public int health;

    public EnemyComponent(int health) {
        this.health = health;
    }

    public EnemyComponent() {
        this(5);
    }
}
