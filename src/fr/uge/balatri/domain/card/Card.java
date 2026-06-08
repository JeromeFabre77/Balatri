package fr.uge.balatri.domain.card;

import java.util.List;
import java.util.Objects;

public record Card(Rank rank, Suit suit) {

	public Card {
		Objects.requireNonNull(rank);
		Objects.requireNonNull(suit);
	}

	public static int computeChips(List<Card> cards) {
		return cards.stream().mapToInt(card -> card.rank.value()).sum();
	}

	@Override
	public String toString() {
		return rank.toString() + " de " + suit.toString();
	}
}
