package fr.uge.balatri.view;

import fr.uge.balatri.domain.Combination;
import fr.uge.balatri.domain.Planet;
import fr.uge.balatri.model.GameState;

import java.util.Set;

public interface View {

	void displayGameState(GameState gameState);

	boolean askAction(GameState gameState);

	Set<Integer> askCardSelection();

	Set<Integer> askDiscardSelection();

	void displayBlindBeaten(Planet planet);

	void displayTurnResult(Combination combination, int chipsCard, int scoreGained);

	void displayGameOver(int totalScore, boolean isWon);
}
