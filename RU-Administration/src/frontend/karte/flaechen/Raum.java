package frontend.karte.flaechen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import frontend.Frontend;
import frontend.EventHandler.EventHandler;
import frontend.EventHandler.UniversumsEventHandler;
import ru.daten.D_Feld;
import ru.daten.Xml;

public class Raum extends Platz{
	private static final long serialVersionUID = 1L;
	private Frontend frontend;
	private boolean aktive;
	private UniversumsEventHandler eventHandler;
	private int id;
	private int zielX = 0; //angaben für ein Wurmloch
	private int zielY = 0; //angaben für ein Wurmloch
	private int biDirektional = 0; //angaben für ein Wurmloch
	private boolean bearbeitet = false; //angaben für ein Wurmloch

	public Raum(Frontend frontend,int x,int y,String feldArt) {
		super(x,y,feldArt);
		this.frontend=frontend;
	}
	
	public Raum(Frontend frontend,int x,int y,String feldArt, int id, int zielX, int zielY, int biDirektional) {
		super(x,y,feldArt);
		this.frontend=frontend;
		this.setId(id);
		this.setBiDirektional(biDirektional);
		this.setZielX(zielX);
		this.setZielY(zielY);
	}
	
	public boolean getAktive() {
		return aktive;
	}

	public void setAktive(boolean aktive) {
		this.aktive = aktive;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getZielX() {
		return zielX;
	}

	public void setZielX(int zielX) {
		this.zielX = zielX;
	}

	public int getZielY() {
		return zielY;
	}

	public void setZielY(int zielY) {
		this.zielY = zielY;
	}

	public int getBiDirektional() {
		return biDirektional;
	}

	public void setBiDirektional(int biDirektional) {
		this.biDirektional = biDirektional;
	}
	
	public boolean isBearbeitet() {
		return bearbeitet;
	}

	public void setBearbeitet(boolean bearbeitet) {
		this.bearbeitet = bearbeitet;
	}

	@Override
	public void setEventhandler(EventHandler eventHandler){
		this.eventHandler = (UniversumsEventHandler) eventHandler;
		if (eventHandler != null) {
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
	
	//Universums Felder werden gezeichnet
	@Override
	public void zeichnen(D_Feld daten){
		Image im;
		BufferedImage bildIcon=frontend.getUniversum().getBildFeld(daten.getString("feldArt"));
		BufferedImage bild=new BufferedImage(frontend.getSpielfeldGroesse(),frontend.getSpielfeldGroesse(),BufferedImage.TYPE_INT_ARGB);
		Graphics g=bild.getGraphics();
		g.drawImage(bildIcon,0,0,null);
		if (daten.getString("ressource").length()>0){
			BufferedImage bildRessource=frontend.getUniversum().getBildRessource(daten.getString("ressource"));
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
	}
	
	@Override
	public String toString(){
		D_Feld d=(D_Feld)Xml.toD(frontend.getBackendKarteneditor().getFeldDaten(x,y));
		String s="Feld "+d.getInt("x")+"/"+d.getInt("y")+" vom Typ "+d.getString("feldArt");
		if (d.getString("ressource").length()>0) s+=" und Ressource "+d.getString("ressource");
		if (d.getInt("spielerstart")>0) s+=" und Start von Spieler Nummer "+d.getInt("spielerstart");
		return s;
	}
}
