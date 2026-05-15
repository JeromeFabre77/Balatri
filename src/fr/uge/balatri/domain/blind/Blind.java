package fr.uge.balatri.domain.blind;

import java.util.Objects;

public record Blind(String name, int score) {
	public Blind {
		Objects.requireNonNull(name);


		if (score < 1) {
			throw new IllegalArgumentException("The score must be greater than or equal to 1");
		}
	}
}