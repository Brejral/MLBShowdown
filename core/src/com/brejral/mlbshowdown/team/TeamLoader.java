package com.brejral.mlbshowdown.team;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.team.TeamLoader.TeamParameter;
import com.brejral.mlbshowdown.user.User;

public class TeamLoader extends SynchronousAssetLoader<Team, TeamParameter> {

   public TeamLoader(FileHandleResolver resolver) {
      super(resolver);
   }

   @Override
   public Team load(AssetManager assetManager, String fileName, FileHandle file, TeamParameter param) {
      return new Team(param.sd, param.user, param.teamName);
   }

   @SuppressWarnings("rawtypes")
   @Override
   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TeamParameter parameter) {
      return null;
   }

   static public class TeamParameter extends AssetLoaderParameters<Team> {
      MLBShowdown sd;
      User user;
      String teamName;
      
      public TeamParameter(MLBShowdown showdown, User usr, String name) {
         sd = showdown;
         user = usr;
         teamName = name;
      }
   }
      
}
