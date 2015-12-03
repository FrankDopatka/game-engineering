package frontend;

import backend.BackendSpielStub;
import schach.daten.D_Spiel;
import schach.daten.Xml;
import schach.daten.ZugEnum;

public class Updater extends Thread{
	private Frontend frontend;
	private BackendSpielStub backendSpiel;
	private int timer;
	private boolean bauernUmwandlungImGange=false;
	
	public Updater(Frontend frontend,int timer){
		this.frontend=frontend;
		backendSpiel=frontend.getBackendSpiel();
		this.timer=timer;
		this.start();
	}
	
	@Override
	public void run(){
		while(true){
			try{
				D_Spiel d_spiel=(D_Spiel)Xml.toD(backendSpiel.getSpielDaten());
				String bemerkungSpielzug=d_spiel.getString("bemerkung");
				if (frontend.getAnzahlZuege()!=d_spiel.getInt("anzahlZuege")){
					update(d_spiel);
				}
				else if (bemerkungSpielzug.equals(""+ZugEnum.BauerUmwandlungImGange)&&(!bauernUmwandlungImGange)){
					if (frontend.ichSpieleWeiss()==d_spiel.getBool("weissAmZug")) frontend.setBauerUmwandelnImGange();	
					bauernUmwandlungImGange=true;
				}else if ((bauernUmwandlungImGange)&&(bemerkungSpielzug.equals(""+ZugEnum.BauerUmwandlung))){
					bauernUmwandlungImGange=false;
				}
			}
			catch (Exception e){}
			try {
				Thread.sleep(timer*2000);
			} catch (InterruptedException e) {}
		}
	}
	
	public void update(D_Spiel d_spiel){
		int anzahlZuege=d_spiel.getInt("anzahlZuege");
		Belegung b=new Belegung(backendSpiel.getAktuelleBelegung(),frontend.ichSpieleWeiss());
		frontend.setBelegung(b.getBild()); 
		if (b.isWeissSchachMatt()||b.isSchwarzSchachMatt()||b.isPatt()){
			// Spiel ist zu Ende
			if (frontend.ichSpieleWeiss()&&b.isWeissSchachMatt()) 
				frontend.log("Leider verloren, Weiss ist SCHACH MATT :-(");
			else if (frontend.ichSpieleSchwarz()&&b.isSchwarzSchachMatt()) 
				frontend.log("Leider verloren, Schwarz ist SCHACH MATT :-(");
			else if (b.isPatt()) 
				frontend.log("UNENDSCHIEDEN! PATT!");
			else
				frontend.log("GEWONNEN! Dein Gegner ist SCHACH MATT!");
			frontend.setEnde(true);
		}else{
			if (frontend.ichSpieleWeiss()&&b.isWeissImSchach()){
				frontend.log("SCHACH!");
			}else if (frontend.ichSpieleSchwarz()&&b.isSchwarzImSchach()){
				frontend.log("SCHACH!");
			}else if (frontend.ichSpieleWeiss()&&b.isSchwarzImSchach()){
				frontend.log("Dein Gegner ist im SCHACH!");
			}else if (frontend.ichSpieleSchwarz()&&b.isWeissImSchach()){
				frontend.log("Dein Gegner ist im SCHACH!");
			}
		}
		frontend.setAnzahlZuege(anzahlZuege);
		frontend.setHistorienAnsicht(false);
		frontend.updateLog();
	}
}
