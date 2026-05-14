package fr.uge.balatri.domain.card;

import java.util.Objects;

public enum Suit {
	CLUBS("Trèfle"), DIAMONDS("Carreau"), HEARTS("Cœur"), SPADES("Pique");

	private final String name;

	Suit(String name) {
		Objects.requireNonNull(name);
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}