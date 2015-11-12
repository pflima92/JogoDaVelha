package broadcast;

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

	private static GameBroadcast instance;

	public static GameBroadcast getInstance() {
		if (instance == null) {
			instance = new GameBroadcast();
		}
		return instance;
	}

	private ConnectionStatus connectionStatus;

	private Server server;

	private Client client;

	private GameManager gameManager;

	private Condition me;

	private String consume;

	private GameBroadcast() {
		gameManager = new GameManager();
	}

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

	public void connectClient(String host, int port) {
		me = Condition.PLAYER_2;
		client = new Client(host, port);
		client.sendMessage(Client.CONNECT);
	}

	public synchronized ConnectionStatus getConnectionStatus() {
		return connectionStatus;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public Condition getMe() {
		return me;
	}

	public boolean isMe() {
		if (gameManager.getCurrentPlay().equals(me)) {
			return true;
		}
		return false;
	}

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

	public synchronized String runCommand(String command) {
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
			return gameManager.validateGameBoard().name();
		}
		return null;
	}

	public synchronized void setConnectionStatus(ConnectionStatus connectionStatus) {
		this.connectionStatus = connectionStatus;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public void startServer(int port) {
		me = Condition.PLAYER_1;
		connectionStatus = ConnectionStatus.WAIT_CONNECTION;

		server = new Server(port);
		server.listen();
	}
	
	public void reset() {
		if(server != null){
			server.stop();
		}
		instance = null;
	}

	public void stopServer() {
		connectionStatus = ConnectionStatus.LOSTED;
		server.stop();
	}

}