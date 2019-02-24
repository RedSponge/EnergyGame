package com.redsponge.energy.screen;

public enum Screens {

    SPLASHSCREEN(SplashScreenScreen.class),
    OTHER(OtherScreen.class);

    private Class<? extends AbstractScreen> screen;

    Screens(Class<? extends AbstractScreen> screen) {
        this.screen = screen;
    }
}
