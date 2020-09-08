package com.yedam.book;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BookMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BookList.fxml"));
		BorderPane bp = loader.load();
		
		BookController controller = loader.getController();
		controller.setPrimaryStage(primaryStage);

		Scene scene = new Scene(bp);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
