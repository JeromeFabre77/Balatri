package fr.uge.balatri.domain.card;

import java.util.Objects;

import fr.uge.balatri.domain.card.rank.Rank;
import fr.uge.balatri.domain.card.suit.Suit;

public record Card(Rank rank, Suit suit) {

	public Card{
		Objects.requireNonNull(rank);
		Objects.requireNonNull(suit);
	}
	
	@Override
	public String toString() {
		return rank.toString() + " " + suit.toString();
	}
}
