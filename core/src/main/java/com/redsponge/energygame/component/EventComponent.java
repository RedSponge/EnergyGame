package com.redsponge.energygame.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapProperties;

public class EventComponent implements Component {

    public String event;
    public boolean executed;
    public MapProperties props;

    public EventComponent(String event,MapProperties props) {
        this.event = event;
        this.props = props;
    }
}
