package frontend.menu.rechts;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;

import frontend.Frontend;
import frontend.EventHandler.UniversumsEventHandler;
import frontend.karte.Universum;
import ru.daten.D;
import ru.daten.D_Fehler;
import ru.daten.D_FeldArt;
import ru.daten.D_Karte;
import ru.daten.Xml;

public class MenuRechtsUniversum extends MenuRechts{
	private static final long serialVersionUID = 1L;
	
	public MenuRechtsUniversum(Frontend frontend) {
		super(frontend);
		this.backendKarteneditor = frontend.getBackendKarteneditor();
		
		D datenKarte = Xml.toD(backendKarteneditor.getKartenDaten());
		if (!(datenKarte instanceof D_Fehler))
			kartenArt = ((D_Karte) datenKarte).getString("kartenArt");

		
		if ("Weltraum".equals(kartenArt)) {
			aktionsButton = new JRadioButton[3];

			ButtonGroup aktionenGruppe = new ButtonGroup();
			
			// Aktionen
			addHeader("AKTION");
			aktionsButton[0] = addAktion(aktionenGruppe, "Gestirn setzen");
			aktionsButton[1] = addAktion(aktionenGruppe, "Gestirn bearbeiten");
			aktionsButton[2] = addAktion(aktionenGruppe, "Wurmloch bearbeiten");

			if (kartenArt != null) {
				for (int i = 0; i < aktionsButton.length; i++)
					aktionsButton[i].setEnabled(true);
				aktionsButton[0].doClick();
			} else {
				for (int i = 0; i < aktionsButton.length; i++)
					aktionsButton[i].setEnabled(false);
			}

			// Feldart
			addHeader("STERNE");
			wahlFeldArt.setFont(new Font("Arial", Font.BOLD, 11));
			if (kartenArt != null) {
				D datenFelder = Xml.toD(backendKarteneditor.getErlaubteFeldArten(kartenArt));
				D_FeldArt feldArten = (D_FeldArt) datenFelder;
				boolean isPlanet = true,isMond = true;
				for (String feldArt : feldArten.getListe()){
					if(feldArt.toLowerCase().contains("planet") && isPlanet){
						wahlFeldArt.addItem("Planet");
						wahlFeldArt.addActionListener(this);
						wahlFeldArt.setEnabled(true);
						isPlanet = false;
					}else if(feldArt.toLowerCase().contains("mond") && isMond){
						wahlFeldArt.addItem("Mond");
						wahlFeldArt.addActionListener(this);
						wahlFeldArt.setEnabled(true);
						isMond = false;
					}else if(feldArt.toLowerCase().contains("wurmloch") || feldArt.toLowerCase().contains("leere")){
						wahlFeldArt.addItem(feldArt);
						wahlFeldArt.addActionListener(this);
						wahlFeldArt.setEnabled(true);
					}
					
				}
			} else {
				wahlFeldArt.setEnabled(false);
			}
			add(wahlFeldArt);
			// Zoom
			addHeader("ZOOM in %");
			if (kartenArt != null) {
				wahlZoom.setFont(new Font("Arial", Font.BOLD, 11));
				for (int i = 20; i <= 200; i += 20)
					wahlZoom.addItem(i);
				wahlZoom.setSelectedItem(frontend.getZoomfaktor());
				wahlZoom.addActionListener(this);
				wahlZoom.setEnabled(true);
			} else {
				wahlZoom.setEnabled(false);
			}
			add(wahlZoom);
			// Spiel-Buttons
			for (int i = 0; i < spielButtons.length; i++) {
				spielButtons[i] = new JButton();
				spielButtons[i].addActionListener(this);
				add(spielButtons[i]);
			}
			spielButtons[0].setText("");
			// Wenn eine Kartenart existiert, diese holen
			if (kartenArt != null) {

				Universum karte = frontend.getUniversum();
				karte.setEventhandler(new UniversumsEventHandler(frontend));
			}
		}
	}

	
	@Override
	public void actionPerformed(ActionEvent ev) {
		Object o = ev.getSource();
		if (o == aktionsButton[0]) // Feldart setzen
				addAktionsReaktion(true, false, "Jetzt koennen Sie die gewaehlte Himmelskoerper auf der Karte verbreiten.",
						AktionMenuRechts.GestirnSetzen);
		if (o == aktionsButton[1]) // Resource setzen
				addAktionsReaktion(false, false, "Jetzt koennen Sie ein Planeten oder ein Mond bearbeiten.",
						AktionMenuRechts.GestirnBearbeiten);
		if (o == aktionsButton[2]) // Resource entfernen
				addAktionsReaktion(false, false, "Jetzt koennen Sie Wurmloecher bearbeiten.",
						AktionMenuRechts.WurmlochBearbeiten);
		if (o == wahlFeldArt) { // Feldart auswaehlen
			String feldArt = (String) wahlFeldArt.getSelectedItem();
			frontend.log("Gewaehlte Feldart: " + feldArt );
		}
		if (o == wahlZoom) { // Zoom auswaehlen
			int zoom = (Integer) wahlZoom.getSelectedItem();
			frontend.log("Zoomfaktor: " + zoom + "%");
			frontend.setZoomfaktor(zoom);
			frontend.zeichneFelderUniversum(null);
		}
		if (o == spielButtons[0]) {
			// TODO Funktionalitaet einbauen
		}
	}


	@Override
	protected void addAktionsReaktion(boolean wahlFeldArtEnabled, boolean wahlWurmlich, String logText,
			AktionMenuRechts aktionArt) {
		wahlFeldArt.setEnabled(wahlFeldArtEnabled);
		frontend.log(logText);
		aktion = aktionArt;
	}

}
