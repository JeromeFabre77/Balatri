package fr.uge.balatri;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.uge.balatri.domain.card.Card;
import fr.uge.balatri.domain.card.Rank;
import fr.uge.balatri.domain.card.Suit;
import fr.uge.balatri.domain.handevaluator.HandEvaluator;
import fr.uge.balatri.domain.planet.Planet;
import fr.uge.balatri.domain.deck.*;
import fr.uge.balatri.domain.hand.PlayerHand;

public class Main {
	public static void main(String[] args) {
		var planets = new HashMap<Planet, Integer>();

		planets.put(Planet.MERCURY, 2);
		planets.put(Planet.NEPTUNE, 1);
		planets.put(Planet.SATURN, 3);
		var planet = Planet.random();
		IO.println("Planete générée aléatoirement : " + planet + System.lineSeparator());
		planets.merge(planet, 1, Integer::sum);

		var pairHand = List.of(new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.ACE, Suit.SPADES));

		var straightFlushHand = List.of(new Card(Rank.TEN, Suit.HEARTS), new Card(Rank.JACK, Suit.HEARTS),
				new Card(Rank.QUEEN, Suit.HEARTS), new Card(Rank.KING, Suit.HEARTS), new Card(Rank.ACE, Suit.HEARTS));

		var straightHand = List.of(new Card(Rank.SEVEN, Suit.CLUBS), new Card(Rank.EIGHT, Suit.HEARTS),
				new Card(Rank.NINE, Suit.DIAMONDS), new Card(Rank.TEN, Suit.SPADES), new Card(Rank.JACK, Suit.CLUBS));

		var lowAceStraightHand = List.of(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.TWO, Suit.HEARTS),
				new Card(Rank.THREE, Suit.DIAMONDS), new Card(Rank.FOUR, Suit.SPADES), new Card(Rank.FIVE, Suit.CLUBS));

		var highAceStraightHand = List.of(new Card(Rank.TEN, Suit.CLUBS), new Card(Rank.JACK, Suit.HEARTS),
				new Card(Rank.QUEEN, Suit.DIAMONDS), new Card(Rank.KING, Suit.SPADES), new Card(Rank.ACE, Suit.CLUBS));

		testHand("Paire avec Mercure niveau 2", pairHand, planets);
		testHand("Quinte flush avec Neptune niveau 1", straightFlushHand, planets);
		testHand("Suite avec Saturne niveau 3", straightHand, planets);
		testHand("Suite As basse", lowAceStraightHand, planets);
		testHand("Suite As haute", highAceStraightHand, planets);

		testDeck();
		testDiscard();
		testDeckAndDiscard();
		testPlayerHand();
	}

	private static void testHand(String title, List<Card> hand, Map<Planet, Integer> planets) {
		var combination = HandEvaluator.evaluate(hand);
		var score = combination.score(planets);

		IO.println("----- " + title + " -----");
		IO.println("Main : " + hand);
		IO.println("Combinaison : " + combination);
		IO.println("Chips de base : " + combination.chips());
		IO.println("Multiplicateur de base : " + combination.multiplier());
		IO.println("Score final : " + score + System.lineSeparator());
	}

	private static void testDeck() {
		var deck = new Deck();
		IO.println("Taille initale du deck : " + deck.size());

		var drawnCards = deck.draw(8);
		IO.println("Cartes tirées : " + drawnCards.size());
		IO.println("Taille du deck après tirage : " + deck.size());

		IO.println("A assez de cartes pour tirer 44 ? " + deck.hasEnoughCards(44));
		IO.println("A assez de cartes pour tirer 45 ? " + deck.hasEnoughCards(45));

		deck.draw(44);
		IO.println("Taille du deck après tirage de 44 cartes : " + deck.size() + System.lineSeparator());
	}

	static void testDiscard() {
		var discard = new Discard();
		IO.println("Taille initiale du discard : " + discard.size());

		var cardsToAdd = List.of(new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.KING, Suit.SPADES),
				new Card(Rank.QUEEN, Suit.DIAMONDS));
		discard.add(cardsToAdd);
		IO.println("Taille du discard après ajout de 3 cartes : " + discard.size());

		var cardsToAdd2 = List.of(new Card(Rank.JACK, Suit.CLUBS), new Card(Rank.TEN, Suit.HEARTS));
		discard.add(cardsToAdd2);
		IO.println("Taille du discard après ajout de 2 cartes : " + discard.size());

		var drawnCards = discard.drawAll();
		IO.println("Cartes tirées du discard : " + drawnCards.size());
		IO.println("Est ce vide ? : " + discard.isEmpty() + System.lineSeparator());
	}

	static void testDeckAndDiscard() {
		var deck = new Deck();
		var discard = new Discard();

		IO.println("Taille initiale du deck : " + deck.size());
		IO.println("Taille initiale du discard : " + discard.size());

		var drawnCards = deck.draw(50);
		discard.add(drawnCards);
		IO.println("Taille du deck après tirage de 50 cartes : " + deck.size());
		IO.println("Taille du discard après ajout de 50 cartes : " + discard.size());

		if (!deck.hasEnoughCards(PlayerHand.HAND_SIZE)) {
			deck.refillFrom(discard.drawAll());
		}

		IO.println("Taille du deck après refill : " + deck.size());
		IO.println("Taille du discard après refill : " + discard.size() + System.lineSeparator());
	}

	static void testPlayerHand() {
		var deck = new Deck();
		var hand = new PlayerHand(deck.draw(PlayerHand.HAND_SIZE));

		IO.println("Main du joueur : " + hand.getCards());
		IO.println(hand.toString());

		var selectedIndices = List.of(0, 1, 2, 3, 4);
		var selectedCards = hand.selectCards(selectedIndices);
		var discardedCards = hand.discardRemainingCards(selectedIndices);

		IO.println("Cartes sélectionnées : " + selectedCards);
		IO.println("Cartes à défausser : " + discardedCards);

		try {
			hand.selectCards(List.of(0, 1, 2));
		} catch (IllegalArgumentException e) {
			IO.println("Erreur attendue : " + e.getMessage());
		}

		try {
			hand.selectCards(List.of(0, 1, 2, 3, 8));
		} catch (IllegalArgumentException e) {
			IO.println("Erreur attendue : " + e.getMessage());
		}

		try {
			hand.selectCards(List.of(0, 1, 2, 3, 3));
		} catch (IllegalArgumentException e) {
			IO.println("Erreur attendue : " + e.getMessage() + System.lineSeparator());
		}
	}
}