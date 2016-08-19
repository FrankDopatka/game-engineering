package frontend.karte;

import ru.daten.D;
import ru.daten.D_Feld;
import ru.daten.D_Karte;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import frontend.FrontendGestirn;
import frontend.EventHandler.EventHandler;
import frontend.karte.flaechen.Feld;

public class Karte extends Atlas{
	private static final long serialVersionUID = 1L;
	private FrontendGestirn frontend;
	private boolean aktive; //gerade zu bearbeitende Karte
	private String name;
	private String kartenArt;
	private String globusArt;
	private EventHandler eventHandler = null;

	public Karte(FrontendGestirn frontend,int id,int groesseX,int groesseY,String fArt,String kartenArt,String kartenName,String globusArt) {
		super(frontend.getBackendKarteneditor(),id,groesseX,groesseY);
		this.frontend=frontend;
		this.kartenArt = kartenArt;
		this.globusArt = globusArt;
		name = kartenName;
		int spielfeldGroesse=frontend.getSpielfeldGroesse();
		int zoomfaktor=frontend.getZoomfaktor();
		int offset=spielfeldGroesse*zoomfaktor/100;
		setLayout(null);
		setPreferredSize(new Dimension(groesseX*offset,groesseY*offset));
		this.felder=new Feld[groesseX+1][groesseY+1];
		for (int i=1;i<=groesseX;i++){
			for (int j=1;j<=groesseY;j++){
				Feld f=new Feld(frontend,i,j,fArt);
				felder[i][j]=f;;
				add(f);
			}
		}
		aktive = true;
	}
	
	public Karte(FrontendGestirn frontend,ArrayList<D> xmlDaten) {
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
		kartenArt = datenKarte.getString("kartenArt");
		globusArt = datenKarte.getString("globusArt");
		int spielfeldGroesse=frontend.getSpielfeldGroesse();
		int zoomfaktor=frontend.getZoomfaktor();
		int offset=spielfeldGroesse*zoomfaktor/100;
		setLayout(null);
		setPreferredSize(new Dimension(groesseX*offset,groesseY*offset));
		this.felder=new Feld[groesseX+1][groesseY+1];
		
		for(D daten:xmlDaten){
			if (daten instanceof D_Feld){
				D_Feld df=(D_Feld)daten;		
				Feld f = new Feld(this.frontend,df.getInt("x"),df.getInt("y"),df.getString("feldArt"),df.getString("ressource"),df.getInt("spielerstart"));
				this.felder[df.getInt("x")][df.getInt("y")] = f;
				add(f);
				f.setVisible(true);
			}
		}
		aktive = true;
	}
	
public String getGlobusArt() {
		return globusArt;
	}

	public void setGlobusArt(String globusArt) {
		this.globusArt = globusArt;
	}

public String getKartenArt() {
		return kartenArt;
	}

	//gibt denn status zurueck
	public boolean getAktive(){
		return aktive;
	}
	
	public void setAktive(boolean aktive){
		this.aktive = aktive;
	}
	
	// fuer das neue Setzten der Karte auf dem Server
	public Feld[][] getFelder() {
		return (Feld[][]) felder;
	}
	
	public void setFelder(Feld[][] felder){
		this.felder = felder;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
	// Ermitteld die GlobusArt fuer denn Jeweiligen Planet oder Mond
	public String ermittleGlobusArt(){
		if(getKartenArt().equals("Planet")){
			return getPlanetArt();
		}else{
			return getMondArt();
		}
	}
	
	//Berechnung der moeglichen spezifischen Feldart
	private String getPlanetArt(){
		int eis=0,wasser=0,wueste=0,dschungel=0,huegel=0,steinberg=0,steppe=0,sumpf=0,wald=0,wiese=0;
		for(int i=1; i <felder.length; i++){
			for(int j=1; j <felder[i].length; j++){
				if(felder[i][j].getFeldArt().equalsIgnoreCase("Eis")){
					eis++;
				}else if(felder[i][j].getFeldArt().equalsIgnoreCase("Dschungel")){
					dschungel++;
				}else if(felder[i][j].getFeldArt().equalsIgnoreCase("Huegel")){
					huegel++;
				}else if(felder[i][j].getFeldArt().equalsIgnoreCase("Steppe")){
					steppe++;
				}else if(felder[i][j].getFeldArt().equalsIgnoreCase("Wald")){
					wald++;
				}else if(felder[i][j].getFeldArt().equalsIgnoreCase("Wiese")){
					wiese++;
				}else if(felder[i][j].getFeldArt().equalsIgnoreCase("Berg")){
					steinberg++;
				}else if(felder[i][j].getFeldArt().equalsIgnoreCase("Meer") || felder[i][j].getFeldArt().equalsIgnoreCase("Kueste")){
					wasser++;
				}else if(felder[i][j].getFeldArt().equalsIgnoreCase("Sumpf")){
					sumpf++;
				}else if(felder[i][j].getFeldArt().equalsIgnoreCase("Wueste")){
					wueste++;
				}
			}
		}
		if(eis>=(double)groesseX*groesseY*0.85){
			this.setGlobusArt("EisPlanet");
		}else if(dschungel>=(double)groesseX*groesseY*0.85){
			this.setGlobusArt("DschungelPlanet");
		}else if(huegel>=(double)groesseX*groesseY*0.85){
			this.setGlobusArt("HuegelPlanet");
		}else if((steinberg+huegel)>=(double)groesseX*groesseY*0.85){
			this.setGlobusArt("SteinBergPlanet");
		}else if(steppe>=(double)groesseX*groesseY*0.85){
			this.setGlobusArt("SteppePlanet");
		}else if(sumpf>=(double)groesseX*groesseY*0.85){
			this.setGlobusArt("SumpfPlanet");
		}else if(wald>=(double)groesseX*groesseY*0.85){
			this.setGlobusArt("WaldPlanet");
		}else if(wasser>=(double)groesseX*groesseY*0.85){
			this.setGlobusArt("WasserPlanet");
		}else if(wueste>=(double)groesseX*groesseY*0.85){
			this.setGlobusArt("WuestenPlanet");
		}else if(wiese>=(double)groesseX*groesseY*0.85){
			this.setGlobusArt("WiesePlanet");
		}else{
			this.setGlobusArt("ErdPlanet");
		}
		return this.getGlobusArt();
	}

	//Berechnung der moeglichen spezifischen MondArt
	private String getMondArt(){
		int eis=0,wueste=0;
		for(int i=1; i <felder.length; i++){
			for(int j=1; j <felder[i].length; j++){
				if(felder[i][j].getFeldArt().equalsIgnoreCase("Eis")){
					eis++;
				}else if(felder[i][j].getFeldArt().equalsIgnoreCase("Steppe") || felder[i][j].getFeldArt().equalsIgnoreCase("Wueste")){
					wueste++;
				}
			}
		}
		if(eis>=(double)groesseX*groesseY*0.85){
			this.setGlobusArt("EisMond");
		}else if(wueste>=(double)groesseX*groesseY*0.85){
			this.setGlobusArt("WuestenMond");
		}else{
			this.setGlobusArt("Mond");
		}
		return globusArt;
	}
	
	@Override
	public void setEventhandler(EventHandler eventHandler){
		this.eventHandler = eventHandler;
		for (int i=1;i<=groesseX;i++){
			for (int j=1;j<=groesseY;j++){
				felder[i][j].setEventhandler(this.eventHandler);
			}
		}
	}
	
	public void neuEventHandler(EventHandler eventHandler){
		this.eventHandler = eventHandler;
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
				Feld f=(Feld) felder[x][y];
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
