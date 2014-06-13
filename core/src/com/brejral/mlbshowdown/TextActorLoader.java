package com.brejral.mlbshowdown;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.brejral.mlbshowdown.TextActorLoader.TextActorParameter;

public class TextActorLoader extends SynchronousAssetLoader<TextActor, TextActorParameter>{

	public TextActorLoader(FileHandleResolver resolver) {
		super(resolver);
	}


	@Override
	public TextActor load(AssetManager assetManager, String fileName,
			FileHandle file, TextActorParameter parameter) {
		TextActor actor = new TextActor(parameter.fontString, parameter.size);
		return actor;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName,
			FileHandle file, TextActorParameter parameter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static class TextActorParameter extends AssetLoaderParameters<TextActor>{
		public String fontString;
		public int size;
		public TextActorParameter(String font, int sz) {
			fontString = font;
			size = sz;
		}
	}
}
