package frontend;

import frontend.karte.Karte;
import frontend.karte.Universum;
import frontend.menu.FunktionenAdmin;
import frontend.menu.FunktionenKarteneditor;
import frontend.menu.MenuTop;
import frontend.menu.rechts.MenuRechts;
import frontend.menu.rechts.MenuRechtsUniversum;
import ru.backend.Parameter;
import ru.daten.D;
import ru.daten.D_Feld;
import ru.daten.D_Karte;
import ru.daten.Xml;
import ru.interfaces.iBackendKarteneditor;
import ru.interfaces.iBackendSpielAdmin;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import backend.BackendKarteneditorStub;
import backend.BackendSpielAdminStub;

public class Frontend extends JFrame{
	private static final long serialVersionUID = 1L;
	private iBackendKarteneditor backendKarteneditor;
	private iBackendSpielAdmin backendSpielAdmin;
	private MenuTop menuTop;
	private MenuRechts menuRechts=null;
	private JPanel panel=new JPanel();
	private JTextArea ta=new JTextArea(6,20);
	private JTextField st=new JTextField("");
	private JScrollPane scrollerKarte;
	private Universum universum; // Universums Karte wird erzeugt
	private ArrayList<Karte> karte;
	private int spielfeldGroesse=50;
	private int zoomfaktor=100;
	private FunktionenAdmin funktionenAdmin;
	private FunktionenKarteneditor funktionenKarteneditor;
	private String url;
	
	public Frontend(String url){
		super();
		this.url = url;
		backendKarteneditor=new BackendKarteneditorStub(url);
		backendSpielAdmin=new BackendSpielAdminStub(url);
		funktionenAdmin=new FunktionenAdmin(this);
		funktionenKarteneditor=new FunktionenKarteneditor(this);
		setTitle("Rising Universe Administration, Version 0.80");
		setLayout(new BorderLayout());
		panel.setLayout(new BorderLayout());
		menuTop=new MenuTop(this);
		// Menu:
		panel.add(menuTop,BorderLayout.NORTH);
    // Logger:
    JPanel logger=new JPanel();
    logger.setLayout(new BorderLayout());
		ta.setFont(new Font("Arial", Font.PLAIN, 11));
		ta.setOpaque(true);
		ta.setEditable(false);
		logger.add(new JScrollPane(ta),BorderLayout.CENTER);
		logger.add(st,BorderLayout.SOUTH);
		panel.add(logger,BorderLayout.SOUTH);
		// Fenster:
		add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		
//		setMenuRechts(new MenuRechtsUniversum(this));
		setVisible(true);
	}
	
	public String getUrl(){
		return url;
	}
	
	public void log(String text){
		if (ta.getText().length()==0)
			ta.setText(" "+text);
		else
			ta.setText(ta.getText()+"\n"+" "+text);
	}
	
	public void logWarte(String text){
		ta.setText(" "+text);
	}
	
	public void setStatus(String text){
		st.setText(" "+text);
	}

	public iBackendKarteneditor getBackendKarteneditor() {
		return backendKarteneditor;
	}
	
	public iBackendSpielAdmin getBackendSpielAdmin() {
		return backendSpielAdmin;
	}

	// Eine nue Universums Karte wird erzeut
	public Universum neuesUniversum(String backendDaten){
		if (this.universum!=null){
			universum.terminate();
			panel.remove(scrollerKarte);
		}
		ArrayList<D> daten=Xml.toArray(backendDaten);
		int i;
		for (i=0;i<daten.size();i++){
			if (daten.get(i) instanceof D_Karte) break;
		}
		D_Karte kartenDaten=(D_Karte)daten.get(0);
		int x=kartenDaten.getInt("x");
		int y=kartenDaten.getInt("y");
		int id=kartenDaten.getInt("id");
		this.universum=new Universum(this,id,x,y,"Leere",kartenDaten.getString("name"));
		zeichneFelderUniversum(daten);
		scrollerKarte=new JScrollPane(universum,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(scrollerKarte,BorderLayout.CENTER);
		panel.revalidate();
		panel.repaint();
		return universum;
	}
	
	//das einladen eines Universum wenn es aus Xml Daten eingelesen wird
	public Universum ladeUniversum(Universum newUniversum,ArrayList<D> daten){
		if (this.universum!=null){
			universum.terminate();
			panel.remove(scrollerKarte);
		}
		this.universum=newUniversum;
		int i;
		for (i=0;i<daten.size();i++){
			if (daten.get(i) instanceof D_Karte) break;
		}
		zeichneFelderUniversum(daten);
		scrollerKarte=new JScrollPane(universum,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(scrollerKarte,BorderLayout.CENTER);
		panel.revalidate();
		panel.repaint();
		return universum;
	}
	
	// zeichen Universums Karte
	public void zeichneFelderUniversum(ArrayList<D> daten) {
		if (daten==null){
			String backendDaten=backendKarteneditor.getKarte();
			daten=Xml.toArray(backendDaten);
		}	
		if (universum!=null) universum.zeichneFelder(daten);
	}

	// zeichene Universums Karte
	public void zeichneFeldUniversum(int[] pos) {
		if (universum!=null) universum.zeichneFeld(pos);
	}
	
	public void setMenuRechts(MenuRechts mr){
		if (menuRechts!=null) panel.remove(menuRechts);
		menuRechts=mr;
		menuRechts.setEnabled(true);
		panel.add(menuRechts,BorderLayout.EAST);
		panel.revalidate();
		panel.repaint();
	}
	
	public MenuRechts getMenuRechts(){
		return menuRechts;
	}
	
	public Universum getUniversum(){
		return universum;
	}
	
	public void setUniversum(Universum universum){
		this.universum = universum;
	}
	
	public ArrayList<Karte> getKarte(){
		return karte;
	}
	
	//setzt die ArrayList von Karten neu 
	public void newKarteArrayList(){
		karte = new ArrayList<Karte>();
	}
	
	public void addKarte(Karte k){
		karte.add(k);
	}
	
	//Gibt eine KArte zurück an hand der ID
	public Karte getKarteByID(int id){
		Karte k=null;
		for(Karte karte : this.karte){
			if(karte.getId()==id){
				k = karte;
			}
		}
		return k;
	}
	
	//Löscht einekarte
	public void delKarte(Karte k){
		karte.remove(k);
	}
	
	// Setzt alle karten wieder aus inaktiv wenn sie grade nicht genutzt werden
	public void inaktivKarte(){
		for(Karte k : karte){
				k.setAktive(false);
		}
	}
	
	public void setZoomfaktor(int x){
		zoomfaktor=x;
	}
	
	public int getZoomfaktor(){
		return zoomfaktor;
	}
	
	public void setSpielfeldGroesse(int x){
		spielfeldGroesse=x;
	}
	
	public int getSpielfeldGroesse(){
		return spielfeldGroesse;
	}

	public FunktionenAdmin getFunktionenAdmin() {
		return funktionenAdmin;
	}
	public FunktionenKarteneditor getFunktionenKarteneditor() {
		return funktionenKarteneditor;
	}
}
