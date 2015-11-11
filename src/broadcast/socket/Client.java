package broadcast.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import broadcast.GameBroadcast;

/**
 * Client Socket
 * 
 * Utilizado para conexão com o Server
 *
 */
public class Client {

	public static final String CONNECT = "CONNECT";
	public static final String ALREADY = "ALREADY";
	public static final String PLAY = "PLAY";
	public static final String VALIDATE = "VALIDATE";

	private final String host;

	private final int port;

	// Declaro o socket cliente
	private Socket socket = null;

	private PrintStream printStream = null;
	private BufferedReader entrada = null;

	/**
	 * @param host
	 * @param port
	 * 
	 *            Construtor que recebe o host e a porta para comunicação
	 * 
	 */
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String sendMessage(String message) {

		try {
			// Cria o socket com o recurso desejado na porta especificada
			socket = new Socket(host, port);

			// Cria a Stream de saida de dados
			printStream = new PrintStream(socket.getOutputStream());

			// Imprime uma linha para a stream de saída de dados
			printStream.println(message);

			// Aguarda pela resposta do servidor e ao receber a executa.
			entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Aguarda pela resposta do servidor e ao receber a executa.
			String received = null;
			boolean wait = true;
			while (wait) {
				received = entrada.readLine();
				if (received == null)
					continue;

				System.out.println("Client - Recebido:" + received);
				GameBroadcast.getInstance().runCommand(received);
				wait = false;
			}
			return received;
		} catch (IOException e) {
			return null;
		} finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
				}
		}
	}

	public static void main(String[] args) {
		Client client = new Client("127.0.0.1", 9000);
		client.sendMessage(Client.CONNECT);
	}
}
