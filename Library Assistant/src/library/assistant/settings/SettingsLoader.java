package library.assistant.settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;

public class SettingsLoader extends Application{
	
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.setTitle("Settings");
		stage.show();

		new Thread(() -> {
			DatabaseHandler.getInstance();
	       }).start();
	      
	}
	public static void main(String[] args) {
	   
		launch(args);

	}
}
