package game;

/**
 * Define os possíveis Resultados retornados pelo GameManager
 *
 */
public enum Result {
	DRAW, WAIT, NEXT_PLAY, PLAYER_1, PLAYER_2;

	public static boolean isWin(Result result) {
		if (result.equals(PLAYER_1) || result.equals(PLAYER_2)) {
			return true;
		}
		return false;
	}
}
