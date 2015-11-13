package game;

/**
 * Define condição atual do Jogo
 */
public enum Condition {

	EMPTY(0), PLAYER_1(1), PLAYER_2(2);

	private int value;

	private Condition(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
