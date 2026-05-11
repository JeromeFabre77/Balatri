package fr.uge.balatri.domain.card.rank;

public record Numeric(int rank) implements Rank {

	public Numeric {
		if (rank < 2 || rank > 10) {
			throw new IllegalArgumentException("The rank of the digital cards must be between 2 and 10");
		}
	}

	@Override
	public String toString() {
		return Integer.toString(rank);
	}
}
