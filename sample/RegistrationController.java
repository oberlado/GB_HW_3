package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RegistrationController {
	@FXML
	public TextField login;
	@FXML
	public PasswordField password;
	@FXML
	public TextField nickname;
	@FXML
	public Label resultLabel;
	@FXML
	public Button signupBtn;

	public void signUp(ActionEvent actionEvent) {
		if ("Sign Up".equals(signupBtn.getText())) {
			Socket socket = null;
			DataOutputStream out = null;
			DataInputStream in = null;
			String result = null;

			try {
				socket = new Socket(Controller.ADDRESS, Controller.PORT);
				out = new DataOutputStream(socket.getOutputStream());
				in = new DataInputStream(socket.getInputStream());
				if (socket != null && !password.getText().isEmpty() && password.getText().equals(password.getText())) {
					out.writeUTF("/signup " + login.getText().toLowerCase() + " " + password.getText() + " " + nickname.getText());
					result = in.readUTF();
				} else {
					result = "Пароли не совпадают";
				}
			} catch (IOException e) {
				signupBtn.setText("Нет соединения с сервером");
			}

			resultLabel.setText(result);
			resultLabel.setVisible(true);
			if (resultLabel.getText() != null && resultLabel.getText().contains("Успешная регистрация")) {
				signupBtn.setText("Exit");
			}
		} else {
			exitSignup();
		}
	}

	public void exitSignup() {
		Stage stage = (Stage) login.getScene().getWindow();
		stage.close();
	}
}
