package library.assistant.ui.main;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.effects.JFXDepthManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;

public class MainController implements Initializable{
	
	@FXML
	private HBox book_info;
	
	@FXML
	private HBox member_info;

	@FXML
	private Text bookName;
	
	@FXML
	private Text bookAuthor;
	
	@FXML
	private Text bookStatus;
	
	@FXML
	private TextField bookIDInput;
	
	@FXML
	private Text memberName;
	
	@FXML
	private Text memberMobile;
	
	@FXML
	private Text memberEmail;
	
	@FXML
	private TextField memberIDInput;
	
	@FXML
	private TextField bookID;
	
	@FXML
	private ListView<String> issueDataList;
	
	@FXML
	private StackPane rootPane;
	
	boolean isReadyForSubmission = false;
	
	DatabaseHandler databaseHandler;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		JFXDepthManager.setDepth(book_info, 1);
		JFXDepthManager.setDepth(member_info, 1);
		databaseHandler = DatabaseHandler.getInstance();
	}
	
	@FXML
	private void loadAddMember(ActionEvent event)
	{
		loadWindow("/library/assistant/ui/addmember/addmember.fxml", "Add New Member");
	}
	@FXML
	private void loadListMember(ActionEvent event)
	{
		loadWindow("/library/assistant/ui/listmember/memberlist.fxml", "Member List");
	}
	@FXML
	private void loadAddBook(ActionEvent event)
	{
		loadWindow("/library/assistant/ui/addbook/addbook.fxml", "Add New Book");
	}
	@FXML
	private void loadListBook(ActionEvent event)
	{
		loadWindow("/library/assistant/ui/listbook/booklist.fxml","Book List");
	}
	@FXML
	private void loadSettings(ActionEvent event)
	{
		loadWindow("/library/assistant/settings/settings.fxml","Settings");
	}
	
	
    void loadWindow(String loc, String title)
    {
    	try {
			Parent parent = FXMLLoader.load(getClass().getResource(loc));
			Stage stage = new Stage(StageStyle.DECORATED);
			Scene scene = new Scene(parent);
			
			Image icon = new Image("LibraryAssistant.png");
			stage.getIcons().add(icon);
			stage.setTitle(title);
//			stage.setScene(new Scene(parent));
			stage.setScene(scene);
			stage.show();
		} catch (IOException ex) {
			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
		}  	
    } 
    @FXML
    private void loadBookInfo(ActionEvent event)
    {
    	String id = bookIDInput.getText();
    	String qu = "SELECT * FROM BOOK WHERE id = '"+ id + "'";
    	ResultSet rs = databaseHandler.execQuery(qu);
    	Boolean flag = false;
    	try {
			while(rs.next())
			{
				String bName = rs.getString("title");
				String bAuthor = rs.getString("author");
				Boolean bStatus = rs.getBoolean("isAvail");
				
				bookName.setText(bName);
				bookAuthor.setText(bAuthor);
				
				String status = (bStatus) ? "Available" : "Uavailable";
				bookStatus.setText(status);
				
				flag = true;
			}
			if(!flag)
			{
				bookName.setText("No Such Book Available");
				bookAuthor.setText("Unavailable");
				bookStatus.setText("Unavailable");
			}
		} catch (SQLException ex) {
			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
    @SuppressWarnings("deprecation") 
	@FXML
    private void loadBookInfo2(ActionEvent event)
    {
    	ObservableList<String> issueData = FXCollections.observableArrayList();
    	isReadyForSubmission = false;
    	String id = bookID.getText();
    	String qu = "SELECT * FROM ISSUE WHERE bookID = '" + id + "'";
    	ResultSet rs = databaseHandler.execQuery(qu);
    	try {
    		while(rs.next())
    		{
    			String mBookID = id;
    			String mMemberID = rs.getString("memberID");
    			Timestamp mIssueTime = rs.getTimestamp("issueTime");
    			int mRenewCount = rs.getInt("renew_count");
    			
    			issueData.add("Issue Date and Time : " + mIssueTime.toGMTString());
    			issueData.add("Renew Count : " + mRenewCount);  
    			
    			
    			issueData.add("Book Information :- ");
    			qu = "SELECT * FROM BOOK WHERE ID = '" + mBookID + "'";
    			ResultSet r1 = databaseHandler.execQuery(qu);
    			while(r1.next()) 
    			{
    				issueData.add("Book Name :- " + r1.getString("title"));
    				issueData.add("Book ID :- " + r1.getString("id"));
    				issueData.add("Book Author :- " + r1.getString("author"));
    				issueData.add("Book Publisher :- " + r1.getString("publisher"));
    				
    			}
    			
    			qu = "SELECT * FROM MEMBER WHERE ID = '" + mMemberID + "'";
    			r1 = databaseHandler.execQuery(qu);
    			
    			issueData.add("Member Information :- ");
    			while(r1.next())
    			{
    				issueData.add("Member Name :- " + r1.getString("name"));
    				issueData.add("Mobile :- " + r1.getString("mobile"));
    				issueData.add("Email :- " + r1.getString("email"));
    			}
    			
    			isReadyForSubmission = true;
    		}
    	}catch(SQLException ex)
    	{
    		Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
    	} 
    	
    	issueDataList.getItems().setAll(issueData);
    }

    @FXML
    private void loadMemberInfo(ActionEvent event)
    {
    	String id = memberIDInput.getText();
    	String qu = "SELECT * FROM MEMBER WHERE id = '"+ id + "'";
    	ResultSet rs = databaseHandler.execQuery(qu);
    	Boolean flag = false;
    	try {
			while(rs.next())
			{
				String mName = rs.getString("name");
				String mMobile = rs.getString("mobile");
				String mEmail = rs.getString("email");
				
				memberName.setText(mName);
				memberMobile.setText(mMobile);
				memberEmail.setText(mEmail);
				
				flag = true;
			}
			if(!flag)
			{
				memberName.setText("No Such Member Exist");
				memberMobile.setText("Unavailable");
				memberEmail.setText("Unavailable");
			}
		} catch (SQLException ex) {
			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
    @FXML
    private void loadIssueOperation(ActionEvent event)
    {
    	String memberID = memberIDInput.getText();
    	String bookID = bookIDInput.getText();
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	alert.setTitle("Confirm Issue Operation");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure you want to issue book " + bookName.getText() + "\n to " + memberName.getText() + " ?");
        
    	Optional<ButtonType> response = alert.showAndWait();
    	if(response.get() == ButtonType.OK)
    	{
    		String str = "INSERT INTO ISSUE(memberID, bookID) VALUES ("
    				+ "'" + memberID + "',"
    				+ "'" + bookID + "')";
    		
    		String str1 = "UPDATE BOOK SET isAvail = false WHERE id = '" + bookID +"'";
    	    System.out.println(str + " and " + str1);
    		if(databaseHandler.execAction(str) && databaseHandler.execAction(str1))
    		{
    			Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
    	    	alert1.setTitle("Success");
    	    	alert1.setHeaderText(null);
    	    	alert1.setContentText("Book Issue Complete");
    	    	alert1.showAndWait();
    		}
    		else
    		{
    			Alert alert1 = new Alert(Alert.AlertType.ERROR);
    	    	alert1.setTitle("Failed");
    	    	alert1.setHeaderText(null);
    	    	alert1.setContentText("Issue Operation Failed");
    	    	alert1.showAndWait();
    		}
    	}
    	else
    	{
    		Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        	alert1.setTitle("Cancelled");
        	alert1.setHeaderText(null);
        	alert1.setContentText("Issue operation Cancelled!");
    	}
    }
    
    @FXML
    private void loadSubmissionOperation(ActionEvent event)
    {
    	if(!isReadyForSubmission)
    	{
    		AlertMaker.showErrorMessage("Failed", "Please select a book to submit");
    		return;
    	}
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	alert.setTitle("Confirm Submission Operation");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure you want to Submit book " + bookName.getText() + " that " + memberName.getText() + " have?");
        
    	Optional<ButtonType> response = alert.showAndWait();
    	if(response.get() == ButtonType.OK)
    	{
    	String id = bookID.getText();
    	String ac1 = "DELETE FROM ISSUE WHERE BOOKID = '" + id + "'";
    	String ac2 = "UPDATE BOOK SET ISAVAIL = TRUE WHERE ID = '"+ id +"'";
        
    	if(databaseHandler.execAction(ac1) && databaseHandler.execAction(ac2))
    	{
    		AlertMaker.showSimpleAlert("Success", "Book Has Been Submitted");
    	}
    	else
    	{
    		AlertMaker.showErrorMessage("Failed", "Book Submission Failed");
    	}
    }
    	
    }
    @FXML
    private void loadRenewOperation(ActionEvent event)
    {
    	if(!isReadyForSubmission)
    	{
    		AlertMaker.showErrorMessage("Failed", "Please select a book to renew");
    		return;
    	}
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	alert.setTitle("Confirm Renew Operation");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure you want to Renew book " + bookName.getText() + " that " + memberName.getText() + " have?");
        
    	Optional<ButtonType> response = alert.showAndWait();
    	if(response.get() == ButtonType.OK)
    	{
    		
    		String ac = "UPDATE ISSUE SET issueTime = CURRENT_TIMESTAMP, renew_count = renew_count + 1 WHERE BOOKID = '" + bookID.getText() +"'";
            if(databaseHandler.execAction(ac))
            {
            	AlertMaker.showSimpleAlert("Success", "Book has been Renewed");
            }
            else
            {
            	AlertMaker.showErrorMessage("Failed", "Book Renew Failed");
            }
    	}
    	else
    	{
    		AlertMaker.showErrorMessage("Cancelled", "Submission Operation Cancelled");
    	}
 }  
    @FXML
    private void handleMenuClose(ActionEvent event)
    {
    	Stage stage = ((Stage)rootPane.getScene().getWindow());
    	stage.close();
    }
    @FXML
    private void handleMenuAddBook(ActionEvent event)
    {
    	loadWindow("/library/assistant/ui/addbook/addbook.fxml", "Add New Book");
    }
    @FXML
    private void handleMenuAddMember(ActionEvent event)
    {
    	loadWindow("/library/assistant/ui/addmember/addmember.fxml", "Add New Member");
    }
    
    @FXML
    private void handleMenuViewBook(ActionEvent event)
    {
    	loadWindow("/library/assistant/ui/listbook/booklist.fxml","Book List");
    }
    @FXML
    private void handleMenuViewMember(ActionEvent event)
    {
    	loadWindow("/library/assistant/ui/listmember/memberlist.fxml", "Member List");
    }
    @FXML
    private void handleMenuFullscreen(ActionEvent event)
    {
       Stage stage = ((Stage)rootPane.getScene().getWindow());
       stage.setFullScreen(!stage.isFullScreen());
    }
}
