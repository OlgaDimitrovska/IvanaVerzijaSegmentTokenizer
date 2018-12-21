package application;

import javafx.scene.control.CheckBox;

public class privSpostapka 
{
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();		
		sb.append("sud: "+sud+" ");
		sb.append("resenie: "+resenie+" ");		
		sb.append("pravnoLice: "+pravnoLice+" ");
		sb.append("edb: "+edb+" ");
		sb.append("predStecajna: "+predStecajna+" ");
		sb.append("otvorena: "+otvorena+" ");
		sb.append("statusZakazanoRociste: "+statusZakazanoRociste+" ");
		sb.append("nesprovedena: "+nesprovedena+" ");		
		sb.append("zaklucuva: "+zaklucuva+" ");
		sb.append("brisenjeOdCR: "+brisenjeOdCR+" ");
		sb.append("zapira: "+zapira+" ");	
		sb.append("datum: "+datum+" ");					
		return sb.toString(); 
	}
	
	public privSpostapka() 
	{
		super();
	}
	
	private String vratiTocnaGoleminaNaString(String praten)
	{
		
		try
		{
			if (praten.length() > 200) {
				praten = praten.substring(0, 200);
				}
			
		}		
		catch(Exception e)
		{
			praten="";
		}
		return praten;
	}
	
	
	public privSpostapka(String sud, String resenie, String pravnoLice, String edb, String datum, String predStecajna,
			String statusZakazanoRociste, String otvorena, String nesprovedena, String zaklucuva, String brisenjeOdCR,
			String zapira, CheckBox check, int id, String embs) {
		super();
		
		this.sud = vratiTocnaGoleminaNaString(sud);
		this.resenie = vratiTocnaGoleminaNaString(resenie);
		this.pravnoLice = vratiTocnaGoleminaNaString(pravnoLice);
		this.edb = vratiTocnaGoleminaNaString(edb);
		this.datum = vratiTocnaGoleminaNaString(datum);
		this.predStecajna = vratiTocnaGoleminaNaString(predStecajna);
		this.statusZakazanoRociste = vratiTocnaGoleminaNaString(statusZakazanoRociste);
		this.otvorena = vratiTocnaGoleminaNaString(otvorena);
		this.nesprovedena = vratiTocnaGoleminaNaString(nesprovedena);
		this.zaklucuva = vratiTocnaGoleminaNaString(zaklucuva);
		this.brisenjeOdCR = vratiTocnaGoleminaNaString(brisenjeOdCR);
		this.zapira = vratiTocnaGoleminaNaString(zapira);
		this.check = check;
		this.id = id;
		this.embs = embs;
	}

	public privSpostapka(String sud, String resenie, String pravnoLice, String edb, String datum, String predStecajna,
			String statusZakazanoRociste, String otvorena, String nesprovedena, String zaklucuva, String brisenjeOdCR,
			String zapira, int id) 
	{
		super();
		this.sud = vratiTocnaGoleminaNaString(sud);
		this.resenie = vratiTocnaGoleminaNaString(resenie);
		this.pravnoLice = vratiTocnaGoleminaNaString(pravnoLice);
		this.edb = vratiTocnaGoleminaNaString(edb);
		this.datum = vratiTocnaGoleminaNaString(datum);
		this.predStecajna = vratiTocnaGoleminaNaString(predStecajna);
		this.statusZakazanoRociste = vratiTocnaGoleminaNaString(statusZakazanoRociste);
		this.otvorena = vratiTocnaGoleminaNaString(otvorena);
		this.nesprovedena = vratiTocnaGoleminaNaString(nesprovedena);
		this.zaklucuva = vratiTocnaGoleminaNaString(zaklucuva);
		this.brisenjeOdCR = vratiTocnaGoleminaNaString(brisenjeOdCR);
		this.zapira = vratiTocnaGoleminaNaString(zapira);		
		this.id = id;
		
	}
	
	

	

	private String sud;
	
	private String resenie;
	
	private String pravnoLice;
	
	private String edb;
	
	private String datum;
	
	private String predStecajna;	


	private String statusZakazanoRociste;
	
	private String otvorena;
	
	private String nesprovedena;
	
	private String zaklucuva;
	
	private String brisenjeOdCR;
	
	private String zapira;
		
	private CheckBox check;
	
	private String sobranie;
	
	private String embs;
	
	private int id;
	
		


	//////////////////



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CheckBox getCheck() {
		return check;
	}

	public void setCheck(CheckBox check) {
		this.check = check;
	}

	public String getSud() {
		return sud;
	}

	public void setSud(String sud) {
		this.sud = sud;
	}

	public String getResenie() {
		return resenie;
	}

	public void setResenie(String resenie) {
		this.resenie = resenie;
	}

	public String getPravnoLice() {
		return pravnoLice;
	}

	public void setPravnoLice(String pravnoLice) {
		this.pravnoLice = pravnoLice;
	}

	public String getEdb() {
		return edb;
	}

	public void setEdb(String edb) {
		this.edb = edb;
	}

	public String getPredStecajna() {
		return predStecajna;
	}

	public void setPredStecajna(String predStecajna) {
		this.predStecajna = predStecajna;
	}


	public String getOtvorena() {
		return otvorena;
	}

	public void setOtvorena(String otvorena) {
		this.otvorena = otvorena;
	}


	public String getStatusZakazanoRociste() {
		return statusZakazanoRociste;
	}

	public void setStatusZakazanoRociste(String statusZakazanoRociste) 
	{
		this.statusZakazanoRociste = statusZakazanoRociste;
	}
	
	public String getNesprovedena() {
		return nesprovedena;
	}

	public void setNesprovedena(String nesprovedena) {
		this.nesprovedena = nesprovedena;
	}

	public String getZaklucuva() {
		return zaklucuva;
	}

	public void setZaklucuva(String zaklucuva) {
		this.zaklucuva = zaklucuva;
	}


	public String getBrisenjeOdCR() {
		return brisenjeOdCR;
	}

	public void setBrisenjeOdCR(String brisenjeOdCR) {
		this.brisenjeOdCR = brisenjeOdCR;
	}

	public String getZapira() {
		return zapira;
	}

	public void setZapira(String zapira) {
		this.zapira = zapira;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}
	
	public String getEmbs() {
		return embs;
	}

	public void setEmbs(String embs) {
		this.embs = embs;
	}

}
