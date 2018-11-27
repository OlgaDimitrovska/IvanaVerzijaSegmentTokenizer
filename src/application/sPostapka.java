package application;

public class sPostapka 
{
	
	@Override
	public String toString() {
		
		//return super.toString();
		StringBuilder sb = new StringBuilder();
		
		sb.append("sud: "+sud+" ");
		sb.append("resenie: "+resenie+" ");
		sb.append("dolznik: "+dolznik+" ");
		sb.append("pravnoLice: "+pravnoLice+" ");
		sb.append("edb: "+edb+" ");
		
		sb.append("stecaenUpravnik: "+stecaenUpravnik+" ");
		sb.append("imeStecUpr: "+imeStecUpr+" ");
		sb.append("dooel: "+dooel+" ");
		sb.append("uvozizvoz: "+uvozizvoz+" ");
		sb.append("drustvo: "+drustvo+" ");
		
		sb.append("dejnost: "+dejnost+" ");
		sb.append("predStecajna: "+predStecajna+" ");
		sb.append("otvorena: "+otvorena+" ");
		sb.append("statusZakazanoRociste: "+statusZakazanoRociste+" ");
		sb.append("nesprovedena: "+nesprovedena+" ");
		
		sb.append("zaklucuva: "+zaklucuva+" ");
		sb.append("brisenjeOdCR: "+brisenjeOdCR+" ");
		sb.append("zapira: "+zapira+" ");
		sb.append("sobranienaDoveriteli: "+sobranienaDoveriteli+" ");
		sb.append("datum: "+datum+" ");		
		
		
		return sb.toString(); 
	}
	
	private String sud;
	
	private String resenie;
	
	private String dolznik;
	
	private String pravnoLice;
	
	private String edb;
	//////////////////
	
	private String stecaenUpravnik;
	
	private String imeStecUpr;
	
	private String dooel;
	
	private String uvozizvoz;
	
	private String drustvo;
	//////////////////
	
	private String dejnost;
	
	private String predStecajna;
	
	
	
	private String otvorena;
	
	
	//////////////////
	private String statusZakazanoRociste;
	
	
	
	private String nesprovedena;
	
	private String zaklucuva;
	
	
	//////////////////
	
	private String brisenjeOdCR;
	
	private String zapira;
	
	
	
	private String sobranienaDoveriteli;
	
	private String datum;
	//////////////////

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

	public String getDolznik() {
		return dolznik;
	}	

	public void setDolznik(String dolznik) {
		this.dolznik = dolznik;
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

	public String getStecaenUpravnik() {
		return stecaenUpravnik;
	}

	public void setStecaenUpravnik(String stecaenUpravnik) {
		this.stecaenUpravnik = stecaenUpravnik;
	}

	public String getImeStecUpr() {
		return imeStecUpr;
	}

	public void setImeStecUpr(String imeStecUpr) {
		this.imeStecUpr = imeStecUpr;
	}

	public String getDooel() {
		return dooel;
	}

	public void setDooel(String dooel) {
		this.dooel = dooel;
	}

	public String getUvozizvoz() {
		return uvozizvoz;
	}

	public void setUvozizvoz(String uvozizvoz) {
		this.uvozizvoz = uvozizvoz;
	}

	public String getDrustvo() {
		return drustvo;
	}

	public void setDrustvo(String drustvo) {
		this.drustvo = drustvo;
	}

	public String getDejnost() {
		return dejnost;
	}

	public void setDejnost(String dejnost) {
		this.dejnost = dejnost;
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


	public String getSobranienaDoveriteli() {
		return sobranienaDoveriteli;
	}

	public sPostapka() {
		super();
		
	}

	public void setSobranienaDoveriteli(String sobranienaDoveriteli) {
		this.sobranienaDoveriteli = sobranienaDoveriteli;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	
	
}
