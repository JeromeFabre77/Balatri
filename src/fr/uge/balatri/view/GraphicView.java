package fr.uge.balatri.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import fr.uge.balatri.domain.Combination;
import fr.uge.balatri.domain.Planet;
import fr.uge.balatri.domain.PlayerHand;
import fr.uge.balatri.domain.card.Card;
import fr.uge.balatri.model.GameState;

public final class GraphicView implements View {
	private static final Color BACKGROUND_COLOR = new Color(24, 16, 36);
	private static final Color PANEL_COLOR = new Color(70, 16, 36);
	private static final Color CARD_COLOR = new Color(240, 232, 210);
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final Color SECONDARY_TEXT_COLOR = new Color(220, 190, 230);
	private static final Color PLAY_BUTTON_COLOR = new Color(210, 50, 130);
	private static final Color DISCARD_BUTTON_COLOR = new Color(180, 45, 70);
	private static final Color DISABLED_BUTTON_COLOR = new Color(80, 70, 90);
	private static final Color BORDER_COLOR = new Color(230, 185, 0);
	private static final Color BANNER_BACKGROUND_COLOR = new Color(30, 20, 50, 220);
	private static final Color BANNER_TITLE_COLOR = new Color(255, 215, 0);
	private static final Color PLANET_TEXT_COLOR = new Color(180, 220, 255);

	private static final int HEADER_X = 40;
	private static final int HEADER_Y = 35;
	private static final int HEADER_HEIGHT = 190;

	private static final int CARD_WIDTH = 140;
	private static final int CARD_HEIGHT = 200;
	private static final int CARD_GAP = 22;
	private static final int CARD_SELECTED_LIFT = 30;

	private static final int BUTTON_WIDTH = 170;
	private static final int BUTTON_HEIGHT = 55;
	private static final int BUTTON_GAP = 30;

	private static final int BANNER_LINE_SPACING = 25;
	private static final int BANNER_HEADER_HEIGHT = 70;
	private static final int BANNER_PADDING = 30;
	private static final int BANNER_DURATION_MS = 3000;

	private final ApplicationContext context;

	private Rectangle2D.Float playButtonBounds;
	private Rectangle2D.Float discardButtonBounds;
	private List<Rectangle2D.Float> cardBounds = new ArrayList<Rectangle2D.Float>();

	public GraphicView(ApplicationContext context) {
		this.context = Objects.requireNonNull(context);
	}

	@Override
	public void displayGameState(GameState gameState) {
		Objects.requireNonNull(gameState);
		context.renderFrame(graphics -> drawGameState(graphics, gameState, Set.of()));
	}

	private void drawGameState(Graphics2D graphics, GameState gameState, Set<Integer> selected) {
		var screenInfo = context.getScreenInfo();
		var width = screenInfo.width();
		var height = screenInfo.height();

		graphics.setColor(BACKGROUND_COLOR);
		graphics.fillRect(0, 0, width, height);

		drawHeader(graphics, gameState, width);
		drawHand(graphics, gameState, width, height, selected);
		drawActionButtons(graphics, gameState.canDiscard(), !selected.isEmpty(), width, height);
	}

	private static void drawHeader(Graphics2D graphics, GameState gameState, int width) {
		graphics.setColor(PANEL_COLOR);
		graphics.fillRoundRect(HEADER_X, HEADER_Y, width - 2 * HEADER_X, HEADER_HEIGHT, 24, 24);

		drawTitle(graphics);
		drawBlindInfo(graphics, gameState);
		drawScoreInfo(graphics, gameState);
		drawResourcesInfo(graphics, gameState);
		drawPlanetsInfo(graphics, gameState);
	}

	private static void drawTitle(Graphics2D graphics) {
		graphics.setColor(TEXT_COLOR);
		graphics.setFont(new Font("Arial", Font.BOLD, 30));
		graphics.drawString("Balatri", 70, 85);
	}

	private static void drawBlindInfo(Graphics2D graphics, GameState gameState) {
		graphics.setColor(SECONDARY_TEXT_COLOR);
		graphics.setFont(new Font("Arial", Font.PLAIN, 18));

		var blind = gameState.currentBlind();

		graphics.drawString("Blind : " + blind.name() + " | Score à battre : " + blind.score(), 70, 125);

		graphics.drawString("Blind " + (gameState.getCurrentBlindIndex() + 1) + " / " + gameState.totalBlinds(), 70,
				155);
	}

	private static void drawScoreInfo(Graphics2D graphics, GameState gameState) {
		graphics.setColor(SECONDARY_TEXT_COLOR);
		graphics.setFont(new Font("Arial", Font.PLAIN, 18));

		graphics.drawString("Score cumulé : " + gameState.getCumulatedScore(), 280, 155);
	}

	private static void drawResourcesInfo(Graphics2D graphics, GameState gameState) {
		graphics.setColor(SECONDARY_TEXT_COLOR);
		graphics.setFont(new Font("Arial", Font.PLAIN, 18));

		graphics.drawString("Mains restantes : " + gameState.getHandsRemainingInBlind(), 70, 185);

		graphics.drawString("Défausses restantes : " + gameState.getDiscardRemainingInBlind(), 280, 185);

		graphics.drawString("Deck : " + gameState.getDeckSize() + " | Défausse : " + gameState.getDiscardSize(), 560,
				185);
	}

	private static void drawPlanetsInfo(Graphics2D graphics, GameState gameState) {
		var planets = gameState.getPlanets();

		graphics.setColor(TEXT_COLOR);
		graphics.setFont(new Font("Arial", Font.PLAIN, 18));
		graphics.drawString("Planètes :", 825, 85);

		if (planets.isEmpty()) {
			graphics.setColor(SECONDARY_TEXT_COLOR);
			graphics.setFont(new Font("Arial", Font.PLAIN, 16));
			graphics.drawString("Aucune", 825, 110);
			return;
		}

		graphics.setColor(PLANET_TEXT_COLOR);
		graphics.setFont(new Font("Arial", Font.PLAIN, 16));

		var y = 110;
		for (var entry : planets.entrySet()) {
			var planet = entry.getKey();
			var count = entry.getValue();
			graphics.drawString("• " + planet.toString() + " (x" + count + ")", 825, y);
			y += 20;
		}
	}

	private void drawHand(Graphics2D graphics, GameState gameState, int screenWidth, int screenHeight,
			Set<Integer> selected) {
		var cards = gameState.getHandCards();
		var cardCount = cards.size();

		if (cardCount == 0) {
			return;
		}

		var totalCardsWidth = cardCount * CARD_WIDTH + (cardCount - 1) * CARD_GAP;
		var startX = (screenWidth - totalCardsWidth) / 2;

		var buttonsY = screenHeight - 100;
		var cardsY = buttonsY - CARD_HEIGHT - 70;

		graphics.setColor(TEXT_COLOR);
		graphics.setFont(new Font("Arial", Font.BOLD, 22));
		graphics.drawString("Votre main :", startX, cardsY - 35);

		var bounds = new ArrayList<Rectangle2D.Float>();
		for (var i = 0; i < cardCount; i++) {
			var x = startX + i * (CARD_WIDTH + CARD_GAP);
			var isSelected = selected.contains(i);
			var y = isSelected ? cardsY - CARD_SELECTED_LIFT : cardsY;
			bounds.add(new Rectangle2D.Float(x, y, CARD_WIDTH, CARD_HEIGHT));
			drawCard(graphics, cards.get(i), i, x, y, isSelected);
		}

		cardBounds = List.copyOf(bounds);
	}

	private static void drawCard(Graphics2D graphics, Card card, int index, int x, int y, boolean isSelected) {
		graphics.setColor(CARD_COLOR);
		graphics.fillRoundRect(x, y, CARD_WIDTH, CARD_HEIGHT, 18, 18);

		graphics.setColor(isSelected ? BORDER_COLOR : Color.BLACK);
		graphics.setStroke(new BasicStroke(isSelected ? 4 : 2));
		graphics.drawRoundRect(x, y, CARD_WIDTH, CARD_HEIGHT, 18, 18);

		graphics.setFont(new Font("Arial", Font.BOLD, 16));
		graphics.drawString(String.valueOf(index), x + 12, y + 26);

		graphics.setFont(new Font("Arial", Font.PLAIN, 13));
		drawCenteredString(graphics, card.toString(), x, y + 80, CARD_WIDTH);
	}

	private void drawActionButtons(Graphics2D graphics, boolean canDiscard, boolean hasSelectedCards, int screenWidth,
			int screenHeight) {
		var totalButtonsWidth = BUTTON_WIDTH * 2 + BUTTON_GAP;
		var startX = (screenWidth - totalButtonsWidth) / 2;
		var buttonY = screenHeight - 100;

		playButtonBounds = new Rectangle2D.Float(startX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
		discardButtonBounds = new Rectangle2D.Float(startX + BUTTON_WIDTH + BUTTON_GAP, buttonY, BUTTON_WIDTH,
				BUTTON_HEIGHT);

		drawButton(graphics, playButtonBounds, hasSelectedCards ? PLAY_BUTTON_COLOR : DISABLED_BUTTON_COLOR, "Jouer");
		drawButton(graphics, discardButtonBounds,
				canDiscard && hasSelectedCards ? DISCARD_BUTTON_COLOR : DISABLED_BUTTON_COLOR,
				"Défausser");
	}

	private static void drawButton(Graphics2D graphics, Rectangle2D.Float button, Color color, String label) {
		graphics.setColor(color);
		graphics.fillRoundRect((int) button.x, (int) button.y, (int) button.width, (int) button.height, 16, 16);

		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("Arial", Font.BOLD, 20));
		drawCenteredString(graphics, label, (int) button.x, (int) button.y + 35, (int) button.width);
	}

	private static void drawCenteredString(Graphics2D graphics, String text, int x, int y, int width) {
		var metrics = graphics.getFontMetrics();
		var textWidth = metrics.stringWidth(text);
		graphics.drawString(text, x + (width - textWidth) / 2, y);
	}

	private static void drawBanner(Graphics2D graphics, String title, String[] lines, int screenWidth,
			int screenHeight) {
		var bannerHeight = BANNER_HEADER_HEIGHT + lines.length * BANNER_LINE_SPACING + BANNER_PADDING;
		var bannerWidth = screenWidth / 2;

		var bannerX = (screenWidth - bannerWidth) / 2;
		var bannerY = (screenHeight - bannerHeight) / 2;

		graphics.setColor(BANNER_BACKGROUND_COLOR);
		graphics.fillRoundRect(bannerX, bannerY, bannerWidth, bannerHeight, 24, 24);

		graphics.setColor(BORDER_COLOR);
		graphics.setStroke(new BasicStroke(3));
		graphics.drawRoundRect(bannerX, bannerY, bannerWidth, bannerHeight, 24, 24);

		graphics.setColor(BANNER_TITLE_COLOR);
		graphics.setFont(new Font("Arial", Font.BOLD, 28));
		drawCenteredString(graphics, title, bannerX, bannerY + 50, bannerWidth);

		var lineY = bannerY + BANNER_HEADER_HEIGHT + BANNER_LINE_SPACING;

		for (var line : lines) {
			graphics.setColor(TEXT_COLOR);
			graphics.setFont(new Font("Arial", Font.PLAIN, 18));
			drawCenteredString(graphics, line, bannerX, lineY, bannerWidth);
			lineY += BANNER_LINE_SPACING;
		}
	}

	private static void drawBlindBeatenBanner(Graphics2D graphics, Planet planet, int screenWidth, int screenHeight) {
		var lines = new String[] {
				"Planète obtenue : " + planet.toString(),
				"Chips +" + planet.bonusChips() + " | Multiplicateur +" + planet.bonusMultiplier()
		};
		drawBanner(graphics, "Blind battu !", lines, screenWidth, screenHeight);
	}

	private static void drawTurnResultBanner(Graphics2D graphics, Combination combination, int chipsCard,
			int scoreGained, int screenWidth, int screenHeight) {
		var lines = new String[] {
				"Combinaison : " + combination.toString(),
				"Chips : " + chipsCard + " + " + combination.chips() + " x " + combination.multiplier(),
				"Score gagné : " + scoreGained
		};
		drawBanner(graphics, "Main jouée !", lines, screenWidth, screenHeight);
	}

	@Override
	public PlayerAction askTurn(GameState gameState) {
		Objects.requireNonNull(gameState);

		var selected = new HashSet<Integer>();

		while (true) {
			context.renderFrame(graphics -> drawGameState(graphics, gameState, selected));

			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}

			switch (event) {
				case PointerEvent pointerEvent -> {
					if (pointerEvent.action() == PointerEvent.Action.POINTER_UP) {
						continue;
					}

					var x = pointerEvent.location().x();
					var y = pointerEvent.location().y();

					for (var i = 0; i < cardBounds.size(); i++) {
						if (cardBounds.get(i).contains(x, y)) {
							if (selected.contains(i)) {
								selected.remove(i);
							} else if (selected.size() < PlayerHand.MAX_SELECTED_SIZE) {
								selected.add(i);
							}
							break;
						}
					}

					if (playButtonBounds != null && playButtonBounds.contains(x, y) && !selected.isEmpty()) {
						return new PlayerAction(true, Set.copyOf(selected));
					} else if (discardButtonBounds != null && discardButtonBounds.contains(x, y)
							&& gameState.canDiscard() && !selected.isEmpty()) {
						return new PlayerAction(false, Set.copyOf(selected));
					}
				}
				case KeyboardEvent _ -> {
					continue;
				}
			}
		}

	}

	@Override
	public void displayBlindBeaten(GameState gameState, Planet planet) {
		Objects.requireNonNull(gameState);
		Objects.requireNonNull(planet);

		var screenInfo = context.getScreenInfo();

		context.renderFrame(graphics -> {
			drawGameState(graphics, gameState, Set.of());
			drawBlindBeatenBanner(graphics, planet, screenInfo.width(), screenInfo.height());
		});

		try {
			Thread.sleep(BANNER_DURATION_MS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void displayTurnResult(GameState gameState, Combination combination, int chipsCard, int scoreGained) {
		Objects.requireNonNull(gameState);
		Objects.requireNonNull(combination);

		var screenInfo = context.getScreenInfo();

		context.renderFrame(graphics -> {
			drawGameState(graphics, gameState, Set.of());
			drawTurnResultBanner(graphics, combination, chipsCard, scoreGained, screenInfo.width(),
					screenInfo.height());
		});

		try {
			Thread.sleep(BANNER_DURATION_MS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void displayGameOver(int totalScore, boolean isWon) {
		// TODO Auto-generated method stub
	}
}