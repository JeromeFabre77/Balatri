package fr.uge.balatri.domain.card.suit;

public record Spades() implements Suit {
	
	@Override
	public String toString() {
		return "♤";
	}

}
