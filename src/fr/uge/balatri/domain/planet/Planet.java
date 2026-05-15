package fr.uge.balatri.domain.planet;

import java.util.Objects;
import java.util.Random;

import fr.uge.balatri.domain.combination.Combination;

public enum Planet {
	PLUTO("Pluton", Combination.HIGH_CARD, 10, 1), MERCURY("Mercure", Combination.PAIR, 15, 1),
	URANUS("Uranus", Combination.TWO_PAIR, 20, 1), VENUS("Vénus", Combination.THREE_OF_A_KIND, 20, 2),
	SATURN("Saturne", Combination.STRAIGHT, 30, 3), JUPITER("Jupiter", Combination.FLUSH, 15, 2),
	EARTH("Terre", Combination.FULL_HOUSE, 25, 2), MARS("Mars", Combination.FOUR_OF_A_KIND, 30, 3),
	NEPTUNE("Neptune", Combination.STRAIGHT_FLUSH, 40, 4);

	private final String name;
	private final Combination targetCombination;
	private final int bonusChips;
	private final int bonusMultiplier;

	Planet(String name, Combination targetCombination, int bonusChips, int bonusMultiplier) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(targetCombination);
		if (bonusChips < 1) {
			throw new IllegalArgumentException("The chips must be greater than or equal to 1");
		}
		if (bonusChips < 1) {
			throw new IllegalArgumentException("The multiplier must be greater than or equal to 1");
		}
		this.name = name;
		this.targetCombination = targetCombination;
		this.bonusChips = bonusChips;
		this.bonusMultiplier = bonusMultiplier;
	}

	public int bonusChips() {
		return bonusChips;
	}

	public int bonusMultiplier() {
		return bonusMultiplier;
	}

	public static Planet getPlanetByCombination(Combination combination) {
	    Objects.requireNonNull(combination);

	    for (var planet : Planet.values()) {
	        if (planet.targetCombination.equals(combination)) {
	            return planet;
	        }
	    }
	    
	    throw new IllegalArgumentException("No planet found for combination: " + combination);
	}
	
	public static Planet random() {
		var random = new Random();
		var planets = Planet.values();
		return planets[random.nextInt(planets.length)];
	}

	@Override
	public String toString() {
		return name;
	}
}