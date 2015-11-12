package broadcast.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import broadcast.GameBroadcast;

public class Server {

	private int port;

	/** Declaro o ServerSocket */
	private ServerSocket serverSocket = null;

	/** Declaro o Socket de comunicação */
	private Socket socket = null;

	// Declaro a Stream de saida de dados
	private PrintStream printStream = null;
	
	private Thread serverThread;

	public Server(int port) {
		this.port = port;
	}

	/**
	 * Método que inicia a escuta do servidor
	 *
	 * Enquanto o servidor estiver ligado ele ira ficar esperando receber um
	 * comando, quando receber ele pega, executa e depois devolve a resposta.
	 *
	 */
	public void listen() {

		serverThread = new Thread(() -> {

			// Declaro o leitor para a entrada de dados
			BufferedReader entrada = null;

			try {

				// Cria o ServerSocket na porta se estiver disponível
				serverSocket = new ServerSocket(port);

				while (true) {

					// Aguarda uma conexão na porta especificada e cria retorna
					// o
					// socket
					// que irá comunicar com o cliente
					socket = serverSocket.accept();

					// Cria um BufferedReader para o canal da stream de entrada
					// de
					// dados
					// do socket s
					entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

					// Aguarda por algum dado e imprime a linha recebida quando
					// recebe
					String received = entrada.readLine();

					System.out.println("Comando recebido: " + received);

					printStream = new PrintStream(socket.getOutputStream());

					// Chama o GameBroadcast que devolve para o client o que o
					// client precisa fazer
					String result = GameBroadcast.getInstance().runCommand(received);

					printStream.println(result);
				}

			} catch (IOException e) {
				System.out.println("Conexão fechada");
			} finally {
				stop();
			}

		});
		serverThread.start();
	}

	/**
	 * Método que para o Server
	 */
	public void stop() {
		
		try {
			// Encerro o socket de comunicação
			if (socket != null) {
				socket.close();
			}
			// Encerro o ServerSocket
			if(serverSocket != null)
				serverSocket.close();
		} catch (IOException e) {

		}
		serverThread.interrupt();
	}
}