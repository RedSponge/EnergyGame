package com.redsponge.energygame.screen;

public enum Screens {

    SPLASHSCREEN(ToastySplashScreenScreen.class),
    OTHER(OtherScreen.class);

    private Class<? extends AbstractScreen> screen;

    Screens(Class<? extends AbstractScreen> screen) {
        this.screen = screen;
    }
}
