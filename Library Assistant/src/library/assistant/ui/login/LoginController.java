package library.assistant.ui.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.digest.DigestUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.settings.Preferences;
import library.assistant.ui.main.MainController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.scene.control.PasswordField;

public class LoginController implements Initializable{
	@FXML
	private Label titleLabel;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	
	Preferences preference;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		preference = Preferences.getPreferences();
	}
	@FXML
	private void handleLoginButtonAction()
	{
        titleLabel.setText("Library Assistant Login");
        titleLabel.setStyle("-fx-background-color:black;-fx-text-fill:white");
        
        String uname = username.getText();
        String pword = DigestUtils.sha1Hex(password.getText());
        
        if(uname.equals(preference.getUsername()) && pword.equals(preference.getPassword()))
        {
        	closeStage();
        	loadMain();
        }
        else
        {
        	titleLabel.setText("Invalid Credentials");
        	titleLabel.setStyle("-fx-background-color :#F44336; -fx-text-fill:white");
        }
	}
	
	@FXML
	private void handleCancelButtonAction()
	{
       System.exit(0);
	}
	private void closeStage()
	{
		Stage stage = ((Stage)username.getScene().getWindow());
		stage.close();
	}
	void loadMain()
    {
    	try {
			Parent parent = FXMLLoader.load(getClass().getResource("/library/assistant/ui/main/main.fxml"));
			Stage stage = new Stage(StageStyle.DECORATED);
			Scene scene = new Scene(parent);
			Image icon = new Image("LibraryAssistant.png");
			stage.getIcons().add(icon);
			stage.setTitle("Library Assistant");
			stage.setScene(scene);
			stage.show();
		} catch (IOException ex) {
			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
		}
    	
    }

}
