package library.assistant.ui.listbook;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.addbook.BookAddController;
import library.assistant.ui.addmember.AddMemberController;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;

public class BookListController implements Initializable{
	ObservableList<Book> list = FXCollections.observableArrayList();
	@FXML
	private AnchorPane rootPane;
	@FXML
	private TableView<Book> tableView;
	@FXML
	private TableColumn<Book,String> titleCol;
	@FXML
	private TableColumn<Book,String> idCol;
	@FXML
	private TableColumn<Book,String> authorCol;
	@FXML
	private TableColumn<Book,String> publisherCol;
	@FXML
	private TableColumn<Book,Boolean> availabilityCol;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initCol();
		loadData();
	}
	private void initCol()
	{
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
		publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
		availabilityCol.setCellValueFactory(new PropertyValueFactory<>("isAvail"));
	}
	private void loadData() {
		list.clear();
		DatabaseHandler handler = DatabaseHandler.getInstance();
		String qu = "SELECT * FROM BOOK";
		ResultSet rs = handler.execQuery(qu);
		try {
			while(rs.next())
			{
				String title = rs.getString("title");
				String id = rs.getString("id");
				String author = rs.getString("author");
				String publisher = rs.getString("publisher");
				Boolean isAvail = rs.getBoolean("isAvail");
				
				list.add(new Book(title, id,author,publisher,isAvail));
			}
		}catch(SQLException ex)
		{
			Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		tableView.setItems(list);
	}
	@FXML
	private void handleRefreshOption(ActionEvent event)
	{
		loadData();
	}
	
	public static class Book
	{
		private final SimpleStringProperty title;
		private final SimpleStringProperty id;
		private final SimpleStringProperty author;
		private final SimpleStringProperty publisher;
		private final SimpleBooleanProperty isAvail;
		
		public Book(String title, String id, String author, String publisher, Boolean isAvail)
		{
			this.title = new SimpleStringProperty(title);
			this.id = new SimpleStringProperty(id);
			this.author = new SimpleStringProperty(author);
			this.publisher = new SimpleStringProperty(publisher);
			this.isAvail = new SimpleBooleanProperty(isAvail);	
		}

		public String getTitle() {
			return title.get();
		}

		public String getId() {
			return id.get();
		}

		public String getAuthor() {
			return author.get();
		}

		public String getPublisher() {
			return publisher.get();
		}

		public Boolean getIsAvail() {
			return isAvail.get();
		}
	}
	
	@FXML
	private void handleBookDeleteOption(ActionEvent event)
	{
		//fetch the selected book
		Book selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
	    if(selectedForDeletion == null)
	    {
	    	AlertMaker.showErrorMessage("No Book Selected", "Please select a Book for Deletion first");
	        return;
	    }
	    if (DatabaseHandler.getInstance().isBookAlreadyIssued(selectedForDeletion)) {
            AlertMaker.showErrorMessage("Cant be deleted", "This book is already issued and cant be deleted.");
            return;
        }
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Deleting Book");
	    alert.setContentText("Are you sure you want to delete the book " + selectedForDeletion.getTitle() + " ?");
	    Optional<ButtonType> answer = alert.showAndWait();
	    if(answer.get() == ButtonType.OK)
	    {
           Boolean result =  DatabaseHandler.getInstance().deleteBook(selectedForDeletion);
           if(result)
           {
        	   AlertMaker.showSimpleAlert("Book Deleted", selectedForDeletion.getTitle() + " deleted successfully");
               list.remove(selectedForDeletion);
           }
           else
           {
        	   AlertMaker.showErrorMessage("Failed", selectedForDeletion.getTitle() + " could not be deleted");
           }
	    }
	    else
	    {
	    	AlertMaker.showSimpleAlert("Deletion Cancelled", "Deletion Process Cancelled");
	    }
	}
	@FXML
	private void handleBookEditOption(ActionEvent event)
	{
		Book selectedForEdit = tableView.getSelectionModel().getSelectedItem();
	    if(selectedForEdit == null)
	    {
	    	AlertMaker.showErrorMessage("No Book Selected", "Please select a Book for Deletion first");
	        return;
	    }
	    
	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/assistant/ui/addbook/addbook.fxml"));
			Parent parent = loader.load();
			
			BookAddController controller =(BookAddController )loader.getController();
			controller.inflateUI(selectedForEdit);
			
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Edit Book");
			stage.setScene(new Scene(parent));
			stage.show();
			
			stage.setOnCloseRequest((e)->
			{
				handleRefreshOption(new ActionEvent());
			});
			
		} catch (IOException ex) {
			Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
