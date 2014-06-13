package com.brejral.mlbshowdown;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.brejral.mlbshowdown.ShapeActorLoader.ShapeActorParameter;

public class ShapeActorLoader extends SynchronousAssetLoader<ShapeActor, ShapeActorParameter> {

	public ShapeActorLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public ShapeActor load(AssetManager assetManager, String fileName,
			FileHandle file, ShapeActorParameter parameter) {
		ShapeActor actor = null;
		switch(parameter.type) {
		case "Triangle":
			actor = new ShapeActor(ShapeType.Filled, new float[0]);
			break;
		case "Circle":
			actor = new ShapeActor(ShapeType.Filled, new float[0], parameter.r);
			break;
		case "Rectangle":
			actor = new ShapeActor(ShapeType.Filled, new float[0], parameter.width, parameter.height);
		}
		return actor;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName,
			FileHandle file, ShapeActorParameter parameter) {
		return null;
	}

	public static class ShapeActorParameter extends AssetLoaderParameters<ShapeActor>{
		public float r;
		public float width, height;
		public String type;
		
		public ShapeActorParameter() {
			type = "Triangle";
		}
		
		public ShapeActorParameter(int radius) {
			type = "Circle";
			r = radius;
		}
		
		public ShapeActorParameter(int w, int h) {
			type = "Rectangle";
			width = w;
			height = h;
		}
	}
}
