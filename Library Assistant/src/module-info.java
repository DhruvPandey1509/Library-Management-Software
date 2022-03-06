module LibraryAssistant {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	requires com.jfoenix;
	requires java.sql;
	requires java.desktop;
	requires org.apache.derby.engine;
	requires com.google.gson;
	requires commons.codec;
	
	opens library.assistant.ui.addbook to javafx.graphics, javafx.fxml,com.jfoenix, java.sql,javafx.base;
	opens library.assistant.ui.addmember to javafx.graphics, javafx.fxml,com.jfoenix, java.sql,javafx.base;
	opens library.assistant.ui.listbook to javafx.graphics, javafx.fxml,com.jfoenix, java.sql, javafx.base;
	opens library.assistant.ui.listmember to javafx.graphics, javafx.fxml,com.jfoenix, java.sql, javafx.base;
	opens library.assistant.ui.main to javafx.graphics, javafx.fxml,com.jfoenix, java.sql, javafx.base;
	opens library.assistant.settings to javafx.graphics, javafx.fxml,com.jfoenix, java.sql, javafx.base,com.google.gson,commons.codec;
	opens library.assistant.ui.login to javafx.graphics, javafx.fxml,com.jfoenix, java.sql, javafx.base,com.google.gson,commons.codec;
	opens library.assistant.database to java.sql,javafx.base;
}
