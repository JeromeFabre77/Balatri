package fr.uge.balatri.domain.handevaluator;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import fr.uge.balatri.domain.card.Card;
import fr.uge.balatri.domain.card.Rank;
import fr.uge.balatri.domain.card.Suit;
import fr.uge.balatri.domain.combination.Combination;

public final class HandEvaluator {
	private HandEvaluator() {
	}

	public static Combination evaluate(List<Card> cards) {
		Objects.requireNonNull(cards);

		if (cards.isEmpty() || cards.size() > 5) {
			throw new IllegalArgumentException("A hand must contain between 1 and 5 cards");
		}

		var rankCounts = cards.stream().collect(Collectors.groupingBy(Card::rank, Collectors.counting()));

		return switch (cards.size()) {
		case 1 -> Combination.HIGH_CARD;

		case 2 -> {
			if (hasCount(rankCounts, 2)) {
				yield Combination.PAIR;
			}
			yield Combination.HIGH_CARD;
		}

		case 3 -> {
			if (hasCount(rankCounts, 3)) {
				yield Combination.THREE_OF_A_KIND;
			}
			if (hasCount(rankCounts, 2)) {
				yield Combination.PAIR;
			}
			yield Combination.HIGH_CARD;
		}

		case 4 -> {
			if (hasCount(rankCounts, 4)) {
				yield Combination.FOUR_OF_A_KIND;
			}
			if (hasCount(rankCounts, 3)) {
				yield Combination.THREE_OF_A_KIND;
			}
			if (pairCount(rankCounts) == 2) {
				yield Combination.TWO_PAIR;
			}
			if (pairCount(rankCounts) == 1) {
				yield Combination.PAIR;
			}
			yield Combination.HIGH_CARD;
		}

		case 5 -> evaluateFiveCards(cards, rankCounts);

		default -> throw new AssertionError("Unexpected hand size");
		};
	}

	private static Combination evaluateFiveCards(List<Card> cards, Map<Rank, Long> rankCounts) {
		var suitCounts = cards.stream().collect(Collectors.groupingBy(Card::suit, Collectors.counting()));

		var isFlush = isFlush(suitCounts);
		var isStraight = isStraight(cards);

		if (isStraight && isFlush) {
			return Combination.STRAIGHT_FLUSH;
		}

		if (hasCount(rankCounts, 4)) {
			return Combination.FOUR_OF_A_KIND;
		}

		if (hasCount(rankCounts, 3) && hasCount(rankCounts, 2)) {
			return Combination.FULL_HOUSE;
		}

		if (isFlush) {
			return Combination.FLUSH;
		}

		if (isStraight) {
			return Combination.STRAIGHT;
		}

		if (hasCount(rankCounts, 3)) {
			return Combination.THREE_OF_A_KIND;
		}

		if (pairCount(rankCounts) == 2) {
			return Combination.TWO_PAIR;
		}

		if (pairCount(rankCounts) == 1) {
			return Combination.PAIR;
		}

		return Combination.HIGH_CARD;
	}

	private static boolean isFlush(Map<Suit, Long> suitCounts) {
		return suitCounts.containsValue(5L);
	}

	private static boolean hasCount(Map<Rank, Long> rankCounts, long count) {
		return rankCounts.containsValue(count);
	}

	private static long pairCount(Map<Rank, Long> rankCounts) {
		return rankCounts.values().stream().filter(count -> count == 2).count();
	}

	private static boolean isStraight(List<Card> cards) {
		var values = cards.stream().map(card -> card.rank().value()).sorted().toList();

		if (values.equals(List.of(2, 3, 4, 5, 14))) {
			return true;
		}

		for (var i = 0; i < values.size() - 1; i++) {
			if (values.get(i) + 1 != values.get(i + 1)) {
				return false;
			}
		}

		return true;
	}
}