package com.brejral.mlbshowdown.card;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.card.CardLoader.CardParameter;

public class CardLoader extends SynchronousAssetLoader<Card, CardParameter> {

	
	public CardLoader(FileHandleResolver resolver) {
		super(resolver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Card load(AssetManager assetManager, String fileName,
			FileHandle file, CardParameter parameter) {
		Card card = new Card(parameter.sd, parameter.id);
		return card;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName,
			FileHandle file, CardParameter parameter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	static public class CardParameter extends AssetLoaderParameters<Card> {
		public MLBShowdown sd;
		public int id;
		public CardParameter(MLBShowdown showdown, int iden) {
			sd = showdown;
			id = iden;
		}
	}
}
