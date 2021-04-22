package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ConsoleServer {
	private Vector<ClientHandler> users;

	public ConsoleServer() {
		users = new Vector<ClientHandler>();
		ServerSocket server = null; // наша сторона
		Socket socket = null; // удаленная (remote) сторона

		try {
			AuthService.connect();
			server = new ServerSocket(6001);
			System.out.println("Server started");

			while (true) {
				socket = server.accept();
				System.out.printf("Client [%s] try to connect\n", socket.getInetAddress());
				new ClientHandler(this, socket);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.printf("Client [%s] disconnected", socket.getInetAddress());
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			AuthService.disconnect();
		}
	}

	public void subscribe(ClientHandler client) {
		users.add(client);
		System.out.println(String.format("User [%s] connected", client.getNickname()));
		broadcastClientsList();
	}

	public void unsubscribe(ClientHandler client) {
		users.remove(client);
		System.out.println(String.format("User [%s] disconnected", client.getNickname()));
		broadcastClientsList();
	}

	public void broadCastMessage(ClientHandler from, String str) {
		for (ClientHandler c : users) {
			if (!c.checkBlackList(from.getNickname().toLowerCase())) {
				c.sendMsg(str);
			}
		}
	}

	public boolean isNickBusy(String nick) {
		for (ClientHandler c : users) {
			if (c.getNickname().equals(nick)) {
				return true;
			}
		}
		return false;
	}

	public void privateMessage(ClientHandler nickFrom, String nickTo, String message) {
		for (ClientHandler nickBase : users) {
			if (nickTo.equals(nickBase.getNickname()) && !nickBase.checkBlackList(nickFrom.getNickname().toLowerCase())) {
				if (!nickFrom.getNickname().equals(nickTo)) {
					nickBase.sendMsg("от " + nickFrom.getNickname() + message);
					nickFrom.sendMsg(nickFrom.getNickname() + " для " + nickTo + message);
				}
			}
		}

	}

	private void broadcastClientsList() {
		StringBuilder sb = new StringBuilder();
		sb.append("/clientList ");
		for (ClientHandler c : users) {
			sb.append(c.getNickname() + " ");
		}

		String out = sb.toString();
		for (ClientHandler c : users) {
			c.sendMsg(out);
		}
	}

}
