package frontend;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import backend.BackendKarteneditorStub;
import frontend.karte.Karte;
import frontend.menu.rechts.MenuRechts;
import ru.daten.D;
import ru.daten.D_Feld;
import ru.daten.D_Karte;
import ru.daten.Xml;
import ru.interfaces.iBackendKarteneditor;

public class FrontendGestirn extends JFrame{

	private JPanel panel=new JPanel();
	private Frontend frontend;
	private MenuRechts menuRechts=null;
	private JScrollPane scrollerKarte;
	private iBackendKarteneditor backendKarteneditor;
	private int spielfeldGroesse=50;
	private int zoomfaktor=100;
	private Karte karte;
	
	private JTextArea ta=new JTextArea(6,20);
	private JTextField st=new JTextField("");
	private JButton ok = new JButton("Speichern");
	private JButton abbruch = new JButton("Abbrechen");
	
	public FrontendGestirn(Frontend f,Karte karte,boolean bearbeite){
		super();
		backendKarteneditor = f.getBackendKarteneditor();
		frontend = f;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("Neue Karte erzeugen");
		setLayout(new BorderLayout());
		panel.setLayout(new BorderLayout());
		
    // Logger:
    JPanel logger=new JPanel();
    logger.setLayout(new BorderLayout());
		ta.setFont(new Font("Arial", Font.PLAIN, 11));
		ta.setOpaque(true);
		ta.setEditable(false);
		logger.add(new JScrollPane(ta),BorderLayout.NORTH);
		logger.add(st,BorderLayout.CENTER);
		JPanel buttons=new JPanel();
		buttons.setLayout(new BorderLayout());
		
		buttons.add(ok,BorderLayout.CENTER);
		if(!bearbeite){
		buttons.add(abbruch, BorderLayout.SOUTH);
		}
		logger.add(buttons, BorderLayout.SOUTH);
		
		panel.add(logger,BorderLayout.SOUTH);
		
		
		
		// Fenster:
		add(panel);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		
	}
	
	public void setKarte(Karte k){
		
		karte = k;
		for (int x = 1; x < karte.getGroesseX(); x++) {
			for (int y = 1; y < karte.getGroesseY(); y++) {
				karte.getFelder()[x][y].zeichnen();
			}
		}
		setTitle("Bearbeitung vom "+karte.getKartenArt()+": "+karte.getName()+" mit der ID: "+karte.getId());
		scrollerKarte=new JScrollPane(karte,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(scrollerKarte,BorderLayout.CENTER);
		panel.revalidate();
		panel.repaint();
		setVisible(true);
	}
	
	public JButton getOk() {
		return ok;
	}

	public JButton getAbbruch() {
		return abbruch;
	}
	
	public void setMenuRechts(MenuRechts mr){
		if (menuRechts!=null) panel.remove(menuRechts);
		
		menuRechts=mr;
		ok.addActionListener(menuRechts);
		abbruch.addActionListener(menuRechts);
		menuRechts.setEnabled(true);
		panel.add(menuRechts,BorderLayout.EAST);
		panel.revalidate();
		panel.repaint();
		
	}
	
	
	public iBackendKarteneditor getBackendKarteneditor() {
		return backendKarteneditor;
	}
	
	public void setSpielfeldGroesse(int x){
		spielfeldGroesse=x;
	}
	
	public int getSpielfeldGroesse(){
		return spielfeldGroesse;
	}
	
	public void setZoomfaktor(int x){
		zoomfaktor=x;
	}
	
	public int getZoomfaktor(){
		return zoomfaktor;
	}

	public MenuRechts getMenuRechts() {
		return menuRechts;
	}
	
//Neue Karten werden in einer ArrayList abgelegt da es nicht bekannt ist wieviele Planeten oder Monde es gibt
	public Karte neueKarte(String backendDaten,String fArt,String kartenArt){
		if (frontend.getKarte()==null){
			frontend.newKarteArrayList(); //verändert
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
		karte = new Karte(this,id,x,y,fArt,kartenArt,kartenDaten.getString("name"),kartenDaten.getString("globusArt")); // Karte wird gesetzt
		frontend.addKarte(karte);
		zeichneFelder(daten);
		scrollerKarte=new JScrollPane(karte,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(scrollerKarte,BorderLayout.CENTER);
		panel.revalidate();
		panel.repaint();
		setVisible(true);
		
		return karte; //verändert
	}
	
//karte wird vom Server geladen
	public Karte ladeKarte(ArrayList<D> daten){
		if (frontend.getKarte()==null){
			frontend.newKarteArrayList(); //verändert
		}
		
		karte = new Karte(this,daten); // Karte wird gesetzt
		
		
		frontend.addKarte(karte);
		zeichneFelder(daten);
		scrollerKarte=new JScrollPane(karte,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(scrollerKarte,BorderLayout.CENTER);
		panel.revalidate();
		panel.repaint();
		
		return karte; //verändert
	}
	
	//karte wird vom Server geladen
	public Karte ladeKarte(ArrayList<D> daten,String kartenArt, int kartenID){
		if (frontend.getKarte()==null){
			frontend.newKarteArrayList(); //verändert
		}
		int i;
		for (i=0;i<daten.size();i++){
			if (daten.get(i) instanceof D_Karte) break;
		}
		D_Karte kartenDaten=(D_Karte)daten.get(0);
		int x=kartenDaten.getInt("x");
		int y=kartenDaten.getInt("y");
		int id=kartenID;
		
		karte = new Karte(this,id,x,y,"Berg",kartenArt,kartenDaten.getString("name"),kartenDaten.getString("globusArt")); // Karte wird gesetzt
		
		for(D d :daten){
			if (d instanceof D_Feld){
				D_Feld df=(D_Feld)d;
				karte.getFelder()[df.getInt("x")][df.getInt("y")].setFeldArt(df.getString("feldArt"));
				if(df.getString("ressource").length()>0){
					karte.getFelder()[df.getInt("x")][df.getInt("y")].setRessource(df.getString("ressource"));
				}
				karte.getFelder()[df.getInt("x")][df.getInt("y")].setSpielerNummer(df.getInt("spielerstart"));
			}
		}
		frontend.addKarte(karte);
		zeichneFelder(daten);
		scrollerKarte=new JScrollPane(karte,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(scrollerKarte,BorderLayout.CENTER);
		panel.revalidate();
		panel.repaint();
		setVisible(true);
		
		return karte; //verändert
	}
	
//Verändert
	public void zeichneFelder(ArrayList<D> daten) {
		if (daten==null){
			String backendDaten=backendKarteneditor.getKarte();
			daten=Xml.toArray(backendDaten);
		}	
		if (karte!=null){ 
			karte.zeichneFelder(daten);
		}
	}
	
//Verändert
	public void zeichneFeld(int[] pos) {
		if (karte!=null){
			karte.zeichneFeld(pos);
		}
	}
	
	public Karte getKarte(){
		return karte;
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
	
	
}
