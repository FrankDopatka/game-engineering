package frontend.menu;

import ru.backend.karte.*;
import ru.daten.D;
import ru.daten.D_Fehler;
import ru.daten.D_FeldArt;
import ru.daten.D_Karte;
import ru.daten.Xml;
import ru.interfaces.iBackendKarteneditor;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.synth.SynthSpinnerUI;

import frontend.Frontend;
import frontend.FrontendGestirn;
import frontend.EventHandler.KartenEventHandler;
import frontend.EventHandler.UniversumsEventHandler;
import frontend.karte.Karte;
import frontend.karte.Universum;
import frontend.karte.flaechen.Raum;
import frontend.menu.rechts.MenuRechtsKarte;
import frontend.menu.rechts.MenuRechtsUniversum;

public class FunktionenKarteneditor {
	private Frontend frontend;
	private iBackendKarteneditor backendUniversumseditor;
	private FrontendGestirn fGestirn = null;
	private KartenEventHandler eventHandler = null;

	public FunktionenKarteneditor(Frontend frontend) {
		this.frontend = frontend;
		this.backendUniversumseditor = frontend.getBackendKarteneditor();
		eventHandler = new KartenEventHandler(frontend, fGestirn, frontend.getBackendKarteneditor());
	}

	public void neueKarte() {
		Integer[] idNummern = this.getMoeglicheUniversumsIds();
		
		JTextField weltraumName = new JTextField();
		JSpinner spinnerId = new JSpinner(new SpinnerListModel(idNummern));
		JSpinner spinnerX = new JSpinner(new SpinnerNumberModel(10, 5, 120, 1));
		JSpinner spinnerY = new JSpinner(new SpinnerNumberModel(10, 5, 120, 1));
		final JLabel weltraumKartenArt = new JLabel(); // Universums Karten art
																										// setzen
		final JLabel weltraumFeldArt = new JLabel(); // Standart feld

		weltraumKartenArt.setText("Weltraum");
		weltraumFeldArt.setText("Leere");

		Object[] eingaben = { "Name des Universums", weltraumName, "Kartenart: " + weltraumKartenArt.getText(), "",
				"ID der Karte", spinnerId, "Kartengroesse X", spinnerX, "KartengroesseY", spinnerY,
				"Standard-Feldart: " + weltraumFeldArt.getText() };
		JOptionPane eingabe = new JOptionPane(eingaben, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		eingabe.createDialog(frontend, "Neue Karte erzeugen...").setVisible(true);
		if ((eingabe.getValue() != null) && (eingabe.getValue().equals(JOptionPane.OK_OPTION))) {
			try {
				int id = D.toInt("" + spinnerId.getValue());
				int x = D.toInt("" + spinnerX.getValue());
				int y = D.toInt("" + spinnerY.getValue());
				String kArt = (String) weltraumKartenArt.getText();
				String fArt = (String) weltraumFeldArt.getText();
				String name = weltraumName.getText();
				if(name.length()<2){
					throw new Exception();
				}
				frontend.log("Erstelle neue Karte ID: " + id + ", X: " + x + " Felder, Y: " + y + " Felder, Kartentyp: " + kArt
						+ ", Standardfeld: " + fArt + "...");
				// Karten im Backend erzeugen:
				String backendDaten = backendUniversumseditor.neueKarte(name, id, x, y, kArt, fArt,0,kArt);
				frontend.neuesUniversum(backendDaten);
				
				frontend.newKarteArrayList();
				
				//Erzeugt ein neues MEnuRechts für die neue Karte
				frontend.setMenuRechts(new MenuRechtsUniversum(frontend));
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frontend, "Ihre Eingaben sind ungueltig!", "Neue Karte erzeugen...",
						JOptionPane.WARNING_MESSAGE);
				//e.printStackTrace();
			}
		}
	}
	
	// Übergibt die Möglichen IDs für neue Universem
	private Integer[] getMoeglicheUniversumsIds(){
		int maxNummern = 999;
		int zaehler = 0;
		Integer[] idNummern;

		// laden der Möglichen auswahl von Universen aus der Server Liste
		ArrayList<D> liste = Xml.toArray(backendUniversumseditor.auswahlUniversum());
		if (liste != null) {
			int[] belegteID = new int[liste.size()];
			if (liste != null) {
				for (D d_Auswahl : liste) {
					belegteID[zaehler] = Integer.valueOf(d_Auswahl.getString("id"));
					zaehler++;
					maxNummern--;
				}
			}

			idNummern = new Integer[maxNummern];
			int eintrag = 1;
			int x = 0;
			while (x < maxNummern) {
				boolean isNichtVergeben = true;
				for (int i = 0; i < zaehler; i++) {
					if (eintrag == belegteID[i]) {
						isNichtVergeben = false;
					}
				}
				if (isNichtVergeben) {
					idNummern[x] = eintrag;
					x++;
					eintrag++;
				} else {
					eintrag++;
				}
			}
		} else {
			idNummern = new Integer[maxNummern];
			for (int x = 1; x <= maxNummern; x++) {

				idNummern[x - 1] = x;
			}
		}
		return idNummern;
	}

	// Berechnung die noch möglichen KartenIDs im Universum
	private Integer[] getMoeglicheIds() {
		int maxNummern = 999;
		int zaehler = 0;
		Integer[] idNummern;
		if (frontend.getKarte() != null) {
			int[] belegteID = new int[frontend.getKarte().size()];
			for (Karte k : frontend.getKarte()) {
				belegteID[zaehler] = k.getId();
				zaehler++;
				maxNummern--;
			}

			idNummern = new Integer[maxNummern];
			int eintrag = 1;
			int x = 0;
			while (x < maxNummern) {
				boolean isNichtVergeben = true;
				for (int i = 0; i < zaehler; i++) {
					if (eintrag == belegteID[i]) {
						isNichtVergeben = false;
					}
				}
				if (isNichtVergeben) {
					idNummern[x] = eintrag;
					x++;
					eintrag++;
				} else {
					eintrag++;
				}
			}
		} else {
			idNummern = new Integer[maxNummern];
			for (int x = 1; x <= maxNummern; x++) {

				idNummern[x - 1] = x;
			}
		}
		return idNummern;
	}

	public boolean neueGestirn(String wahlKartenArt) {
		// Auswahl Fenster für dei Planeten Erstelung
		Object[] wahleingaben = { "Karte Erzeugen", "Karte Laden", "Abbruch" };
		int auswahl = JOptionPane.showOptionDialog(null, "Wollen sie eine Karte Laden oder Neue Erstellen",
				"Neue Karte Wählen", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, wahleingaben,
				wahleingaben[0]);

		// Wenn neue Karte Erzeugt werden soll
		if (auswahl == 0) {
			try {
				Integer[] idNummern = this.getMoeglicheIds();

				JTextField KartenName = new JTextField();
				JSpinner spinnerId = new JSpinner(new SpinnerListModel(idNummern));
				JSpinner spinnerX = new JSpinner(new SpinnerNumberModel(10, 5, 120, 1));
				JSpinner spinnerY = new JSpinner(new SpinnerNumberModel(10, 5, 120, 1));
				final JComboBox<String> wahlFeldArt = new JComboBox<String>();

				D_FeldArt feldArten = (D_FeldArt) Xml.toD(backendUniversumseditor.getErlaubteFeldArten(wahlKartenArt));
				if (feldArten != null) {
					for (String feldArt : feldArten.getListe()) {
						wahlFeldArt.addItem(feldArt);
					}
				}

				Object[] eingaben = { "Gestirn Name: ", KartenName, "ID der Karte", spinnerId, "Kartengroesse X", spinnerX,
						"KartengroesseY", spinnerY, "Kartenart", wahlKartenArt, "Standard-Feldart", wahlFeldArt };
				JOptionPane eingabe = new JOptionPane(eingaben, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
				eingabe.createDialog(frontend, "Neue Karte erzeugen...").setVisible(true);
				if ((eingabe.getValue() != null) && (eingabe.getValue().equals(JOptionPane.OK_OPTION))) {
					try {
						int id = D.toInt("" + spinnerId.getValue());
						int x = D.toInt("" + spinnerX.getValue());
						int y = D.toInt("" + spinnerY.getValue());
						String kArt = (String) wahlKartenArt;
						String fArt = (String) wahlFeldArt.getSelectedItem();
						String name = KartenName.getText();
						if(name.length()<2){
							throw new Exception();
						}
						frontend.log("Erstelle neue Karte ID: " + id + ", X: " + x + " Felder, Y: " + y + " Felder, Kartentyp: "
								+ kArt + ", Standardfeld: " + fArt + "...");
						
						// Karten im Backend erzeugen:
						String backendDaten = backendUniversumseditor.neueKarte(name, id, x, y, kArt, fArt,frontend.getUniversum().getId(),kArt);
						fGestirn = new FrontendGestirn(frontend, null, false);
						Karte k = fGestirn.neueKarte(backendDaten, fArt, kArt);
						// Eventhandler der Frontend-Karte definieren:
						eventHandler.setfGestirn(fGestirn);
						k.setEventhandler(eventHandler);
						// rechtes Menu einblenden:
						fGestirn.setMenuRechts(new MenuRechtsKarte(frontend, fGestirn, frontend.getUrl(), false));

					} catch (Exception e) {
						JOptionPane.showMessageDialog(frontend, "Ihre Eingaben sind ungueltig!", "Neue Karte erzeugen...",
								JOptionPane.WARNING_MESSAGE);
					//	e.printStackTrace();
						return false;
					}
				} else {
					return false;
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frontend, "Ihre Eingaben sind ungueltig!", "Neue Karte erzeugen...",
						JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
			}
		} else if (auswahl == 1) { // Wenn gewält wird das eine Karte geladen werden
																// soll
			return ladenKarte(wahlKartenArt);
		} else {
			return false;
		}
		return true;

	}

	// FrontendGestirn ist das Frontend was erzeugt wird wenn ein neuer Planet
	// erzeut wird
	public FrontendGestirn getFrontendGestirn() {
		return fGestirn;
	}

	// Erstellen eines Wurmlochs
	public Raum bearbeiteWurmloch(Raum r) {
		final JLabel bearbeitet = new JLabel();

		if (r.isBearbeitet()) {
			if (r.getBiDirektional() > 0) {
				bearbeitet
						.setText("Ist ein Bi-Direktionals Wurmloch\nZielID: " + r.getBiDirektional() + " EigeneID: " + r.getId());
			} else {
				bearbeitet.setText("Ist ein Direktionals Wurmloch\nKoordinaten: X=" + r.getZielX() + " : Y=" + r.getZielY());
			}
		} else {
			bearbeitet.setText("Noch nicht gesetzt");
		}
		JRadioButton dir = new JRadioButton("Direktional");
		dir.setActionCommand("Dir");

		JRadioButton bidir = new JRadioButton("Bi-Direktional");
		bidir.setActionCommand("BiDir");

		if (r.getBiDirektional() > 0) {
			bidir.setSelected(true);
		} else {
			dir.setSelected(true);
		}

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(dir);
		group.add(bidir);

		JSpinner zielX;
		JSpinner zielY;

		if (r.getZielX() > 0) {
			zielX = new JSpinner(new SpinnerNumberModel(r.getZielX(), 1, frontend.getUniversum().getGroesseX(), 1)); // ausgabe
																																																								// der
																																																								// feld
																																																								// positionX
																																																								// auf
																																																								// der
																																																								// karte
			zielY = new JSpinner(new SpinnerNumberModel(r.getZielY(), 1, frontend.getUniversum().getGroesseX(), 1)); // ausgabe
																																																								// der
																																																								// feld
																																																								// positionY
																																																								// auf
																																																								// der
																																																								// karte
		} else {
			zielX = new JSpinner(
					new SpinnerNumberModel(frontend.getUniversum().getGroesseX(), 1, frontend.getUniversum().getGroesseX(), 1)); // ausgabe
																																																												// der
																																																												// feld
																																																												// positionX
																																																												// auf
																																																												// der
																																																												// karte
			zielY = new JSpinner(
					new SpinnerNumberModel(frontend.getUniversum().getGroesseY(), 1, frontend.getUniversum().getGroesseX(), 1)); // ausgabe
																																																												// der
																																																												// feld
																																																												// positionY
																																																												// auf
																																																												// der
																																																												// karte
		}

		Object[] eingaben = { "Bearbeitungsstatus:", bearbeitet.getText(), "Auswählen", dir, bidir, "", "Ziel Koordinaten:",
				"X-Achse: ", zielX, "Y-Achse: ", zielY };
		JOptionPane eingabe = new JOptionPane(eingaben, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		eingabe.createDialog(frontend, "Neues Wurmloch erzeugen...").setVisible(true);
		if ((eingabe.getValue() != null) && (eingabe.getValue().equals(JOptionPane.OK_OPTION))) {
			try {
				Raum raum = (Raum) frontend.getUniversum().getFelder()[Integer.parseInt(zielX.getValue().toString())][Integer
						.parseInt(zielY.getValue().toString())];
				if (group.isSelected(dir.getModel())) {
					if (!raum.getFeldArt().equals("Wurmloch")) {
						frontend.log(
								"Direktionaler Wurmloch hat die Ziel Koordinaten: X=" + zielX.getValue() + " : Y=" + zielY.getValue());
						return raum;
					} else {
						frontend.log("Kein Wurmloch an der Position");
						return null;
					}
				} else {
					if (raum.getFeldArt().equals("Wurmloch")) {
						return raum;
					} else {
						frontend.log("Auf diesem Pfeld ist ein Wurmloch");
						return null;
					}
				}

			} catch (IndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(frontend, "Bitte gültige zahlen eingeben!", "Neues Wurmloch erzeugen...",
						JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frontend, "Ihre Eingaben sind ungueltig!", "Neues Wurmloch erzeugen...",
						JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
			}
		}
		return null;
	}


	//Speichern des gesamten Universums
	public void speichernUniversum() {
		String daten = "";
		if (frontend.getUniversum() != null) {
			daten = daten + backendUniversumseditor.getKarte();
			if (frontend.getKarte() != null) {
				for (Karte karte : frontend.getKarte()) {
					karteEinladen(karte);
					daten = daten + backendUniversumseditor.getKarte();
				}
			}

			backendUniversumseditor.speichernUniversum(daten);
			frontend.log("Speicher war erfolgreich...");
			UniversumWiederHolen(false);
		} else {
			frontend.log("Keine Karte zum Speicher nicht vorhanden!");
		}
	}
	
	// Laden des gesamten Universums
	public void ladenUniversum() {
		ArrayList<ArrayList <D>> kartenListe = null;
		// laden der Möglichen auswahl von Universen aus der Server Liste
		ArrayList<D> liste = Xml.toArray(backendUniversumseditor.auswahlUniversum());
		final JComboBox<String> weltraumWahl = new JComboBox<String>();

		//liste befühlen für auswahl im Dialog
		if (liste != null) {
			for (D d_Auswahl : liste) {
				weltraumWahl.addItem(d_Auswahl.getString("name"));
			}
		}
		
		Object[] eingaben = { "Weltraum wahl:", weltraumWahl };
		JOptionPane eingabe = new JOptionPane(eingaben, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		eingabe.createDialog(frontend, "Weltraum Laden...").setVisible(true);
		if ((eingabe.getValue() != null) && (eingabe.getValue().equals(JOptionPane.OK_OPTION))) {
			try {
				frontend.log("Lade Kartendatei...");
				fGestirn = new FrontendGestirn(frontend, null, false);
				String name = (String) weltraumWahl.getSelectedItem();
				for (D d_Auswahl : liste) {
					if(d_Auswahl.getString("name").equals(name)){
						//Universum nach id wird geladen
						kartenListe = Xml.toDArray(backendUniversumseditor.ladenUniversum(d_Auswahl.getInt("id")));
						
					}
				}
				
				for(ArrayList<D> daten : kartenListe){
					if(daten.get(0).getString("kartenArt").equals("Weltraum")){
						frontend.ladeUniversum(new Universum(frontend,daten),daten);
					}
				}
				
				for (ArrayList<D> daten : kartenListe) {
					if (!daten.get(0).getString("kartenArt").equals("Weltraum")) {
						
						Karte k = fGestirn.ladeKarte(daten);
						this.karteEinladen(k);
						
						
					}
				}

				frontend.inaktivKarte();
				this.UniversumWiederHolen(false);
				// rechtes Menu einblenden:
					frontend.setMenuRechts(new MenuRechtsUniversum(frontend));
				
					frontend.log("Kartendatei erfolgreich geladen.");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frontend, "Ihre Eingaben sind ungueltig!", "Neue Karte laden...",
						JOptionPane.WARNING_MESSAGE);
			//	e.printStackTrace();
			}
		}
	}

	//lade eine einzele karte auf das Universum
	public boolean ladenKarte(String kArt) {
		frontend.log("Lade Kartendatei...");
		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setCurrentDirectory(new File("../Game-Engineering/ru.spiel"));
		chooser.setFileFilter(new FileNameExtensionFilter("Kartendaten (.map)", "map"));
		if (chooser.showOpenDialog(frontend) == JFileChooser.APPROVE_OPTION) {
			try {
				String kartenPfad = chooser.getSelectedFile().toString();
				ArrayList<D> antwort = null;
				if(kartenPfad.contains(kArt)){
					antwort = Xml.toArray(backendUniversumseditor.ladenKarte(chooser.getSelectedFile().toString()));
				
				if (antwort.get(0) instanceof D_Fehler)
					frontend.log("FEHLER: " + antwort.get(0).getString("meldung"));
				else {
					fGestirn = new FrontendGestirn(frontend, null, false);
					Karte k = fGestirn.ladeKarte(antwort, kArt,getMoeglicheIds()[0]);
					// Eventhandler der Frontend-Karte definieren:
					eventHandler.setfGestirn(fGestirn);
					k.setEventhandler(eventHandler);
					// rechtes Menu einblenden:
					fGestirn.setMenuRechts(new MenuRechtsKarte(frontend, fGestirn, frontend.getUrl(), false));

					fGestirn.log("Kartendatei erfolgreich geladen.");

					return true;
				}
				}else{
					frontend.log("Diese Xml Datei ist keine Karte der Art: "+ kArt);
					return false;
				}
			} catch (RuntimeException e) {
				e.printStackTrace();
				frontend.log(e.getMessage());
				return false;
			}
		} 
			frontend.log("ABGEBROCHEN");
			return false;
		
	}

	// Universum wieder zurückgeladen mit beachtung ob ein neuer Planet oder Mond gesetzt wurde oder nicht
	public void UniversumWiederHolen(boolean isSave) {
		Universum u = frontend.getUniversum();
		frontend.getBackendKarteneditor().neueKarte(u.getName(), u.getId(), u.getGroesseX(), u.getGroesseY(), "Weltraum",
				"Leere",0,"Weltraum");
		Karte karte = null;
		if(frontend.getKarte()!=null){
			for (Karte k : frontend.getKarte()) {
				if (k.getAktive()) {
					karte = k;
				}
			}
		}
		Raum raum = null;
		if (!isSave) {
			for (int x = 1; x < u.getFelder().length; x++) {
				for (int y = 1; y < u.getFelder()[x].length; y++) {
					raum = (Raum) u.getFelder()[x][y];
					if (raum.getFeldArt().contains("Planet") || raum.getFeldArt().contains("Mond")) {
						if (raum.getAktive()) {
							raum.setAktive(false);
							raum.setFeldArt("Leere");
							frontend.getBackendKarteneditor().setFeldArt(0, x, y, u.getFelder()[x][y].getFeldArt());
						} else {
							Karte k = frontend.getKarteByID(raum.getId());
							if (k != null) {
								frontend.getBackendKarteneditor().setFeldArt(k.getId(), x, y, k.ermittleGlobusArt());
								raum.zeichnen();
							}
						}
					} else {
						if (raum.getFeldArt().equals("Wurmloch")) {
							frontend.getBackendKarteneditor().setFeldArt(frontend.getUniversum().getId(), raum.getPos()[0],
									raum.getPos()[1], raum.getFeldArt(), raum.getZielX(), raum.getZielY(), raum.getBiDirektional());
						}
						frontend.getBackendKarteneditor().setFeldArt(x, y, u.getFelder()[x][y].getFeldArt());
					}
				}
			}
		} else {
			for (int x = 1; x < u.getFelder().length; x++) {
				for (int y = 1; y < u.getFelder()[x].length; y++) {
					raum = (Raum) u.getFelder()[x][y];
					if (raum.getFeldArt().contains("Planet") || raum.getFeldArt().contains("Mond")) {
						if (raum.getAktive()) {
							raum.setAktive(false);
							raum.setId(karte.getId());
							frontend.getBackendKarteneditor().setFeldArt(karte.getId(), x, y, karte.ermittleGlobusArt());
							raum.zeichnen();
						} else {
							Karte k = frontend.getKarteByID(raum.getId());
							frontend.getBackendKarteneditor().setFeldArt(k.getId(), x, y, k.ermittleGlobusArt());
							raum.zeichnen();
						}
					} else {
						if (raum.getFeldArt().equals("Wurmloch")) {
							frontend.getBackendKarteneditor().setFeldArt(frontend.getUniversum().getId(), raum.getPos()[0],
									raum.getPos()[1], raum.getFeldArt(), raum.getZielX(), raum.getZielY(), raum.getBiDirektional());
						}
						frontend.getBackendKarteneditor().setFeldArt(x, y, raum.getFeldArt());
					}
				}
			}
			frontend.inaktivKarte();
		}

	}

	//läde die karte wieder ein wenn eine karte bereits exsestiert
	public void KarteWiederHolen(Raum r, String fArt) {
		Karte karte = null;
		Raum raum = r;
		if (frontend.getKarte() != null) {
			for (Karte k : frontend.getKarte()) {
				if (raum.getId() == k.getId()) {
					karte = k;
				}
			}
		}
		if (karte != null) {
			fGestirn = new FrontendGestirn(frontend, null, true);

			karteEinladen(karte);
			fGestirn.setKarte(karte);
			
			karte.setAktive(true);

		// Eventhandler der Frontend-Karte definieren:
			eventHandler.setfGestirn(fGestirn);
			karte.setEventhandler(eventHandler);
			// rechtes Menu einblenden:
			fGestirn.setMenuRechts(new MenuRechtsKarte(frontend, fGestirn, frontend.getUrl(), false));
		} else {
			frontend.setEnabled(true);
		}
	}

	public void karteEinladen(Karte karte) {

		if (karte != null) {
			String backendDaten = frontend.getBackendKarteneditor().neueKarte(karte.getName(), karte.getId(),
					karte.getGroesseX(), karte.getGroesseY(), karte.getKartenArt(), karte.getFelder()[1][1].getFeldArt(),frontend.getUniversum().getId(),karte.getGlobusArt());
			ArrayList<D> daten = Xml.toArray(backendDaten);
			int i;
			for (i = 0; i < daten.size(); i++) {
				if (daten.get(i) instanceof D_Karte)
					break;
			}
			for (int x = 1; x < karte.getGroesseX(); x++) {
				for (int y = 1; y < karte.getGroesseY(); y++) {
					frontend.getBackendKarteneditor().setFeldArt(x, y, karte.getFelder()[x][y].getFeldArt());
					if (karte.getFelder()[x][y].getRessource().length()>2) {
						frontend.getBackendKarteneditor().setRessource(x, y, karte.getFelder()[x][y].getRessource());
					}
					if (karte.getFelder()[x][y].getSpielerNummer() != 0) {
						frontend.getBackendKarteneditor().setSpielerstart(x, y, karte.getFelder()[x][y].getSpielerNummer());
					}
					
				}
			}
		}
	}
}
