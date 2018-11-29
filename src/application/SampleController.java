package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.Map.Entry;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;

public class SampleController implements Initializable
{
	
	  //@FXML
	   // private Button openButton;
	
	@FXML 
	private TableView<privSpostapka> tablePrivremeni;
	
	@FXML 
	private TableView<sPostapka> tableTrajni;
	
	@FXML 
	private TableColumn<privSpostapka, String> col_sud;
	
	@FXML 
	private TableColumn<privSpostapka, String> col_resenie;
	
	@FXML 
	private TableColumn<privSpostapka, String> col_pravnoLice;
	
	@FXML 
	private TableColumn<privSpostapka, String> col_edb;
	
	@FXML 
	private TableColumn<privSpostapka, String> col_datum;
	
	@FXML 
	private TableColumn<privSpostapka, String> col_predStecajna;
	
	@FXML 
	private TableColumn<privSpostapka, String> col_zakazanoRoc;
	
	@FXML 
	private TableColumn<privSpostapka, String> col_otvorena;
	
	@FXML 
	private TableColumn<privSpostapka, String> col_nesprovedena;
	
	@FXML 
	private TableColumn<privSpostapka, String> col_zaklucena;
	
	@FXML 
	private TableColumn<privSpostapka, String> col_brisenjeOdCR;
	
	@FXML 
	private TableColumn<privSpostapka, String> col_zaprena;
	
	@FXML 
	private TableColumn<privSpostapka, CheckBox> col_check;
	
	/////////////////////////////////////////////////////////
	
	@FXML 
	private TableColumn<sPostapka, String> col_sudT;
	
	@FXML 
	private TableColumn<sPostapka, String> col_resenieT;
	
	@FXML 
	private TableColumn<sPostapka, String> col_pravnoLiceT;
	
	@FXML 
	private TableColumn<sPostapka, String> col_edbT;
	
	@FXML 
	private TableColumn<sPostapka, String> col_datumT;
	
	@FXML 
	private TableColumn<sPostapka, String> col_predStecajnaT;
	
	@FXML 
	private TableColumn<sPostapka, String> col_zakazanoRocT;
	
	@FXML 
	private TableColumn<sPostapka, String> col_otvorenaT;
	
	@FXML 
	private TableColumn<sPostapka, String> col_nesprovedenaT;
	
	@FXML 
	private TableColumn<sPostapka, String> col_zaklucenaT;
	
	@FXML 
	private TableColumn<sPostapka, String> col_brisenjeOdCRT;
	
	@FXML 
	private TableColumn<sPostapka, String> col_zaprenaT;
	
		
	
	private ObservableList<privSpostapka> oblist= null;	
	private ObservableList<sPostapka> oblistTrajni= null;
	
	private Stage myStage;
	private ResultSet rs;
	private ResultSet rsTrajni;
	

	
	private void polniGrid() 
	{			
		polnenjeGridPrivremeniOdBaza();					
	}
	
	
	public void setStage(Stage stage) 
	{
	     myStage = stage;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		tablePrivremeni.setVisible(false);
		tableTrajni.setVisible(false);
		polnenjeGridPrivremeniOdBaza();
	}

	  
	  @FXML
	    private void PrebarajPrivremeniPostapki(ActionEvent event) {
	        System.out.println("You clicked me!");
	      
	        final FileChooser fileChooser = new FileChooser();
	        File file = fileChooser.showOpenDialog(myStage);
	        
	        tablePrivremeni.setVisible(false); 
	     	tableTrajni.setVisible(false); 
	        
            if (file != null) 
            {
                //openFile(file);
            	String pateka = "";
            	pateka = file.getAbsolutePath();
            	System.out.println(pateka);
            	
             	 List<String> dokument;
					try 
					{
						dokument = getSlucai(PDFtoTEXT(pateka));
						
						 System.out.println("----------------Slucai: ----------------");
                 	 
                 	 for(String slucaj : dokument)
     				 {
     					
                 		findPredstecajni(slucaj);
                 		findOtvoreni(slucaj);
                 		findZapreni(slucaj);
                 		findSobranieNaDoveriteli(slucaj);
                 		findZakluceniNesprovedeniBriseni(slucaj);
                 		findZakluceniNesprovedeni(slucaj);
     					 
     				 }
                 	 
                 	polniGrid();
                 	tablePrivremeni.setVisible(true); 
                 	tableTrajni.setVisible(false); 
					
					}
					
					catch (IOException e1) 
					{
						//ne naogja pdf so takva pateka, treba da pecati ALERT
						e1.printStackTrace();
					}
            	
            }
	        
	    }
	  
		public static List<String> getSlucai(String str) 

		{
		    List<String> tokens = new ArrayList<>();
		    
		    String[] tokenizer = str.split("\\(\\d+\\)");
		    
		    //StringTokenizer tokenizer = new StringTokenizer(str,("\\([0-9]*\\)"));
		  for(int l=0; l<tokenizer.length; l++) 
		  {
		    	//addnuva string
			  if(tokenizer[l].contains("__________"))
			  {
				  tokenizer[l] = tokenizer[l].replace("__________", "");
			  }
			  
		    	String tmp=tokenizer[l];
		    	
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
	  
		public static LinkedHashMap<String, String> find(String content) throws IOException {
		    
						
	        String workingDir = System.getProperty("user.dir");

	        Path filePath = Paths.get(workingDir+File.separator+"\\src\\Files\\predstecajni-ner-model.bin");

	      	InputStream is= new  FileInputStream(filePath.toString());
	    	
			 
			TokenNameFinderModel model = new TokenNameFinderModel(is);
		    is.close();
		    		     
		    NameFinderME nameFinder = new NameFinderME(model);
	        
	        String[] tokens = SimpleTokenizer.INSTANCE.tokenize(content);
	        
	        LinkedHashMap<String, String> settt = new LinkedHashMap<String, String>();
	        
	        Span spans[] = nameFinder.find(tokens);
	        
	      
	        
	        if(spans.length == 0)
	        {
	        	System.out.println("nema tag");
	        }
	        else
	        {
	        	
	        	String spans2[]= new String[spans.length-1];
	        	
		        for (int i=0; i<spans.length-1; ++i) {
		            spans2[i] =spans[i].toString();
		        }

	        	String[] names = Span.spansToStrings(spans, tokens);      
	        
		        for(int j=0;j<spans2.length-1;j++) {
		        	
		        	settt.put(spans2[j], names[j]);
		        	
		        }
	        
	        //momentalno kreiran objekt za punennje postapka u baza, prazen, u for polnet
		        	sPostapka tmpStecPost = new sPostapka();
		        	
		        	parsingRegex( content, tmpStecPost);
		        	
		        for (Entry<String, String> entry : settt.entrySet()) {
		            System.out.println(entry.getKey().toString()+" : "+entry.getValue().toString());
		             
		            parsing(entry.getKey().toString(), entry.getValue().toString(), tmpStecPost);
		        }
		        
		      
		        try {
					insertiranje(tmpStecPost);
				} catch (SQLException e) {
					//System.out.println("Probajte povtorno, error pri zapis u baza!");
					e.printStackTrace();
				}
		        
		        
	        }
	        
	        return settt;
	        
	        
	   
	}
	  
		private static void insertiranje(sPostapka tmpStecPost) throws SQLException
		{
			 try 
			 {		
				System.out.println("Database Name: " + DBConnector.getConnect().getMetaData().getDatabaseProductName());
				
				PreparedStatement prep = DBConnector.getConnect().prepareStatement("INSERT INTO privremeniPostapki(sud, resenie, dolznik, pravnoLice, edb, stecaenUpravnik, imeStecUpr, dooel, uvozizvoz, drustvo, dejnost, predStecajna, otvorena, statusZakazanoRociste, nesprovedena, zaklucuva, brisenjeOdCR, zapira, sobranieNaDoveriteli, datum) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				prep.setString(1, tmpStecPost.getSud());
				prep.setString(2, tmpStecPost.getResenie().trim());
				prep.setString(3, tmpStecPost.getDolznik());
				prep.setString(4, tmpStecPost.getPravnoLice());
				prep.setString(5, tmpStecPost.getEdb());
				
				prep.setString(6, tmpStecPost.getStecaenUpravnik());
				prep.setString(7, tmpStecPost.getImeStecUpr());
				prep.setString(8, tmpStecPost.getDooel());
				prep.setString(9, tmpStecPost.getUvozizvoz());
				prep.setString(10, tmpStecPost.getDrustvo());
				
				prep.setString(11, tmpStecPost.getDejnost());
				prep.setString(12, tmpStecPost.getPredStecajna());
				prep.setString(13, tmpStecPost.getOtvorena());
				prep.setString(14, tmpStecPost.getStatusZakazanoRociste());
				prep.setString(15, tmpStecPost.getNesprovedena());
				
				prep.setString(16, tmpStecPost.getZaklucuva());
				prep.setString(17, tmpStecPost.getBrisenjeOdCR());
				prep.setString(18, tmpStecPost.getZapira());
				prep.setString(19, tmpStecPost.getSobranienaDoveriteli());
				prep.setString(20, tmpStecPost.getDatum());		
				prep.executeUpdate();
				
			} 
			 
			 catch (Exception e) 
			 {
				  System.out.println("Probajte povtorno, error pri zapis u baza!");
				  if(tmpStecPost.getResenie().contains("58/"))
					{
						e.printStackTrace();
					}
				//e.printStackTrace();
			}
			

		}
	  
		private static void parsingRegex(String content, sPostapka tmpStecPost)
		{
			
			content = content.replace(System.getProperty("line.separator"), "");
			
			int startIndex=0;
			int end=0;
			
			String reshenie="";
			String datum="";
			
			startIndex=content.indexOf("Ст.");
			end=content.indexOf("година");
						
			if((startIndex < end) && (startIndex >=0) && (end >=0))
			{
				String izmegu = content.substring(startIndex, end);
				String listaZborcinja[] = izmegu.split(" ");
				
				reshenie=listaZborcinja[2];
				datum=listaZborcinja[listaZborcinja.length - 1];
				tmpStecPost.setDatum(datum);
				tmpStecPost.setResenie(reshenie);
				
				//System.out.println("resnenie  " + reshenie + " datum " +datum);
			}
			
		
			
				
		}
		
		private static void parsing(String tag, String value, sPostapka tmpStecPost)
		{
			//momentaLNO SREDUVANJE NA TAGCE
			tag=tag.trim();
			String tagArray[] = tag.split(" ");
			tag=tagArray[1];
			tag=tag.trim();
			
			if(tag.equals("sud"))
			{
				tmpStecPost.setSud(value);
			}
			
			if(tag.equals("dolznik"))
			{
				tmpStecPost.setDolznik(value);
			}
			
			if(tag.equals("edb"))
			{
				value.trim();
				
				if(value.length()==13)
				{
					tmpStecPost.setEdb(value);
				}
				
			}
			
			if(tag.equals("pravnoLice"))
			{
				tmpStecPost.setPravnoLice(value);
			}
			
			if(tag.equals("stecaenUpravnik"))
			{
				tmpStecPost.setStecaenUpravnik(value);
			}
			
			if(tag.equals("imeStecUpr"))
			{
				tmpStecPost.setImeStecUpr(value);
			}
			//////////////////////////////////////////////
			
			if(tag.equals("dooel"))
			{
				tmpStecPost.setDooel(value);
			}
			
			if(tag.equals("uvozizvoz"))
			{
				tmpStecPost.setUvozizvoz(value);
			}
			
			if(tag.equals("drustvo"))
			{
				tmpStecPost.setDrustvo(value);
			}
			
			if(tag.equals("dejnost"))
			{
				tmpStecPost.setDejnost(value);
			}
			
			if(tag.equals("otvorena"))
			{
				tmpStecPost.setOtvorena(value);
			}
			//////////////////////////////////////////////
			
			if(tag.equals("predStecajna"))
			{
				tmpStecPost.setPredStecajna(value);
			}
			
			if(tag.equals("statusZakazanoRociste"))
			{
				tmpStecPost.setStatusZakazanoRociste(value);
			}
			
			if(tag.equals("nesprovedena"))
			{
				tmpStecPost.setNesprovedena(value);
			}
			
			if(tag.equals("zaklucuva"))
			{
				tmpStecPost.setZaklucuva(value);
			}
			
			if(tag.equals("brisenjeOdCR"))
			{
				tmpStecPost.setBrisenjeOdCR(value);
			}
			//////////////////////////////////////////////
			
			if(tag.equals("zapira"))
			{
				tmpStecPost.setZapira(value);
			}
			
			if(tag.equals("sobranieNaDoveriteli"))
			{
				tmpStecPost.setSobranienaDoveriteli(value);
			}
			//////////////////////////////////////////////
			
		}
		
		public static String PDFtoTEXT(String pateka) throws IOException{			
			 
			  File file = new File(pateka);
		      PDDocument document = PDDocument.load(file);

		      PDFTextStripper pdfStripper = new PDFTextStripper();

		      String text = pdfStripper.getText(document);

		      document.close();
		     
		      return text;

		}
		
		public static LinkedHashMap<String, String> findPredstecajni(String content) throws IOException {
		    
			
	        String workingDir = System.getProperty("user.dir");

	        Path filePath = Paths.get(workingDir+File.separator+"\\src\\Files\\predstecajni-ner-model.bin");

	      	InputStream is= new  FileInputStream(filePath.toString());
	    	
			 
			TokenNameFinderModel model = new TokenNameFinderModel(is);
		    is.close();
		    		     
		    NameFinderME nameFinder = new NameFinderME(model);
	        
	        String[] tokens = SimpleTokenizer.INSTANCE.tokenize(content);
	        
	        LinkedHashMap<String, String> settt = new LinkedHashMap<String, String>();
	        
	        Span spans[] = nameFinder.find(tokens);
	        
	      
	        
	        if(spans.length == 0)
	        {
	        	System.out.println("nema tag");
	        }
	        else
	        {
	        	
	        	String spans2[]= new String[spans.length-1];
	        	
		        for (int i=0; i<spans.length-1; ++i) {
		            spans2[i] =spans[i].toString();
		        }

	        	String[] names = Span.spansToStrings(spans, tokens);      
	        
		        for(int j=0;j<spans2.length-1;j++) {
		        	
		        	settt.put(spans2[j], names[j]);
		        	
		        }
	        
	        //momentalno kreiran objekt za punenje postapka u baza, prazen, u for polnet
		        	sPostapka tmpStecPost = new sPostapka();
		        	
		        	parsingRegex( content, tmpStecPost);
		        	
		        	/*if(tmpStecPost.getResenie().contains("58/"))
		        	{
		        		System.out.println("Content: "+ content);
		        	}
		        	
		        	System.out.println("resenie: "+ tmpStecPost.getResenie());
		        	*/
		        	
		        for (Entry<String, String> entry : settt.entrySet()) {
		            System.out.println(entry.getKey().toString()+" : "+entry.getValue().toString());
		             
		            parsing(entry.getKey().toString(), entry.getValue().toString(), tmpStecPost);
		        }
		        
		      
		        try {
					insertiranje(tmpStecPost);
				} catch (SQLException e) {
					System.out.println("Probajte povtorno, error pri zapis u baza!");
					if(tmpStecPost.getResenie().contains("58/"))
					{
						e.printStackTrace();
					}
					//e.printStackTrace();
				}
		        
		        
	        }
	        
	        return settt;
	        
	        
	   
	}
		
		public static LinkedHashMap<String, String> findOtvoreni(String content) throws IOException {
		    
			
	        String workingDir = System.getProperty("user.dir");

	        Path filePath = Paths.get(workingDir+File.separator+"\\src\\Files\\otvoreni-ner-model.bin");

	      	InputStream is= new  FileInputStream(filePath.toString());
	    	
			 
			TokenNameFinderModel model = new TokenNameFinderModel(is);
		    is.close();
		    		     
		    NameFinderME nameFinder = new NameFinderME(model);
	        
	        String[] tokens = SimpleTokenizer.INSTANCE.tokenize(content);
	        
	        LinkedHashMap<String, String> settt = new LinkedHashMap<String, String>();
	        
	        Span spans[] = nameFinder.find(tokens);
	        
	      
	        
	        if(spans.length == 0)
	        {
	        	System.out.println("nema tag");
	        }
	        else
	        {
	        	
	        	String spans2[]= new String[spans.length-1];
	        	
		        for (int i=0; i<spans.length-1; ++i) {
		            spans2[i] =spans[i].toString();
		        }

	        	String[] names = Span.spansToStrings(spans, tokens);      
	        
		        for(int j=0;j<spans2.length-1;j++) {
		        	
		        	settt.put(spans2[j], names[j]);
		        	
		        }
	        
	        //momentalno kreiran objekt za punenje postapka u baza, prazen, u for polnet
		        	sPostapka tmpStecPost = new sPostapka();
		        	
		        	parsingRegex( content, tmpStecPost);
		        	
		        for (Entry<String, String> entry : settt.entrySet()) {
		            System.out.println(entry.getKey().toString()+" : "+entry.getValue().toString());
		             
		            parsing(entry.getKey().toString(), entry.getValue().toString(), tmpStecPost);
		        }
		        
		      
		        try {
					insertiranje(tmpStecPost);
				} catch (SQLException e) {
					//System.out.println("Probajte povtorno, error pri zapis u baza!");
					e.printStackTrace();
				}
		        
		        
	        }
	        
	        return settt;
	        
	        
	   
	}

		public static LinkedHashMap<String, String> findZapreni(String content) throws IOException {
		    
						
	        String workingDir = System.getProperty("user.dir");

	        Path filePath = Paths.get(workingDir+File.separator+"\\src\\Files\\zapr.bin");

	      	InputStream is= new  FileInputStream(filePath.toString());
	    	
			 
			TokenNameFinderModel model = new TokenNameFinderModel(is);
		    is.close();
		    		     
		    NameFinderME nameFinder = new NameFinderME(model);
	        
	        String[] tokens = SimpleTokenizer.INSTANCE.tokenize(content);
	        
	        LinkedHashMap<String, String> settt = new LinkedHashMap<String, String>();
	        
	        Span spans[] = nameFinder.find(tokens);
	        
	      
	        
	        if(spans.length == 0)
	        {
	        	System.out.println("nema tag");
	        }
	        else
	        {
	        	
	        	String spans2[]= new String[spans.length-1];
	        	
		        for (int i=0; i<spans.length-1; ++i) {
		            spans2[i] =spans[i].toString();
		        }

	        	String[] names = Span.spansToStrings(spans, tokens);      
	        
		        for(int j=0;j<spans2.length-1;j++) {
		        	
		        	settt.put(spans2[j], names[j]);
		        	
		        }
	        
	        //momentalno kreiran objekt za punenje postapka u baza, prazen, u for polnet
		        	sPostapka tmpStecPost = new sPostapka();
		        	
		        	parsingRegex( content, tmpStecPost);
		        	
		        for (Entry<String, String> entry : settt.entrySet()) {
		            System.out.println(entry.getKey().toString()+" : "+entry.getValue().toString());
		             
		            parsing(entry.getKey().toString(), entry.getValue().toString(), tmpStecPost);
		        }
		        
		      
		        try {
					insertiranje(tmpStecPost);
				} catch (SQLException e) {
					//System.out.println("Probajte povtorno, error pri zapis u baza!");
					e.printStackTrace();
				}
		        
		        
	        }
	        
	        return settt;
	        
	        
	   
	}
		
	public static LinkedHashMap<String, String> findSobranieNaDoveriteli(String content) throws IOException {
    
	
    String workingDir = System.getProperty("user.dir");

    Path filePath = Paths.get(workingDir+File.separator+"\\src\\Files\\SobrDover.bin");

  	InputStream is= new  FileInputStream(filePath.toString());
	
	 
	TokenNameFinderModel model = new TokenNameFinderModel(is);
    is.close();
    		     
    NameFinderME nameFinder = new NameFinderME(model);
    
    String[] tokens = SimpleTokenizer.INSTANCE.tokenize(content);
    
    LinkedHashMap<String, String> settt = new LinkedHashMap<String, String>();
    
    Span spans[] = nameFinder.find(tokens);
    
  
    
    if(spans.length == 0)
    {
    	System.out.println("nema tag");
    }
    else
    {
    	
    	String spans2[]= new String[spans.length-1];
    	
        for (int i=0; i<spans.length-1; ++i) {
            spans2[i] =spans[i].toString();
        }

    	String[] names = Span.spansToStrings(spans, tokens);      
    
        for(int j=0;j<spans2.length-1;j++) {
        	
        	settt.put(spans2[j], names[j]);
        	
        }
    
    //momentalno kreiran objekt za punenje postapka u baza, prazen, u for polnet
        	sPostapka tmpStecPost = new sPostapka();
        	
        	parsingRegex( content, tmpStecPost);
        	
        for (Entry<String, String> entry : settt.entrySet()) {
            System.out.println(entry.getKey().toString()+" : "+entry.getValue().toString());
             
            parsing(entry.getKey().toString(), entry.getValue().toString(), tmpStecPost);
        }
        
      
        try {
			insertiranje(tmpStecPost);
		} catch (SQLException e) {
			//System.out.println("Probajte povtorno, error pri zapis u baza!");
			e.printStackTrace();
		}
        
        
    }
    
    return settt;
    
    

}
		
	public static LinkedHashMap<String, String> findZakluceniNesprovedeniBriseni(String content) throws IOException {
    
	
    String workingDir = System.getProperty("user.dir");

    Path filePath = Paths.get(workingDir+File.separator+"\\src\\Files\\zaklucNesprBr-ner-model.bin");

  	InputStream is= new  FileInputStream(filePath.toString());
	
	 
	TokenNameFinderModel model = new TokenNameFinderModel(is);
    is.close();
    		     
    NameFinderME nameFinder = new NameFinderME(model);
    
    String[] tokens = SimpleTokenizer.INSTANCE.tokenize(content);
    
    LinkedHashMap<String, String> settt = new LinkedHashMap<String, String>();
    
    Span spans[] = nameFinder.find(tokens);
    
  
    
    if(spans.length == 0)
    {
    	System.out.println("nema tag");
    }
    else
    {
    	
    	String spans2[]= new String[spans.length-1];
    	
        for (int i=0; i<spans.length-1; ++i) {
            spans2[i] =spans[i].toString();
        }

    	String[] names = Span.spansToStrings(spans, tokens);      
    
        for(int j=0;j<spans2.length-1;j++) {
        	
        	settt.put(spans2[j], names[j]);
        	
        }
    
    //momentalno kreiran objekt za punenje postapka u baza, prazen, u for polnet
        	sPostapka tmpStecPost = new sPostapka();
        	
        	parsingRegex( content, tmpStecPost);
        	
        for (Entry<String, String> entry : settt.entrySet()) {
            System.out.println(entry.getKey().toString()+" : "+entry.getValue().toString());
             
            parsing(entry.getKey().toString(), entry.getValue().toString(), tmpStecPost);
        }
        
      
        try {
			insertiranje(tmpStecPost);
		} catch (SQLException e) {
			//System.out.println("Probajte povtorno, error pri zapis u baza!");
			e.printStackTrace();
		}
        
        
    }
    
    return settt;
    
    

}
		
	public static LinkedHashMap<String, String> findZakluceniNesprovedeni(String content) throws IOException {
    
	
    String workingDir = System.getProperty("user.dir");

    Path filePath = Paths.get(workingDir+File.separator+"\\src\\Files\\zaklucNespr-ner-model.bin");

  	InputStream is= new  FileInputStream(filePath.toString());
	
	 
	TokenNameFinderModel model = new TokenNameFinderModel(is);
    is.close();
    		     
    NameFinderME nameFinder = new NameFinderME(model);
    
    String[] tokens = SimpleTokenizer.INSTANCE.tokenize(content);
    
    LinkedHashMap<String, String> settt = new LinkedHashMap<String, String>();
    
    Span spans[] = nameFinder.find(tokens);
    
  
    
    if(spans.length == 0)
    {
    	System.out.println("nema tag");
    }
    else
    {
    	
    	String spans2[]= new String[spans.length-1];
    	
        for (int i=0; i<spans.length-1; ++i) {
            spans2[i] =spans[i].toString();
        }

    	String[] names = Span.spansToStrings(spans, tokens);      
    
        for(int j=0;j<spans2.length-1;j++) {
        	
        	settt.put(spans2[j], names[j]);
        	
        }
    
    //momentalno kreiran objekt za punenje postapka u baza, prazen, u for polnet
        	sPostapka tmpStecPost = new sPostapka();
        	
        	parsingRegex( content, tmpStecPost);
        	
        for (Entry<String, String> entry : settt.entrySet()) {
            System.out.println(entry.getKey().toString()+" : "+entry.getValue().toString());
             
            parsing(entry.getKey().toString(), entry.getValue().toString(), tmpStecPost);
        }
        
      
        try {
			insertiranje(tmpStecPost);
		} catch (SQLException e) {
			//System.out.println("Probajte povtorno, error pri zapis u baza!");
			e.printStackTrace();
		}
        
        
    }
    
    return settt;
    
    

}
		
		
	private void polnenjeGridPrivremeniOdBaza()
		{
			Connection conn = DBConnector.getConnect();
			oblist = FXCollections.observableArrayList();
			//ova e za novo vcituvanje
			try 
			{
				rs = conn.createStatement().executeQuery("select id, sud, resenie, pravnoLice, edb, datum, predStecajna, statusZakazanoRociste, otvorena, nesprovedena, zaklucuva, brisenjeOdCR, zapira from privremeniPostapki");
				
				while(rs.next())
				{
					CheckBox tmp = new CheckBox(null);
					oblist.add(new privSpostapka(rs.getString("sud"), rs.getString("resenie"), rs.getString("pravnoLice"),  rs.getString("edb"), rs.getString("datum"), rs.getString("predStecajna"),
					 rs.getString("statusZakazanoRociste"), rs.getString("otvorena"), rs.getString("nesprovedena"), rs.getString("zaklucuva"), rs.getString("brisenjeOdCR"), rs.getString("zapira"), tmp, rs.getInt("id")));
				}
				
			} 
			catch (SQLException e) 
			{			
				e.printStackTrace();
			}
			
			
			col_sud.setCellValueFactory(new PropertyValueFactory<>("sud"));
			col_resenie.setCellValueFactory(new PropertyValueFactory<>("resenie"));
			col_pravnoLice.setCellValueFactory(new PropertyValueFactory<>("pravnoLice"));
			col_edb.setCellValueFactory(new PropertyValueFactory<>("edb"));
			
			col_datum.setCellValueFactory(new PropertyValueFactory<>("datum"));
			col_predStecajna.setCellValueFactory(new PropertyValueFactory<>("predStecajna"));
			col_zakazanoRoc.setCellValueFactory(new PropertyValueFactory<>("statusZakazanoRociste"));
			col_otvorena.setCellValueFactory(new PropertyValueFactory<>("otvorena"));
			
			col_nesprovedena.setCellValueFactory(new PropertyValueFactory<>("nesprovedena"));
			col_zaklucena.setCellValueFactory(new PropertyValueFactory<>("zaklucuva"));
			col_brisenjeOdCR.setCellValueFactory(new PropertyValueFactory<>("brisenjeOdCR"));
			col_zaprena.setCellValueFactory(new PropertyValueFactory<>("zapira"));
			col_check.setCellValueFactory(new PropertyValueFactory<>("check"));
			
			
			tablePrivremeni.setItems(oblist);
		}
	
	private void polnenjeGridTrajni()
	{
		Connection conn = DBConnector.getConnect();
		oblistTrajni = FXCollections.observableArrayList();
		try 
		{
			rsTrajni = conn.createStatement().executeQuery("select id, sud, resenie, pravnoLice, edb, datum, predStecajna, statusZakazanoRociste, otvorena, nesprovedena, zaklucuva, brisenjeOdCR, zapira from trajniPostapki");
			
			while(rsTrajni.next())
			{
				System.out.println("Dali vlaga tuka");
				oblistTrajni.add(new sPostapka(rsTrajni.getString("sud"), rsTrajni.getString("resenie"), rsTrajni.getString("pravnoLice"),  rsTrajni.getString("edb"), rsTrajni.getString("datum"), rsTrajni.getString("predStecajna"),
				 rsTrajni.getString("statusZakazanoRociste"), rsTrajni.getString("otvorena"), rsTrajni.getString("nesprovedena"), rsTrajni.getString("zaklucuva"), rsTrajni.getString("brisenjeOdCR"), rsTrajni.getString("zapira"), rsTrajni.getInt("id")));
			}
			
		} 
		catch (SQLException e) 
		{			
			e.printStackTrace();
		}
		
		
		col_sudT.setCellValueFactory(new PropertyValueFactory<>("sud"));
		col_resenieT.setCellValueFactory(new PropertyValueFactory<>("resenie"));
		col_pravnoLiceT.setCellValueFactory(new PropertyValueFactory<>("pravnoLice"));
		col_edbT.setCellValueFactory(new PropertyValueFactory<>("edb"));
		
		col_datumT.setCellValueFactory(new PropertyValueFactory<>("datum"));
		col_predStecajnaT.setCellValueFactory(new PropertyValueFactory<>("predStecajna"));
		col_zakazanoRocT.setCellValueFactory(new PropertyValueFactory<>("statusZakazanoRociste"));
		col_otvorenaT.setCellValueFactory(new PropertyValueFactory<>("otvorena"));
		
		col_nesprovedenaT.setCellValueFactory(new PropertyValueFactory<>("nesprovedena"));
		col_zaklucenaT.setCellValueFactory(new PropertyValueFactory<>("zaklucuva"));
		col_brisenjeOdCRT.setCellValueFactory(new PropertyValueFactory<>("brisenjeOdCR"));
		col_zaprenaT.setCellValueFactory(new PropertyValueFactory<>("zapira"));
		
		
		
		tableTrajni.setItems(oblistTrajni);
	}
	
	private void polnenjeGridPrivremeniOdSet()
	{
		/*
		Connection conn = DBConnector.getConnect();
		
		//ova e za novo vcituvanje
		try 
		{
			rs = conn.createStatement().executeQuery("select id, sud, resenie, pravnoLice, edb, datum, predStecajna, statusZakazanoRociste, otvorena, nesprovedena, zaklucuva, brisenjeOdCR, zapira from privremeniPostapki");
			
			while(rs.next())
			{
				CheckBox tmp = new CheckBox(null);
				oblist.add(new privSpostapka(rs.getString("sud"), rs.getString("resenie"), rs.getString("pravnoLice"),  rs.getString("edb"), rs.getString("datum"), rs.getString("predStecajna"),
				 rs.getString("statusZakazanoRociste"), rs.getString("otvorena"), rs.getString("nesprovedena"), rs.getString("zaklucuva"), rs.getString("brisenjeOdCR"), rs.getString("zapira"), tmp, rs.getInt("id")));
			}
			
		} 
		catch (SQLException e) 
		{			
			e.printStackTrace();
		}
		
		
		col_sud.setCellValueFactory(new PropertyValueFactory<>("sud"));
		col_resenie.setCellValueFactory(new PropertyValueFactory<>("resenie"));
		col_pravnoLice.setCellValueFactory(new PropertyValueFactory<>("pravnoLice"));
		col_edb.setCellValueFactory(new PropertyValueFactory<>("edb"));
		
		col_datum.setCellValueFactory(new PropertyValueFactory<>("datum"));
		col_predStecajna.setCellValueFactory(new PropertyValueFactory<>("predStecajna"));
		col_zakazanoRoc.setCellValueFactory(new PropertyValueFactory<>("statusZakazanoRociste"));
		col_otvorena.setCellValueFactory(new PropertyValueFactory<>("otvorena"));
		
		col_nesprovedena.setCellValueFactory(new PropertyValueFactory<>("nesprovedena"));
		col_zaklucena.setCellValueFactory(new PropertyValueFactory<>("zaklucuva"));
		col_brisenjeOdCR.setCellValueFactory(new PropertyValueFactory<>("brisenjeOdCR"));
		col_zaprena.setCellValueFactory(new PropertyValueFactory<>("zapira"));
		col_check.setCellValueFactory(new PropertyValueFactory<>("check"));
		*/
		
		tablePrivremeni.setItems(oblist);
	}


@FXML
public void ispolniTrajni(ActionEvent event)
{
	
	ObservableList<privSpostapka> lista = tablePrivremeni.getItems();
	
	ArrayList<Integer> listaIDzaDelete = new ArrayList<Integer>();
	
	
	for(int i=0; i<lista.size(); i++)
	{
		if(lista.get(i).getCheck().isSelected())
		{
			//tuka insert od prevremeniPostapki vo trajniPostapki
			System.out.println("ZNACI GI PRINTA: " + lista.get(i).getId());
			System.out.println("Kolkava e listata: " + lista.size());
			
			 try 
			 {		
				 //da gi vnese shtiklirani vo trajni
				System.out.println("Database Name: " + DBConnector.getConnect().getMetaData().getDatabaseProductName());
				
				PreparedStatement prep = DBConnector.getConnect().prepareStatement("insert into trajniPostapki select sud, resenie, dolznik, pravnoLice, edb, 	stecaenUpravnik, imeStecUpr, dooel, uvozizvoz, drustvo, dejnost, predStecajna, otvorena, statusZakazanoRociste, nesprovedena, zaklucuva, brisenjeOdCR, zapira, sobranieNaDoveriteli, datum from privremeniPostapki where privremeniPostapki.id=?");
				prep.setInt(1, lista.get(i).getId());				
				prep.executeUpdate();
				
				listaIDzaDelete.add(lista.get(i).getId());
				
				
				
				
			} 
			 
			 catch (Exception e) 
			 {
				  System.out.println("Probajte povtorno, error pri insert u baza trajniPostapki!");
				
				//e.printStackTrace();
			 }
			 
			 
		}
	}
	
	Predicate<privSpostapka> postapkaPredicate = p-> p.getCheck().isSelected();
	lista.removeIf(postapkaPredicate);
	
	for(Integer j : listaIDzaDelete)
	{
		System.out.println("ID za brisenje "+ j);
		
		try 
		{
			//da gi trgne stikliranite od privremeni vo tabelata				
			PreparedStatement prepDelete = DBConnector.getConnect().prepareStatement("delete from privremeniPostapki where privremeniPostapki.id=?");
			prepDelete.setInt(1, j);
			prepDelete.executeUpdate();
			
						
			
		}

		catch (Exception e) 
		{
			System.out.println("Probajte povtorno, error pri delete od privremeniPostapki!");	
			//e.printStackTrace();
		}
		
		
		
	}

	listaIDzaDelete.clear();
	polnenjeGridTrajni();	
	tablePrivremeni.setVisible(false); 
 	tableTrajni.setVisible(true); 
	
	
	
	

	
}

@FXML
public void PrikaziPrivremeniPostapki(ActionEvent event)
{
	tablePrivremeni.setVisible(true); 
 	tableTrajni.setVisible(false); 
 	polnenjeGridPrivremeniOdSet();
}

@FXML
public void PrikaziTrajniPostapki(ActionEvent event)
{
	tablePrivremeni.setVisible(false); 
 	tableTrajni.setVisible(true); 
	polnenjeGridTrajni();
}







		
}
