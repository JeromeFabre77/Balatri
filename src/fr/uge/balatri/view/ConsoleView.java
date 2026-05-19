package fr.uge.balatri.view;

import fr.uge.balatri.domain.combination.Combination;
import fr.uge.balatri.domain.planet.Planet;
import fr.uge.balatri.model.GameState;
import fr.uge.balatri.domain.hand.PlayerHand;

import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public final class ConsoleView implements View {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void displayGameState(GameState gameState) {
        Objects.requireNonNull(gameState);

        IO.println(separatorLine("-"));

        IO.println("Blind : " + gameState.currentBlind().name() + " ( Score à battre : "
                + gameState.currentBlind().score() + " )");
        IO.println("Score cumulé : " + gameState.getCumulatedScore());
        IO.println("Mains restantes dans le blind : " + gameState.getHandsRemainingInBlind());
        IO.println("Deck : " + gameState.getDeckSize() + " cartes restantes | Défausse : " + gameState.getDiscardSize()
                + " cartes");

        displayPlanets(gameState);

        IO.println(separatorLine("~"));

        displayHand(gameState);

        IO.println(separatorLine("-"));
    }

    @Override
    public Set<Integer> askCardSelection() {
        while (true) {
            IO.println("Sélectionnez les cartes à jouer (ex : 0 2 4) : ");

            var input = scanner.nextLine().trim();

            try {
                return parseCardSelection(input);
            } catch (IllegalArgumentException e) {
                IO.println("Erreur : " + e.getMessage());
                IO.println("Entrez entre 1 et " + PlayerHand.MAX_SELECTED_SIZE
                        + " indices de cartes valides, séparés par des espaces.");
            }
        }
    }

    @Override
    public void displayBlindBeaten(Planet planet) {
        Objects.requireNonNull(planet);

        IO.println(separatorLine("*"));

        IO.println("Bravo ! Vous avez battu le blind");
        IO.println("Vous avez conquis la planète : " + planet);

        IO.println(separatorLine("*"));
    }

    @Override
    public void displayTurnResult(Combination combination, int scoreGained) {
        Objects.requireNonNull(combination);

        IO.println(separatorLine("-"));

        IO.println("Combinaison obtenue : " + combination);
        IO.println("Chips : " + combination.chips() + " x " + combination.multiplier());
        IO.println("Score gagné : " + scoreGained);

        IO.println(separatorLine("-"));
    }

    @Override
    public void displayGameOver(int totalScore) {
        IO.println(separatorLine("!"));

        IO.println("Game Over !");
        IO.println("Votre score final : " + totalScore);

        IO.println(separatorLine("!"));
    }

    @Override
    public void displayGameWon(int totalScore) {
        IO.println(separatorLine("*"));

        IO.println("Félicitations ! Vous avez conquis toutes les planètes !");
        IO.println("Votre score final : " + totalScore);

        IO.println(separatorLine("*"));
    }

    private void displayHand(GameState gameState) {
        IO.println("Votre main :");
        var cards = gameState.getHandCards();
        for (int i = 0; i < cards.size(); i++) {
            IO.println("[" + i + "] " + cards.get(i));
        }
    }

    private void displayPlanets(GameState gameState) {
        var planets = gameState.getPlanets();
        if (planets.isEmpty()) {
            IO.println("Aucune planète conquise pour le moment.");
            return;
        }

        IO.println("Planètes conquises :");

        planets.forEach((planet, count) -> {
            IO.println("- " + planet + " (x" + count + ")");
        });
    }

    private Set<Integer> parseCardSelection(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("La sélection ne peut pas être vide");
        }

        var parts = input.split("\\s+");
        var selectedIndices = new HashSet<Integer>();

        for (var part : parts) {
            int index;
            try {
                index = Integer.parseInt(part);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Indices de cartes invalides : " + part);
            }

            if (index < 0 || index >= PlayerHand.MAX_HAND_SIZE) {
                throw new IllegalArgumentException("Indices de cartes hors limites : " + index);
            }
            selectedIndices.add(index);
        }

        if (selectedIndices.size() > PlayerHand.MAX_SELECTED_SIZE) {
            throw new IllegalArgumentException(
                    "Sélectionnez au maximum " + PlayerHand.MAX_SELECTED_SIZE + " cartes");
        }

        return selectedIndices;
    }

    private String separatorLine(String separator) {
        return separator.repeat(45);
    }
}
