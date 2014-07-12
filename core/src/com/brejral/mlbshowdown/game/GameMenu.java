package com.brejral.mlbshowdown.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.card.Card;
import com.brejral.mlbshowdown.card.CardConstants;
import com.brejral.mlbshowdown.card.CardWidget;
import com.brejral.mlbshowdown.menu.MainMenu;
import com.brejral.mlbshowdown.team.Team;

public class GameMenu extends Window {
   Skin skin;
   Game game;
   Team team;
   ClickListener subsClickListener;
   InputListener subsInputListener;
   public GameMenu(Skin skn, Game gm) {
      super("", skn);
      skin = skn;
      game = gm;
      team = game.getCurrentUserTeam();
      setModal(true);
      setName("Game Menu");
      setZIndex(10000);
      setMainMenu();
      setPosition();
   }
   
   private void setPosition() {
      int height = (int) getHeight();
      int width = (int) getWidth();
      setPosition(MLBShowdown.SCREEN_WIDTH/2 - width/2, MLBShowdown.SCREEN_HEIGHT/2 - height/2);
   }
   
   private void setMainMenu() {
      VerticalGroup group = new VerticalGroup();
      group.setName("Main Menu Buttons");
      group.setFillParent(true);
      group.space(0);
      group.padTop(5);
      addActor(group);
      TextButton resumeButton = new TextButton("Resume Game", game.sd.skin);
      resumeButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            remove();
         }
      });
      group.addActor(resumeButton);
      TextButton substitutionsButton = new TextButton("Substitutions", game.sd.skin);
      substitutionsButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            findActor("Main Menu Buttons").setVisible(false);
            setSubstitutionsMenu();
         }
      });
      group.addActor(substitutionsButton);
      TextButton exitButton = new TextButton("Exit Game", game.sd.skin);
      exitButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            game.sd.setScreen(new MainMenu(game.sd));
         }
      });
      group.addActor(exitButton);
   }
   
   private void setSubstitutionsMenu() {
      Table table = new Table();
      table.setName("Substitution Menu Table");
      table.setFillParent(true);
      addActor(table);
      System.out.println(getWidth() + " " + getHeight());
      setSize(800, 500);
      setPosition();
      
      Table lineupTable = new Table();
      lineupTable.setBackground(new NinePatchDrawable(game.sd.boardNP));
      Label lineup = new Label("Lineup", game.sd.skin, "aero20");
      lineup.setAlignment(Align.center);
      lineupTable.add(lineup).colspan(3);
      for (int i = 0; i < team.lineup.size(); i++) {
         lineupTable.row();
         TextButton num = new TextButton(Integer.toString(i + 1), game.sd.skin, "subs");
         num.setName("Subs Lineup Num " + i);
         num.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
               String name = event.getListenerActor().getName();
               String number = name.substring(name.length() - 1);
               ((TextButton)findActor("Subs Lineup Pos " + number)).getLabel().setColor(Color.YELLOW);
               ((TextButton)findActor("Subs Lineup Name " + number)).getLabel().setColor(Color.YELLOW);
               ((CardWidget)findActor("Substitutions Card")).setCard(team.lineup.get(Integer.parseInt(number)));
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
               String name = event.getListenerActor().getName();
               String number = name.substring(name.length() - 1);
               ((TextButton)findActor("Subs Lineup Pos " + number)).getLabel().setColor(Color.WHITE);
               ((TextButton)findActor("Subs Lineup Name " + number)).getLabel().setColor(Color.WHITE);
               ((CardWidget)findActor("Substitutions Card")).setCard(null);
            }
         });
         lineupTable.add(num).width(20).center();
         TextButton pos = new TextButton("", game.sd.skin, "subs");
         pos.setName("Subs Lineup Pos " + i);
         pos.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
               String name = event.getListenerActor().getName();
               String number = name.substring(name.length() - 1);
               ((TextButton)findActor("Subs Lineup Num " + number)).getLabel().setColor(Color.YELLOW);
               ((TextButton)findActor("Subs Lineup Name " + number)).getLabel().setColor(Color.YELLOW);
               ((CardWidget)findActor("Substitutions Card")).setCard(team.lineup.get(Integer.parseInt(number)));
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
               String name = event.getListenerActor().getName();
               String number = name.substring(name.length() - 1);
               ((TextButton)findActor("Subs Lineup Num " + number)).getLabel().setColor(Color.WHITE);
               ((TextButton)findActor("Subs Lineup Name " + number)).getLabel().setColor(Color.WHITE);
               ((CardWidget)findActor("Substitutions Card")).setCard(null);
            }
         });
         lineupTable.add(pos).width(35).center();
         TextButton name = new TextButton("", game.sd.skin, "subs");
         name.setName("Subs Lineup Name " + i);
         name.getLabel().setAlignment(Align.left);
         name.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
               String name = event.getListenerActor().getName();
               String number = name.substring(name.length() - 1);
               ((TextButton)findActor("Subs Lineup Num " + number)).getLabel().setColor(Color.YELLOW);
               ((TextButton)findActor("Subs Lineup Pos " + number)).getLabel().setColor(Color.YELLOW);
               ((CardWidget)findActor("Substitutions Card")).setCard(team.lineup.get(Integer.parseInt(number)));
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
               String name = event.getListenerActor().getName();
               String number = name.substring(name.length() - 1);
               ((TextButton)findActor("Subs Lineup Num " + number)).getLabel().setColor(Color.WHITE);
               ((TextButton)findActor("Subs Lineup Pos " + number)).getLabel().setColor(Color.WHITE);
               ((CardWidget)findActor("Substitutions Card")).setCard(null);
            }
         });
         lineupTable.add(name).width(175).left();
      }
      
      Table pitcherTable = new Table();
      pitcherTable.setBackground(new NinePatchDrawable(game.sd.boardNP));
      Label pitcher = new Label("Pitcher", game.sd.skin, "aero20");
      pitcher.setAlignment(Align.center);
      pitcherTable.add(pitcher).colspan(2);
      pitcherTable.row();
      TextButton throwsPitcher = new TextButton("", game.sd.skin, "subs");
      throwsPitcher.setName("Subs Pitcher Throws");
      throwsPitcher.addListener(new InputListener() {
         @Override
         public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            ((TextButton)findActor("Subs Pitcher Name")).getLabel().setColor(Color.YELLOW);
            ((CardWidget)findActor("Substitutions Card")).setCard(team.positions.get(1));
         }
         @Override
         public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            ((TextButton)findActor("Subs Pitcher Name")).getLabel().setColor(Color.WHITE);
            ((CardWidget)findActor("Substitutions Card")).setCard(null);
         }
      });
      pitcherTable.add(throwsPitcher).width(45);
      TextButton namePitcher = new TextButton("", game.sd.skin, "subs");
      namePitcher.setName("Subs Pitcher Name");
      namePitcher.getLabel().setAlignment(Align.left);
      namePitcher.addListener(new InputListener() {
         @Override
         public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            event.getListenerActor().getParent();
            ((TextButton)findActor("Subs Pitcher Throws")).getLabel().setColor(Color.YELLOW);
            ((CardWidget)findActor("Substitutions Card")).setCard(team.positions.get(1));
         }
         @Override
         public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            ((TextButton)findActor("Subs Pitcher Throws")).getLabel().setColor(Color.WHITE);
            ((CardWidget)findActor("Substitutions Card")).setCard(null);
         }
      });pitcherTable.add(namePitcher).width(150);
      
      Table benchTable = new Table();
      benchTable.setBackground(new NinePatchDrawable(game.sd.boardNP));
      Label bench = new Label("Bench", game.sd.skin, "aero20");
      bench.setAlignment(Align.center);
      benchTable.add(bench).colspan(2);
      for (int i = 0; i < team.bench.size(); i++) {
         benchTable.row();
         TextButton posBench = new TextButton("", game.sd.skin, "subs");
         posBench.setName("Subs Bench Pos " + i);
         posBench.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
               String name = event.getListenerActor().getName();
               String number = name.substring(name.length() - 1);
               ((TextButton)findActor("Subs Bench Name " + number)).getLabel().setColor(Color.YELLOW);
               ((CardWidget)findActor("Substitutions Card")).setCard(team.bench.get(Integer.parseInt(number)));
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
               String name = event.getListenerActor().getName();
               String number = name.substring(name.length() - 1);
               ((TextButton)findActor("Subs Bench Name " + number)).getLabel().setColor(Color.WHITE);
               ((CardWidget)findActor("Substitutions Card")).setCard(null);
            }
         });
         benchTable.add(posBench).width(80).center();
         TextButton nameBench = new TextButton("", game.sd.skin, "subs");
         nameBench.setName("Subs Bench Name " + i);
         nameBench.getLabel().setAlignment(Align.left);
         nameBench.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
               String name = event.getListenerActor().getName();
               String number = name.substring(name.length() - 1);
               ((TextButton)findActor("Subs Bench Pos " + number)).getLabel().setColor(Color.YELLOW);
               ((CardWidget)findActor("Substitutions Card")).setCard(team.bench.get(Integer.parseInt(number)));
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
               String name = event.getListenerActor().getName();
               String number = name.substring(name.length() - 1);
               ((TextButton)findActor("Subs Bench Pos " + number)).getLabel().setColor(Color.WHITE);
               ((CardWidget)findActor("Substitutions Card")).setCard(null);
            }
         });
         benchTable.add(nameBench).width(150).left();
      }
      
      Table bullpenTable = new Table();
      bullpenTable.setBackground(new NinePatchDrawable(game.sd.boardNP));
      Label bullpen = new Label("Bullpen", game.sd.skin, "aero20");
      bullpen.setAlignment(Align.center);
      bullpenTable.add(bullpen).colspan(2);
      for (int i = 0; i < team.bullpen.size(); i++) {
         bullpenTable.row();
         TextButton throwsBullpen = new TextButton("", game.sd.skin, "subs");
         throwsBullpen.setName("Subs Bullpen Throws " + i);
         throwsBullpen.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
               String name = event.getListenerActor().getName();
               String number = name.substring(name.length() - 1);
               ((TextButton)findActor("Subs Bullpen Name " + number)).getLabel().setColor(Color.YELLOW);
               ((CardWidget)findActor("Substitutions Card")).setCard(team.bullpen.get(Integer.parseInt(number)));
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
               String name = event.getListenerActor().getName();
               String number = name.substring(name.length() - 1);
               ((TextButton)findActor("Subs Bullpen Name " + number)).getLabel().setColor(Color.WHITE);
               ((CardWidget)findActor("Substitutions Card")).setCard(null);
            }
         });
         bullpenTable.add(throwsBullpen).width(45);
         TextButton nameBullpen = new TextButton("", game.sd.skin, "subs");
         nameBullpen.setName("Subs Bullpen Name " + i);
         nameBullpen.getLabel().setAlignment(Align.left);
         nameBullpen.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
               String name = event.getListenerActor().getName();
               String number = name.substring(name.length() - 1);
               ((TextButton)findActor("Subs Bullpen Throws " + number)).getLabel().setColor(Color.YELLOW);
               ((CardWidget)findActor("Substitutions Card")).setCard(team.bullpen.get(Integer.parseInt(number)));
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
               String name = event.getListenerActor().getName();
               String number = name.substring(name.length() - 1);
               ((TextButton)findActor("Subs Bullpen Throws " + number)).getLabel().setColor(Color.WHITE);
               ((CardWidget)findActor("Substitutions Card")).setCard(null);
            }
         });
         bullpenTable.add(nameBullpen).width(150);
      }
      
      Table leftTable = new Table();
      Table middleTable = new Table();
      Table rightTable = new Table();
      leftTable.add(lineupTable);
      leftTable.row();
      leftTable.add(benchTable);
      middleTable.add(pitcherTable);
      middleTable.row();
      middleTable.add(bullpenTable);
      
      TextButton backButton = new TextButton("Back", game.sd.skin);
      backButton.getLabel().setAlignment(Align.right);
      backButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            removeActor(findActor("Substitution Menu Table"));
            setSize(150, 150);
            setPosition();
            findActor("Main Menu Buttons").setVisible(true);
         }
      });
      CardWidget card = new CardWidget();
      card.setName("Substitutions Card");
      card.setScale(.6f);
      rightTable.add(backButton).right();
      rightTable.row();
      rightTable.add(card).width(CardConstants.CARD_WIDTH * .6f).height(CardConstants.CARD_HEIGHT * .6f);
      
      table.add(leftTable).top();
      table.add(middleTable).top();
      table.add(rightTable).top();
      
      populateSubstitutionTables();
   }
   
   public void populateSubstitutionTables() {
      Team team = game.getCurrentUserTeam();
      for (int i = 0; i < team.lineup.size(); i++) {
         Card card = team.lineup.get(i);
         ((TextButton)findActor("Subs Lineup Pos " + i)).setText(team.getPosition(card));
         ((TextButton)findActor("Subs Lineup Name " + i)).setText(card.name);
      }
      ((TextButton)findActor("Subs Pitcher Throws")).setText(team.positions.get(1).throwHand + "HP");
      ((TextButton)findActor("Subs Pitcher Name")).setText(team.positions.get(1).name);
      for (int i = 0; i < team.bench.size(); i++) {
         Card card = team.bench.get(i);
         ((TextButton)findActor("Subs Bench Pos " + i)).setText(card.getPositions());
         ((TextButton)findActor("Subs Bench Name " + i)).setText(card.name);
      }
      for (int i = 0; i < team.bullpen.size(); i++) {
         Card card = team.bullpen.get(i);
         ((TextButton)findActor("Subs Bullpen Throws " + i)).setText(card.throwHand + "HP");
         ((TextButton)findActor("Subs Bullpen Name " + i)).setText(card.name);
      }
   }
}
