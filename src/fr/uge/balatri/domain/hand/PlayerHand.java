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

		this.cards = new ArrayList<>(newCards);
	}

	public List<Card> get() {
		return List.copyOf(cards);
	}

	public void clear() {
		cards.clear();
	}

	public int missingCards() {
		return MAX_HAND_SIZE - cards.size();
	}

	public List<Card> selectCards(Set<Integer> selectedIndices) {
		Objects.requireNonNull(selectedIndices);

		validateSelection(selectedIndices);

		var selected = selectedIndices.stream().sorted().map(cards::get).toList();

		selectedIndices.stream().sorted(Comparator.reverseOrder()).forEach(index -> cards.remove((int) index));

		return selected;
	}

	public void refill(List<Card> newCards) {
		Objects.requireNonNull(newCards);

		if (newCards.size() != missingCards()) {
			throw new IllegalArgumentException(
					"Must refill exactly " + missingCards() + " cards, but got " + newCards.size());
		}

		cards.addAll(newCards);
	}

	private void validateSelection(Set<Integer> selectedIndices) {
		if (selectedIndices.size() < 1 || selectedIndices.size() > MAX_SELECTED_SIZE) {
			throw new IllegalArgumentException(
					"Must select between 1 and " + MAX_SELECTED_SIZE + " cards, but got " + selectedIndices.size());
		}

		for (var index : selectedIndices) {
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