package main;

import java.awt.EventQueue;

import view.GameScreen;

public class Start {

	/**
	 * Executa o início do jogo
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(() -> {
			try {
				// Criar a janela principal do jogo e setar visivel
				GameScreen window = new GameScreen();
				window.getFrame().setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
