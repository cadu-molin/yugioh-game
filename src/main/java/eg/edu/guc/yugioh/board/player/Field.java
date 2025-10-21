package eg.edu.guc.yugioh.board.player;

import eg.edu.guc.yugioh.cards.Card;
import eg.edu.guc.yugioh.cards.Location;
import eg.edu.guc.yugioh.cards.Mode;
import eg.edu.guc.yugioh.cards.MonsterCard;
import eg.edu.guc.yugioh.cards.monsterEffect.MonsterEffects;
import eg.edu.guc.yugioh.cards.spells.SpellCard;
import eg.edu.guc.yugioh.configsGlobais.GameConstants;
import eg.edu.guc.yugioh.configsGlobais.Logger;
import eg.edu.guc.yugioh.exceptions.IllegalSpellTargetException;
import eg.edu.guc.yugioh.exceptions.GameException;
import eg.edu.guc.yugioh.exceptions.UnexpectedFormatException;

import java.io.IOException;
import java.util.ArrayList;

public class Field {

	private Phase phase = Phase.MAIN1;
	private final Deck deck;
	private ArrayList<MonsterCard> monstersArea;
	private ArrayList<SpellCard> spellArea;
	private ArrayList<Card> hand;
	private ArrayList<Card> graveyard;

	public Field() throws IOException, UnexpectedFormatException {

		monstersArea = new ArrayList<MonsterCard>();
		spellArea = new ArrayList<SpellCard>();
		hand = new ArrayList<Card>();
		graveyard = new ArrayList<Card>();
		deck = new Deck();

	}

	public boolean addMonsterToField(MonsterCard monster, Mode m,
			boolean isHidden) {

		if (!(hand.contains(monster) && monster.getLocation() == Location.HAND))
			return false;

		if (monstersArea.size() >= GameConstants.MAX_MONSTERS_ON_FIELD)
			throw new GameException(GameException.Type.NO_MONSTER_SPACE);

		if (phase == Phase.BATTLE)
			throw new GameException(GameException.Type.WRONG_PHASE);

		hand.remove(monster);
		monster.setHidden(isHidden);
		monster.setMode(m);
		monster.setLocation(Location.FIELD);
		monstersArea.add(monster);
		return true;

	}

	public boolean addMonsterToField(MonsterCard monster, Mode m,
			ArrayList<MonsterCard> sacrifices) {

		if (!(hand.contains(monster) && monster.getLocation() == Location.HAND))
			return false;

		if (monster.getLevel() <= GameConstants.MAX_LEVEL_NO_SACRIFICE) {
			if (sacrifices != null)
				return false;
		} else if (monster.getLevel() <= GameConstants.MAX_LEVEL_ONE_SACRIFICE) {
			if (sacrifices.size() != GameConstants.ONE_SACRIFICE)
				return false;
		} else {
			if (sacrifices.size() != GameConstants.TWO_SACRIFICES)
				return false;
		}

		boolean hidden = (m == Mode.DEFENSE);

		boolean monsterAdded = addMonsterToField(monster, m, hidden);

		if (!monsterAdded)
			return false;

		if (sacrifices != null) {
			removeMonsterToGraveyard(sacrifices);
		}
		return true;

	}

	public void removeMonsterToGraveyard(MonsterCard monster) {

		if (monstersArea.contains(monster)) {

			monstersArea.remove(monster);
			graveyard.add(monster);
			monster.setLocation(Location.GRAVEYARD);

		}

	}

	public void removeMonsterToGraveyard(ArrayList<MonsterCard> monsters) {

		for (int i = 0; i < monsters.size(); i++)
			removeMonsterToGraveyard(monsters.get(i));

	}

	public boolean addSpellToField(SpellCard spell, MonsterCard monster,
			boolean hidden) throws IllegalSpellTargetException {

		if (!hand.contains(spell))
			return false;

		if (spellArea.size() >= GameConstants.MAX_SPELLS_ON_FIELD)
			throw new GameException(GameException.Type.NO_SPELL_SPACE);

		if (phase == Phase.BATTLE)
			throw new GameException(GameException.Type.WRONG_PHASE);

		hand.remove(spell);
		spellArea.add(spell);
		spell.setLocation(Location.FIELD);

		if (!hidden)
			return activateSetSpell(spell, monster);

		return true;

	}

	public boolean activateSetSpell(SpellCard spell, MonsterCard monster) throws IllegalSpellTargetException {

		if (!spellArea.contains(spell))
			return false;

		if (phase == Phase.BATTLE)
			throw new GameException(GameException.Type.WRONG_PHASE);

		spell.action(monster);
		removeSpellToGraveyard(spell);

		return true;

	}

	public void removeSpellToGraveyard(SpellCard spell) {

		if (!spellArea.contains(spell))
			return;

		spellArea.remove(spell);
		graveyard.add(spell);
		spell.setLocation(Location.GRAVEYARD);

	}

	public void removeSpellToGraveyard(ArrayList<SpellCard> spells) {

		for (int i = 0; i < spells.size(); i++) {

			SpellCard c = spells.get(i);

			if (!spellArea.contains(c))
				continue;

			spellArea.remove(c);
			graveyard.add(c);
			c.setLocation(Location.GRAVEYARD);

		}

	}

	public boolean declareAttack(MonsterCard m1, MonsterCard m2) {

		if (phase != Phase.BATTLE)
			throw new GameException(GameException.Type.WRONG_PHASE);

		if (m1.getMode() != Mode.ATTACK)
			throw new GameException(GameException.Type.DEFENSE_MONSTER_ATTACK);

		if (m1.isAttacked())
			throw new GameException(GameException.Type.MONSTER_MULTIPLE_ATTACK);

		ArrayList<MonsterCard> oppMonstersArea = Card.getBoard()
				.getOpponentPlayer().getField().monstersArea;

		if (m2 == null && oppMonstersArea.size() == 0) {
			m1.action();
		}
		else if (m2 != null && oppMonstersArea.contains(m2)) {
			MonsterEffects man =  new MonsterEffects(m2);
			if (m2.getName().equals("Man-Eater Bug")) {

				man.destroir(m1);
			}

			if(m2.getName().equals("Cyber Jar")){
				man.destroirAll();
			}
			m1.action(m2);
		}
		else
			return false;

		if (Card.getBoard().getActivePlayer().getLifePoints() <= 0) {
			Card.getBoard().getActivePlayer().setLifePoints(0);
			Card.getBoard().setWinner(Card.getBoard().getOpponentPlayer());
		}
		if (Card.getBoard().getOpponentPlayer().getLifePoints() <= 0) {
			Card.getBoard().getOpponentPlayer().setLifePoints(0);
			Card.getBoard().setWinner(Card.getBoard().getActivePlayer());
		}

		return true;

	}

	public void endPhase() {

		switch (phase) {

		case MAIN1:
			setPhase(Phase.BATTLE);
			break;

		case BATTLE:
			setPhase(Phase.MAIN2);
			break;

		case MAIN2:
			endTurn();
			break;

		}

	}

	public void endTurn() {

		phase = Phase.MAIN1;

		for (MonsterCard m : monstersArea) {
			m.setAttacked(false);
			m.setSwitchedMode(false);
		}

		Card.getBoard().nextPlayer();

	}

	public boolean switchMonsterMode(MonsterCard monster) {

		if (!monstersArea.contains(monster))
			return false;

		if (phase == Phase.BATTLE)
			throw new GameException(GameException.Type.WRONG_PHASE);

		if (monster.isSwitchedMode())
			return false;

		monster.switchMode();
		monster.setSwitchedMode(true);

		return true;

	}

	public void addCardToHand() {

		if (deck.getDeck().size() == 0) {

			if (this == Card.getBoard().getActivePlayer().getField())
				Card.getBoard().setWinner(Card.getBoard().getOpponentPlayer());
			else
				Card.getBoard().setWinner(Card.getBoard().getActivePlayer());

			return;
		}

		Card temp = deck.drawOneCard();
		hand.add(temp);
		temp.setLocation(Location.HAND);

	}

	public void addNCardsToHand(int n) {

		for (int j = 0; j < n; j++)
			addCardToHand();

	}

	public Phase getPhase() {
		return phase;
	}

	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	public Deck getDeck() {
		return deck;
	}

	public ArrayList<MonsterCard> getMonstersArea() {
		return monstersArea;
	}

	public ArrayList<SpellCard> getSpellArea() {
		return spellArea;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

	public ArrayList<Card> getGraveyard() {
		return graveyard;
	}

	public int discardHand() {

		Logger.logs().info("Field - discardHand" );

		int discardedCards = hand.size();
		for (int i = 0; i < hand.size();)
			graveyard.add(hand.remove(i));
		return (discardedCards);

	}

	public MonsterCard strongestMonsterInGraveyard() {

		MonsterCard strongest = new MonsterCard("", "", 0, 0, 0);
		int strongestValue = 0;
		for (int i = 0; i < graveyard.size(); i++) {

			if (graveyard.get(i) instanceof MonsterCard) {

				if (((MonsterCard) graveyard.get(i)).getAttackPoints() > strongestValue) {

					strongest = (MonsterCard) graveyard.get(i);
					strongestValue = ((MonsterCard) graveyard.get(i))
							.getAttackPoints();

				}

			}

		}

		Logger.logs().info("Field - discardHand strongest monster: " + strongest.getName() );

		return (strongest);

	}

}
