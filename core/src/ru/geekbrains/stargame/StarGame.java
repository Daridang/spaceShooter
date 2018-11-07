package ru.geekbrains.stargame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import ru.geekbrains.stargame.screens.mainscreen.MainScreen;

public class StarGame extends Game {

    public enum ScreenType {
        GAME_SCREEN,
        LOADING_SCREEN,
        MAIN_SCREEN
    }

    private static GameScreen gameScreen;
    private static LoadingScreen loadingScreen;
    private static MainScreen mainScreen;

    public static final float WORLD_WIDTH = 720f;
    public static final float WORLD_HEIGHT = 1024f;

    private final AssetManager assetManager = new AssetManager();

    private final  SoundManager sm = new SoundManager(assetManager);


    @Override
    public void create() {

        gameScreen = new GameScreen(this);
        loadingScreen = new LoadingScreen(this);
        mainScreen = new MainScreen(this);
        setScreen(loadingScreen);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public Screen getScreenType(ScreenType screenType) {
        switch (screenType) {
            case GAME_SCREEN:
                return gameScreen;
            case LOADING_SCREEN:
                return loadingScreen;
            case MAIN_SCREEN:
                return mainScreen;
            default:
                return loadingScreen;
        }
    }


    public SoundManager getSm() {
        return sm;
    }

    @Override
    public void dispose() {
        sm.dispose();
        assetManager.dispose();
    }
}
