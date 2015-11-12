package game;

public enum Condition {

	EMPTY(0), PLAYER_1(1), PLAYER_2(2);

	private int value;

	private Condition(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	// public Condition getCondition(int value){
	// if(EMPTY.getValue() == value){
	// return EMPTY;
	// }
	// if(PLAYER_1.getValue() == value){
	// return EMPTY;
	// }
	// if(PLAYER_2.getValue() == value){
	// return EMPTY;
	// }
	// return null;
	// }
}
