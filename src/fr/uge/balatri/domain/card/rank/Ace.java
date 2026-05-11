package fr.uge.balatri.domain.card.rank;

public record Ace() implements Rank {

	@Override
	public String toString() {
		return "Ace";
	}
}
