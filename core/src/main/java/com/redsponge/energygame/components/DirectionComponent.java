package com.redsponge.energygame.components;

import com.badlogic.ashley.core.Component;

public class DirectionComponent implements Component {
    public Direction direction;

    public DirectionComponent(Direction direction) {
        this.direction = direction;
    }

    public DirectionComponent() {
        this(Direction.RIGHT);
    }

    public enum Direction {
        LEFT(-1),
        RIGHT(1);

        public int mult;
        Direction(int mult) {
            this.mult = mult;
        }

        public static Direction fromNumber(int horiz) {
            return horiz > 0 ? RIGHT : LEFT;
        }
    }
}
