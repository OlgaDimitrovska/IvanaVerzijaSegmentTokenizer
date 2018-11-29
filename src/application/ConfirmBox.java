package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox 
{
	private static boolean answer;
	
	public static boolean display(String title, String message)
	{
		Stage window = new Stage();
		//koga ke go otvori alert-ov da ne mi mrda se dodeka ne go sredi prozorcevo...
		//nema klikanje nastrana
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		//window.setMaxHeight(100);
		
		Label label = new Label();
		label.setText(message);
		
		Button yesButton = new Button("Yes");		
		Button noButton = new Button("No");
		
		yesButton.setOnAction(e  -> {
			answer=true;
			window.close();
		});
		
		noButton.setOnAction(e  -> {
			answer=false;
			window.close();
		});
		
		
		Pane layoutGlaven =new Pane();		
		
		
		HBox layout = new HBox();
		layout.getChildren().addAll(label);
		layout.setAlignment(Pos.CENTER);
		
		HBox layoutHor = new HBox();
		layoutHor.getChildren().addAll(yesButton, noButton);
		layoutHor.setAlignment(Pos.CENTER);
		
		layoutGlaven.getChildren().addAll(layout, layoutHor);
		
		Scene scene = new Scene(layoutGlaven);
		window.setScene(scene);
		window.showAndWait();
		
		return answer;
		
	}
}
