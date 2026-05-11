package fr.uge.balatri.domain.card.suit;

public record Diamonds() implements Suit {

	@Override
	public String toString() {
		return "♢";
	}
}
