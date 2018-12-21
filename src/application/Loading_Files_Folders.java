package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


//import com.gluonhq.charm.glisten.control.ProgressIndicator;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;

public class Loading_Files_Folders 
{
	//Stage popupStage = new Stage(StageStyle.TRANSPARENT);
	
	private final  String[] iminjaBinovi = {"brisenje2.bin", "nesprovedena.bin", "otvorena.bin", "predstecajna.bin", "rociste-ner-model.bin", "sobranie.bin", "zaklucena2.bin", "zaprena.bin"};
	
	public Loading_Files_Folders() 
	{
		super();
		
	}
	
	public void loadingStage(Stage popupStage, Stage mainStage) throws IOException
	{
		
		popupStage.initOwner(mainStage);
		 
		
		 popupStage.initModality(Modality.APPLICATION_MODAL);
         AnchorPane rootce;
         
         rootce = (AnchorPane)FXMLLoader.load(getClass().getResource("Loading.fxml"));	
         //rootce.  mainStage.getWidth(), mainStage.getHeight(), Color.BLUEVIOLET));	
         rootce.setMaxSize(mainStage.getWidth(), mainStage.getHeight());
         
		  Label label = new Label();		
		  label.setText("Почекајте се вчитуваат податоците!");
		  label.setStyle("-fx-font-family: Ebrima; -fx-font-size: 28;");
		  label.setTextFill(Color.BLACK);
		  label.setAlignment(Pos.CENTER);

		  label.setMaxHeight(200);
		  label.setPrefWidth(750);	


		  HBox layout = new HBox();
		  layout.setPrefHeight(240);
		  layout.setPrefWidth(1400);
		  layout.getChildren().addAll(label);
		  layout.setAlignment(Pos.CENTER);

		  HBox layoutHor = new HBox();
		  
		  //posto ne raboti u drugi okolini
		  //ProgressIndicator pi = new ProgressIndicator();
		  //layoutHor.getChildren().add(pi);
		  
		 // WebView web = new WebView(); 
	     // web.getEngine().load("C:\\Users\\ivanam\\eclipse-workspace\\StecajniPostapki\\topcinja.gif"); 
		  
	      //ImageView imageView = new ImageView();
		  try
		  {
			  ClassLoader classLoader = getClass().getClassLoader();
			  InputStream isi = classLoader.getResourceAsStream("loading_icon.gif"); 
			  
		      final ImageView imageView = new ImageView(new Image(isi));
		      layoutHor.getChildren().add( imageView ); 			  
		  }
		 catch(Exception e)
		  {
			 System.out.println("ne go zema gifot za loading-prikaz");
		  }
		  
		  
		  layoutHor.setPrefHeight(50);
		  layoutHor.setPrefWidth(1400);
		  layoutHor.setAlignment(Pos.CENTER);
		  
		 VBox Grupno = new VBox();
		 Grupno.getChildren().addAll(layout, layoutHor);
		 rootce.setStyle("-fx-background-color: white;");
		 		 
		 rootce.getChildren().add(Grupno);		 
		
		 popupStage.setScene(new Scene(rootce, mainStage.getWidth(), mainStage.getHeight(), Color.WHITE));	
			 
		 popupStage.show();
		
		
	}
	
	public void loadingFileSporedTip(File file, String tip) throws Exception
	{
		//SPORED TIPOT NA VCITAN FILE/FOLDER OD MAIN_WINDOW_CONTROLLER SE POVIKUVA SOODVETNA F-JA
		if(tip.compareToIgnoreCase("edinPdf")==0)
		{
			loadingFileSlucaevi(file);
		}
		if(tip.compareToIgnoreCase("pluralPdf")==0)
		{
			loadingFileSlucaeviOdFolder(file);
		}
	}
	
	
     
	public void loadingFileSlucaevi(File file) throws Exception 
	{
		String pateka = "";
    	pateka = file.getAbsolutePath();
    	System.out.println(pateka);
    	
    	if(file.getName().toLowerCase().endsWith("pdf"))
    	{
    		 List<String> dokument;
	    	 
    	     	
 			dokument = getSlucai(PDFtoTEXT(pateka));
 			
 			 System.out.println("----------------Slucai: ----------------");
 			 
 			            
       
              try
              {            	                   
                  ClassLoader classLoader = getClass().getClassLoader();
                  
                 
                  
                  for(String slucaj : dokument)
       			 {
                	  
                        for(String imeBin : iminjaBinovi) 
            			{
                        	//File fijlce = new File("C:\\Users\\ivanam\\eclipse-workspace\\StecajniPostapki\\"+imeBin);
                        	InputStream is = classLoader.getResourceAsStream(imeBin);            				
            				
                        	
            					findStatus(slucaj, is);
            					 System.out.println("----------------Koj file working: -------" + imeBin);
            					
            					//InputStream is = MyTest.class.getResourceAsStream("/test.csv");
            					 
            					 //ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            					 //InputStream is = classloader.getResourceAsStream("test.csv");
            			}
       				
       			 }                  
                  
                  /*File filef = new File(classLoader.getResource("Files").getFile());
                  File[] listingAllFilesf = filef.listFiles();
                  for(File d : listingAllFilesf)
                  {
                	  System.out.println("kravicka: "+d.getName());
                  }
                
                  
                  if (filef == null)
                  {
                      throw new Exception("Cannot find file " + "brisenje2.bin");
                  }
                  else
                  {
                	  System.out.println("Jeeej bumbar!!!" + filef.getName());
                  }
                  
                  for(String slucaj : dokument)
      			 {
           				
                       for (File fijlce : listingAllFilesf) 
           			{
           				 if(fijlce.getName().toLowerCase().endsWith("bin")) 
           				 {
           					findStatus(slucaj, fijlce);
           					 System.out.println("----------------Koj file working: -------" + fijlce.getName());
           					
           				 }
           			}
      				
      			 }*/
                 
              }
              catch (Exception e) 
              {
            	  
                  e.printStackTrace();

              }             
              
      	 
          
              //tuka povikaj procedura za privremeni postapki da gi sredi spored status
              try
              {
            	  	PreparedStatement prep = DBConnector.getConnect().prepareStatement("EXEC dbo.SP_sreduvanje_momentalni_privremeni");
    							
    				prep.executeUpdate();
    				
              }
              catch(Exception e)
              {
            	  System.out.println("Greska pri izvrshuvanje procedura dbo.SP_sreduvanje_momentalni_privremeni");
              }
             
    	}
    	
     
     	 
  }
	
	
	public void loadingFileSlucaeviOdFolder(File fil) throws Exception 
	{
		File[] files = fil.listFiles();
		ClassLoader classLoader = getClass().getClassLoader();
		//zemi gi samo so ekstenzija .pdf
		//if (file.getName().toLowerCase().endsWith("pdf"))	
		
         		
		for (File file : files) 
		{
			if(file.getName().toLowerCase().endsWith("pdf"))
			{
				String pateka = "";
		    	pateka = file.getAbsolutePath();
		    	System.out.println("Nokaut: " + pateka);
		    	
		     	 List<String> dokument;	     	 
		     	
					dokument = getSlucai(PDFtoTEXT(pateka));
					
					 System.out.println("----------------Slucai: ----------------");
					
		     	 
		     	 for(String slucaj : dokument)
					 {
		     					                
		                 for(String imeBin : iminjaBinovi) 
		        			{
		                    	//File fijlce = new File(classLoader.getResource(imeBin).getFile());
		                    	InputStream is = classLoader.getResourceAsStream(imeBin);            				
		        				
		                    	
		        					findStatus(slucaj, is);
		        					//System.out.println("----------------Koj file working: -------" + imeBin);		                        
		                    	      					
                             }
						
					   }
			  }
		
		  }
		
     	 
  }
	
	public static List<String> getSlucai(String str) 
	{
	    List<String> tokens = new ArrayList<>();
	    
	    System.out.println("ovcicki: " + str);
	    
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
	
	public static String PDFtoTEXT(String pateka) throws IOException{			
		 
		  File file = new File(pateka);
	      PDDocument document = PDDocument.load(file);

	      PDFTextStripper pdfStripper = new PDFTextStripper();

	      String text = pdfStripper.getText(document);

	      document.close();
	     
	      return text;

	}  
	
	
	/*public static LinkedHashMap<String, String> findStatus(String content, File filjce) throws IOException 
	{
			  		
				LinkedHashMap<String, String> settt = new LinkedHashMap<String, String>();
				
				sPostapka tmpStecPost = new sPostapka();
					
					try
					{
						InputStream is= new  FileInputStream(filjce);
		       
				TokenNameFinderModel model = new TokenNameFinderModel(is);
			    is.close();
			    		     
			    NameFinderME nameFinder = new NameFinderME(model);
		        
		        String[] tokens = SimpleTokenizer.INSTANCE.tokenize(content);      
		              
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
			        	
			        	
			        	parsingRegex( content, tmpStecPost);
			        	
			        	
			        	
			        for (Entry<String, String> entry : settt.entrySet()) {
			            System.out.println(entry.getKey().toString()+" : "+entry.getValue().toString());
			             
			            parsing(entry.getKey().toString(), entry.getValue().toString(), tmpStecPost);
			        }
					}
			        
			        
		        }
				
				 catch (Exception e) 
					{
						System.out.println("Probajte povtorno, error pri parsiranje slucaj so find i regex!");
						
						//e.printStackTrace();
					}
					
					 try 
					 {
						 if((tmpStecPost.getStatusZakazanoRociste()!=null) && (tmpStecPost.getStatusZakazanoRociste()!=""))
						 {
							 //drugo parsiranje za datum, oti drugoto e default za site
							 System.out.println("dali vlaga za status zakazano rocishte!!!");
							 parsingDatumZakazanoRocishte(content, tmpStecPost);
							 
						 }
						insertiranje(tmpStecPost);
					 }
					 catch (SQLException e) 
					 {
						System.out.println("Probajte povtorno, error pri zapis u baza!");
						
						//e.printStackTrace();
					}
		        
		        return settt;
		        
		        
		   
	}*/
	
	public static LinkedHashMap<String, String> findStatus(String content, InputStream iss) throws IOException 
	{
			  		
				LinkedHashMap<String, String> settt = new LinkedHashMap<String, String>();
				
				sPostapka tmpStecPost = new sPostapka();
					
			try
			 {
				InputStream is= iss;
		       
				TokenNameFinderModel model = new TokenNameFinderModel(is);
			    is.close();
			    		     
			    NameFinderME nameFinder = new NameFinderME(model);
		        
		        String[] tokens = SimpleTokenizer.INSTANCE.tokenize(content);      
		              
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
			        	
			        	
			        	parsingRegex( content, tmpStecPost);
			        	
			        	
			        	
			        for (Entry<String, String> entry : settt.entrySet()) {
			            System.out.println(entry.getKey().toString()+" : "+entry.getValue().toString());
			             
			            parsing(entry.getKey().toString(), entry.getValue().toString(), tmpStecPost);
			        }
					}
			        
			        
		        }
				
				 catch (Exception e) 
					{
						System.out.println("Probajte povtorno, error pri parsiranje slucaj so find i regex!");
						
						//e.printStackTrace();
					}
					
					 try 
					 {
						 if((tmpStecPost.getStatusZakazanoRociste()!=null) && (tmpStecPost.getStatusZakazanoRociste()!=""))
						 {
							 //drugo parsiranje za datum, oti drugoto e default za site
							 System.out.println("dali vlaga za status zakazano rocishte!!!");
							 parsingDatumZakazanoRocishte(content, tmpStecPost);
							 
						 }
						insertiranje(tmpStecPost);
					 }
					 catch (SQLException e) 
					 {
						System.out.println("Probajte povtorno, error pri zapis u baza!");
						
						//e.printStackTrace();
					}
		        
		        return settt;
		        
		        
		   
	}
	
	  
		private static void parsingRegex(String cont, sPostapka tmpStecPost)
		{
			try
			{
				String content=cont;
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
					String tmpResenie = reshenie.replace(",", "");
					tmpStecPost.setResenie(tmpResenie);
					
					//System.out.println("resnenie  " + reshenie + " datum " +datum);
				}
			}
			
			catch(Exception e)
			{
				System.out.println("Problem so parsiranje reshenie i datum!!!");
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
			
			if(tag.equals("embs"))
			{
				value.trim();
				
				if(value.length()==7)
				{						
					tmpStecPost.setEmbs(value);					
				}
				
			}			
			
			if(tag.equals("sobranieNaDoveriteli"))
			{
				tmpStecPost.setSobranie(value);
			}
			
			//////////////////////////////////////////////
			
		}
		
		private static void parsingDatumZakazanoRocishte(String cont, sPostapka tmpStecPost)
		{
			
			try
			{
				String content=cont;				
				content = content.replace(System.getProperty("line.separator"), "");
				
				int startIndex=0;
				int end=0;
				
				
				String datum="";
				
				startIndex=content.indexOf("рочиште");
				content=content.substring(startIndex);
				
				startIndex=content.indexOf("рочиште");
				end=content.indexOf("година");
				
				
				if((startIndex < end) && (startIndex >=0) && (end >=0))
				{
					String izmegu = content.substring(startIndex, end);
					String listaZborcinja[] = izmegu.split(" ");				
					
					datum=listaZborcinja[listaZborcinja.length - 1];					
					tmpStecPost.setDatum(datum);								
					
				}		
			}
			
			catch(Exception e)
			{
				System.out.println("Problem so parsiranje datum za zakazano rocishte!!!");
			}
			
			
				
		}
		
		private static void insertiranje(sPostapka tmpStecPost) throws SQLException
		{
			 try 
			 {		
				System.out.println("Database Name: " + DBConnector.getConnect().getMetaData().getDatabaseProductName());
				
				
				PreparedStatement prep = DBConnector.getConnect().prepareStatement("EXEC SP_insert_SlucajVoTrajni_ZavisnoSigurniNesigurni ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
				/**/
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
				prep.setString(19, tmpStecPost.getEmbs());				
				prep.setDate(20, parsiranjeDatumOdString(tmpStecPost.getDatum()));	
				
				prep.setString(21,tmpStecPost.getSobranie());
				prep.executeUpdate();
				
				 System.out.println("Nema problem se executira PRIVREMENI zapis u baza!");
				
			} 
			 
			 catch (Exception e) 
			 {
				  System.out.println("DALI RABOTI PROCEDURATA!!!!");
				
				//e.printStackTrace();
			}
			

		}
		
		
		private static java.sql.Date parsiranjeDatumOdString(String datum)
		{
			java.sql.Date datumSQL=null;		
			
			if(datum != null)
			{
				String datumce = null;
				datumce=datum;
				datumce.trim();
				
				String kraenDatum="";
				
				try
				{
					String[] arrayDatum=datumce.split("\\.");
					String day=arrayDatum[0];
					String month=arrayDatum[1];
					String year=arrayDatum[2];
					
					if(day.length() == 1)
					{
						day="0"+day;
					}
					if(month.length() == 1)
					{
						month="0"+month;
					}
					
					kraenDatum = day + "." + month + "." + year;
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
					
					 Date date = (Date) formatter.parse(kraenDatum);
			         System.out.println(date);
			         System.out.println("|_R-|_i-|_g-|_h-|_t-|_D-|_a-|_t-|_e-|" + formatter.format(date));   
			            
			         long millis=date.getTime();  
			         datumSQL=new java.sql.Date(millis); 
					
				}
				catch(Exception e)
				{
					System.out.println("|_W-|_r-|_o-|_n-|_g-|_D-|_a-|_t-|_e-|Dava greska pri parsiranje na datum: "+datum);
					//e.printStackTrace();
					datumSQL=null;
				}
				
			}
			
	        
	        return datumSQL;
	        
		}
	
	
	
}
