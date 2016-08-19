package frontend.EventHandler;

import java.awt.event.MouseEvent;
import frontend.Frontend;
import frontend.karte.flaechen.Platz;
import frontend.karte.flaechen.Raum;
import frontend.menu.rechts.MenuRechts;
import frontend.menu.rechts.MenuRechts.AktionMenuRechts;

public class UniversumsEventHandler extends EventHandler {

	public UniversumsEventHandler(Frontend frontend) {
		super(frontend);

	}

	@Override
	protected void aktion(Platz feld) {
		AktionMenuRechts a = frontend.getMenuRechts().getAktion();
		switch (a) {
		case GestirnSetzen:
			feldSetzen(feld);
			break;
		case GestirnBearbeiten:
			bearbeiteGestirn(feld);
			break;
		case WurmlochBearbeiten:
			Wurmlochbearbeiten(feld);
			break;
		case WurmlochKoordinaten:
			break;
		default:
			break;
		}
	}

	//Setzen von felder algemein im Universum
	@Override
	protected void feldSetzen(Platz feld) {
		MenuRechts menu = frontend.getMenuRechts();
		String fArtNeu = menu.getFeldart();
		boolean isfeld = true;
		Raum raum = (Raum) feld;

		// Gibt ReferenzID wenn Feldart Planet oder Mond..
		if (fArtNeu.contains("Planet") || fArtNeu.contains("Mond")) {
			delFeld(raum,fArtNeu);
			raum.setAktive(true);
			isfeld = frontend.getFunktionenKarteneditor().neueGestirn(fArtNeu);
			
			if (isfeld) {
				raum.setFeldArt(fArtNeu);
				frontend.setEnabled(false);
			}
		} else if ("Wurmloch".equals(fArtNeu)) {
			delFeld(raum,fArtNeu);
			backendKarteneditor.setFeldArt(frontend.getUniversum().getId(), raum.getPos()[0], raum.getPos()[1], fArtNeu, 0, 0,
					0);
			raum.setFeldArt(fArtNeu);
			raum.zeichnen();
		} else {
			delFeld(raum,fArtNeu);
			backendKarteneditor.setFeldArt(raum.getPos()[0], raum.getPos()[1], fArtNeu);
			raum.setFeldArt(fArtNeu);
			raum.zeichnen();
		}
	}
	
	//Loescht die alte Felder zugehoerichkeit beim änderen einse Feldes
	private void delFeld(Raum r,String fArtNeu){
		if(r.getFeldArt().equals("Wurmloch")){
			if(r.getId()>0){
			frontend.getUniversum().removeWurlochID(r.getId()-1);
			r = new Raum(frontend,r.getPosX(),r.getPosY(),fArtNeu);
			}
		}else if(!r.getFeldArt().equals("Wurmloch") && !r.getFeldArt().equals("Leere") ){
			frontend.delKarte(frontend.getKarteByID(r.getId()));
			r = new Raum(frontend,r.getPosX(),r.getPosY(),fArtNeu);
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent ev){
		
	}

	//Gibt fuer Bi-Direktionale Wurmloecher die Ziel Koordinaten aus
	private int[] getRendomZiel(int x, int y) {
		int maxX = frontend.getUniversum().getGroesseX(), maxY = frontend.getUniversum().getGroesseY();
		int[] ziel = new int[2];
		if (x + 1 < maxX && y < maxY) {
			ziel[0] = x + 1;
			ziel[1] = y;
		} else if (x - 1 < maxX && y < maxY) {
			ziel[0] = x - 1;
			ziel[1] = y;
		} else if (x < maxX && y < maxY + 1) {
			ziel[0] = x;
			ziel[1] = y + 1;
		} else if (x < maxX && y < maxY - 1) {
			ziel[0] = x;
			ziel[1] = y - 1;
		}

		return ziel;
	}

	//Bearbeitungs boduf fuer ein Planeten oder Mond
	private void bearbeiteGestirn(Platz feld) {
		Raum raum = (Raum) feld;
		String fArtNeu = raum.getFeldArt();
		if (fArtNeu.contains("Planet") || fArtNeu.contains("Mond")) {
			frontend.getFunktionenKarteneditor().KarteWiederHolen(raum, fArtNeu);
			frontend.setEnabled(false);
			raum.setAktive(true);
		}

	}

	//Aufwahl fuer die Bearbeitung der Wurmloecher
	private void Wurmlochbearbeiten(Platz feld) {
		Raum raum = (Raum) feld;
		int wurmlochID = 1;
		for (@SuppressWarnings("unused") int id : frontend.getUniversum().getWurlochID()) {
			wurmlochID++;
		}
		if (raum.getFeldArt().equals("Wurmloch")) {
			Raum zielRaum = frontend.getFunktionenKarteneditor().bearbeiteWurmloch(raum);
			if (zielRaum != null && "Wurmloch".equals(zielRaum.getFeldArt()) && !raum.equals(zielRaum)) {
				// Neues Wurmloch wir erstellt und mit anderem Wurmloch gekopplet
				if (raum.getId() <= 0) {
					raum.setId(wurmlochID);
					frontend.getUniversum().addWurlochID(wurmlochID);
				}
				if (zielRaum.getId() <= 0) {
					wurmlochID++;
					zielRaum.setId(wurmlochID);
					frontend.getUniversum().addWurlochID(wurmlochID);
				}

				raum.setBiDirektional(zielRaum.getId());
				raum.setFeldArt(raum.getFeldArt());
				int ziel[] = getRendomZiel(zielRaum.getPosX(), zielRaum.getPosY());
				raum.setZielX(ziel[0]);
				raum.setZielY(ziel[1]);
				backendKarteneditor.setFeldArt(frontend.getUniversum().getId(), raum.getPos()[0], raum.getPos()[1], raum.getFeldArt(),
						ziel[0], ziel[1], zielRaum.getId());
				frontend.log("Bi-Direktionaler Wurmloch hat die Ziel ID: " + zielRaum.getId());
				raum.setBearbeitet(true);
				raum.zeichnen();

				// Bearbeitung ders ziel Wurmlochs
				zielRaum.setBiDirektional(raum.getId());
				zielRaum.setFeldArt(zielRaum.getFeldArt());
				ziel = getRendomZiel(raum.getPosX(), raum.getPosY());
				zielRaum.setZielX(ziel[0]);
				zielRaum.setZielY(ziel[1]);
				zielRaum.setBearbeitet(true);
				backendKarteneditor.setFeldArt(frontend.getUniversum().getId(), raum.getPos()[0], raum.getPos()[1], zielRaum.getFeldArt(),
						ziel[0], ziel[1], zielRaum.getId());

			}
			if (zielRaum != null) {
				if (raum.getId() <= 0) {
					raum.setId(wurmlochID);
					frontend.getUniversum().addWurlochID(wurmlochID);
				}
				raum.setFeldArt(raum.getFeldArt());
				raum.setZielX(zielRaum.getPosX());
				raum.setZielY(zielRaum.getPosY());
				raum.setBearbeitet(true);
				backendKarteneditor.setFeldArt(frontend.getUniversum().getId(), raum.getPos()[0], raum.getPos()[1], raum.getFeldArt(),
						zielRaum.getPosX(), zielRaum.getPosY(), zielRaum.getId());
				raum.zeichnen();
			} else {
				frontend.log("Bearbeitung fehlgeschlagen eingaben nicht Korekt");
			}
		}
	}
}
