package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import broadcast.ConnectionStatus;
import broadcast.GameBroadcast;

/**
 * Janela que define a criação de um novo jogo
 *
 */
public class NewGameDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final static int PORT = 9000;

	private final JPanel contentPanel = new JPanel();
	private JTextField textPort;
	private JTextField textAddress;

	/**
	 * Create the dialog.
	 */
	public NewGameDialog() {
		setType(Type.POPUP);
		setTitle("Novo Jogo (Online)");
		setBounds(100, 100, 353, 206);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblPorta = new JLabel("Porta:");
			lblPorta.setBounds(6, 50, 61, 16);
			contentPanel.add(lblPorta);
		}

		textPort = new JTextField();
		textPort.setEditable(false);
		textPort.setBounds(6, 69, 222, 26);
		textPort.setColumns(10);
		textPort.setText(String.valueOf(PORT));
		contentPanel.add(textPort);

		JLabel lblEndereoLocal = new JLabel("Endereço local:");
		lblEndereoLocal.setBounds(6, 6, 103, 16);
		contentPanel.add(lblEndereoLocal);

		textAddress = new JTextField();
		textAddress.setEditable(false);
		textAddress.setBounds(6, 23, 222, 26);
		contentPanel.add(textAddress);
		textAddress.setColumns(10);
		try {
			textAddress.setText(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {

		}
		JLabel lblAguardandoConexo = new JLabel("Aguardando conexão...");
		lblAguardandoConexo.setBounds(6, 107, 341, 16);
		contentPanel.add(lblAguardandoConexo);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnCancel = new JButton("Cancelar");
		btnCancel.addActionListener(e -> {

			GameBroadcast.getInstance().stopServer();

			dispose();
		});
		btnCancel.setActionCommand("Cancel");
		buttonPane.add(btnCancel);

		startConnection();
	}

	private void startConnection() {

		// Chama o GameBroadcast e pede para iniciar a conexao com o servidor
		try {
			GameBroadcast.getInstance().startServer(Integer.valueOf(textPort.getText()));
		} catch (IOException e1) {
			
			JOptionPane.showMessageDialog(null, "Conexão indisponível.", "Erro", JOptionPane.ERROR_MESSAGE);
			
			return;
		}

		Thread waitConnectionThread = new Thread(() -> {

			boolean connected = false;
			// Enquanto nao estiver conectado, pergunte ao GameBroadcast qual o
			// status da conexao.
			while (!connected) {

				connected = GameBroadcast.getInstance().getConnectionStatus().equals(ConnectionStatus.CONNECTED);

				try {
					// Nao mata o processador
					Thread.sleep(100);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			dispose();
		});
		waitConnectionThread.start();
	}
}
