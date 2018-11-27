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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;

public class SampleController implements Initializable{
	
	  //@FXML
	   // private Button openButton;
	
	@FXML 
	private TableView<?> table;
	
	@FXML 
	private TableColumn<?, ?> col_sud;
	
	@FXML 
	private TableColumn<?, ?> col_resenie;
	
	@FXML 
	private TableColumn<?, ?> col_pravnoLice;
	
	@FXML 
	private TableColumn<?, ?> col_edb;
	
	@FXML 
	private TableColumn<?, ?> col_datum;
	
	@FXML 
	private TableColumn<?, ?> col_predStecajna;
	
	@FXML 
	private TableColumn<?, ?> col_zakazanoRoc;
	
	@FXML 
	private TableColumn<?, ?> col_otvorena;
	
	@FXML 
	private TableColumn<?, ?> col_nesprovedena;
	
	@FXML 
	private TableColumn<?, ?> col_zaklucena;
	
	@FXML 
	private TableColumn<?, ?> col_brisenjeOdCR;
	
	@FXML 
	private TableColumn<?, ?> col_zaprena;
	

	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}
	
	
	private Stage myStage;
	
	public void setStage(Stage stage) {
	     myStage = stage;
	}
	  
	  @FXML
	    private void ClickedClickMe(ActionEvent event) {
	        System.out.println("You clicked me!");
	        //label.setText("Clicked Try me!!");
	        final FileChooser fileChooser = new FileChooser();
	        File file = fileChooser.showOpenDialog(myStage);
	        
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

     					//find(slucaj);
                 		 //za relativnite 
                 		 //getResource("/Files/myfile.txt")
     					//System.out.printf("%n"); 
                 		 
     					 //System.out.println(slucaj);

     					find(slucaj);
     					System.out.printf("%n"); 
     				
     					 
     				 }
                 	 
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
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				//database=databasename; mozda vaka treba
				Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=StecajniPostapki", "sa", "sql&627S");
				//;databaseName=company", "sa", "root");
				
				System.out.println("Database Name: " + connection.getMetaData().getDatabaseProductName());
				PreparedStatement prep = connection.prepareStatement("INSERT INTO privremeniPostapki(sud, resenie, dolznik, pravnoLice, edb, stecaenUpravnik, imeStecUpr, dooel, uvozizvoz, drustvo, dejnost, predStecajna, otvorena, statusZakazanoRociste, nesprovedena, zaklucuva, brisenjeOdCR, zapira, sobranieNaDoveriteli, datum) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				prep.setString(1, tmpStecPost.getSud());
				prep.setString(2, tmpStecPost.getResenie());
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
			 catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
						
			if(startIndex < end)
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

}
