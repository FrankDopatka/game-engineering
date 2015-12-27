package ki;

import java.util.ArrayList;
import java.util.Random;

import backend.BackendSpielStub;
import frontend.KI;
import schach.daten.D;
import schach.daten.D_Figur;
import schach.daten.D_Spiel;
import schach.daten.D_Zug;
import schach.daten.Xml;

public class KI002 extends KI {

	public KI002(){
		super("Zufallszuege, aber schlagen wenn moeglich");
	}

	@Override
	public void ichBinAmZug(D_Spiel d_Spiel) {
		BackendSpielStub b=getBackend();
		// alle erlaubten Zuege auslesen
		ArrayList<D> zuege=Xml.toArray(b.getAlleErlaubtenZuege());
	//	System.out.println(zuege);

		ArrayList<D_Zug> zuegeSchlagen=new ArrayList<D_Zug>();
		for(D zug:zuege){
			D_Zug datenZug=(D_Zug)zug;
			String zielfeld=datenZug.getString("nach");		
	
			String figur=b.getFigur(zielfeld);
			if ((figur!=null)&&((!figur.equals("")))){
				D_Figur datenFigur=(D_Figur)Xml.toD(figur);
				if (datenFigur.getBool("isWeiss")!=binWeiss()){
					zuegeSchlagen.add(datenZug);
				}
			}
		}

		// zufaelligen Zug durchfuehren, aber schlagen wenn moeglich
		if (zuegeSchlagen.size()>0){
			System.out.println("Kann schlagen!");
			int zugNummer=getZufallszahl(0,zuegeSchlagen.size());
			D_Zug zugGewaehlt=zuegeSchlagen.get(zugNummer);				
			b.ziehe(zugGewaehlt.getString("von"),zugGewaehlt.getString("nach"));
		}
		else{
			System.out.println("Kann nix schlagen!");
			int zugNummer=getZufallszahl(0,zuege.size());
			D_Zug zugGewaehlt=(D_Zug)zuege.get(zugNummer);								
			b.ziehe(zugGewaehlt.getString("von"),zugGewaehlt.getString("nach"));
		}
	}

	@Override
	public void ichBinNichtZug(D_Spiel d_Spiel) {
		//TODO ggf. weitere Spielzuege analysieren
	}
	
	@Override
	public void ichHabeVerloren() {
		if (binWeiss())
			getFrontend().log("MIST, die KI "+getInfo()+" (weiss) hat VERLOREN!");
		else
			getFrontend().log("MIST, die KI "+getInfo()+" (schwarz) hat VERLOREN!");
	}

	@Override
	public void ichHabeGewonnen() {
		if (binWeiss())
			getFrontend().log("JUCHU, die KI "+getInfo()+" (weiss) hat GEWONNEN!");
		else
			getFrontend().log("JUCHU, die KI "+getInfo()+" (schwarz) hat GEWONNEN!");
	}

	@Override
	public void patt() {
		if (binWeiss())
			getFrontend().log("NAJA, die KI "+getInfo()+" (weiss)  hat PATT gespielt!");
		else
			getFrontend().log("NAJA, die KI "+getInfo()+" (schwarz) hat PATT gespielt!");
		getFrontend().log("Eigentlich eine gute Leistung dafuer, dass ich nur zufaellig ziehe... ;-)");
	}
	
	private int getZufallszahl(int ug,int og){
		Random r=new Random();
		return r.nextInt(og-ug)+ug;
	}
}
