package com.redsponge.energygame.components;

import com.badlogic.ashley.core.Component;

public class EventComponent implements Component {

    public String event;
    public boolean executed;

    public EventComponent(String event) {
        this.event = event;
    }
}
