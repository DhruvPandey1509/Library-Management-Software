package library.assistant.ui.addmember;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.listmember.MemberListController;
import library.assistant.ui.listmember.MemberListController.Member;

public class AddMemberController implements Initializable{
	
	DatabaseHandler handler;
	@FXML
	private AnchorPane rootPane;
	@FXML
	private TextField name;
	@FXML
	private TextField id;
	@FXML
	private TextField mobile;
	@FXML
	private TextField email;
	@FXML
	private JFXButton saveButton;
	@FXML
	private JFXButton cancelButton;
	
	private Boolean isInEditMode = Boolean.FALSE;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
			handler = DatabaseHandler.getInstance(); 
	}
	@FXML
	public void addMember(ActionEvent event) {
		String mName = name.getText();
		String mID = id.getText();
		String mMobile = mobile.getText();
		String mEmail = email.getText();
		
        Boolean flag = mName.isEmpty() || mID.isEmpty() || mMobile.isEmpty() || mEmail.isEmpty();
        if(flag)
        {
        	AlertMaker.showErrorMessage("Cant process Member", "Please Enter in all Fields");
   		 return;
        }        
        if(isInEditMode)
        {
        	handleUpdateMember();
        	return;
        }  
        String st = "INSERT INTO MEMBER VALUES ("+
        		 "'" + mID + "'," + 
        		 "'" + mName + "'," +
        		 "'" + mMobile + "'," + 
        		 "'" + mEmail + "'" +
        		 ")";
        
        System.out.println(st);        
		if(handler.execAction(st)) {
			 Alert alert = new Alert(Alert.AlertType.INFORMATION);
	   		 alert.setHeaderText(null);
	   		 alert.setContentText("Saved");
	   		 alert.showAndWait();
		}
		else
		{
			 Alert alert = new Alert(Alert.AlertType.ERROR);
	   		 alert.setHeaderText(null);
	   		 alert.setContentText("Error Occured");
	   		 alert.showAndWait();
		}
	}
	
	@FXML
	public void cancel(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
		
	}
	
	 public void infalteUI(MemberListController.Member member) {
	        name.setText(member.getName());
	        id.setText(member.getId());
	        id.setEditable(false);
	        mobile.setText(member.getMobile());
	        email.setText(member.getEmail());
	        
	        isInEditMode = Boolean.TRUE;
	    }
	 
	 private void handleUpdateMember() {
	        Member member = new MemberListController.Member(name.getText(), id.getText(), mobile.getText(), email.getText());
	        if (DatabaseHandler.getInstance().updateMember(member)) {
                 AlertMaker.showSimpleAlert("Success", "Member Updated");
	        } else {
                 AlertMaker.showErrorMessage("Failed", "Cant Update Member");
	        }
	    }
	
}
