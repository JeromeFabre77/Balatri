package fr.uge.balatri.domain.card.suit;

public record Hearts() implements Suit {

	@Override
	public String toString() {
		return "♡";
	}
}
