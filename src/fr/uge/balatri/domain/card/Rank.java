package fr.uge.balatri.domain.card;

import java.util.Objects;

public enum Rank {
	TWO("2", 2), THREE("3", 3), FOUR("4", 4), FIVE("5", 5), SIX("6", 6), SEVEN("7", 7), EIGHT("8", 8), NINE("9", 9),
	TEN("10", 10), JACK("Valet", 11), QUEEN("Dame", 12), KING("Roi", 13), ACE("As", 14);

	private final String name;
	private final int value;

	Rank(String name, int value) {
		Objects.requireNonNull(name);
		if(value < 2) {
			throw new IllegalArgumentException("The value must be greater than or equal to 2");
		}
		this.name = name;
		this.value = value;
	}

	public int value() {
		return value;
	}

	@Override
	public String toString() {
		return name;
	}
}
