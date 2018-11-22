package application;
	
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.apache.fontbox.afm.AFMParser;


public class Main extends Application 
{
	Stage window;
	Scene scene1, scene2;
	
	
	
	public static void main(String[] args) 
	{
		//System.setProperty("file.encoding","UTF-8");	
		launch(args);
	}
	
	
	
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			
			 
				 

				 List<String> dokument = getSlucai(PDFtoTEXT());
				 
				 System.out.println("----------------Slucai: ----------------");
	 
				 for(String slucaj : dokument)
				 {
					//find(slucaj);
					//System.out.printf("%n"); 
					 System.out.println(slucaj);
					 
				 }
				 

		
		      
		      
			
			window=primaryStage;
			primaryStage.setTitle("Voila probno");
			
			//window.setOnCloseRequest(e -> closeProgram());
			HBox topMenu = new HBox();
			Button btnA = new Button("File");
			Button btnB = new Button("Edit");
			Button btnC = new Button("View");
			topMenu.getChildren().addAll(btnA, btnB, btnC);
			
			VBox leftMenu = new VBox();
			Button btnD = new Button("D");
			Button btnE = new Button("E");
			Button btnF = new Button("F");
			leftMenu.getChildren().addAll(btnD, btnE, btnF);
			
			BorderPane borderPane = new BorderPane();
			borderPane.setTop(topMenu);
			borderPane.setLeft(leftMenu);		
			
			scene1 = new Scene(borderPane, 300, 250);
			
			window.setScene(scene1);
			window.show();
			
		} 		
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static String PDFtoTEXT() throws IOException{
		
		  
        
		  File file = new File("C:\\Users\\ivanam\\Downloads\\Stecajni postapki 2018\\Stecajni postapki 2018\\SLU@BEN VESNIK NA RM br. 2-STE^AJNI POSTAPKI.pdf");
	      PDDocument document = PDDocument.load(file);

	      PDFTextStripper pdfStripper = new PDFTextStripper();

	      String text = pdfStripper.getText(document);

	      document.close();
	     
	      return text;

	}
	

	public static List<String> getSlucai(String str) {
	    List<String> tokens = new ArrayList<>();
	    StringTokenizer tokenizer = new StringTokenizer(str,("\\([0-9]*\\)"));
	    while (tokenizer.hasMoreElements()) {
	    	//addnuva string
	    	String tmp=tokenizer.nextToken();
	    	
	    	//tuka sredi gi so na kraj -
	    	String[] lines = tmp.split(System.getProperty("line.separator"));
	    	String posledenZbor="";
	    	
	    	StringBuilder odnovoTmp=new StringBuilder("");	    	
	    	
	    	for(int i=0; i<lines.length; i++)
	    	{
	    		lines[i] = posledenZbor + lines[i];
	    		posledenZbor="";
	    		
	    		if(lines[i].endsWith("-")) //recenicata mi zavrsuva so tire
	    		{
	    			String[] arr = lines[i].split(" ");
	    			posledenZbor = (arr[arr.length - 1]).replace("-", "");
	    			//go kratam posledniot zbor od line
	    			String[] pokratkaArray = Arrays.copyOf(arr, arr.length - 1);
	    			StringBuilder tmpLine=new StringBuilder("");
	    			for(int j=0; j<pokratkaArray.length; j++)
	    			{
	    				tmpLine.append(pokratkaArray[j]).append(" ");
	    			}
	    			lines[i] = tmpLine.toString() ;
	    			
	    		}
	    		else //obicna recenica, bez tire nakraj
	    		{
	    			
	    		}
	    		
	    		
	    		odnovoTmp.append(lines[i]);
	    		odnovoTmp.append(System.getProperty("line.separator"));
	    	}
	    	
	        tokens.add(odnovoTmp.toString());
	        
	    }
	    return tokens;
	    
	
	}
	
	
	private void closeProgram()
	{
		Boolean r = ConfirmBox.display("Blu bla blo", "Sure you want to go?");
		if(r==true)
		{
			window.close();
		}
		else
		{
			
		}
		
	}
	
	
}





	/*@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			window=primaryStage;
			primaryStage.setTitle("Voila probno");
			
			window.setOnCloseRequest(e -> 
			{
				//ocekuvano e posto e default event ali so consume go stopira
				e.consume();
				closeProgram();
			});
			
			Button btn=new Button();
			btn.setText("Klik");
			btn.setOnAction(e -> closeProgram());
			
			VBox layout1 = new VBox(100);
			layout1.getChildren().addAll(btn);
			scene1=new Scene(layout1, 400, 400);
			window.setScene(scene1);
			window.show();*/
			
			//Label label1 = new Label("welcome to the first scene!");
			/*Button btn;			
			btn=new Button();
			btn.setText("odi na scena 2");*/
			
			//btn.setOnAction(e -> window.setScene(scene2));
			/*VBox layout1 = new VBox(20);
			layout1.getChildren().addAll(btn);*/
			
			//scene1=new Scene(layout1, 200, 200);			
			/*Scene scene = new Scene(layout, 300, 250);
			primaryStage.setScene(scene);
			primaryStage.show();*/
			
			/*Button btn1=new Button();
			btn1.setText("This sucks, go back!");
			btn1.setOnAction(e -> window.setScene(scene1));
			
			StackPane layout = new StackPane();
			layout.getChildren().add(btn1);
			scene2=new Scene(layout, 600, 300);
			
			
			window.setScene(scene1);
			window.show();*/
	/*		
		} 		
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private void closeProgram()
	{
		Boolean r = ConfirmBox.display("Blu bla blo", "Sure you want to go?");
		if(r==true)
		{
			window.close();
		}
		else
		{
			
		}
		
	}
	*/