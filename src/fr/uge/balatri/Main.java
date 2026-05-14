package fr.uge.balatri;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.uge.balatri.domain.card.Card;
import fr.uge.balatri.domain.card.Rank;
import fr.uge.balatri.domain.card.Suit;
import fr.uge.balatri.domain.handevaluator.HandEvaluator;
import fr.uge.balatri.domain.planet.Planet;

public class Main {
	public static void main(String[] args) {
		var planets = new HashMap<Planet, Integer>();

		planets.put(Planet.MERCURY, 2);
		planets.put(Planet.NEPTUNE, 1);
		planets.put(Planet.SATURN, 3);

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
}