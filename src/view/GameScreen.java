package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import broadcast.GameBroadcast;
import game.Condition;
import game.Result;

public class GameScreen {

	/** Frame principal do game. */
	private JFrame frmJogoDaVelha;

	/** Lista com todos os botãos, utilizado para controle em tela. */
	private List<GameButton> buttons = new ArrayList<>();

	private final String ICON_EMPTY = "";

	private final String ICON_PLAYER_1 = "X";

	private final String ICON_PLAYER_2 = "O";

	private JLabel title;

	/**
	 * Create the application.
	 */
	public GameScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmJogoDaVelha = new JFrame();
		frmJogoDaVelha.setResizable(false);
		frmJogoDaVelha.setTitle("Jogo da Velha");
		frmJogoDaVelha.setBounds(100, 100, 406, 456);
		frmJogoDaVelha.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.scrollbar);
		frmJogoDaVelha.setJMenuBar(menuBar);

		JMenu mnArquivo = new JMenu("Arquivo");
		mnArquivo.setBackground(SystemColor.scrollbar);
		menuBar.add(mnArquivo);

		JMenuItem menuNewGame = new JMenuItem("Novo Jogo (Online)");
		menuNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				frmJogoDaVelha.setEnabled(false);

				NewGameDialog newGameDialog = new NewGameDialog();
				newGameDialog.setAlwaysOnTop(true);
				newGameDialog.setVisible(true);
				newGameDialog.toFront();
				newGameDialog.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						frmJogoDaVelha.setEnabled(true);
						lookup();
					}
				});
			}
		});
		menuNewGame.setBackground(SystemColor.menu);
		mnArquivo.add(menuNewGame);

		JMenuItem menuConnect = new JMenuItem("Conectar (Online)");
		menuConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				frmJogoDaVelha.setEnabled(false);

				ConnectDialog connectDialog = new ConnectDialog();
				connectDialog.setAlwaysOnTop(true);
				connectDialog.setVisible(true);
				connectDialog.toFront();
				connectDialog.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						frmJogoDaVelha.setEnabled(true);
						lookup();
					}
				});

			}
		});
		menuConnect.setBackground(SystemColor.menu);
		mnArquivo.add(menuConnect);

		JMenuItem menuSair = new JMenuItem("Sair");
		menuSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO valida se existe conexao aberta e fecha...
				System.exit(0);
			}
		});
		menuSair.setBackground(SystemColor.menu);
		mnArquivo.add(menuSair);
		frmJogoDaVelha.getContentPane().setLayout(null);

		title = new JLabel("Title");
		title.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		title.setBounds(6, 6, 394, 26);
		frmJogoDaVelha.getContentPane().add(title);

		JPanel gamePanel = new JPanel();
		gamePanel.setBackground(new Color(255, 255, 255));
		gamePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		gamePanel.setBounds(44, 44, 315, 315);
		frmJogoDaVelha.getContentPane().add(gamePanel);
		gamePanel.setLayout(null);

		GameButton btn0 = new GameButton(0);
		btn0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performPlay(btn0);
			}
		});
		btn0.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn0.setBounds(0, 0, 105, 105);
		gamePanel.add(btn0);

		GameButton btn1 = new GameButton(1);
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performPlay(btn1);
			}
		});
		btn1.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn1.setBounds(105, 0, 105, 105);
		gamePanel.add(btn1);

		GameButton btn2 = new GameButton(2);
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performPlay(btn2);
			}
		});
		btn2.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn2.setBounds(210, 0, 105, 105);
		gamePanel.add(btn2);

		GameButton btn3 = new GameButton(3);
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performPlay(btn3);
			}
		});
		btn3.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn3.setBounds(0, 105, 105, 105);
		gamePanel.add(btn3);

		GameButton btn4 = new GameButton(4);
		btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performPlay(btn4);
			}
		});
		btn4.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn4.setBounds(105, 105, 105, 105);
		gamePanel.add(btn4);

		GameButton btn5 = new GameButton(5);
		btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performPlay(btn5);
			}
		});
		btn5.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn5.setBounds(210, 105, 105, 105);
		gamePanel.add(btn5);

		GameButton btn6 = new GameButton(6);
		btn6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performPlay(btn6);
			}
		});
		btn6.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn6.setBounds(0, 210, 105, 105);
		gamePanel.add(btn6);

		GameButton btn7 = new GameButton(7);
		btn7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performPlay(btn7);
			}
		});
		btn7.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn7.setBounds(105, 210, 105, 105);
		gamePanel.add(btn7);

		GameButton btn8 = new GameButton(8);
		btn8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performPlay(btn8);
			}
		});
		btn8.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		btn8.setBounds(210, 210, 105, 105);
		gamePanel.add(btn8);

		// Adiciona todos os botãos a lista de controle de botãos
		buttons.addAll(Arrays.asList(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8));

		cleanBoard();
	}

	protected void cleanBoard() {
		// Reinicia todos os botãos
		for (GameButton gameButton : buttons) {
			gameButton.setCondition(Condition.EMPTY);
			gameButton.setText(ICON_EMPTY);
			gameButton.setEnabled(false);
		}
	}

	protected void enableButtons() {
		for (GameButton gameButton : buttons) {
			gameButton.setEnabled(true);
		}
	}

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
				
		Result result = GameBroadcast.getInstance().play(gameButton.getPosition());
		buildBoard(result);
		
	}

	private void lookup() {
		enableButtons();

		if (GameBroadcast.getInstance().isMe()) {
			title.setText("Sua vez de jogar!");
		} else {
			title.setText("Aguarde seu adversário!");

			new Thread(() -> {

				// Enquanto a ação é esperar pergunte para o server
				while (!GameBroadcast.getInstance().isMe()) {
					// Não matar o servidor
					try {
						Thread.sleep(200);
						
						if(GameBroadcast.getInstance().already()){
							buildBoard(Result.WAIT);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				lookup();
				
			}).start();
		}
	}
	
	private void buildBoard(Result result){
		for (int i = 0; i < GameBroadcast.getInstance().getGameManager().getBoard().length; i++) {
			Condition condition = GameBroadcast.getInstance().getGameManager().getBoard()[i];
			if(condition.equals(Condition.PLAYER_1)){
				buttons.get(i).setText(ICON_PLAYER_1);
				buttons.get(i).setForeground(Color.RED);
			}else if(condition.equals(Condition.PLAYER_2)){
				buttons.get(i).setText(ICON_PLAYER_2);
				buttons.get(i).setForeground(Color.BLUE);
			}else{
				buttons.get(i).setText(ICON_EMPTY);
			}
		}
		
		if(result.equals(Result.NEXT_PLAY)){
			lookup();
		}else if (result.equals(Result.DRAW)) {
			
			JOptionPane.showMessageDialog(getFrame(), "Deu velha...", "Acabou", JOptionPane.OK_OPTION);
		}else if (result.equals(Result.WAIT)) {
		}
		else {
			
			JOptionPane.showMessageDialog(getFrame(), "Vitória do jogador: " + result);
			cleanBoard();
		}
	}

	public JFrame getFrame() {
		return frmJogoDaVelha;
	}

	public void setFrame(JFrame frame) {
		this.frmJogoDaVelha = frame;
	}
}