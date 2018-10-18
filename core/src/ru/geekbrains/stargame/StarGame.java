package ru.geekbrains.stargame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class StarGame extends Game {

	public static final float WORLD_WIDTH = 720f;
	public static final float WORLD_HEIGHT = 1024f;

	private final AssetManager assetManager = new AssetManager();
	@Override
	public void create () {
		assetManager.setLoader(
				TiledMap.class,
				new TmxMapLoader(new InternalFileHandleResolver())
		);

		setScreen(new LoadingScreen(this));
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
}
