package fr.uge.balatri.domain.hand;

import fr.uge.balatri.domain.card.Card;

import module java.base;

public final class PlayerHand {

	public static final int MAX_HAND_SIZE = 8;

	public static final int MAX_SELECTED_SIZE = 5;

	private final List<Card> cards;

	public PlayerHand(List<Card> newCards) {
		Objects.requireNonNull(newCards);

		if (newCards.size() != MAX_HAND_SIZE) {
			throw new IllegalArgumentException(
					"A hand must contain " + MAX_HAND_SIZE + " cards, but got " + newCards.size());
		}

		this.cards = new ArrayList<Card>(newCards);
	}

	public List<Card> get() {
		return cards;
	}

	public List<Card> selectCards(HashSet<Integer> selectedCards) {
		Objects.requireNonNull(selectedCards);

		validateSelection(selectedCards);

		return selectedCards.stream().map(cards::get).toList();
	}

	private void validateSelection(HashSet<Integer> selectedCards) {
		if (selectedCards.size() < 1 || selectedCards.size() > MAX_SELECTED_SIZE) {
			throw new IllegalArgumentException(
					"Must select between 1 and " + MAX_SELECTED_SIZE + " cards, but got " + selectedCards.size());
		}

		for (var index : selectedCards) {
			if (index < 0 || index >= cards.size()) {
				throw new IllegalArgumentException("Selected card index out of bounds: " + index);
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