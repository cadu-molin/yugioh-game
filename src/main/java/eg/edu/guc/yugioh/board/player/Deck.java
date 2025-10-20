package eg.edu.guc.yugioh.board.player;

import eg.edu.guc.yugioh.cards.Card;
import eg.edu.guc.yugioh.cards.Location;
import eg.edu.guc.yugioh.cards.MonsterCard;
import eg.edu.guc.yugioh.cards.spells.CardDestruction;
import eg.edu.guc.yugioh.cards.spells.ChangeOfHeart;
import eg.edu.guc.yugioh.cards.spells.DarkHole;
import eg.edu.guc.yugioh.cards.spells.GracefulDice;
import eg.edu.guc.yugioh.cards.spells.HarpieFeatherDuster;
import eg.edu.guc.yugioh.cards.spells.HeavyStorm;
import eg.edu.guc.yugioh.cards.spells.MagePower;
import eg.edu.guc.yugioh.cards.spells.MonsterReborn;
import eg.edu.guc.yugioh.cards.spells.PotOfGreed;
import eg.edu.guc.yugioh.cards.spells.Raigeki;
import eg.edu.guc.yugioh.cards.spells.SpellCard;
import eg.edu.guc.yugioh.configsGlobais.Logger;
import eg.edu.guc.yugioh.exceptions.EmptyFieldException;
import eg.edu.guc.yugioh.exceptions.MissingFieldException;
import eg.edu.guc.yugioh.exceptions.UnexpectedFormatException;
import eg.edu.guc.yugioh.exceptions.UnknownCardTypeException;
import eg.edu.guc.yugioh.exceptions.UnknownSpellCardException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Deck {
	private static final int MONSTER_QUOTA = 25;
	private static final int MONSTER_SACRIFICE_QUOTA = 7;
	private static final int SPELL_QUOTA = 5;

	private static ArrayList<Card> monsters;

	private static ArrayList<Card> monstersSacrifices;
	private static ArrayList<Card> spells;

	private static String monstersPath = "Database-Monsters.csv";

	private static String monstersPathSacrifices = "Database-MonstersSacrifices.csv";

	private static String spellsPath = "Database-Spells.csv";

	private final ArrayList<Card> deck;
	int trials = 0;

	public Deck() throws IOException, NumberFormatException, UnexpectedFormatException {

		if ((monsters == null) || (spells == null) || (monstersSacrifices==null))  {

			Scanner sc = new Scanner(System.in);

			while (true) {
				try {
					monsters = loadCardsFromFile(Deck.getMonstersPath());
					monstersSacrifices = loadCardsFromFile(Deck.getMonstersPathSacrifices());
					spells = loadCardsFromFile(Deck.getSpellsPath());
					break;

				} catch (UnexpectedFormatException e) {
					if (trials >= 3) {
						sc.close();
						throw e;
					}

					System.out.println("Error in reading from file "
							+ e.getSourceFile() + " at line "
							+ e.getSourceLine());
					System.out.println(e.getMessage());
					System.out.println("Please enter another path:");

					trials++;

					if (e.getSourceFile().equalsIgnoreCase(
							Deck.getMonstersPath())) {
						Deck.setMonstersPath(sc.nextLine());
					}
					if (e.getSourceFile().equalsIgnoreCase(Deck.getMonstersPathSacrifices())){
						Deck.setMonstersPathSacrifices(sc.nextLine());
					}
					if (e.getSourceFile()
							.equalsIgnoreCase(Deck.getSpellsPath())) {
						Deck.setSpellsPath(sc.nextLine());
					}

				} catch (FileNotFoundException e) {
					if (trials >= 3) {
						sc.close();
						throw e;
					}

					String s = (monsters == null) ? Deck.getMonstersPath()
							: (monstersSacrifices == null) ? Deck.getMonstersPathSacrifices() :Deck.getSpellsPath();

					System.out.println("The file \"" + s + "\" is not found.");
					System.out.println("Please enter another path:");

					trials++;
					String path = sc.nextLine();

					if (monsters == null)
						Deck.setMonstersPath(path);
					else if(monstersSacrifices ==null)
						Deck.setMonstersPathSacrifices(path);
					else
						Deck.setSpellsPath(path);
				}
			}
			sc.close();
		}

		deck = new ArrayList<Card>();

		buildDeck(monsters,monstersSacrifices, spells);
		shuffleDeck();
	}

	private static void handleLoadFieldException(BufferedReader br, String message, String path, int lineNumber)
			throws IOException, MissingFieldException {

		Logger.logs().error("Deck - handleLoadFieldException: " + message + "lineNumber " + lineNumber );

		br.close();
		throw new MissingFieldException(message, path, lineNumber);
	}

	public ArrayList<Card> loadCardsFromFile(String path) throws IOException, UnexpectedFormatException {

		Logger.logs().info("Deck - loadCardsFromFile path: " + path );

		ArrayList<Card> temp = new ArrayList<Card>();
		String line;
		BufferedReader br = new BufferedReader(new FileReader(path));

		int lineNumber = 0;

		while ((line = br.readLine()) != null) {

			lineNumber++;
			String[] cardInfo = line.split(",");

			if (cardInfo.length == 0) {
				handleLoadFieldException(br, "No fields available.", path, lineNumber);
			} else if (cardInfo[0].equalsIgnoreCase("Monster") && cardInfo.length != 6) {
				handleLoadFieldException(br, "Monster fields in the line did not match the expected.", path, lineNumber);
			} else if (cardInfo[0].equalsIgnoreCase("Spell") && cardInfo.length != 3) {
				handleLoadFieldException(br, "Spell fields in the line did not match the expected.", path, lineNumber);
			}

			for (int i = 0; i < cardInfo.length; i++)
				if (cardInfo[i].equals("") || cardInfo[i].equals(" ")) {
					br.close();
					throw new EmptyFieldException("Empty Field.", path,
							lineNumber, i + 1);
				}

			if (cardInfo[0].equalsIgnoreCase("Monster")) {
				temp.add(new MonsterCard(cardInfo[1], cardInfo[2], Integer
						.parseInt(cardInfo[5]), Integer.parseInt(cardInfo[3]),
						Integer.parseInt(cardInfo[4])));
			} else {
				if (!cardInfo[0].equalsIgnoreCase("Spell")) {
					br.close();
					throw new UnknownCardTypeException("Unknown Card type.",
							path, lineNumber, cardInfo[0]);
				}

				temp.add(SimpleSpellFactory.createSpell(cardInfo[1], cardInfo[2], path, lineNumber));
			}
		}

		br.close();
		return (temp);
	}

	private void addMonsterDeck(MonsterCard monster){
		MonsterCard clone = new MonsterCard(monster.getName(), monster.getDescription(), monster.getLevel(), monster.getAttackPoints(), monster.getDefensePoints());
		clone.setMode(monster.getMode());
		clone.setHidden(monster.isHidden());
		addClone(clone);
	}

	private void addClone(Card clone){
		clone.setLocation(Location.DECK);
		deck.add(clone);
	}

	private void addSpellDeck(SpellCard clone){
		addClone(clone);
	}

	private void buildDeck(ArrayList<Card> Monsters,ArrayList<Card> MonstersSacrifices, ArrayList<Card> Spells) {

		Logger.logs().info("Deck - buildDeck monstersSize: " + Monsters.size() + "Deck - buildDeck monstersSacrificesSize: " + MonstersSacrifices.size() + "spellsSize: " + Spells.size() );

		addRandomMonsters(monsters, MONSTER_QUOTA);
		addRandomMonsters(monstersSacrifices, MONSTER_SACRIFICE_QUOTA);
		addRandomSpells(spells, SPELL_QUOTA);
	}

	private void addRandomMonsters(ArrayList<Card> source, int quantity) {
		Random r = new Random();
		for (int i = 0; i < quantity; i++) {
			MonsterCard monster = (MonsterCard) source.get(r.nextInt(source.size()));
			addMonsterDeck(monster);
		}
	}

	private void addRandomSpells(ArrayList<Card> source, int quantity) {
		Random r = new Random();

		for (int i = 0; i < quantity; i++) {
			Card spell = source.get(r.nextInt(source.size()));

			try {
				SpellCard clone = SimpleSpellFactory.createSpell(spell.getName(), spell.getDescription(), "", 0);

				addSpellDeck(clone);
			} catch (UnknownSpellCardException e) {
				continue;
			}

		}
	}

	private void shuffleDeck() {

		Collections.shuffle(deck);

	}

	public ArrayList<Card> drawNCards(int quantidade) {

		Logger.logs().info("Deck - drawNCards quantidade: " + quantidade );

		ArrayList<Card> cards = new ArrayList<Card>(quantidade);

		for (int i = 0; i < quantidade; i++)
			cards.add(deck.remove(0));

		return (cards);

	}

	public Card drawOneCard() {

		Logger.logs().info("Deck - drawOneCard" );

		return (deck.remove(0));

	}

	public static ArrayList<Card> getMonsters() {
		return monsters;
	}

	public static void setMonsters(ArrayList<Card> monsters) {
		Deck.monsters = monsters;
	}

	public static ArrayList<Card> getSpells() {
		return spells;
	}

	public static void setSpells(ArrayList<Card> spells) {
		Deck.spells = spells;
	}

	public ArrayList<Card> getDeck() {
		return deck;
	}

	public static String getMonstersPath() {
		return monstersPath;
	}

	public static void setMonstersPath(String monstersPath) {
		Deck.monstersPath = monstersPath;
	}

	public static String getSpellsPath() {
		return spellsPath;
	}

	public static void setSpellsPath(String spellsPath) {
		Deck.spellsPath = spellsPath;
	}

	public static String getMonstersPathSacrifices() {
		return monstersPathSacrifices;
	}

	public static void setMonstersPathSacrifices(String monstersPathSacrifices) {
		Deck.monstersPathSacrifices = monstersPathSacrifices;
	}
}
