package server;
//Изменял. Блоки истории и черного листа
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuthService {
	private static Connection connection;
	private static Statement statement;

	public static void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:main.db");
			statement = connection.createStatement();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static int addUser(String login, String pass, String nickname) {
		try {
			String query = "INSERT INTO users (login, password, nickname) VALUES (?, ?, ?);";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, login);
			ps.setInt(2, pass.hashCode());
			ps.setString(3, nickname);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static ArrayList<String> getBlackList(String nickname) {
		String query = String.format("select blockedUsers from blacklist where nickname='%s'", nickname);
		try {
			ResultSet rs = statement.executeQuery(query);
			if (rs.next()) {
				if (rs.getString(1) != null) {
					String blacklist = rs.getString(1);
					return new ArrayList<>(Arrays.asList(blacklist.split(" ")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	public static String getNickNameByLoginAndPassword(String login, String password) {
		String query = String.format("select nickname, password from users where login='%s'", login);
		try {
			ResultSet rs = statement.executeQuery(query);
			int myHash = password.hashCode();

			if (rs.next()) {
				String nick = rs.getString(1);
				int dbHash = rs.getInt(2);
				if (myHash == dbHash) {
					return nick;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static void disconnect() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void blackListSQLSynchronization(String nickname, List<String> blacklistArray) {
		String querySelect = String.format("select blockedUsers from blacklist where nickname='%s'", nickname);
		String queryAdd = "INSERT INTO blacklist (nickname, blockedUsers) VALUES (?, ?);";
		String queryUpdate = "UPDATE blacklist SET blockedUsers=? where nickname=?";
		StringBuilder blockedUsers = new StringBuilder();
		for (String l : blacklistArray) {
			blockedUsers.append(l + " ");
		}
		try {
			ResultSet rsSelect = statement.executeQuery(querySelect);
			if (rsSelect.next()) {
				PreparedStatement rsUpdate = connection.prepareStatement(queryUpdate);
				rsUpdate.setString(1, blockedUsers.toString());
				rsUpdate.setString(2, nickname);
				rsUpdate.executeUpdate();
			} else {
				PreparedStatement rsAdd = connection.prepareStatement(queryAdd);
				rsAdd.setString(1, nickname);
				rsAdd.setString(2, blockedUsers.toString());
				rsAdd.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void historySave(String nickname, String chatHistory) {
		String queryUpdate = "UPDATE messageHistory SET chatHistory=? where nickname=?";
		String queryAdd = "INSERT INTO messageHistory (nickname, chatHistory) VALUES (?, ?);";
		String querySelect = String.format("select chatHistory from messageHistory where nickname='%s'", nickname);
		StringBuilder stringToSQL = new StringBuilder();
		try {
			ResultSet rsSelect = statement.executeQuery(querySelect);
			if (rsSelect.next()) {
				stringToSQL.append(rsSelect.getString(1).length() == 0 ? chatHistory : rsSelect.getString(1) + "\n" + chatHistory);
				PreparedStatement rsUpdate = connection.prepareStatement(queryUpdate);
				rsUpdate.setString(1, stringToSQL.toString());
				rsUpdate.setString(2, nickname);
				rsUpdate.executeUpdate();
			} else {
				PreparedStatement rsAdd = connection.prepareStatement(queryAdd);
				rsAdd.setString(1, nickname);
				rsAdd.setString(2, chatHistory);
				rsAdd.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String getHistory(String nickname) {
		String query = String.format("select chatHistory from messageHistory where nickname='%s'", nickname);
		StringBuilder stringOut = new StringBuilder();
		try {
			ResultSet rs = statement.executeQuery(query);
			if (rs.next()) {
				if (!rs.getString(1).isEmpty()) {
					String[] str = rs.getString(1).split("\n");
					for (int i = (str.length <= 100 ? 0 : str.length - 100); i < str.length; i++) {
						stringOut.append(str[i] + "\n");
					}
					return stringOut.toString();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
}