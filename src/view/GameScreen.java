package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import broadcast.ConnectionStatus;
import broadcast.GameBroadcast;
import game.Condition;
import game.Result;

public class GameScreen {

	/** Frame principal do game. */
	private JFrame frmJogoDaVelha;

	/** Lista com todos os botãos, utilizado para controle em tela. */
	private List<GameButton> buttons = new ArrayList<>();

	/**
	 * Texto se Vazio
	 */
	private final String ICON_EMPTY = "";

	/**
	 * Texto para o Player 1
	 */
	private final String ICON_PLAYER_1 = "X";

	/**
	 * Texto para o Player 2
	 */
	private final String ICON_PLAYER_2 = "O";

	/**
	 * Label que controla o status do jogo.
	 */
	private JLabel labelTitle;
	
	/**
	 * Label que controla o status do jogo.
	 */
	private JLabel labelConnectedWith;

	/**
	 * Declaração da Thread que verifica se a rodada já está disponível para ser
	 * jogada.
	 */
	private Thread alreadyPlayThread;

	/**
	 * Cria a tela principal do Jogo
	 */
	public GameScreen() {

		// Cria Frame Principal do Jogo e seta seus atributos
		frmJogoDaVelha = new JFrame();
		frmJogoDaVelha.setResizable(false);
		frmJogoDaVelha.setTitle("Jogo da Velha");
		frmJogoDaVelha.setBounds(100, 100, 406, 456);
		frmJogoDaVelha.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		labelTitle = new JLabel("");
		labelTitle.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		labelTitle.setBounds(6, 6, 394, 26);
		frmJogoDaVelha.getContentPane().add(labelTitle);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.scrollbar);
		frmJogoDaVelha.setJMenuBar(menuBar);

		JMenu mnArquivo = new JMenu("Arquivo");
		mnArquivo.setBackground(SystemColor.scrollbar);
		menuBar.add(mnArquivo);

		JMenuItem menuNewGame = new JMenuItem("Novo Jogo (Online)");
		menuNewGame.setBackground(SystemColor.menu);

		// Ao clicar em novo jogo, abre Dialog como ação para criar o server.
		menuNewGame.addActionListener(e -> {
			
			if (!GameBroadcast.getInstance().getConnectionStatus().equals(ConnectionStatus.STOPED)){
				JOptionPane.showMessageDialog(null, "Existe uma conexão aberta, encerre o jogo para iniciar novo jogo.", "Atenção", JOptionPane.WARNING_MESSAGE);
				return;
			}

			frmJogoDaVelha.setEnabled(false);

			NewGameDialog newGameDialog = new NewGameDialog();
			newGameDialog.setAlwaysOnTop(true);
			newGameDialog.setVisible(true);
			newGameDialog.toFront();
			newGameDialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					frmJogoDaVelha.setEnabled(true);
					if (!GameBroadcast.getInstance().getConnectionStatus().equals(ConnectionStatus.STOPED)) {
						// Ao encerrar o frame principal é chamado novamente e o
						// métódo lookup é chamado para iniciar o jogo.
						labelConnectedWith.setText("Conectado como: Servidor");
						lookup();
					}
				}
			});
		});

		JMenuItem menuConnect = new JMenuItem("Conectar (Online)");
		menuConnect.setBackground(SystemColor.menu);
		menuConnect.addActionListener(e -> {
			
			if (!GameBroadcast.getInstance().getConnectionStatus().equals(ConnectionStatus.STOPED)){
				JOptionPane.showMessageDialog(null, "Existe uma conexão aberta, encerre o jogo para iniciar novo jogo.", "Atenção", JOptionPane.WARNING_MESSAGE);
				return;
			}

			frmJogoDaVelha.setEnabled(false);

			ConnectDialog connectDialog = new ConnectDialog();
			connectDialog.setAlwaysOnTop(true);
			connectDialog.setVisible(true);
			connectDialog.toFront();
			connectDialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					frmJogoDaVelha.setEnabled(true);
					if (!GameBroadcast.getInstance().getConnectionStatus().equals(ConnectionStatus.STOPED)) {
						
						labelConnectedWith.setText("Conectado como: Cliente");
						lookup();
					}
				}
			});

		});

		JMenuItem menuSair = new JMenuItem("Sair");
		menuSair.addActionListener(e -> System.exit(0));
		menuSair.setBackground(SystemColor.menu);

		// Adiciona ao Menu Arquivo os itens
		mnArquivo.add(menuNewGame);
		mnArquivo.add(menuConnect);
		mnArquivo.add(menuSair);

		// Seta o layout padrão nulo
		frmJogoDaVelha.getContentPane().setLayout(null);

		JPanel gamePanel = new JPanel();
		gamePanel.setBackground(new Color(255, 255, 255));
		gamePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		gamePanel.setBounds(44, 44, 315, 315);
		frmJogoDaVelha.getContentPane().add(gamePanel);
		gamePanel.setLayout(null);

		GameButton btn0 = new GameButton(0);
		btn0.addActionListener(e -> performPlay(btn0));
		btn0.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn0.setBounds(0, 0, 105, 105);
		gamePanel.add(btn0);

		GameButton btn1 = new GameButton(1);
		btn1.addActionListener(e -> performPlay(btn1));
		btn1.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn1.setBounds(105, 0, 105, 105);
		gamePanel.add(btn1);

		GameButton btn2 = new GameButton(2);
		btn2.addActionListener(e -> performPlay(btn2));
		btn2.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn2.setBounds(210, 0, 105, 105);
		gamePanel.add(btn2);

		GameButton btn3 = new GameButton(3);
		btn3.addActionListener(e -> performPlay(btn3));
		btn3.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn3.setBounds(0, 105, 105, 105);
		gamePanel.add(btn3);

		GameButton btn4 = new GameButton(4);
		btn4.addActionListener(e -> performPlay(btn4));
		btn4.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn4.setBounds(105, 105, 105, 105);
		gamePanel.add(btn4);

		GameButton btn5 = new GameButton(5);
		btn5.addActionListener(e -> performPlay(btn5));
		btn5.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn5.setBounds(210, 105, 105, 105);
		gamePanel.add(btn5);

		GameButton btn6 = new GameButton(6);
		btn6.addActionListener(e -> performPlay(btn6));
		btn6.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn6.setBounds(0, 210, 105, 105);
		gamePanel.add(btn6);

		GameButton btn7 = new GameButton(7);
		btn7.addActionListener(e -> performPlay(btn7));
		btn7.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn7.setBounds(105, 210, 105, 105);
		gamePanel.add(btn7);

		GameButton btn8 = new GameButton(8);
		btn8.addActionListener(e -> performPlay(btn8));
		btn8.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn8.setBounds(210, 210, 105, 105);
		gamePanel.add(btn8);

		// Adiciona todos os botãos a lista de controle de botãos
		buttons.addAll(Arrays.asList(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8));
		
		labelConnectedWith = new JLabel();
		labelConnectedWith.setBounds(6, 371, 394, 16);
		frmJogoDaVelha.getContentPane().add(labelConnectedWith);

		// Limpa e libera a tela para as ações do usuário.
		cleanBoard();
	}

	/**
	 * 
	 * Método responsável por construir o tabuleiro do Jogo
	 * 
	 * Recebe o @param result como resultado da validação, que é informado pelo
	 * Game Manager para tomada de decisão
	 * 
	 */
	private void buildBoard(Result result) {

		System.out.println("Construindo tela ==> " + result);

		// Varre todos os botãos e seta cada um condizente a sua condicão atual.
		for (int i = 0; i < GameBroadcast.getInstance().getGameManager().getBoard().length; i++) {
			Condition condition = GameBroadcast.getInstance().getGameManager().getBoard()[i];
			if (condition.equals(Condition.PLAYER_1)) {
				buttons.get(i).setCondition(Condition.PLAYER_1);
				buttons.get(i).setText(ICON_PLAYER_1);
				buttons.get(i).setForeground(Color.RED);
			} else if (condition.equals(Condition.PLAYER_2)) {
				buttons.get(i).setCondition(Condition.PLAYER_2);
				buttons.get(i).setText(ICON_PLAYER_2);
				buttons.get(i).setForeground(Color.BLUE);
			} else {
				buttons.get(i).setText(ICON_EMPTY);
			}
		}

		if (result.equals(Result.WAIT)) {
			// No caso de espera, não faz nada
		} else if (result.equals(Result.NEXT_PLAY)) {
			// Se o result for NEXT_PLAY encaminha próxima jogada com o méotodo
			// lookup();

			lookup();
		} else if (result.equals(Result.DRAW)) {
			// No caso de empate, imprime mensagem e chama método para liberar o
			// tabuleiro.

			JOptionPane.showMessageDialog(getFrame(), "Deu velha...", "Acabou", JOptionPane.OK_OPTION);
			cleanBoard();
		} else {
			// Os outros dois estados possíveis são vitória do PLAYER1 ou
			// PLAYER2, é realizado o tratamento e amostragem da mensagem
			// apropriada.

			String message = Condition.valueOf(result.name()).equals(GameBroadcast.getInstance().getMe())
					? "Parabéns, você venceu!" : "Que pena, você perdeu!";
			JOptionPane.showMessageDialog(getFrame(), message);

			// Chama método para liberar o tabuleiro
			cleanBoard();
		}
	}

	/**
	 * Libera o tabuleiro
	 */
	protected void cleanBoard() {

		// Verifica se a Thread que fazia a validação ainda não foi encerrada e
		// encerra
		if (alreadyPlayThread != null && alreadyPlayThread.isAlive())
			alreadyPlayThread.interrupt();

		// Reinicia o GameBroadcast e o GameManager
		GameBroadcast.getInstance().reset();

		// Limpa a status da label
		labelTitle.setText("");
		labelConnectedWith.setText("");

		// Reinicia todos os botãos
		for (GameButton gameButton : buttons) {
			gameButton.setCondition(Condition.EMPTY);
			gameButton.setText(ICON_EMPTY);
			gameButton.setEnabled(false);
		}
	}

	/**
	 * Habilita todos os botões
	 */
	protected void enableButtons() {
		for (GameButton gameButton : buttons) {
			gameButton.setEnabled(true);
		}
	}

	/**
	 * Retorna o frame principal
	 */
	public JFrame getFrame() {
		return frmJogoDaVelha;
	}

	/**
	 * Verifica com o GameBroadcast de quem é a vez da jogada.
	 * 
	 * Em caso de ser sua jogada, libera para jogar.
	 * 
	 * Em caso de não ser sua jogada, abre Thread que conversa com o
	 * GameBroadcast perguntando se agora já é seu turno Quando for turno,
	 * valida a resposta do adversário e aplica o método BuildBoard.
	 * 
	 */
	private void lookup() {
		enableButtons();

		if (GameBroadcast.getInstance().isMe()) {
			labelTitle.setText("Sua vez de jogar!");
		} else {
			labelTitle.setText("Aguarde seu adversário!");
			
			System.out.println("Aguardando adversário...");

			// Inicia Thread para validação se é minha vez, criar uma Thread
			// nesse momento impede que a aplicação
			// fique travada enquanto espera a ação do adversário
			alreadyPlayThread = new Thread(() -> {

				// Enquanto a ação é esperar pergunte para o server se é a vez
				while (!GameBroadcast.getInstance().getConnectionStatus().equals(ConnectionStatus.STOPED)
						&& !GameBroadcast.getInstance().isMe()) {

					// Não matar o servidor
					try {
						Thread.sleep(100);

						// Pergunta para o GameBroadcast se já está disponível
						// para aplicar as ações de tela
						if (GameBroadcast.getInstance().already()) {
							buildBoard(GameBroadcast.getInstance().getGameManager().validateGameBoard());
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
					}
				}
				alreadyPlayThread.interrupt();
			});
			alreadyPlayThread.start();
		}
	}

	/**
	 * 
	 * Através do performPlay um botão executa sua jogada.
	 * 
	 * Todas as ações dos botãos precisam passar por aqui.
	 * 
	 */
	private void performPlay(GameButton gameButton) {

		// Verifica se o campo atual está livre
		if (!gameButton.getCondition().equals(Condition.EMPTY)) {
			JOptionPane.showMessageDialog(getFrame(), "Você não pode jogar aqui...", "Ops...",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		// Verifica se é a vez de jogar
		if (!GameBroadcast.getInstance().isMe()) {
			JOptionPane.showMessageDialog(getFrame(), "Aguarde sua vez para jogar.", "Ops...",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Realiza a jogada e recupera qual o resultado dessa jogada e passa
		// para o construtor do tabuleiro montar novamente com os valores da
		// jogada atualizados
		Result result = GameBroadcast.getInstance().play(gameButton.getPosition());
		buildBoard(result);
	}
}