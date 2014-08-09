package com.brejral.mlbshowdown.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
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
import com.esotericsoftware.tablelayout.Cell;

public class GameMenu extends Window {
   Skin skin;
   Game game;
   Team team;
   List<Card> lineup, bench, bullpen, positions;
   Integer runner1Num, runner2Num, runner3Num;
   ClickListener subsListener;
   boolean showOnlySubsMenu;

   public GameMenu(Skin skn, Game gm) {
      super("", skn);
      skin = skn;
      game = gm;
      team = game.getCurrentUserTeam();
      subsListener = new ClickListener() {
         @Override
         public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            enterSubsButton(event.getListenerActor());
         }

         @Override
         public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            exitSubsButton(event.getListenerActor(), pointer);
         }

         @Override
         public void clicked(InputEvent event, float x, float y) {
            clickedSubsButton(event.getListenerActor());
         }
      };
      setModal(true);
      setName("Game Menu");
      setZIndex(10000);
      setMainMenu();
      setPosition();
   }
   
   public GameMenu(Skin skn, Game gm, String menuString) {
      this(skn, gm);
      switch(menuString) {
      case "Subs":
         showOnlySubsMenu = true;
         findActor("Main Menu Buttons").setVisible(false);
         setSubstitutionsMenu();
      }
   }
   
   private void setPosition() {
      int height = (int) getHeight();
      int width = (int) getWidth();
      setPosition(MLBShowdown.SCREEN_WIDTH / 2 - width / 2, MLBShowdown.SCREEN_HEIGHT / 2 - height / 2);
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
      lineup = new ArrayList<Card>(team.lineup);
      positions = new ArrayList<Card>(team.positions);
      bench = new ArrayList<Card>(team.bench);
      bullpen = new ArrayList<Card>(team.bullpen);
      runner1Num = game.runner1 != null ? team.lineup.indexOf(game.runner1) : null;
      runner2Num = game.runner2 != null ? team.lineup.indexOf(game.runner2) : null;
      runner3Num = game.runner3 != null ? team.lineup.indexOf(game.runner3) : null;
      Table table = new Table();
      table.setName("Substitution Menu Table");
      table.setFillParent(true);
      addActor(table);
      System.out.println(getWidth() + " " + getHeight());
      setSize(785, 475);
      setPosition();

      /*
       * Lineup
       */
      Table lineupTable = new Table();
      lineupTable.setName("Lineup Table");
      lineupTable.setBackground(new NinePatchDrawable(game.sd.boardNP));
      Label lineupLabel = new Label("Lineup", game.sd.skin, "aero20");
      lineupLabel.setAlignment(Align.center);
      lineupTable.add(lineupLabel).colspan(3);
      for (int i = 0; i < lineup.size(); i++) {
         lineupTable.row();
         TextButton num = new TextButton(Integer.toString(i + 1), game.sd.skin, "subs");
         num.setName("Subs Lineup Num " + i);
         num.addListener(subsListener);
         lineupTable.add(num).width(20).center();
         TextButton pos = new TextButton("", game.sd.skin, "subs");
         pos.setName("Subs Lineup Pos " + i);
         pos.addListener(subsListener);
         lineupTable.add(pos).width(35).center();
         TextButton name = new TextButton("", game.sd.skin, "subs");
         name.setName("Subs Lineup Name " + i);
         name.getLabel().setAlignment(Align.left);
         name.addListener(subsListener);
         lineupTable.add(name).width(175).left();
      }

      /*
       * Pitcher
       */
      Table pitcherTable = new Table();
      pitcherTable.setName("Pitcher Table");
      pitcherTable.setBackground(new NinePatchDrawable(game.sd.boardNP));
      Label pitcherLabel = new Label("Pitcher", game.sd.skin, "aero20");
      pitcherLabel.setAlignment(Align.center);
      pitcherTable.add(pitcherLabel).colspan(2);
      pitcherTable.row();
      TextButton throwsPitcher = new TextButton("", game.sd.skin, "subs");
      throwsPitcher.setName("Subs Pitcher Throws");
      throwsPitcher.addListener(subsListener);
      pitcherTable.add(throwsPitcher).width(45);
      TextButton namePitcher = new TextButton("", game.sd.skin, "subs");
      namePitcher.setName("Subs Pitcher Name");
      namePitcher.getLabel().setAlignment(Align.left);
      namePitcher.addListener(subsListener);
      pitcherTable.add(namePitcher).width(150);

      /*
       * Bench
       */
      Table benchTable = new Table();
      benchTable.setName("Bench Table");
      benchTable.setBackground(new NinePatchDrawable(game.sd.boardNP));
      Label benchLabel = new Label("Bench", game.sd.skin, "aero20");
      benchLabel.setAlignment(Align.center);
      benchTable.add(benchLabel).colspan(2);
      for (int i = 0; i < bench.size(); i++) {
         benchTable.row();
         TextButton posBench = new TextButton("", game.sd.skin, "subs");
         posBench.setName("Subs Bench Pos " + i);
         posBench.getLabel().setWrap(true);
         posBench.addListener(subsListener);
         benchTable.add(posBench).width(80).center();
         TextButton nameBench = new TextButton("", game.sd.skin, "subs");
         nameBench.setName("Subs Bench Name " + i);
         nameBench.getLabel().setAlignment(Align.left);
         nameBench.addListener(subsListener);
         benchTable.add(nameBench).width(150).left();
      }

      /*
       * Bullpen
       */
      Table bullpenTable = new Table();
      bullpenTable.setName("Bullpen Table");
      bullpenTable.setBackground(new NinePatchDrawable(game.sd.boardNP));
      Label bullpenLabel = new Label("Bullpen", game.sd.skin, "aero20");
      bullpenLabel.setAlignment(Align.center);
      bullpenTable.add(bullpenLabel).colspan(2);
      for (int i = 0; i < bullpen.size(); i++) {
         bullpenTable.row();
         TextButton throwsBullpen = new TextButton("", game.sd.skin, "subs");
         throwsBullpen.setName("Subs Bullpen Throws " + i);
         throwsBullpen.addListener(subsListener);
         bullpenTable.add(throwsBullpen).width(45);
         TextButton nameBullpen = new TextButton("", game.sd.skin, "subs");
         nameBullpen.setName("Subs Bullpen Name " + i);
         nameBullpen.getLabel().setAlignment(Align.left);
         nameBullpen.addListener(subsListener);
         bullpenTable.add(nameBullpen).width(150);
      }

      TextButton acceptChangesButton = new TextButton("Accept Changes", game.sd.skin);
      acceptChangesButton.setName("Accept Changes Button");
      acceptChangesButton.getLabel().setAlignment(Align.left);
      acceptChangesButton.setDisabled(true);
      acceptChangesButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            if (!((TextButton) event.getListenerActor()).isDisabled()) {
               for (int i = 0; i < team.positions.size(); i++) {
                  Card cardOld = team.positions.get(i);
                  Card newCard = positions.get(i);
                  if (!cardOld.equals(newCard)) {
                     int index = team.roster.indexOf(newCard);
                     team.positions.set(i, team.roster.get(index));
                  }
               }
               for (int i = 0; i < team.lineup.size(); i++) {
                  Card cardOld = team.lineup.get(i);
                  Card newCard = lineup.get(i);
                  if (!cardOld.equals(newCard)) {
                     int index = team.roster.indexOf(newCard);
                     team.lineup.set(i, team.roster.get(index));
                  }
               }
               for (int i = 0; i < team.bench.size(); i++) {
                  Card cardOld = team.bench.get(i);
                  if (!bench.contains(cardOld)) {
                     team.bench.remove(team.bench.get(i));
                  }
               }
               for (int i = 0; i < team.bullpen.size(); i++) {
                  Card cardOld = team.bullpen.get(i);
                  if (!bullpen.contains(cardOld)) {
                     team.bullpen.remove(team.bullpen.get(i));
                  }
               }
               game.statKeeper.setUpStatsForSubstitutions();
               game.updateGameFromSubstitutions(runner1Num, runner2Num, runner3Num);
               removeActor(findActor("Substitution Menu Table"));
               setSize(150, 150);
               setPosition();
               findActor("Main Menu Buttons").setVisible(true);
               if (game.isBattingTeamUser()) {
                  game.validateTeams = true;
               }
               if (showOnlySubsMenu) {
                  remove();
               }
            }
         }
      });
      TextButton backButton = new TextButton("Cancel", game.sd.skin);
      backButton.getLabel().setAlignment(Align.right);
      backButton.setDisabled(showOnlySubsMenu);
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

      Table leftTable = new Table();
      Table middleTable = new Table();
      Table rightTable = new Table();
      leftTable.add(lineupTable);
      leftTable.row();
      leftTable.add(benchTable);
      middleTable.add(pitcherTable);
      middleTable.row();
      middleTable.add(bullpenTable);
      rightTable.add(acceptChangesButton).left();
      rightTable.add(backButton).right();
      rightTable.row();
      rightTable.add(card).width(CardConstants.CARD_WIDTH * .6f).height(CardConstants.CARD_HEIGHT * .6f).colspan(2);

      table.add(leftTable).top();
      table.add(middleTable).top();
      table.add(rightTable).top();

      populateSubstitutionTables();
      enableAllCards();
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
   private void populateSubstitutionTables() {
      for (int i = 0; i < lineup.size(); i++) {
         Card card = lineup.get(i);
         ((TextButton) findActor("Subs Lineup Pos " + i)).setText(Team.getPosition(card, positions));
         ((TextButton) findActor("Subs Lineup Name " + i)).setText(card.name);
      }
      ((TextButton) findActor("Subs Pitcher Throws")).setText(positions.get(1).throwHand + "HP");
      ((TextButton) findActor("Subs Pitcher Name")).setText(positions.get(1).name);
      Table table = (Table) findActor("Bench Table");
      for (int i = 0; i < bench.size(); i++) {
         Card card = bench.get(i);
         ((TextButton) findActor("Subs Bench Pos " + i)).setText(card.getPositions());
         ((TextButton) findActor("Subs Bench Name " + i)).setText(card.name);
      }
      while (table.getCells().size() / 2 > bench.size()) {
         List<Cell> cells = table.getCells();
         cells.get(cells.size() - 1).setWidget(null);
         cells.remove(cells.size() - 1);
         cells.get(cells.size() - 1).setWidget(null);
         cells.remove(cells.size() - 1);
      }
      table = (Table) findActor("Bullpen Table");
      for (int i = 0; i < bullpen.size(); i++) {
         Card card = bullpen.get(i);
         ((TextButton) findActor("Subs Bullpen Throws " + i)).setText(card.throwHand + "HP");
         ((TextButton) findActor("Subs Bullpen Name " + i)).setText(card.name);
      }
      while (table.getCells().size() / 2 > bullpen.size()) {
         List<Cell> cells = table.getCells();
         cells.get(cells.size() - 1).setWidget(null);
         cells.remove(cells.size() - 1);
         cells.get(cells.size() - 1).setWidget(null);
         cells.remove(cells.size() - 1);
      }
   }

   private void enterSubsButton(Actor actor) {
      String actorName = actor.getName();
      String number = actorName.substring(actorName.length() - 1);
      if (actorName.contains("Lineup")) {
         if (actorName.contains("Num")) {
            setStyle("Subs Lineup Pos " + number, true);
            setStyle("Subs Lineup Name " + number, true);
         } else if (actorName.contains("Pos")) {
            setStyle("Subs Lineup Num " + number, true);
            setStyle("Subs Lineup Name " + number, true);
         } else {
            setStyle("Subs Lineup Num " + number, true);
            setStyle("Subs Lineup Pos " + number, true);
         }
         ((CardWidget) findActor("Substitutions Card")).setCard(lineup.get(Integer.parseInt(number)));
      } else if (actorName.contains("Bench")) {
         if (actorName.contains("Pos")) {
            setStyle("Subs Bench Name " + number, true);
         } else {
            setStyle("Subs Bench Pos " + number, true);
         }
         ((CardWidget) findActor("Substitutions Card")).setCard(bench.get(Integer.parseInt(number)));
      } else if (actorName.contains("Pitcher")) {
         if (actorName.contains("Throws")) {
            setStyle("Subs Pitcher Name", true);
         } else {
            setStyle("Subs Pitcher Throws", true);
         }
         ((CardWidget) findActor("Substitutions Card")).setCard(positions.get(1));
      } else if (actorName.contains("Bullpen")) {
         if (actorName.contains("Throws")) {
            setStyle("Subs Bullpen Name " + number, true);
         } else {
            setStyle("Subs Bullpen Throws " + number, true);
         }
         ((CardWidget) findActor("Substitutions Card")).setCard(bullpen.get(Integer.parseInt(number)));
      }
   }

   private void exitSubsButton(Actor actor, int pointer) {
      if (pointer < 0 && findActor(actor.getName()) != null) {
         String actorName = actor.getName();
         String number = actorName.substring(actorName.length() - 1);
         if (actorName.contains("Lineup")) {
            if (actorName.contains("Num")) {
               setStyle("Subs Lineup Pos " + number, false);
               setStyle("Subs Lineup Name " + number, false);
            } else if (actorName.contains("Pos")) {
               setStyle("Subs Lineup Num " + number, false);
               setStyle("Subs Lineup Name " + number, false);
            } else {
               setStyle("Subs Lineup Num " + number, false);
               setStyle("Subs Lineup Pos " + number, false);
            }
         } else if (actorName.contains("Bench")) {
            if (actorName.contains("Pos")) {
               setStyle("Subs Bench Name " + number, false);
            } else {
               setStyle("Subs Bench Pos " + number, false);
            }
         } else if (actorName.contains("Pitcher")) {
            if (actorName.contains("Throws")) {
               setStyle("Subs Pitcher Name", false);
            } else {
               setStyle("Subs Pitcher Throws", false);
            }
         } else if (actorName.contains("Bullpen")) {
            if (actorName.contains("Throws")) {
               setStyle("Subs Bullpen Name " + number, false);
            } else {
               setStyle("Subs Bullpen Throws " + number, false);
            }
         }
         ((CardWidget) findActor("Substitutions Card")).setCard(null);
      }
   }

   private void clickedSubsButton(Actor actor) {
      if (!((TextButton) actor).isDisabled()) {
         String actorName = actor.getName();
         Integer number = !actorName.contains("Pitcher") ? Integer.parseInt(actorName.substring(actorName.length() - 1)) : null;
         Card actorCard = getActorCard(actorName, number);
         Card selectedCard = getSelected(actorName.contains("Lineup") || actorName.contains("Bench"), actorCard);
         if (selectedCard != null) {
            if (actorName.contains("Lineup")) {
               if (lineup.contains(selectedCard)) {
                  int posSelected = positions.indexOf(selectedCard);
                  int posActor = positions.indexOf(lineup.get(number));
                  positions.set(posSelected, lineup.get(number));
                  positions.set(posActor, selectedCard);
                  setChanged(actorName, lineup.indexOf(positions.get(posSelected)), true);
                  setChanged(actorName, lineup.indexOf(positions.get(posActor)), true);
               } else {
                  if (isChanged((TextButton) findActor("Subs Lineup Name " + number))) {
                     int selectedIndex = bench.indexOf(selectedCard);
                     bench.set(selectedIndex, lineup.get(number));
                  } else {
                     bench.remove(selectedCard);
                  }
                  positions.set(positions.indexOf(lineup.get(number)), selectedCard);
                  lineup.set(number, selectedCard);
                  setChanged(actorName, number, false);
               }
            } else if (actorName.contains("Bench")) {
               int selectedIndex = lineup.indexOf(selectedCard);
               positions.set(positions.indexOf(selectedCard), bench.get(number));
               lineup.set(selectedIndex, bench.get(number));
               if (isChanged((TextButton) findActor("Subs Lineup Name " + selectedIndex))) {
                  bench.set(number, selectedCard);
               } else {
                  bench.remove(bench.get(number));
               }
               setChanged(actorName, selectedIndex, false);
            } else if (actorName.contains("Pitcher")) {
               if (isChanged((TextButton) findActor("Subs Pitcher Name"))) {
                  bullpen.set(bullpen.indexOf(selectedCard), positions.get(1));
               } else {
                  bullpen.remove(selectedCard);
               }
               positions.set(1, selectedCard);
               setChanged(actorName, null, false);
            } else if (actorName.contains("Bullpen")) {
               if (isChanged((TextButton) findActor("Subs Pitcher Name"))) {
                  Card bullpenCard = bullpen.get(number);
                  bullpen.set(number, positions.get(1));
                  positions.set(1, bullpenCard);
               } else {
                  positions.set(1, bullpen.get(number));
                  bullpen.remove(bullpen.get(number));
               }
               setChanged(actorName, null, false);
            }
            populateSubstitutionTables();
            enableAllCards();
            validateSubstitutionChanges();
            removeChecked(actorName.contains("Lineup") || actorName.contains("Bench"));
         } else {
            if (actorName.contains("Lineup")) {
               if (actorName.contains("Num")) {
                  TextButton pos = (TextButton) findActor("Subs Lineup Pos " + number);
                  TextButton name = (TextButton) findActor("Subs Lineup Name " + number);
                  pos.setChecked(!pos.isChecked());
                  name.setChecked(!name.isChecked());
               } else if (actorName.contains("Pos")) {
                  TextButton num = (TextButton) findActor("Subs Lineup Num " + number);
                  TextButton name = (TextButton) findActor("Subs Lineup Name " + number);
                  num.setChecked(!num.isChecked());
                  name.setChecked(!name.isChecked());
               } else {
                  TextButton num = (TextButton) findActor("Subs Lineup Num " + number);
                  TextButton pos = (TextButton) findActor("Subs Lineup Pos " + number);
                  num.setChecked(!num.isChecked());
                  pos.setChecked(!pos.isChecked());
               }
            } else if (actorName.contains("Bench")) {
               if (actorName.contains("Pos")) {
                  TextButton name = (TextButton) findActor("Subs Bench Name " + number);
                  name.setChecked(!name.isChecked());
               } else {
                  TextButton pos = (TextButton) findActor("Subs Bench Pos " + number);
                  pos.setChecked(!pos.isChecked());
               }
            } else if (actorName.contains("Pitcher")) {
               if (actorName.contains("Throws")) {
                  TextButton name = (TextButton) findActor("Subs Pitcher Name");
                  name.setChecked(!name.isChecked());
               } else {
                  TextButton throwsBtn = (TextButton) findActor("Subs Pitcher Throws");
                  throwsBtn.setChecked(!throwsBtn.isChecked());
               }
            } else if (actorName.contains("Bullpen")) {
               if (actorName.contains("Throws")) {
                  TextButton name = (TextButton) findActor("Subs Bullpen Name " + number);
                  name.setChecked(!name.isChecked());
               } else {
                  TextButton throwsBtn = (TextButton) findActor("Subs Bullpen Throws " + number);
                  throwsBtn.setChecked(!throwsBtn.isChecked());
               }
            }
            if (((TextButton) actor).isChecked()) {
               if (actorName.contains("Lineup")) {
                  disableForLineupAndBenchCards(lineup.get(number), "Lineup");
               } else if (actorName.contains("Bench")) {
                  disableForLineupAndBenchCards(bench.get(number), "Bench");
               } else {
                  disableForBullpenAndPitcherCards(actorName.contains("Pitcher") ? null : number);
               }
            } else {
               enableAllCards();
            }
         }
      }
   }

   private Card getActorCard(String actorName, Integer index) {
      if (actorName.contains("Lineup"))
         return lineup.get(index);
      if (actorName.contains("Bench"))
         return bench.get(index);
      if (actorName.contains("Pitcher"))
         return positions.get(1);
      if (actorName.contains("Bullpen"))
         return bullpen.get(index);
      return null;
   }

   private void disableForLineupAndBenchCards(Card card, String table) {
      disableLineupCards(card, table);
      disableBenchCards(card, table);
      ((TextButton) findActor("Subs Pitcher Throws")).setDisabled(true);
      ((TextButton) findActor("Subs Pitcher Name")).setDisabled(true);
      for (int i = 0; i < bullpen.size(); i++) {
         ((TextButton) findActor("Subs Bullpen Throws " + i)).setDisabled(true);
         ((TextButton) findActor("Subs Bullpen Name " + i)).setDisabled(true);
      }
   }

   private void disableLineupCards(Card card, String table) {
      for (Integer i = 0; i < lineup.size(); i++) {
         Card lineupCard = lineup.get(i);
         boolean canPlayPosition = false;
         if (table.equals("Bench")) {
            canPlayPosition = card.canPlayPosition(Team.getPosition(lineupCard, positions));
         } else {
            canPlayPosition = lineupCard.canPlayPosition(Team.getPosition(card, positions)) && card.canPlayPosition(Team.getPosition(lineupCard, positions));
         }
         if (!lineupCard.equals(card) && ((!canPlayPosition && game.isFieldingTeamUser()) || (game.isBattingTeamUser() && !isRunnerOrBatter(lineupCard)))) {
            ((TextButton) findActor("Subs Lineup Num " + i)).setDisabled(true);
            ((TextButton) findActor("Subs Lineup Pos " + i)).setDisabled(true);
            ((TextButton) findActor("Subs Lineup Name " + i)).setDisabled(true);
         }
      }
   }

   private void disableBenchCards(Card card, String table) {
      for (int i = 0; i < bench.size(); i++) {
         Card benchCard = bench.get(i);
         boolean canPlayPosition = false;
         if (table.equals("Lineup")) {
            canPlayPosition = benchCard.canPlayPosition(Team.getPosition(card, positions));
         }
         if (table.equals("Lineup") && !canPlayPosition && game.isFieldingTeamUser()) {
            ((TextButton) findActor("Subs Bench Pos " + i)).setDisabled(true);
            ((TextButton) findActor("Subs Bench Name " + i)).setDisabled(true);
         } else if (table.equals("Bench") && !benchCard.equals(card)) {
            ((TextButton) findActor("Subs Bench Pos " + i)).setDisabled(true);
            ((TextButton) findActor("Subs Bench Name " + i)).setDisabled(true);
         }
      }
   }

   private void disableForBullpenAndPitcherCards(Integer index) {
      if (index != null) {
         for (int i = 0; i < bullpen.size(); i++) {
            if (!index.equals(i)) {
               ((TextButton) findActor("Subs Bullpen Throws " + i)).setDisabled(true);
               ((TextButton) findActor("Subs Bullpen Name " + i)).setDisabled(true);
            }
         }
      }
      for (int i = 0; i < lineup.size(); i++) {
         ((TextButton) findActor("Subs Lineup Num " + i)).setDisabled(true);
         ((TextButton) findActor("Subs Lineup Pos " + i)).setDisabled(true);
         ((TextButton) findActor("Subs Lineup Name " + i)).setDisabled(true);
      }
      for (int i = 0; i < bench.size(); i++) {
         ((TextButton) findActor("Subs Bench Pos " + i)).setDisabled(true);
         ((TextButton) findActor("Subs Bench Name " + i)).setDisabled(true);
      }
   }

   private void enableAllCards() {
      for (int i = 0; i < lineup.size(); i++) {
         if (!game.isBattingTeamUser() || isRunnerOrBatter(lineup.get(i))) {
            ((TextButton) findActor("Subs Lineup Num " + i)).setDisabled(false);
            ((TextButton) findActor("Subs Lineup Pos " + i)).setDisabled(false);
            ((TextButton) findActor("Subs Lineup Name " + i)).setDisabled(false);
         } else {
            ((TextButton) findActor("Subs Lineup Num " + i)).setDisabled(true);
            ((TextButton) findActor("Subs Lineup Pos " + i)).setDisabled(true);
            ((TextButton) findActor("Subs Lineup Name " + i)).setDisabled(true);
         }
      }
      for (int i = 0; i < bench.size(); i++) {
         ((TextButton) findActor("Subs Bench Pos " + i)).setDisabled(false);
         ((TextButton) findActor("Subs Bench Name " + i)).setDisabled(false);
      }
      ((TextButton) findActor("Subs Pitcher Throws")).setDisabled(false);
      ((TextButton) findActor("Subs Pitcher Name")).setDisabled(false);
      for (int i = 0; i < bullpen.size(); i++) {
         ((TextButton) findActor("Subs Bullpen Throws " + i)).setDisabled(false);
         ((TextButton) findActor("Subs Bullpen Name " + i)).setDisabled(false);
      }
   }

   private Card getSelected(boolean isLineupBench, Card card) {
      if (isLineupBench) {
         for (int i = 0; i < lineup.size(); i++) {
            if (((TextButton) findActor("Subs Lineup Name " + i)).isChecked() && !lineup.get(i).equals(card)) {
               return lineup.get(i);
            }
         }
         for (int i = 0; i < bench.size(); i++) {
            if (((TextButton) findActor("Subs Bench Name " + i)).isChecked() && !bench.get(i).equals(card)) {
               return bench.get(i);
            }
         }
      } else {
         if (((TextButton) findActor("Subs Pitcher Name")).isChecked() && !positions.get(1).equals(card)) {
            return positions.get(1);
         }
         for (int i = 0; i < bullpen.size(); i++) {
            if (((TextButton) findActor("Subs Bullpen Name " + i)).isChecked() && !bullpen.get(i).equals(card)) {
               return bullpen.get(i);
            }
         }
      }
      return null;
   }

   private void removeChecked(boolean isLineupBench) {
      if (isLineupBench) {
         for (int i = 0; i < lineup.size(); i++) {
            if (((TextButton) findActor("Subs Lineup Name " + i)).isChecked()) {
               ((TextButton) findActor("Subs Lineup Num " + i)).setChecked(false);
               ((TextButton) findActor("Subs Lineup Pos " + i)).setChecked(false);
               ((TextButton) findActor("Subs Lineup Name " + i)).setChecked(false);
            }
         }
         for (int i = 0; i < bench.size(); i++) {
            if (((TextButton) findActor("Subs Bench Name " + i)).isChecked()) {
               ((TextButton) findActor("Subs Bench Pos " + i)).setChecked(false);
               ((TextButton) findActor("Subs Bench Name " + i)).setChecked(false);
            }
         }
      } else {
         if (((TextButton) findActor("Subs Pitcher Name")).isChecked()) {
            ((TextButton) findActor("Subs Pitcher Throws")).setChecked(false);
            ((TextButton) findActor("Subs Pitcher Name")).setChecked(false);
         }
         for (int i = 0; i < bullpen.size(); i++) {
            if (((TextButton) findActor("Subs Bullpen Name " + i)).isChecked()) {
               ((TextButton) findActor("Subs Bullpen Throws " + i)).setChecked(false);
               ((TextButton) findActor("Subs Bullpen Name " + i)).setChecked(false);
            }
         }
      }
   }

   private void setChanged(String actorName, Integer index, boolean onlyPos) {
      if (actorName.contains("Pitcher") || actorName.contains("Bullpen")) {
         ((TextButton) findActor("Subs Pitcher Throws")).setStyle(game.sd.skin.get(actorName.contains("Pitcher") && !actorName.contains("Throws") ? "overchanged" : "changed", TextButtonStyle.class));
         ((TextButton) findActor("Subs Pitcher Name")).setStyle(game.sd.skin.get(actorName.contains("Pitcher") && !actorName.contains("Name") ? "overchanged" : "changed", TextButtonStyle.class));
      } else if (actorName.contains("Lineup") || actorName.contains("Bench")) {
         ((TextButton) findActor("Subs Lineup Pos " + index)).setStyle(game.sd.skin.get(actorName.contains("Lineup") && !actorName.contains("Pos") ? "overchanged" : "changed", TextButtonStyle.class));
         if (!onlyPos) {
            ((TextButton) findActor("Subs Lineup Num " + index)).setStyle(game.sd.skin.get(actorName.contains("Lineup") && !actorName.contains("Num") ? "overchanged" : "changed",
                  TextButtonStyle.class));
            ((TextButton) findActor("Subs Lineup Name " + index)).setStyle(game.sd.skin.get(actorName.contains("Lineup") && !actorName.contains("Name") ? "overchanged" : "changed",
                  TextButtonStyle.class));
         }
      }
   }

   private void setStyle(String actorName, boolean isEnter) {
      TextButton button = (TextButton) findActor(actorName);
      if (isEnter) {
         if (isChanged(button)) {
            button.setStyle(game.sd.skin.get("overchanged", TextButtonStyle.class));
         } else {
            button.setStyle(game.sd.skin.get("over", TextButtonStyle.class));
         }
      } else {
         if (isChanged(button)) {
            button.setStyle(game.sd.skin.get("changed", TextButtonStyle.class));
         } else {
            button.setStyle(game.sd.skin.get("subs", TextButtonStyle.class));
         }
      }
   }

   private boolean isRunnerOrBatter(Card card) {
      return (game.batter.card.equals(card) || lineup.indexOf(card) == team.lineup.indexOf(game.batter.card))
            || (game.runner1 != null && (game.runner1.card.equals(card) || lineup.indexOf(card) == team.lineup.indexOf(game.runner1.card)))
            || (game.runner2 != null && (game.runner2.card.equals(card) || lineup.indexOf(card) == team.lineup.indexOf(game.runner2.card)))
            || (game.runner3 != null && (game.runner3.card.equals(card) || lineup.indexOf(card) == team.lineup.indexOf(game.runner3.card)));
   }

   private boolean isChanged(TextButton button) {
      return button.getStyle().equals(game.sd.skin.get("overchanged", TextButtonStyle.class)) || 
            button.getStyle().equals(game.sd.skin.get("changed", TextButtonStyle.class));
   }

   private void validateSubstitutionChanges() {
      boolean isValidated = true;
      for (int i = 0; i < lineup.size(); i++) {
         Card card = lineup.get(i);
         String position = Team.getPosition(card, positions);
         if (!card.canPlayPosition(position)) {
            isValidated = isValidated && canFillAllPositions(card, position);
         }
      }
      ((TextButton) findActor("Accept Changes Button")).setDisabled(!isValidated);
   }
   
   private boolean canFillAllPositions(Card card, String position) {
      boolean validation = false;
      for (Card benchCard : bench) {
         if (benchCard.canPlayPosition(position)) {
            validation = true;
            break;
         }
      }
      if (!validation) {
         for (Card lineupCard : lineup) {
            if (lineupCard.canPlayPosition(position) && card.canPlayPosition(Team.getPosition(lineupCard, positions))) {
               validation = true;
               break;
            } else if (lineupCard.canPlayPosition(position)) {
               String position2 = Team.getPosition(lineupCard, positions);
               for (Card lineupCard2 : lineup) {
                  if (!lineupCard2.equals(lineupCard) && lineupCard2.canPlayPosition(position2) && card.canPlayPosition(Team.getPosition(lineupCard2, positions))) {
                     validation = true;
                     break;
                  }
               }
               if (validation) {
                  break;
               }
            }
         }
      }
      return validation;
   }
}
