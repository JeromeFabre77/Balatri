package fr.uge.balatri.domain.deck;

import fr.uge.balatri.domain.card.*;

import module java.base;

public final class Deck {

	private final List<Card> cards;

	public Deck() {
		this.cards = new ArrayList<>();
		for (var suit : Suit.values()) {
			for (var rank : Rank.values()) {
				cards.add(new Card(rank, suit));
			}
		}

		Collections.shuffle(cards);
	}

	public List<Card> draw(int count) {
		if (count < 0) {
			throw new IllegalArgumentException("Count must be non-negative");
		}
		if (count > cards.size()) {
			throw new IllegalArgumentException(
					"Not enough cards in deck: " + cards.size() + " available, " + count + " requested");
		}

		var drawnCards = new ArrayList<Card>(count);
		for (var i = 0; i < count; i++) {
			drawnCards.add(cards.removeLast());
		}
		return List.copyOf(drawnCards);
	}

	public boolean hasEnoughCards(int count) {
		return cards.size() >= count;
	}

	public int size() {
		return cards.size();
	}

	public boolean isEmpty() {
		return cards.isEmpty();
	}

	public void refillFrom(List<Card> newCards) {
		Objects.requireNonNull(newCards);

		cards.addAll(newCards);
		Collections.shuffle(cards);
	}

	@Override
	public String toString() {
		return "Deck : " + cards.size() + " cartes restantes";
	}
}
