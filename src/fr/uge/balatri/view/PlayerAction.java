package fr.uge.balatri.view;

import java.util.Objects;
import java.util.Set;

public record PlayerAction(boolean isPlay, Set<Integer> selectedIndices) {
	public PlayerAction {
        Objects.requireNonNull(selectedIndices);
        if (selectedIndices.isEmpty()) {
            throw new IllegalArgumentException("selectedIndices cannot be empty");
        }
    }
}
