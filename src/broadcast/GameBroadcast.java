package broadcast;

import java.net.ConnectException;

import broadcast.socket.Client;
import broadcast.socket.Server;
import game.Condition;
import game.GameManager;
import game.Result;

/**
 * Classe GameBroadcast
 *
 * Singleton, que tem a responsabilidade de orquestrar o funcionamento do
 * GameManagaer e estabeler a ponte de comunicação entre Server e Client.
 *
 * Através
 *
 */
public class GameBroadcast {

	/**
	 * Instância do GameBroadCast
	 */
	private static GameBroadcast instance;

	/**
	 * Recupera a instância atual do GameBroadcast
	 */
	public static GameBroadcast getInstance() {
		if (instance == null) {
			instance = new GameBroadcast();
		}
		return instance;
	}

	/**
	 * Define o estado atual da conexão, inicia STOPED.
	 */
	private ConnectionStatus connectionStatus = ConnectionStatus.STOPED;

	/**
	 * Definição do Server, é instânciado quando o jogador local é um Server
	 */
	private Server server;

	/**
	 * Definição do Client, é instânciado quando o jogador local é um Client
	 */
	private Client client;

	/**
	 * Centralização do GameManager, todos os acessos ao gameManager precisam
	 * ser feitos pelo GameBroadcast
	 */
	private GameManager gameManager;

	/**
	 * Indica qual a minha condição para o jogo:
	 * 
	 * PLAYER_1 - Server PLAYER_2 - Client
	 */
	private Condition me;

	/**
	 * Declaração da variavel responsável por armazenar uma ação disponível para
	 * ser consumida pelo Client.
	 * 
	 * Quando um server quer fazer uma comunicação com o Client, ele ira colocar
	 * nessa variável o valor de comunicação, e o Client devera perguntar para o
	 * Server se existe algo a ser consumido. (Modelo: Produtor x Consumidor)
	 * 
	 */
	private String consume;

	/**
	 * Construtor
	 */
	private GameBroadcast() {
		gameManager = new GameManager();
	}

	/**
	 * O método already é o responsável por efetuar a comunicação entre Client e
	 * Server perguntando se o Server já possui algo dispon;ivel para ser
	 * consumido, no caso positivo o Client irá receber o que foi
	 * disponibilizado e retornar que nesse momento já está disponível.
	 */
	public boolean already() {
		if (me.equals(Condition.PLAYER_2)) {
			String response = client.sendMessage(Client.ALREADY);
			if (response.startsWith(Client.PLAY)) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * Através do Host e da Porta quando o jogador for um Client, faz a
	 * comunicação com o servidor e seta o jogo atual como Client Side
	 * 
	 * @param host
	 * @param port
	 */
	public void connectClient(String host, int port) throws ConnectException {
		client = new Client(host, port);
		String response = client.sendMessage(Client.CONNECT);
		if(response == null){
			throw new ConnectException("Não foi possível estabelecer conexão com o servidor.");
		}
		me = Condition.PLAYER_2;
		connectionStatus = ConnectionStatus.CONNECTED;
	}

	/**
	 * Recupera qual o estado atual da conexão.
	 */
	public synchronized ConnectionStatus getConnectionStatus() {
		return connectionStatus;
	}

	/**
	 * Retorna instância do GameManager
	 */
	public GameManager getGameManager() {
		return gameManager;
	}

	/**
	 * Retorna condição do Player atual
	 */
	public Condition getMe() {
		return me;
	}

	/**
	 * Retorna se é minha a jogada.
	 */
	public boolean isMe() {
		if (gameManager.getCurrentPlay().equals(me)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Executa uma jogada, independente de ser Server ou Client.
	 * 
	 * É tomada a decisão, através dos controles definidos no GameBroadcast.
	 * 
	 * @param position
	 * @return
	 */
	public Result play(int position) {
		gameManager.play(position);
		String command = Client.PLAY + ";" + position;
		if (me.equals(Condition.PLAYER_1)) {
			consume = command;
		} else {
			client.sendMessage(command);
		}
		gameManager.changePlay();
		return gameManager.validateGameBoard();
	}

	/**
	 * 
	 * O método RunCommand é o responsável pela execução dos comandos entre
	 * Server e Client.
	 * 
	 * Quem o utiliza não tem conhecimento, as tomadas de decisão serão feitas
	 * em cima dos controles estabelecidos nesta classe
	 * 
	 * @param command
	 * @return
	 */
	public synchronized String runCommand(String command) {

		System.out.println(String.format("Run command %s", command));
		if (command.equals(Client.CONNECT)) {

			connectionStatus = ConnectionStatus.CONNECTED;
			return Result.NEXT_PLAY.name();
		} else if (command.startsWith(Client.CONNECT)) {

			return "OK";
		} else if (command.equals(Client.ALREADY)) {
			if (consume != null) {
				String response = consume;
				consume = null;
				return response;
			}
			return Result.WAIT.name();
		} else if (command.startsWith(Client.PLAY)) {
			gameManager.play(Integer.valueOf(command.split(";")[1]));
			gameManager.changePlay();
			Result result = gameManager.validateGameBoard();
			return Result.isWin(result) ? "END" : result.name();
		}
		return null;
	}

	/**
	 * Inicia o Server
	 * 
	 * @param port
	 */
	public void startServer(int port) {
		me = Condition.PLAYER_1;
		connectionStatus = ConnectionStatus.WAIT_CONNECTION;

		server = new Server(port);
		server.listen();
	}

	/**
	 * Reiniciar o GameBroadcast
	 */
	public void reset() {
		if (server != null) {
			server.stop();
		}
		instance = null;
	}

	/**
	 * Para o Servidor
	 */
	public void stopServer() {
		connectionStatus = ConnectionStatus.STOPED;
		server.stop();
	}

}