package frontend.menu.rechts;

import frontend.Frontend;
import frontend.EventHandler.KartenEventHandler;
import frontend.karte.Karte;
import frontend.karte.Universum;
import ru.daten.D;
import ru.daten.D_Fehler;
import ru.daten.D_FeldArt;
import ru.daten.D_Karte;
import ru.daten.D_RessourcenArt;
import ru.daten.Xml;
import ru.interfaces.iBackendKarteneditor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public abstract class MenuRechts extends JPanel implements ActionListener {
	protected static final long serialVersionUID = 1L;
	protected Frontend frontend;
	protected iBackendKarteneditor backendKarteneditor;
	protected JRadioButton[] aktionsButton;
	protected JComboBox<String> wahlFeldArt = new JComboBox<String>();
	protected JComboBox<Integer> wahlZoom = new JComboBox<Integer>();
	protected JButton[] spielButtons = new JButton[8];
	protected String kartenArt = null;
	protected AktionMenuRechts aktion = null;

	public enum AktionMenuRechts {
		FeldArtSetzen, RessourceSetzen, RessourceLoeschen, SpielerstartSetzen,
		GestirnSetzen, GestirnBearbeiten, WurmlochBearbeiten, WurmlochKoordinaten;// AktionMenu Für Universum
	}

	public MenuRechts(Frontend frontend) {
		this.frontend = frontend;
	
		setLayout(new GridLayout(19, 1));
		setPreferredSize(new Dimension(200, 200));

		// Auswahl ob Weltraum bearbeitet wird oder nicht
		
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
	}

	public AktionMenuRechts getAktion() {
		return aktion;
	}

	public String getFeldart() {
		return "" + wahlFeldArt.getSelectedItem();
	}

	// Karten Art ausgabe für die kontrolle was bearbeitet wird
	public String getKartenArt() {
		return "" + kartenArt;
	}

	protected void addHeader(String text) {
		JLabel x = new JLabel(text);
		x.setFont(new Font("Aral", Font.BOLD, 14));
		add(x);
	}

	protected JRadioButton addAktion(ButtonGroup gruppe, String text) {
		JRadioButton b = new JRadioButton(text);
		b.addActionListener(this);
		gruppe.add(b);
		add(b);
		return b;
	}

	abstract protected void addAktionsReaktion(boolean wahlFeldArtEnabled, boolean wahlSpielernummerEnabled, String logText,
			AktionMenuRechts aktionArt);
}