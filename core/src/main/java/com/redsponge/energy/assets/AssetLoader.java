package com.redsponge.energy.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;

public interface AssetLoader {

    void load(AssetManager am);

    void getResources(AssetManager am);
}
