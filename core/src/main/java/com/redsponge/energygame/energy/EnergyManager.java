package com.redsponge.energygame.energy;

import com.badlogic.ashley.core.Entity;
import com.redsponge.energygame.assets.Assets;
import com.redsponge.energygame.screen.GameScreen;

public class EnergyManager {
    private HeatEnergy heat;
    private LightEnergy light;
    private ElectricEnergy electric;
    private Assets assets;

    public EnergyManager(GameScreen gameScreen, float pixelsPerMeter, Assets assets) {
        this.heat = new HeatEnergy(pixelsPerMeter, assets);
        this.light = new LightEnergy(pixelsPerMeter, assets);
        this.electric = new ElectricEnergy(pixelsPerMeter, assets);
        this.assets = assets;
    }

    public void setPlayer(Entity player) {
        this.heat.setPlayer(player);
        this.light.setPlayer(player);
        this.electric.setPlayer(player);
    }

    public void update(float delta) {
        this.heat.update(delta);
        this.light.update(delta);
        this.electric.update(delta);
    }

    public void setOnGround(boolean onGround) {
        this.heat.setOnGround(onGround);
    }

    public boolean isHeatPunchOn() {
        return this.heat.isPunchOn();
    }

    public boolean isSuperJumpOn() {
        return this.heat.isJumpOn();
    }

    public boolean isSuperDashOn() {
        return this.light.isDashOn();
    }

    public boolean isElectricFieldOn() {
        return this.electric.isFieldOn();
    }

    public boolean isChargingElectricField() {
        return this.electric.isChargingField();
    }

    public HeatEnergy getHeat() {
        return heat;
    }

    public LightEnergy getLight() {
        return light;
    }

    public ElectricEnergy getElectric() {
        return electric;
    }
}
