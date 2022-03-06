package library.assistant.settings;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SettingsController implements Initializable{
	
	@FXML
	private TextField nDaysWithoutFine;
	@FXML
	private TextField finePerDay;
	@FXML
	private TextField username;
	@FXML
	private TextField password;
	@FXML
	private AnchorPane rootPane;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initDefaultValues();
	}
	
	@FXML
	private void handleSaveButtonAction(ActionEvent event)
	{
		int ndays = Integer.parseInt(nDaysWithoutFine.getText());
	    float fine = Float.parseFloat(finePerDay.getText());
		String uname = username.getText();
		String pass = password.getText();
	    
	    Preferences preferences = Preferences.getPreferences();
	    preferences.setnDaysWithoutFine(ndays);
	    preferences.setFinePerDay(fine);
	    preferences.setUsername(uname);
	    preferences.setPassword(pass);
	    
	    Preferences.writePreferenceToFile(preferences);
	    
	}
	
	@FXML
	private void handleCancelButtonAction(ActionEvent event)
	{
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
	}
	
	private void initDefaultValues()
	{
		Preferences preferences = Preferences.getPreferences();
		nDaysWithoutFine.setText(String.valueOf(preferences.getnDaysWithoutFine()));
		finePerDay.setText(String.valueOf(preferences.getFinePerDay()));
		username.setText(String.valueOf(preferences.getUsername()));
		password.setText(String.valueOf(preferences.getPassword()));
	}
}
