package view;

import javax.swing.JButton;

import game.Condition;

public class GameButton extends JButton{
	
	public GameButton(int position) {
		condition = Condition.EMPTY;
		this.position = position;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7245543906921121890L;
	
	
	/**
	 * Indica qual é a posição no tabuleiro do botão, nunca é alterado
	 */
	private final int position;
	
	/**
	 * Indica a condição atual Vazio, Player 1 ou Player 2.
	 */
	private Condition condition;

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	
	public int getPosition() {
		return position;
	}
}
