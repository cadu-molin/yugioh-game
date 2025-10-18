package eg.edu.guc.yugioh.board.player;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import eg.edu.guc.yugioh.cards.Card;
import eg.edu.guc.yugioh.cards.Mode;
import eg.edu.guc.yugioh.cards.MonsterCard;
import eg.edu.guc.yugioh.cards.spells.SpellCard;
import eg.edu.guc.yugioh.configsGlobais.Logger;
import eg.edu.guc.yugioh.exceptions.IllegalSpellTargetException;
import eg.edu.guc.yugioh.exceptions.MultipleMonsterAdditionException;
import eg.edu.guc.yugioh.exceptions.UnexpectedFormatException;

public class Player implements Duelist {

	private final String name;
	private int lifePoints;
	private final Field field;
	private boolean addedMonsterThisTurn;
	private Color colorHud;
	private String imagePath;

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Color getColorHud() {
		return colorHud;
	}

	public void setColorHud(Color colorHud) {
		this.colorHud = colorHud;
	}

	public Player(String name, Color colorHud, String imagePath ) throws IOException, UnexpectedFormatException {

		this.name = name;
		this.lifePoints = 8000;
		this.field = new Field();
		this.colorHud = colorHud;
		this.imagePath = imagePath;
		addedMonsterThisTurn = false;

	}

	private boolean performMonsterPlacement(MonsterCard monster, ArrayList<MonsterCard> sacrifices, Mode mode, boolean isSet) {
		if (Card.getBoard().isGameOver())
			return false;

		if (this != Card.getBoard().getActivePlayer())
			return false;

		if (addedMonsterThisTurn)
			throw new MultipleMonsterAdditionException();

		boolean monsterAdded = (sacrifices == null)
				? this.field.addMonsterToField(monster, mode, isSet)
				: this.field.addMonsterToField(monster, mode, sacrifices);

		if (!monsterAdded)
			return false;

		addedMonsterThisTurn = true;
		return true;
	}

	@Override
	public boolean summonMonster(MonsterCard monster) {

		Logger.logs().info("Player - summonMonster monster name: " + monster.getName() );
		return performMonsterPlacement(monster, null, Mode.ATTACK, false);
	}

	@Override
	public boolean summonMonster(MonsterCard monster,
			ArrayList<MonsterCard> sacrifices) {

		Logger.logs().info("Player - summonMonster monster name: " + monster.getName() + "sacrifices: " + sacrifices.size());
		return performMonsterPlacement(monster, sacrifices, Mode.ATTACK, false);
	}

	@Override
	public boolean setMonster(MonsterCard monster) {

		Logger.logs().info("Player - setMonster monster name: " + monster.getName() );
		return performMonsterPlacement(monster, null, Mode.DEFENSE, true);
	}

	@Override
	public boolean setMonster(MonsterCard monster,
			ArrayList<MonsterCard> sacrifices) {

		Logger.logs().info("Player - setMonster monster name: " + monster.getName() + " " + "sacrifices: " + sacrifices.size());
		return performMonsterPlacement(monster, sacrifices, Mode.DEFENSE, true);
	}

	@Override
	public boolean setSpell(SpellCard spell) throws IllegalSpellTargetException {

		Logger.logs().info("Player - setSpell spell name: " + spell.getName() );

		if (Card.getBoard().isGameOver())
			return false;

		if (this != Card.getBoard().getActivePlayer())
			return false;

		boolean spellAdded = this.field.addSpellToField(spell, null, true);

		return spellAdded;

	}

	@Override
	public boolean activateSpell(SpellCard spell, MonsterCard monster) throws IllegalSpellTargetException {

		Logger.logs().info("Player - activateSpell spell name: " + spell.getName() + " " + "monster: " + monster );

		if (Card.getBoard().isGameOver())
			return false;

		if (this != Card.getBoard().getActivePlayer())
			return false;

		boolean spellActivated;

		if (this.field.getSpellArea().contains(spell))
			spellActivated = this.field.activateSetSpell(spell, monster);
		else
			spellActivated = this.field.addSpellToField(spell, monster, false);

		return spellActivated;

	}

	@Override
	public boolean declareAttack(MonsterCard monster) {

		Logger.logs().info("Player - declareAttack monster name: " + monster.getName() );

		if (Card.getBoard().isGameOver())
			return false;

		if (this != Card.getBoard().getActivePlayer())
			return false;

		boolean monsterAttacked = this.field.declareAttack(monster, null);

		return monsterAttacked;

	}

	@Override
	public boolean declareAttack(MonsterCard activeMonster,
			MonsterCard opponentMonster) {

		Logger.logs().info("Player - declareAttack activeMonster name: " + activeMonster.getName() + " " + "opponentMonster name: " + opponentMonster.getName() );

		if (Card.getBoard().isGameOver())
			return false;

		if (this != Card.getBoard().getActivePlayer())
			return false;

		boolean monsterAttacked = this.field.declareAttack(activeMonster,
				opponentMonster);

		return monsterAttacked;

	}

	@Override
	public void endPhase() {

		if (Card.getBoard().isGameOver())
			return;

		if (this != Card.getBoard().getActivePlayer())
			return;

		this.getField().endPhase();

	}

	@Override
	public boolean endTurn() {

		if (Card.getBoard().isGameOver())
			return false;

		if (this != Card.getBoard().getActivePlayer())
			return false;

		addedMonsterThisTurn = false;
		this.getField().endTurn();

		return true;

	}

	@Override
	public boolean switchMonsterMode(MonsterCard monster) {

		Logger.logs().info("Player - switchMonsterMode monster name: " + monster.getName() );

		if (Card.getBoard().isGameOver())
			return false;

		if (this != Card.getBoard().getActivePlayer())
			return false;

		boolean monsterSwitched = this.field.switchMonsterMode(monster);

		return monsterSwitched;

	}

	@Override
	public void addCardToHand() {

		this.field.addCardToHand();

	}

	@Override
	public void addNCardsToHand(int n) {

		this.field.addNCardsToHand(n);

	}

	public int getLifePoints() {
		return lifePoints;
	}

	public void setLifePoints(int lifePoints) {
		this.lifePoints = lifePoints;
	}

	public void takeDamage(int damage){

		Logger.logs().info("Player - takeDamage Damage: " + damage );

		int lp = getLifePoints();
		setLifePoints(lp - damage);

		Logger.logs().info("Player - takeDamage Lifepoints: " + getLifePoints() );

		playDamageSong();

	}

	private void playDamageSong(){

		Logger.logs().info("Player - playDamageSong");

		String sourceSong;
		if(getLifePoints() <=0 ) {
			sourceSong = "src/main/resources/audios/gritoplayerlose.wav";
		} else {
			sourceSong = "src/main/resources/audios/gritoplayer.wav";
		}

		File musicPath = new File(sourceSong);

		try{
			AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInput);
			clip.start();
		} catch ( Exception e){

			Logger.logs().error("Player - Exception: " + e + " " + musicPath.getName() );		}
	}

	public String getName() {
		return name;
	}

	public Field getField() {
		return field;
	}

}
