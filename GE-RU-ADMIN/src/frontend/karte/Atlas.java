package frontend.karte;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Scrollable;

import frontend.EventHandler.EventHandler;
import frontend.karte.flaechen.Platz;
import ru.daten.D;
import ru.daten.D_FeldArt;
import ru.daten.D_RessourcenArt;
import ru.daten.Xml;
import ru.interfaces.iBackendKarteneditor;

public abstract class Atlas extends JPanel implements Scrollable {
	protected static final long serialVersionUID = 1L;
	protected iBackendKarteneditor backendKarteneditor;
	protected int groesseX;
	protected int groesseY;
	protected HashMap<String, BufferedImage> bildFeld = new HashMap<String, BufferedImage>();
	protected HashMap<String, BufferedImage> bildRessource = new HashMap<String, BufferedImage>();
	protected int id;
	protected Platz[][] felder;

	public Atlas(iBackendKarteneditor backendKarteneditor, int id, int groesseX, int groesseY) {
		this.groesseX = groesseX;
		this.groesseY = groesseY;
		this.backendKarteneditor = backendKarteneditor;
		this.id = id;
		String kartenArt = Xml.toD(backendKarteneditor.getKartenDaten()).getString("kartenArt");
		D_FeldArt feldArten = (D_FeldArt) Xml.toD(backendKarteneditor.getErlaubteFeldArten(kartenArt));
		String pfadBild = "daten//felder";
		String pfadRessource = "daten//ressourcen";
		String pfadBildPlanet = "daten//planeten";
		for (String feldArt : feldArten.getListe()) {
			try {
				D_RessourcenArt feldRessourcen = null;
				if (feldArt.contains("Planet") || feldArt.contains("Mond")) {
					File f = new File(pfadBildPlanet);
					File[] files = f.listFiles();
					if (files != null) { // Erforderliche Berechtigungen etc. sind
																// vorhanden
						for (int i = 0; i < files.length; i++) {
							if (!files[i].isDirectory()) {
								bildFeld.put(feldArt, ImageIO.read(new File(pfadBildPlanet, feldArt.toLowerCase() + ".jpg")));
							}
						}
					}
				} else {
					bildFeld.put(feldArt, ImageIO.read(new File(pfadBild, feldArt.toLowerCase() + ".jpg")));
					if (!kartenArt.equals("Weltraum")) {
						feldRessourcen = (D_RessourcenArt) Xml.toD(backendKarteneditor.getErlaubteRessourcenArten(feldArt));
					}
				}
				if (feldRessourcen != null) {
					for (String resourcenArt : feldRessourcen.getListe()) {
						if (!bildRessource.containsKey(resourcenArt)) {
							bildRessource.put(resourcenArt,
									ImageIO.read(new File(pfadRessource, resourcenArt.toLowerCase() + ".png")));
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// Für das neue Setzten der Karte auf dem Server
	public int getGroesseX() {
		return groesseX;
	}

	// Für das neue Setzten der Karte auf dem Server
	public int getGroesseY() {
		return groesseY;
	}

	// Für das neue Setzten der Karte auf dem Server
	public Platz[][] getFelder() {
		return felder;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BufferedImage getBildFeld(String feldArt) {
		return bildFeld.get(feldArt);
	}

	public BufferedImage getBildRessource(String ressorcenArt) {
		return bildRessource.get(ressorcenArt);
	}

	public abstract void setEventhandler(EventHandler eventHandler);

	public abstract void zeichneFelder(ArrayList<D> daten);

	public abstract void zeichneFeld(int[] pos);

	public abstract void terminate();

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}
}
