package fr.uge.balatri.domain.deck;

import fr.uge.balatri.domain.card.Card;

import module java.base;

public final class Discard {

	private final List<Card> cards;

	public Discard() {
		this.cards = new ArrayList<>();
	}

	public void add(List<Card> newCards) {
		Objects.requireNonNull(newCards);
		cards.addAll(newCards);
	}

	public List<Card> drawAll() {
		var drawnCards = List.copyOf(cards);
		cards.clear();
		return drawnCards;
	}

	public int size() {
		return cards.size();
	}

	public boolean isEmpty() {
		return cards.isEmpty();
	}

	@Override
	public String toString() {
		return "Discard : " + cards.size() + " cartes";
	}
}
