package frontend.karte.flaechen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import frontend.FrontendGestirn;
import frontend.EventHandler.EventHandler;
import frontend.EventHandler.KartenEventHandler;
import frontend.karte.Karte;
import ru.daten.*;

public class Feld extends Platz{
	private static final long serialVersionUID = 1L;
	private FrontendGestirn frontend;
	private KartenEventHandler eventHandler;
	private String ressource;
	private int spielerNummer;
	
	public Feld(FrontendGestirn frontend,int x,int y, String feldArt) {
		super(x,y,feldArt);
		this.frontend=frontend;
		ressource = "";
		spielerNummer = 0;
	}
	
	public Feld(FrontendGestirn frontend,int x,int y, String feldArt, String ressource,int spielerNummer) {
		super(x,y,feldArt);
		this.frontend=frontend;
		this.ressource = ressource;
		this.spielerNummer = spielerNummer;
	}
	
	public String getRessource() {
		return ressource;
	}

	public void setRessource(String ressource) {
		this.ressource = ressource;
	}

	public int getSpielerNummer() {
		return spielerNummer;
	}

	public void setSpielerNummer(int spielerNummer) {
		this.spielerNummer = spielerNummer;
	}

	@Override
	public void setEventhandler(EventHandler eventHandler){
		this.eventHandler=(KartenEventHandler)eventHandler;
		if (eventHandler!=null){
			addMouseListener(eventHandler);
			addMouseMotionListener(eventHandler);
		}
	}
	
	@Override
	public void terminate(){
		removeMouseListener(eventHandler);
		removeMouseMotionListener(eventHandler);
		setIcon(null);
		setVisible(false);
	}
	
	@Override
	public void zeichnen(){
		
		D_Feld daten=(D_Feld)Xml.toD(frontend.getBackendKarteneditor().getFeldDaten(x,y));
		
		zeichnen(daten);
		
	}
	
	@Override
	public void zeichnen(D_Feld daten){
		try{
		Image im;
		//Verändert
		Karte karte = frontend.getKarte();
		
		BufferedImage bildIcon=karte.getBildFeld(daten.getString("feldArt"));
		BufferedImage bild=new BufferedImage(frontend.getSpielfeldGroesse(),frontend.getSpielfeldGroesse(),BufferedImage.TYPE_INT_ARGB);
		Graphics g=bild.getGraphics();
		g.drawImage(bildIcon,0,0,null);
		if (daten.getString("ressource").length()>0){
			BufferedImage bildRessource=karte.getBildRessource(daten.getString("ressource"));
			if(bildIcon!=null)
				g.drawImage(bildRessource,bildIcon.getWidth()/2-bildRessource.getWidth()/2,bildIcon.getHeight()/2-bildRessource.getWidth()/2,null);
		}			
		if (daten.getInt("spielerstart")!=0){
			g.setFont(new Font("Arial",Font.BOLD,24));
			g.setColor(new Color(0,0,0));
			g.drawString(daten.getString("spielerstart"),5,24);
		}
		g.dispose();
		if (frontend.getZoomfaktor()==100)
			im=bild;
		else
			im=bild.getScaledInstance(bild.getWidth()*frontend.getZoomfaktor()/100,bild.getHeight()*frontend.getZoomfaktor()/100,Image.SCALE_FAST);
		if (im!=null){
			Icon ic=new ImageIcon(im);
			setIcon(ic);			
		}
		else{
			setIcon(null);
		}
		}catch(RuntimeException e){
			e.printStackTrace();
		}
	}
	
	public void zeichnenSonderWurmloch(D_Feld daten){
		try{
		Image im;
		//Verändert
		Karte karte = frontend.getKarte();
		
		BufferedImage bildIcon=karte.getBildFeld(daten.getString("feldArt"));
		BufferedImage bild=new BufferedImage(frontend.getSpielfeldGroesse(),frontend.getSpielfeldGroesse(),BufferedImage.TYPE_INT_ARGB);
		Graphics g=bild.getGraphics();
		g.drawImage(bildIcon,0,0,null);
		if (daten.getString("ressource").length()>0){
			BufferedImage bildRessource=karte.getBildRessource(daten.getString("ressource"));
			g.drawImage(bildRessource,bildIcon.getWidth()/2-bildRessource.getWidth()/2,bildIcon.getHeight()/2-bildRessource.getWidth()/2,null);
		}			
		if (daten.getInt("spielerstart")!=0){
			g.setFont(new Font("Arial",Font.BOLD,24));
			g.setColor(new Color(0,0,0));
			g.drawString(daten.getString("spielerstart"),5,24);
		}
		g.dispose();
		if (frontend.getZoomfaktor()==100)
			im=bild;
		else
			im=bild.getScaledInstance(bild.getWidth()*frontend.getZoomfaktor()/100,bild.getHeight()*frontend.getZoomfaktor()/100,Image.SCALE_FAST);
		if (im!=null){
			Icon ic=new ImageIcon(im);
			setIcon(ic);			
		}
		else{
			setIcon(null);
		}
		}catch(RuntimeException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString(){
		String s="Feld "+this.x+"/"+this.y+" vom Typ "+this.feldArt;
		if (this.ressource.length()>0) s+=" und Ressource "+this.ressource;
		if (this.spielerNummer>0) s+=" und Start von Spieler Nummer "+this.spielerNummer;
		return s;
	}
	
}
