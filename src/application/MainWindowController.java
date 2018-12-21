package application;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;


import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;



import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;

import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MainWindowController implements Initializable
{
	
	@FXML 
	private TableView<privSpostapka> tablePrivremeni;
	
	private AutoCompletionBinding<String> autoCompletionBindinEdmb;
	
	private AutoCompletionBinding<String> autoCompletionBindinEmbs;
	
	@FXML 
	private AnchorPane privremeni;
	
	@FXML 
	private ImageView lupa;
	
	@FXML 
	private AnchorPane trajni;
	
	@FXML 
	private ToggleButton prikaziPrivremeni;
	
	@FXML 
	private ToggleButton prikaziTrajni;
	
	@FXML
	private RadioButton rbEdmb;
	
	@FXML
	private RadioButton rbEmbs;	
	
	@FXML
	private RadioButton edinPdf;
	
	@FXML
	private RadioButton pluralPdf;	
	
	 @FXML
	 ToggleGroup togPdf;
	
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
	
	@FXML 
	private TableColumn<privSpostapka, String> col_embs;
			
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
	
	@FXML 
	private TableColumn<sPostapka, String> col_embsT;
	
	@FXML 
	private TextField txtEdb;
		
	private ObservableList<privSpostapka> oblist= null;	
	private ObservableList<sPostapka> oblistTrajni= null;
	private ObservableList<String> listaEdb= null;
	private ObservableList<String> listaEmb= null;
	private FilteredList<sPostapka> filter=null; //= new FilteredList(oblistTrajni, e->true);	
	
	private static Stage myStage;
	private ResultSet rs;
	private ResultSet rsTrajni;

	
	
	public static void setStage(Stage stage) 
	{
	     myStage = stage;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		privremeni.setVisible(false);
		trajni.setVisible(false);			
		
		/////////////////////////////////////////////////////////////////////
		polnenjeGridPrivremeniOdBaza();
		////////////////////////////////////////////////////////////////////			
		
		prebaruvanjeEdmbNePojava();
		
	}
	
	@FXML
	public void onEnter(ActionEvent ae)
	{		
		//TXTeDB POLETO ZA AUTOCOMPLETE, NA ENTER GO SORTIRA GRIDOT VO TRAJNI STECAJNI POSTAPKI		
	   
	 
	   SortedList<sPostapka> sort = new SortedList<>(filter);
	   sort.comparatorProperty().bind(tableTrajni.comparatorProperty());
	   tableTrajni.setItems(sort);
	   
	}	
	
	private void polniListaEDBautoComplete()
	{
		Connection conn = DBConnector.getConnect();
		listaEdb = FXCollections.observableArrayList();
		
		//POLNI LISTA SO EDB OD TRAJNI POSTAPKI, ZA AUTOCOMPLETE NA TXTeDB
		
		try 
		{
			rs = conn.createStatement().executeQuery("EXEC dbo.SP_select_EDB_TrajniPostapki");
			
			while(rs.next())
			{					
				String sss=rs.getString("edb");
				if(sss==null)
				{
					
				}
				else					
				listaEdb.add(sss);
			}
			
		} 
		catch (SQLException e) 
		{			
			e.printStackTrace();
		}
	}
	
	private void polniListaEMBSautoComplete()
	{
		Connection conn = DBConnector.getConnect();
		listaEmb = FXCollections.observableArrayList();
		
		//POLNI LISTA SO EMBS OD TRAJNI POSTAPKI, ZA AUTOCOMPLETE NA TXTeDB
		
		try 
		{
			rs = conn.createStatement().executeQuery("EXEC dbo.SP_select_EMBS_TrajniPostapki");
			
			while(rs.next())
			{					
				String sss=rs.getString("embs");
				if(sss==null)
				{
					
				}
				else					
					listaEmb.add(sss);
			}
			
		} 
		catch (SQLException e) 
		{			
			e.printStackTrace();
		}
	}
	  
 @FXML
    private void PrebarajPrivremeniPostapki(ActionEvent event) 
 {
	      //PRI KLIK NA KOPCETO "IZBERI SLUZBEN VESNIK" SE AKTIVIRA OVOJ NASTAN
	 
	        final File fileTmp1;
	        final File fileTmp2;
	        File file=null;       
	        
	        
	        privremeni.setVisible(false); 
	     	trajni.setVisible(false); 
	     	Stage popupStage = new Stage(StageStyle.TRANSPARENT);
	     	
	     	//SE INICIJALIZIRA OBJEKT OD KLASATA Loading_Files_Folders, POMOSNA KLASA ZA VCITUVANJE PDF DOKUMENTI, PARSIRANJETO I INSERTIRANJETO VO BAZA NA STEC. POSTAPKI SE IZVRSUVAAT TAMU! 
	      	Loading_Files_Folders hC = new Loading_Files_Folders();	     	
	     	
	      	//SE GLEDA KOE RADIO BUTTON E IZBRANO, BIDEJKI MORA DA E IZBRANO ILI "ED. PDF" ILI "POVEKE PDF" ZA DANZAE KAKO DA PARSIRA PDF DOKUMENTI
	     	RadioButton selectedRadioButton = (RadioButton) togPdf.getSelectedToggle();
	     	String toogleGroupValue="";
	     	
	     	try
	     	{
	     		toogleGroupValue = selectedRadioButton.getId();
	     			  
	     		//IZBRANO E ZA VCITUVANJE EDINECEN PDF FILE
	     		if(toogleGroupValue.compareToIgnoreCase("edinPdf")==0)
	     		{
	     			
	     			FileChooser fileChooser = new FileChooser();
	     			//morav racno da postavam pocetok, primer C-direktorium, oti ima bug nekad FileChooser voopsto ne otvora i ne moze da go izvrsi metodot fileChooser.showOpenDialog
	     				     			
	     			try
	     			{
	     				fileChooser.setInitialDirectory(new File("C:\\"));
	     			}	 
	     			catch(Exception e)
	    	     	{
	     				//ne go pronaslo fajlot za .setInitialDirectory
	    	     	}
	     			
	     			fileTmp1 = fileChooser.showOpenDialog(myStage);	
	     			
	     			file=fileTmp1;
	     		}
	     		
	     		//IZBRANA E OPCIJATA, RADIO_BUTTON ZA VCITUVANJE CEL FOLDER
	     		if(toogleGroupValue.compareToIgnoreCase("pluralPdf")==0)
	     		{
	     			DirectoryChooser chooser = new DirectoryChooser();
	     			
	     			try
	     			{
	     				chooser.setInitialDirectory(new File("C:\\"));	
	     			}	 
	     			catch(Exception e)
	    	     	{
	    	     		//ne go pronaslo fajlot za .setInitialDirectory
	    	     	}		
	     			 
	     			fileTmp2 = chooser.showDialog(myStage);
	     			file=fileTmp2;
	     		}
	     		
	     	}
	     	catch(Exception e)
	     	{	     		
	     		toogleGroupValue="";
	     	}
	     	
	     	
	     	//PROVERKA DALI NESTO E IZBRANO VO BILO KOJ SLUCAJ NA IZBOR NA RADIO_BUTTON, DALI NESTO E IZBRANO/SELEKTIRANO
            if ((file != null) && ((toogleGroupValue.compareToIgnoreCase("edinPdf")==0) || (toogleGroupValue.compareToIgnoreCase("pluralPdf")==0)) ) 
            { 
            	File fff = file; 
            	String tipFileFolder = toogleGroupValue;
            	
            	new Thread(() -> {
            		   Platform.runLater(()-> {
            			               			  
							try {					
								//PRIKAZ NA LOADING.FXML 								
								hC.loadingStage(popupStage, myStage);	
								System.out.println("erkondisan");
								} 
							
							catch (Exception e) 
								{	
								
								Platform.runLater(()-> {
									   popupStage.hide();
									});
								
								e.printStackTrace();
								}					
            	           
   					});
            		 
            		
					try 
					{
					//SE POVIKUVA F-JA KOJA PRVIN GLEDA KOJ TIP NA VCITUVANJE E IZBRANO, DALI E VCITAN EDINECEN FILE ILI CEL DIREKTORIUM
					hC.loadingFileSporedTip(fff, tipFileFolder);
                 	
					//SE POLNI TABLEVIEW ZA PRIVREMENI STECAJNI POSTAPKI I SE PRIKAZIVA
					polnenjeGridPrivremeniOdBaza();					
                 	privremeniPressed();
					
                 		Platform.runLater(()-> {
                 			popupStage.hide();    
                	
                 		});
                 	
					}
					
					catch (Exception e1) 
					{
						//ne naogja pdf so takva pateka, treba da pecati ALERT						
						Platform.runLater(()-> {
							   popupStage.hide();
							});
						//e1.printStackTrace();
						System.out.println("greska pri biranje file/folder, moving on...");
					}
					//KOGA KE ZAVRSI VCITUVANJETO LOADING.FXML NE S EPRIKAZUVA POVEKJE
					   Platform.runLater(()-> {
						   popupStage.hide();
						});
					               	
					  }).start();		
            }
            
            else
            {
            	//VO SLUCAJ NA GRESKA, ODNOSNO NISTO NE E IZBRANO IAKO NEKOJ RADIO_BUTTON E KLIKNAT, SE PRIKAZUVAAT DOSEGASHNITE PRIVREMENI STECAJNI POSTAPKI
            	//AKO E PRVO VCITUVANJE TABLEVIEW-TO SO PRIVREMENI STECAJNI POSTAPKI KE BIDE PRAZNO
            	privremeniPressed();
            }
                    
	        
	    }
	  
	private static String parsiranjeDatumVoString(java.sql.Date datum)
	{
		//PRI VCITUVANJE OD BAZA VO GRID I OD GRID VO BAZA, IMAM PARSIRANJE/CONVERTIRANJE NA DATUMOT VO STRING
		//OVAA F-JA PRIMA SQL DATUM I GO KONVERTIRA VO String format, so tockasto oddeluvanje
		
		String datumString =null;
		
		try
		{
			if(datum != null)
			{			
				DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
				datumString = df.format(datum);			
			}
		}
		catch(Exception e)
		{
			System.out.println("Problem pri parsiranje na sql datum vo String datum!");
			datumString =null;
		}
		
		
		
		return datumString;		
		
	}
		
		
	private void polnenjeGridPrivremeniOdBaza()
		{
		//POLNAM GRID OD BAZA SO PRIVREMENI STECAJNI POSTAPKI
		
			Connection conn = DBConnector.getConnect();
			oblist = FXCollections.observableArrayList();
			//ova e za novo vcituvanje
			
			try 
			{
				rs = conn.createStatement().executeQuery("EXEC dbo.SP_select_polinja_PrivremeniPostapki");
				
				while(rs.next())
				{
					CheckBox tmp = new CheckBox(null);					
					oblist.add(new privSpostapka(rs.getString("sud"), rs.getString("resenie"), rs.getString("pravnoLice"),  rs.getString("edb"), parsiranjeDatumVoString(rs.getDate("datum")), rs.getString("predStecajna"), rs.getString("statusZakazanoRociste"), rs.getString("otvorena"), rs.getString("nesprovedena"), rs.getString("zaklucuva"), rs.getString("brisenjeOdCR"), rs.getString("zapira"), tmp, rs.getInt("id"), rs.getString("embs")));			
				}
				
			} 
			
			catch (SQLException e) 
			{			
				e.printStackTrace();
			}
			
			//GI SREDUVAM KOLONITE SO IZVLECENITE POLINJA OD SELECTOT, PODESUVAM ZA TABLEVIEW
			
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
			col_embs.setCellValueFactory(new PropertyValueFactory<>("embs"));
			
			tablePrivremeni.setItems(oblist);
			
		}
	
	private void polnenjeGridTrajni()
	{
		//VOSPOSTAVUVANJE KONEKCIJA SO BAZA, POLNENJE TABLEVIEW SO TRAJNI STECAJNI POSTAPKI, FILTER POSTAVUVANJE
		Connection conn = DBConnector.getConnect();
		oblistTrajni = FXCollections.observableArrayList();
		try 
		{
			rsTrajni = conn.createStatement().executeQuery("EXEC dbo.SP_select_polinja_TrajniPostapki");
			
			while(rsTrajni.next())
			{
				System.out.println("Dali vlaga tuka");
				// parsiranjeDatumVoString(rsTrajni.getDate("datum")),
				
				oblistTrajni.add(new sPostapka(rsTrajni.getString("sud"), rsTrajni.getString("resenie"), rsTrajni.getString("pravnoLice"),  rsTrajni.getString("edb"),  parsiranjeDatumVoString(rsTrajni.getDate("datum")), rsTrajni.getString("predStecajna"),
				 rsTrajni.getString("statusZakazanoRociste"), rsTrajni.getString("otvorena"), rsTrajni.getString("nesprovedena"), rsTrajni.getString("zaklucuva"), rsTrajni.getString("brisenjeOdCR"), rsTrajni.getString("zapira"), rsTrajni.getString("embs"), rsTrajni.getInt("id")));
			}
			
			filter = new FilteredList(oblistTrajni, e->true);	
			
			   
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
		col_embsT.setCellValueFactory(new PropertyValueFactory<>("embs"));				
		
		tableTrajni.setItems(oblistTrajni);
	}
	
	private void polnenjeGridPrivremeniOdSet()
	{
		//TABLEVIEW ZA PRIVREMENI SE POSTAVUVA NA MOMENTALEN SET STO GO IMA VO APLIKACIJATA, ZNACI OBLIST ODNOSNO OBSERVABLELIST , NE SE CITA ODNOVO OD BAZA
		
		tablePrivremeni.setItems(oblist);
	}


@FXML
public void ispolniTrajni(ActionEvent event)
{
	//NASTAN STO SE IZVRSUVA KOGA KE SE KLIKNE NA VNESI VO TRAJNI, TIE STO SE STIKLIRANI OD TABLEVIEW NA PRIVREMENI, SE VNESUVAAT/PREFRLUVAAT VO TABELA VO BAZA TRAJNI STECAJNI POSTAPKI
	
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
				
				PreparedStatement prep = DBConnector.getConnect().prepareStatement("EXEC  dbo.SP_insert_Slucaj_od_Privremeni_vo_Trajni ?");				
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
	
	/////////SE BRISAT OD SETOT-PRIVREMENI I TABELATA VO BAZA ZA PRIVREMENIS TECAJNI POSTAPKI TIE STO SE STIKLIRANI, OTI OTKKAO KE SE RPEFRLAT VO TRAJNI, NEMA PRICINA DA STOJAT I VO PRIVREMENI
	
	//SO PREDICAT SE OTSTRANUVAAT OD LISTATA OBSERVALBLELIST PRIVREMENITE TIE STO SE STIKLIRANI
	Predicate<privSpostapka> postapkaPredicate = p-> p.getCheck().isSelected();
	lista.removeIf(postapkaPredicate);
	
	//VO SLEDNIOV FOR-CIKLUS GI BRISE OD DATABAZA TIE STO SE STIKLIRANI, SO TOA STO IM GI ZACUVUVAM ID-ATA I SPORED TOA ZNAM KOJA PRIVREMENA DA JA IZBRISAM
	for(Integer j : listaIDzaDelete)
	{
		System.out.println("ID za brisenje "+ j);
		
		try 
		{
							
			
			PreparedStatement prepDelete = DBConnector.getConnect().prepareStatement("EXEC dbo.SP_delete_slucaj_od_Privremeni ?");
			
			prepDelete.setInt(1, j);
			prepDelete.executeUpdate();	
			
		}

		catch (Exception e) 
		{
			System.out.println("Probajte povtorno, error pri delete od privremeniPostapki!");	
			
		}
		
	}

	listaIDzaDelete.clear();
	//SE POLNI ODNOVO TABLEVIEW SO TRAJNI STECAJNI POSTAPKI, ZA DA GI OPFATI NOVO-VNESENITE
	polnenjeGridTrajni();	
	
	/////ODNOVO SE POLNAT LISTITE ZA AUTOCOMPLETE SO EDB I EMBS, POSTO IMA IZMENI VO TRAJNI STECAJNI POSTAPKI, PA DA SE OPFATAT I NOVO-VNESENITE EDB/EMBS
	polniListaEDBautoComplete();
	polniListaEMBSautoComplete();
	
	//TextFields.bindAutoCompletion(txtEdb, listaEdb);
	
	/////SE PRETSTAVUVA KAKO TRAJNI DA E KLIKNATO, I SE PRIKAZUVA TABLEVIEW SO TRAJNI STECAJNI POSTAPKI
	trajniPressed(); 
	
	
}


@FXML
public void kliknatoEdmb(ActionEvent event)
{
	//OVOJ NASTAN MI E DEKA MI E KLIKNATO RADIOBUTTON ZA EDB ZNACI AUTOCOMPLETE DA MI E POLN SO EDB OD TRAJNI STECAJNI POSTAPKI	
	
	polniListaEDBautoComplete();
	
	txtEdb.textProperty().addListener((observable, oldValue, newValue) -> {
		   
		   filter.setPredicate(sPostapka ->{
			  
			   System.out.println("dali ima nesto vo sPost" + sPostapka.getEdb());
			   
			   if (newValue == null || newValue.isEmpty()) 
				{
				   System.out.println("dali vlaga vo prviot if");
					return true;
				}
			   
			   else if ((sPostapka.getEdb() != null) && (sPostapka.getEdb().contains(newValue))) 
				{
				   System.out.println("dali vlaga vo vtoriot if");
					return true;
				}
			  
			   return false;
		   });
		   
	   });
	
	if(autoCompletionBindinEmbs != null)
	{
		autoCompletionBindinEmbs.dispose();
	}	
	
	autoCompletionBindinEdmb = TextFields.bindAutoCompletion(txtEdb, listaEdb);
	
	
}

@FXML
public void kliknatoEmbs(ActionEvent event)
{
	//OVOJ NASTAN MI E DEKA MI E KLIKNATO RADIOBUTTON ZA EMBS ZNACI AUTOCOMPLETE DA MI E POLN SO EMBS OD TRAJNI STECAJNI POSTAPKI
	
	polniListaEMBSautoComplete();
	
	
	txtEdb.textProperty().addListener((observable, oldValue, newValue) -> {
		   
		   filter.setPredicate(sPostapka ->{
			  
			   System.out.println("dali ima nesto vo sPost" + sPostapka.getEmbs());
			   
			   if (newValue == null || newValue.isEmpty()) 
				{
				   System.out.println("dali vlaga vo prviot if");
					return true;
				}
			   
			   else if ((sPostapka.getEmbs() != null) && (sPostapka.getEmbs().contains(newValue))) 
				{
				   System.out.println("dali vlaga vo vtoriot if");
					return true;
				}
			   
			   return false;
		   });
		   
	   });
	
	
	if(autoCompletionBindinEdmb != null)
	{
		autoCompletionBindinEdmb.dispose();
	}
	
	autoCompletionBindinEmbs = TextFields.bindAutoCompletion(txtEdb, listaEmb);
	
}

@FXML
public void PrikaziPrivremeniPostapki(ActionEvent event)
{
	//nz dali e podobro od set ili od baza, posto ako e od set, ako se startuva primer od drug korisnik, ke mu se prebrishat na momentalniot privremenite, bolje po user da ide
	polnenjeGridPrivremeniOdBaza();
	
	
	privremeniPressed();
	
}

@FXML
public void PrikaziTrajniPostapki(ActionEvent event)
{
	//POLNAM GRID 
	
	polnenjeGridTrajni();
	trajniPressed();			
	
}

private void trajniPressed()
{
	//SREDUVANJE NA IZGLED/PRIKAZ ZNACI AKO TRAJNI SE KLIKNATI, SE SREDUVAAT STILOVITE, PRVO GI PREVZEMAM TIE STO GI IMA, PA GI BRISAM PA STAVAM DA E IZBLEDENO
	ObservableList<String> stiloviP = prikaziPrivremeni.getStyleClass();
	prikaziPrivremeni.getStyleClass().removeAll(stiloviP);
	prikaziPrivremeni.getStyleClass().add("btns_toggle");
	
	ObservableList<String> stiloviT = prikaziTrajni.getStyleClass();
	prikaziTrajni.getStyleClass().removeAll(stiloviT); 
	prikaziTrajni.getStyleClass().add("btns_toggle_smeneto");
		
	privremeni.setVisible(false); 	
 	trajni.setVisible(true); 
 	prebaruvanjeEdmbPojava();
}



private void privremeniPressed()
{	
	//SREDUVANJE NA IZGLED/PRIKAZ ZNACI AKO PRIVREMENI SE KLIKNATI, SE SREDUVAAT STILOVITE, PRVO GI PREVZEMAM TIE STO GI IMA, PA GI BRISAM PA STAVAM DA E IZBLEDENO 
	ObservableList<String> stiloviP = prikaziPrivremeni.getStyleClass();
	prikaziPrivremeni.getStyleClass().removeAll(stiloviP);
	prikaziPrivremeni.getStyleClass().add("btns_toggle_smeneto");
	
	ObservableList<String> stiloviT = prikaziTrajni.getStyleClass();
	prikaziTrajni.getStyleClass().removeAll(stiloviT); 
	prikaziTrajni.getStyleClass().add("btns_toggle");
		
	privremeni.setVisible(true); 	
 	trajni.setVisible(false);
 	//KAJ PRIVREMENI NEMA PRIKAZ NA PREBARUVANJE PO EDB/EMBS
 	prebaruvanjeEdmbNePojava();
}

private void prebaruvanjeEdmbPojava()
{
	//OVA E GENERALNO ZA TRAJNI, TUKA E POTREBEN PRIKAZ NA LUPA-SLIKA, TEXT POLE ZA AUTOCOMPLETE, RADIOBUTTONS ZA EDB I EMBS
	txtEdb.setVisible(true);
	lupa.setVisible(true);
	rbEdmb.setVisible(true); 
	rbEmbs.setVisible(true); 
}

private void prebaruvanjeEdmbNePojava()
{
	//OVA E GENERALNO ZA PRIVREMENI, NEMA PRIKAZ NA LUPA-SLIKA, TEXT POLE ZA AUTOCOMPLETE, RADIOBUTTONS ZA EDB I EMBS
	
	txtEdb.setVisible(false);
	lupa.setVisible(false);
	rbEdmb.setVisible(false); 
	rbEmbs.setVisible(false);
}


@FXML
public void stikliraj(ActionEvent event)
{
	//OVOJ NASTAN E ZA KLIK NA KOPCETO STIKLIRAJ, ZA DA GI IZMINE SITE OD LISTATA PRIVREMENI VO TABLEVIEW-TO ZA PRIVREMENI STECAJNI POSTAPKI, I CHECKBOXOT DA GO STIKLIRA
	for(privSpostapka psp : oblist)
	{
		psp.getCheck().setSelected(true);
	}
	tablePrivremeni.setItems(oblist);
	
}

@FXML
public void odstikliraj(ActionEvent event)
{
	//OVOJ NASTAN E ZA KLIK NA KOPCETO ODSTIKLIRAJ, ZA DA GI IZMINE SITE OD LISTATA PRIVREMENI VO TABLEVIEW-TO ZA PRIVREMENI STECAJNI POSTAPKI, I CHECKBOXOT DA GO ODSTIKLIRA
	for(privSpostapka psp : oblist)
	{
		psp.getCheck().setSelected(false);
	}
	tablePrivremeni.setItems(oblist);
}

		
}
