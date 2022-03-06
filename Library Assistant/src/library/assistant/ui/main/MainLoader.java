package library.assistant.ui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;

public class MainLoader extends Application{
	
	@Override
	public void start(Stage stage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("/library/assistant/ui/login/login.fxml"));
		Scene scene = new Scene(root);
		Image icon = new Image("LibraryAssistant.png");
		stage.getIcons().add(icon);
		stage.setScene(scene);
		stage.setTitle("Library Assistant Login");
		stage.show();
		new Thread(() -> {
				DatabaseHandler.getInstance();
		}).start();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
