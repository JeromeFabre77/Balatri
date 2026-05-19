package fr.uge.balatri.view;

import fr.uge.balatri.domain.combination.Combination;
import fr.uge.balatri.domain.planet.Planet;
import fr.uge.balatri.model.GameState;

import java.util.Set;

public interface View {

    void displayGameState(GameState gameState);

    Set<Integer> askCardSelection();

    void displayBlindBeaten(Planet planet);

    void displayTurnResult(Combination combination, int scoreGained);

    void displayGameOver(int totalScore);

    void displayGameWon(int totalScore);
}
