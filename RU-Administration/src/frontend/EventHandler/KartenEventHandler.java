package frontend.EventHandler;

import ru.daten.*;
import ru.interfaces.iBackendKarteneditor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import frontend.Frontend;
import frontend.FrontendGestirn;
import frontend.karte.Atlas;
import frontend.karte.Karte;
import frontend.karte.flaechen.Feld;
import frontend.karte.flaechen.Platz;
import frontend.karte.flaechen.Raum;
import frontend.menu.rechts.MenuRechts;
import frontend.menu.rechts.MenuRechtsKarte;
import frontend.menu.rechts.MenuRechts.AktionMenuRechts;

public class KartenEventHandler extends EventHandler{

	private FrontendGestirn fGestirn;

	public KartenEventHandler(Frontend frontend,FrontendGestirn fGestirn, iBackendKarteneditor backendK) {
		super(frontend);
		backendKarteneditor= backendK;
		this.fGestirn = fGestirn;
		
	}
	
	public void setfGestirn(FrontendGestirn fGestirn) {
		this.fGestirn = fGestirn;
	}

	@Override
	protected void aktion(Platz feld){
		AktionMenuRechts a=fGestirn.getMenuRechts().getAktion();
		switch (a){
		case FeldArtSetzen:
			feldSetzen(feld);
			break;
		case RessourceSetzen:
			ressourceSetzen(feld);
			break;
		case RessourceLoeschen:
			ressourceLoeschen(feld);
			break;
		case SpielerstartSetzen:
			spielerstartSetzen(feld);
			break;
		}
	}
	
	private void spielerstartSetzen(Platz feld){
		dragging=false;
		MenuRechtsKarte menu=(MenuRechtsKarte) fGestirn.getMenuRechts();
		try{
			int spielernummer=menu.getSpielernummer();
			D_Position posAlt=(D_Position)Xml.toD(backendKarteneditor.getSpielerstart(spielernummer));
			int x=posAlt.getInt("x");
			int y=posAlt.getInt("y");
			if ((posAlt.getInt("x")!=0)&&(posAlt.getInt("y")!=0)){
				backendKarteneditor.setSpielerstart(x,y,0);
				//Verändert geht alle karten durch bis zur Aktiven Karte
				for(Karte k:frontend.getKarte()){
					if(k.getAktive()){
						k.getFelder()[x][y].setSpielerNummer(0);
						k.zeichneFeld(new int[]{x,y});
					}
				}
			}
			backendKarteneditor.setSpielerstart(feld.getPos()[0],feld.getPos()[1],spielernummer);
			((Feld) feld).setSpielerNummer(spielernummer);
		}
		catch(RuntimeException e){
			fGestirn.log("FEHLER:"+e.getMessage());
		}
		feld.zeichnen();
	}
	
	@Override
	protected void feldSetzen(Platz feld){
		MenuRechts menu=fGestirn.getMenuRechts();
		String fArtNeu=menu.getFeldart();
		Feld f = (Feld) feld;
		
	
		backendKarteneditor.setFeldArt(f.getPos()[0],f.getPos()[1],fArtNeu);
		f.setFeldArt(fArtNeu);
		f.zeichnen();
		
	}
	
	@Override
	public void mouseMoved(MouseEvent ev) {
		Platz feld=(Platz)ev.getSource();
		fGestirn.setStatus(feld.toString());
	}
	
	
	private void ressourceSetzen(Platz feld){
		dragging=false;
		D_Feld feldDaten=(D_Feld)Xml.toD(backendKarteneditor.getFeldDaten(feld.getPos()[0],feld.getPos()[1]));
		String feldArt=feldDaten.getString("feldArt");
		D_RessourcenArt feldRessourcenArten=(D_RessourcenArt)Xml.toD(backendKarteneditor.getErlaubteRessourcenArten(feldArt));
		ArrayList<String> feldRessourcenArtenEnum=feldRessourcenArten.getListe();
		Object[] optionen=new Object[feldRessourcenArtenEnum.size()];
		for (int i=0;i<feldRessourcenArtenEnum.size();i++){
			optionen[i]=feldRessourcenArtenEnum.get(i);
		}
		if (optionen.length==0) return;
	  int gewaehlt=JOptionPane.showOptionDialog(fGestirn,
	  		"Setzen Sie die Resource...", 
	  		"Resource fuer "+feld.getPosX()+"/"+feld.getPosY()+" vom Typ "+feldArt,
	    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
	    null, optionen, optionen[0]);
	  if (gewaehlt==-1) return;
	  backendKarteneditor.setRessource(feld.getPos()[0],feld.getPos()[1],""+optionen[gewaehlt]);
	  ((Feld) feld).setRessource(""+optionen[gewaehlt]);
	  feld.zeichnen();
	}

	private void ressourceLoeschen(Platz feld){
		dragging=false;
		backendKarteneditor.delRessource(feld.getPos()[0],feld.getPos()[1]);
		((Feld) feld).setRessource(null);
		feld.zeichnen();
	}

}