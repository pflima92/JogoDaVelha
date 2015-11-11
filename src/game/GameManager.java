package game;

import java.util.Arrays;

public class GameManager {
	
	/**
	 * Define o tamanho fixo para as casas do board
	 */
	private final int SIZE = 9;
	
	/** Indica o jogador atual */
	private Condition currentPlay = Condition.PLAYER_1;
	
	/**
	 * board do jogo, privada. 
	 */
	private Condition[] board = new Condition[SIZE];
	
	private int[][] winCombination = new int[][] {
		{ 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, // Vitória em linha
		{ 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 }, // Vitória em coluna
		{ 0, 4, 8 }, { 2, 4, 6 } // Vitória em diagonal
	};
	
	
	/**
	 * Inicia o GameManager
	 * 
	 * Inicia o tabuleiro na condição de Vazio (EMPTY)
	 * 
	 */
	public GameManager() {
		
		cleanBoard();
	}
	
	public Result validateGameBoard() {
		// Verifica se houve vencedor
		for (int i = 0; i <= 7; i++) {
			if (board[winCombination[i][0]].equals(
				board[winCombination[i][1]]) &&
				board[winCombination[i][1]].equals(
				board[winCombination[i][2]]) &&
				!board[winCombination[i][0]].equals(Condition.EMPTY)) {

				if (board[winCombination[i][0]].equals(Condition.PLAYER_1)) {
					return Result.PLAYER_1;
				} else {
					return Result.PLAYER_2;
				}
			}
		}
		//Senão teve vencendor, verificar se possui jogadas disponíveis
		if(Arrays.asList(board).contains(Condition.EMPTY)){
			return Result.NEXT_PLAY;
		}
		
		return Result.DRAW;
	}
	
	public void play(int position){
		board[position] = getCurrentPlay();
	}
	
	/**
	 * Limpa tabuleiro, setando todas as casas na condição de vazio
	 */
	public void cleanBoard(){
		for (int i = 0; i < SIZE; i++) {
			board[i] = Condition.EMPTY;
		}
	}
	
	public Condition[] getBoard(){
		return board;
	}
	
	protected void printBoard(){
		for (int i = 0; i < SIZE; i++) {
			System.out.println(String.valueOf(i) + "=" + board[i]);
		}
	}

	public Condition getCurrentPlay() {
		return currentPlay;
	}

	public void setCurrentPlay(Condition currentPlay) {
		this.currentPlay = currentPlay;
	}
	
	public void changePlay(){
		if(currentPlay.equals(Condition.PLAYER_1)){
			currentPlay = Condition.PLAYER_2;
		}else{
			currentPlay = Condition.PLAYER_1;
		}
	}
}
