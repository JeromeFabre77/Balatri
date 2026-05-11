package fr.uge.balatri;

import fr.uge.balatri.domain.card.Card;
import fr.uge.balatri.domain.card.rank.Numeric;
import fr.uge.balatri.domain.card.rank.Queen;
import fr.uge.balatri.domain.card.suit.Diamonds;
import fr.uge.balatri.domain.card.suit.Spades;

public class Main {

	static void main() {
		var numeric = new Numeric(5);
		var suit = new Diamonds();
		var card1 = new Card(numeric, suit);
		
		var queen = new Queen();
		var suit2 = new Spades();
		var card2 = new Card(queen, suit2);
		
		IO.println(card1);
		IO.println(card2);
	}
}
