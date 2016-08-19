package frontend.menu.rechts;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import frontend.Frontend;
import frontend.FrontendGestirn;
import ru.daten.D;
import ru.daten.D_Fehler;
import ru.daten.D_FeldArt;
import ru.daten.D_Karte;
import ru.daten.D_RessourcenArt;
import ru.daten.Xml;

public class MenuRechtsKarte extends MenuRechts {

	private static final long serialVersionUID = 1L;
	private FrontendGestirn fGestirn;
	private boolean bearbeitet;

	private JSpinner wahlSpielernummer = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));

	public MenuRechtsKarte(Frontend frontend, FrontendGestirn fGestirn, String url,boolean bearbeitung) {
		super(frontend);
		this.backendKarteneditor = fGestirn.getBackendKarteneditor();
		this.fGestirn = fGestirn;
		this.bearbeitet = bearbeitung;
		wahlFeldArt.setEnabled(false);
		wahlSpielernummer.setEnabled(false);
		D datenKarte = Xml.toD(backendKarteneditor.getKartenDaten());
		if (!(datenKarte instanceof D_Fehler))
			kartenArt = ((D_Karte) datenKarte).getString("kartenArt");

		if (!"Weltraum".equals(kartenArt)) {
			// Aktionen
			addHeader("AKTION");
			aktionsButton = new JRadioButton[4];

			ButtonGroup aktionenGruppe = new ButtonGroup();

			aktionsButton[0] = addAktion(aktionenGruppe, "Feldart setzen");
			aktionsButton[1] = addAktion(aktionenGruppe, "Resource setzen");
			aktionsButton[2] = addAktion(aktionenGruppe, "Resource loeschen");
			aktionsButton[3] = addAktion(aktionenGruppe, "Spielerstart setzen");
			if (kartenArt != null) {
				for (int i = 0; i < aktionsButton.length; i++)
					aktionsButton[i].setEnabled(true);
				if (!kartenArt.equals("Planet"))
					aktionsButton[3].setEnabled(false);
				aktionsButton[0].doClick();
			} else {
				for (int i = 0; i < aktionsButton.length; i++)
					aktionsButton[i].setEnabled(false);
			}
			// Feldart
			addHeader("FELDART");
			wahlFeldArt.setFont(new Font("Arial", Font.BOLD, 11));
			if (kartenArt != null) {
				D datenFelder = Xml.toD(backendKarteneditor.getErlaubteFeldArten(kartenArt));
				D_FeldArt feldArten = (D_FeldArt) datenFelder;
				for (String feldArt : feldArten.getListe())
					wahlFeldArt.addItem(feldArt);
				wahlFeldArt.addActionListener(this);
				wahlFeldArt.setEnabled(true);
			} else {
				wahlFeldArt.setEnabled(false);
			}
			add(wahlFeldArt);
			// Spielernummer
			addHeader("SPIELERNUMMER");
			if ((kartenArt != null) && (kartenArt.equals("Planet"))) {
				wahlSpielernummer.setFont(new Font("Arial", Font.BOLD, 11));
				wahlSpielernummer.setEditor(new JSpinner.DefaultEditor(wahlSpielernummer));
				wahlSpielernummer.setEnabled(true);
			} else {
				wahlSpielernummer.setEnabled(false);
			}
			add(wahlSpielernummer);
			// Zoom
			addHeader("ZOOM in %");
			if (kartenArt != null) {
				wahlZoom.setFont(new Font("Arial", Font.BOLD, 11));
				for (int i = 20; i <= 200; i += 20)
					wahlZoom.addItem(i);
				wahlZoom.setSelectedItem(fGestirn.getZoomfaktor());
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
			aktionsButton[0].doClick();

		} else {
			frontend.log("Falsche Karten Art es muss ein Universum sein");
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		Object o = ev.getSource();
		if (o == aktionsButton[0]) // Feldart setzen
			addAktionsReaktion(true, false, "Jetzt koennen Sie die gewaehlte Feldart auf der Karte verbreiten.",
					AktionMenuRechts.FeldArtSetzen);
		if (o == aktionsButton[1]) // Resource setzen
				addAktionsReaktion(false, false,"Jetzt koennen Sie je eine passende Resource pro Feld verbreiten.",
						AktionMenuRechts.RessourceSetzen);
		if (o == aktionsButton[2]) // Resource entfernen
				addAktionsReaktion(false, false,"Jetzt koennen Sie Resourcen von der Karte entfernen.",
						AktionMenuRechts.RessourceLoeschen);
		if (o == aktionsButton[3]) // Spielerstart setzen
				addAktionsReaktion(false, true,
						"Jetzt koennen Sie Startposition der gewaehlten Spielernummer auf der Karte definieren.",
						AktionMenuRechts.SpielerstartSetzen);
		if (o == wahlFeldArt) { // Feldart auswaehlen
			String feldArt = (String) wahlFeldArt.getSelectedItem();
			D_RessourcenArt feldRessourcenArten = (D_RessourcenArt) Xml
					.toD(backendKarteneditor.getErlaubteRessourcenArten(feldArt));
			fGestirn.log("Gewaehlte Feldart: " + feldArt + " " + feldRessourcenArten.getListe());
		}
		if (o == wahlSpielernummer) { // Spielerstart auswaehlen
			fGestirn.log("Gewaehlte Spielernummer: " + wahlSpielernummer.getValue());
		}
		if (o == wahlZoom) { // Zoom auswaehlen
			int zoom = (Integer) wahlZoom.getSelectedItem();
			fGestirn.log("Zoomfaktor: " + zoom + "%");
			fGestirn.setZoomfaktor(zoom);

			fGestirn.zeichneFelder(null);

		}
		// Abbruch wenn keine karte erzeugt werden soll
		if (o == fGestirn.getAbbruch()) {
			frontend.setEnabled(true);
			if (!bearbeitet) {
				frontend.getFunktionenKarteneditor().UniversumWiederHolen(false);
				frontend.delKarte(fGestirn.getKarte());
			}
			fGestirn.dispose();
			bearbeitet = false;
		}
		// Karte speichern und erzeugen in ein Universum
		if (o == fGestirn.getOk()) {
			
			frontend.setEnabled(true);
			frontend.getFunktionenKarteneditor().UniversumWiederHolen(true);
			fGestirn.dispose();
			fGestirn = null;
			bearbeitet = true;
		}
	}

	public int getSpielernummer() {
		return (int) wahlSpielernummer.getValue();
	}

	@Override
	protected void addAktionsReaktion(boolean wahlFeldArtEnabled, boolean wahlSpielernummerEnabled, String logText,
			AktionMenuRechts aktionArt) {
		wahlFeldArt.setEnabled(wahlFeldArtEnabled);
		wahlSpielernummer.setEnabled(wahlSpielernummerEnabled);
		fGestirn.log(logText);
		aktion = aktionArt;
	}

}
