package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
		window.setMaxWidth(350);
		//window.setMaxHeight(100);
		
		Label label = new Label();		
		label.setText(message);
		label.setStyle("-fx-background-color: #00A2D3; -fx-font-family: Ebrima; -fx-font-size: 14;");
		label.setTextFill(Color.WHITE);
		label.setAlignment(Pos.CENTER);
		
		label.setMaxHeight(100);
		label.setPrefWidth(350);
		
		Button yesButton = new Button("Да");		
		Button noButton = new Button("Не");
		
		yesButton.setPrefHeight(50);
		noButton.setPrefHeight(50);
		
		yesButton.setPrefWidth(175);
		noButton.setPrefWidth(175);
		
		yesButton.setOnMouseEntered(e -> yesButton.setStyle("-fx-background-color: #202332;"));
		noButton.setOnMouseEntered(e -> noButton.setStyle("-fx-background-color: #202332;"));
		
		yesButton.setOnMouseExited(e -> yesButton.setStyle("-fx-background-color: transparent;"));
		noButton.setOnMouseExited(e -> noButton.setStyle("-fx-background-color: transparent;"));
						
		yesButton.setTextFill(Color.WHITE);
		noButton.setTextFill(Color.WHITE);
		yesButton.setStyle("-fx-background-color: transparent;");		
		noButton.setStyle("-fx-background-color: transparent;");
		
		yesButton.setOnAction(e  -> {
			answer=true;
			window.close();
		});
		
		noButton.setOnAction(e  -> {
			answer=false;
			window.close();
		});
		
		
		//Pane layoutGlaven =new Pane();		
		
		VBox Grupno = new VBox();
		
		HBox layout = new HBox();
		layout.setPrefHeight(50);
		layout.getChildren().addAll(label);
		layout.setAlignment(Pos.CENTER);
		
		HBox layoutHor = new HBox();
		layoutHor.setStyle("-fx-background-color: #333645");
		layoutHor.setPrefHeight(50);
		layoutHor.getChildren().addAll(yesButton, noButton);
		layoutHor.setAlignment(Pos.CENTER);
		
		Grupno.getChildren().addAll(layout, layoutHor);
		
		Scene scene = new Scene(Grupno);
		window.setScene(scene);
		
		
		FileInputStream inputstream;
		try 
		{
			
			  ClassLoader classLoader = ConfirmBox.class.getClassLoader();
			  InputStream isi = classLoader.getResourceAsStream("blueHHHHH.png");
			  Image image = new Image(isi); 	
			  
			  window.getIcons().add(image);
			  
			  
			
		} 
		catch (Exception e1) 
		{
			System.out.println("Problem so icon za title bar na confirmBox_window");
		} 
		
		window.showAndWait();
		
		return answer;
		
	}
}
