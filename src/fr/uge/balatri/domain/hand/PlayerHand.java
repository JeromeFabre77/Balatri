package fr.uge.balatri.domain.hand;

import fr.uge.balatri.domain.card.Card;

import module java.base;

public final class PlayerHand {

	public static final int HAND_SIZE = 8;

	public static final int SELECTED_SIZE = 5;

	private final List<Card> cards;

	public PlayerHand(List<Card> newCards) {
		Objects.requireNonNull(newCards);

		if (newCards.size() != HAND_SIZE) {
			throw new IllegalArgumentException(
					"A hand must contain " + HAND_SIZE + " cards, but got " + newCards.size());
		}

		this.cards = List.copyOf(newCards);
	}

	public List<Card> getCards() {
		return cards;
	}

	public List<Card> selectCards(List<Integer> selectedCards) {
		Objects.requireNonNull(selectedCards);

		validateSelection(selectedCards);

		return selectedCards.stream().map(cards::get).toList();
	}

	public List<Card> discardRemainingCards(List<Integer> selectedCards) {
		Objects.requireNonNull(selectedCards);

		validateSelection(selectedCards);

		var selectedSet = new HashSet<>(selectedCards);
		var remainingCards = new ArrayList<Card>();
		for (int i = 0; i < cards.size(); i++) {
			if (!selectedSet.contains(i)) {
				remainingCards.add(cards.get(i));
			}
		}

		return List.copyOf(remainingCards);
	}

	private void validateSelection(List<Integer> selectedCards) {
		if (selectedCards.size() != SELECTED_SIZE) {
			throw new IllegalArgumentException(
					"Exactly " + SELECTED_SIZE + " cards must be selected, but got " + selectedCards.size());
		}

		var seen = new HashSet<Integer>();
		for (var index : selectedCards) {
			if (index < 0 || index >= HAND_SIZE) {
				throw new IllegalArgumentException("Selected card index out of bounds: " + index);
			}
			if (!seen.add(index)) {
				throw new IllegalArgumentException("Duplicate card index selected: " + index);
			}
		}
	}

	@Override
	public String toString() {
		var sb = new StringBuilder();
		for (int i = 0; i < cards.size(); i++) {
			sb.append(i).append(": ").append(cards.get(i)).append("\n");
		}
		return sb.toString();
	}

}