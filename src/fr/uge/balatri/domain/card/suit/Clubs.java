package fr.uge.balatri.domain.card.suit;

public record Clubs() implements Suit {

	@Override
	public String toString() {
		return "♧";
	}
}
