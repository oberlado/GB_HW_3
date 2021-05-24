package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
public class ClientHandler {

	private ConsoleServer server;
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private String nickname;
	private long timer;
	// черный список у пользователя, а не у сервера
	private List<String> blacklist;
	private StringBuilder chatHistory;

	public ClientHandler(ConsoleServer server, Socket socket) {
		try {
			this.server = server;
			this.socket = socket;
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			this.blacklist = new ArrayList<>();
			this.timer = System.currentTimeMillis();
			new Thread(() -> {
				while (true) {
					long timerEnd = System.currentTimeMillis() - timer;
					if (!this.socket.isClosed() && nickname == null && timerEnd > 50000) {
						try {
							this.socket.close();
							break;
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (nickname != null) {
						break;
					}
				}
			new Thread(() -> {
				try {
				boolean isExit = false;
				while (true) {
					String str = null;
					try {
						str = in.readUTF();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (!str.isEmpty() && str.startsWith("/auth ")) {
						String[] tokens = str.split(" ");
						String nick = AuthService.getNickNameByLoginAndPassword(tokens[1], tokens[2]);
						if (nick != null) {
							if (!server.isNickBusy(nick)) {
								setNickname(nick);
								sendMsg("/auth-ok " + nickname);
								server.subscribe(ClientHandler.this);
								blacklist = AuthService.getBlackList(nickname);
								sendMsg("/history " + AuthService.getHistory(nickname));
								break;
							} else {
								sendMsg("Учетная запись уже используется");
							}
						} else {
							sendMsg("Неверный логин/пароль");
						}
					}
					//Регистрация пользователя
					if (!str.isEmpty() && str.startsWith("/signup ")) {
						String[] tokens = str.split(" ");
						int result = AuthService.addUser(tokens[1], tokens[2], tokens[3]);
						if (result > 0) {
							sendMsg("Успешная регистрация");
						} else {
							sendMsg("В регистрации отказано");
						}
					}
					if (!str.isEmpty() && "/end".equals(str)) {
						isExit = true;
						break;
					}
				}
				if (!isExit) {
					while (true) {
						String str = null;
						try {
							str = in.readUTF();
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (!str.isEmpty() && str.equals("/end")) {
							isExit = true;
							AuthService.historySave(nickname, chatHistory.toString());
							try {
								out.writeUTF("server closed");
							} catch (IOException e) {
								e.printStackTrace();
							}
							System.out.printf("Client [$s] disconnected\n", socket.getInetAddress());
							break;
						} else if (str.startsWith("@")) {
							String[] tokens = str.split(" ", 2);
							String nick = tokens[0].substring(1);
							server.privateMessage(this, tokens[0].substring(1), tokens[1]);

						} else if (str.startsWith("/blacklist ")) {
							String[] tokens = str.split(" ");
							blacklist.add(tokens[1].toLowerCase());
							sendMsg("You added " + tokens[1] + " to blacklist");
							AuthService.blackListSQLSynchronization(nickname, blacklist);
						} else {
							server.broadCastMessage(this, nickname + ": " + str);
						}
					}
				}
			} finally {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					server.unSubscribe(this);
				}
			}).start();
		});} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void sendMsg(String msg) {
		try {
			if (nickname != null && !msg.startsWith("/") && !msg.startsWith("@")) {
				chatHistory.append(msg + "\n");
			}
			out.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public boolean checkBlackList(String nickname) {
		return blacklist.contains(nickname);
	}
}