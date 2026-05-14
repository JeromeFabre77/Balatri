package fr.uge.balatri.domain.combination;

import java.util.Map;
import java.util.Objects;

import fr.uge.balatri.domain.planet.Planet;

public enum Combination {
	HIGH_CARD("Carte haute", 5, 1), PAIR("Paire", 10, 2), TWO_PAIR("Double paire", 20, 2),
	THREE_OF_A_KIND("Brelan", 30, 3), STRAIGHT("Suite", 30, 4), FLUSH("Couleur", 35, 4), FULL_HOUSE("Full", 40, 4),
	FOUR_OF_A_KIND("Carré", 60, 7), STRAIGHT_FLUSH("Quinte flush", 100, 8);

	private final String name;
	private final int chips;
	private final int multiplier;

	Combination(String name, int chips, int multiplier) {
		Objects.requireNonNull(name);
		if(chips < 1) {
			throw new IllegalArgumentException("The chips must be greater than or equal to 1");
		}
		if(multiplier < 1) {
			throw new IllegalArgumentException("The multiplier must be greater than or equal to 1");
		}
		this.name = name;
		this.chips = chips;
		this.multiplier = multiplier;
	}

	public int chips() {
		return chips;
	}

	public int multiplier() {
		return multiplier;
	}

	public int score(Map<Planet, Integer> planets) {
		Objects.requireNonNull(planets);

	    var planet = Planet.getPlanetByCombination(this);
	    var level = planets.getOrDefault(planet, 0);

	    var finalChips = chips + planet.bonusChips() * level;
	    var finalMultiplier = multiplier + planet.bonusMultiplier() * level;
	    return finalChips * finalMultiplier;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
