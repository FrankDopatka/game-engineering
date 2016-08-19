package frontend.karte;

import ru.daten.D;
import ru.daten.D_Feld;
import ru.daten.D_Karte;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import frontend.Frontend;
import frontend.EventHandler.EventHandler;
import frontend.karte.flaechen.Raum;

public class Universum extends Atlas{
	private static final long serialVersionUID = 1L;
	private Frontend frontend;
	private String name;
	private ArrayList<Integer> wurrmlochID = new ArrayList<Integer>();

	public Universum(Frontend frontend,int id,int groesseX,int groesseY,String fArt,String universumsName) {
		super(frontend.getBackendKarteneditor(),id,groesseX,groesseY);
		this.frontend=frontend;
		name = universumsName;
		
		int spielfeldGroesse=frontend.getSpielfeldGroesse();
		int zoomfaktor=frontend.getZoomfaktor();
		int offset=spielfeldGroesse*zoomfaktor/100;
		setLayout(null);
		setPreferredSize(new Dimension(groesseX*offset,groesseY*offset));
		felder=new Raum[groesseX+1][groesseY+1];
		for (int i=1;i<=groesseX;i++){
			for (int j=1;j<=groesseY;j++){
				Raum f=new Raum(frontend,i,j,fArt);
				felder[i][j]=f;
				add(f);
			}
		}
	}
	
	public Universum(Frontend frontend,ArrayList<D> xmlDaten) {
		super(frontend.getBackendKarteneditor(),0,0,0);
		this.frontend=frontend;
		
		D_Karte datenKarte=null;
		D datenRoh=xmlDaten.get(0);
		if(!(datenRoh instanceof D_Karte))
			throw new RuntimeException("Karte karteVonXml: Die eingelesenden Rohdaten sind keine Karte!");
		try {
			datenKarte=(D_Karte)datenRoh;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Karte karteVonXml: "+e.getMessage());
		}
		this.groesseX = datenKarte.getInt("x");
		this.groesseY = datenKarte.getInt("y");
		this.id = datenKarte.getInt("id");
		
		name = datenKarte.getString("name");
		
		int spielfeldGroesse=frontend.getSpielfeldGroesse();
		int zoomfaktor=frontend.getZoomfaktor();
		int offset=spielfeldGroesse*zoomfaktor/100;
		setLayout(null);
		setPreferredSize(new Dimension(groesseX*offset,groesseY*offset));
		felder=new Raum[groesseX+1][groesseY+1];
		for(D daten:xmlDaten){
			if (daten instanceof D_Feld){
				D_Feld df=(D_Feld)daten;
				int d_x = df.getInt("x");
				int d_y = df.getInt("y");
				String d_fArt = df.getString("feldArt");
				int d_id = df.getInt("id");
				if(df.existKey("refKarte")){
					d_id = df.getInt("refKarte");
				}
				int d_zielX = 0;
				int d_zielY = 0;
				int d_biDi = 0;
				if(df.existKey("zielX")){
					d_zielX = df.getInt("zielX");
					d_zielY = df.getInt("zielY");
					d_biDi = df.getInt("biDirektional");
				}
				Raum f = new Raum(this.frontend,d_x,d_y,d_fArt,d_id,d_zielX,d_zielY,d_biDi);
				f.setAktive(false);
				this.felder[d_x][d_y] = f;
				add(f);
			}
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Integer> getWurlochID() {
		return wurrmlochID;
	}
	
	public void removeWurlochID(int i){
		wurrmlochID.remove(i);
	}

	public void addWurlochID(int i) {
		boolean isBesetzt = false;
		for(int pruefen : wurrmlochID){
			if(pruefen == i){
				isBesetzt = true;
			}
		}
		if(isBesetzt){
			System.out.println("ID ist bereits vergeben");
		}else{
			this.wurrmlochID.add(i);
		}
	}

	@Override
	public void setEventhandler(EventHandler eventHandler){
		for (int i=1;i<=groesseX;i++){
			for (int j=1;j<=groesseY;j++){
				felder[i][j].setEventhandler(eventHandler);
			}
		}
	}

	@Override
	public void zeichneFelder(ArrayList<D> daten){
		if (daten==null) return;
		int spielfeldGroesse=frontend.getSpielfeldGroesse();
		int zoomfaktor=frontend.getZoomfaktor();
		int offset=spielfeldGroesse*zoomfaktor/100;
		setPreferredSize(new Dimension(groesseX*offset,groesseY*offset));
		for(D datenwert:daten){
			if (datenwert instanceof D_Feld){
				D_Feld datenFeld=(D_Feld)datenwert;
				int x=datenFeld.getInt("x");
				int y=datenFeld.getInt("y");
				Raum f=(Raum) felder[x][y];
				f.setBounds(new Rectangle(offset,offset));
				f.setLocation((f.getPosX()-1)*offset,(f.getPosY()-1)*offset);
				f.zeichnen(datenFeld);
			}
		}
	}
	
	@Override
	public void zeichneFeld(int[] pos) {
		felder[pos[0]][pos[1]].zeichnen();
	}
	
	@Override
	public void terminate() {
		for (int i=1;i<=groesseX;i++){
			for (int j=1;j<=groesseY;j++){
				felder[i][j].terminate();
				felder[i][j]=null;
			}
		}
		felder=null;
	}
	
}
