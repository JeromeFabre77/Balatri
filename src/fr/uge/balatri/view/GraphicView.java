package fr.uge.balatri.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.Set;

import com.github.forax.zen.ApplicationContext;

import fr.uge.balatri.domain.Combination;
import fr.uge.balatri.domain.Planet;
import fr.uge.balatri.domain.card.Card;
import fr.uge.balatri.model.GameState;

public final class GraphicView implements View {
	private static final Color PANEL_COLOR = new Color(70, 16, 36);
	private static final Color CARD_COLOR = new Color(240, 232, 210);
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final Color SECONDARY_TEXT_COLOR = new Color(220, 190, 230);
	private static final Color PLAY_BUTTON_COLOR = new Color(210, 50, 130);
	private static final Color DISCARD_BUTTON_COLOR = new Color(180, 45, 70);
	private static final Color DISABLED_BUTTON_COLOR = new Color(80, 70, 90);

	private static final int HEADER_X = 40;
	private static final int HEADER_Y = 35;
	private static final int HEADER_HEIGHT = 170;

	private static final int CARD_WIDTH = 140;
	private static final int CARD_HEIGHT = 200;
	private static final int CARD_GAP = 22;

	private static final int BUTTON_WIDTH = 170;
	private static final int BUTTON_HEIGHT = 55;
	private static final int BUTTON_GAP = 30;

	private final ApplicationContext context;

	public GraphicView(ApplicationContext context) {
		this.context = Objects.requireNonNull(context);
	}

	@Override
	public void displayGameState(GameState gameState) {
		Objects.requireNonNull(gameState);
		context.renderFrame(graphics -> drawGameState(graphics, gameState));
	}

	private void drawGameState(Graphics2D graphics, GameState gameState) {
		var screenInfo = context.getScreenInfo();
		var width = screenInfo.width();
		var height = screenInfo.height();

		drawHeader(graphics, gameState, width);
		drawHand(graphics, gameState, width, height);
		drawActionButtons(graphics, gameState.canDiscard(), width, height);
	}

	private static void drawHeader(Graphics2D graphics, GameState gameState, int width) {
		graphics.setColor(PANEL_COLOR);
		graphics.fillRoundRect(HEADER_X, HEADER_Y, width - 2 * HEADER_X, HEADER_HEIGHT, 24, 24);

		drawTitle(graphics);
		drawBlindInfo(graphics, gameState);
		drawScoreInfo(graphics, gameState);
		drawResourcesInfo(graphics, gameState);
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

	private static void drawHand(Graphics2D graphics, GameState gameState, int screenWidth, int screenHeight) {
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

		for (var i = 0; i < cardCount; i++) {
			var x = startX + i * (CARD_WIDTH + CARD_GAP);
			drawCard(graphics, cards.get(i), i, x, cardsY);
		}
	}

	private static void drawCard(Graphics2D graphics, Card card, int index, int x, int y) {
		graphics.setColor(CARD_COLOR);
		graphics.fillRoundRect(x, y, CARD_WIDTH, CARD_HEIGHT, 18, 18);

		graphics.setColor(Color.BLACK);
		graphics.setStroke(new BasicStroke(2));
		graphics.drawRoundRect(x, y, CARD_WIDTH, CARD_HEIGHT, 18, 18);

		graphics.setFont(new Font("Arial", Font.BOLD, 16));
		graphics.drawString(String.valueOf(index), x + 12, y + 26);

		graphics.setFont(new Font("Arial", Font.PLAIN, 13));
		drawCenteredString(graphics, card.toString(), x, y + 80, CARD_WIDTH);
	}

	private static void drawActionButtons(Graphics2D graphics, boolean canDiscard, int screenWidth, int screenHeight) {
		var totalButtonsWidth = BUTTON_WIDTH * 2 + BUTTON_GAP;
		var startX = (screenWidth - totalButtonsWidth) / 2;
		var buttonY = screenHeight - 100;

		var playButton = new Rectangle2D.Float(startX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
		var discardButton = new Rectangle2D.Float(startX + BUTTON_WIDTH + BUTTON_GAP, buttonY, BUTTON_WIDTH,
				BUTTON_HEIGHT);

		drawButton(graphics, playButton, PLAY_BUTTON_COLOR, "Jouer");
		drawButton(graphics, discardButton, canDiscard ? DISCARD_BUTTON_COLOR : DISABLED_BUTTON_COLOR, "Défausser");
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

	@Override
	public boolean askAction(GameState gameState) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<Integer> askCardSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Integer> askDiscardSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void displayBlindBeaten(Planet planet) {
		// TODO Auto-generated method stub
	}

	@Override
	public void displayTurnResult(Combination combination, int chipsCard, int scoreGained) {
		// TODO Auto-generated method stub
	}

	@Override
	public void displayGameOver(int totalScore, boolean isWon) {
		// TODO Auto-generated method stub
	}
}