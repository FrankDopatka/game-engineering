package ru.interfaces;

public interface iBackendKarteneditor {
	String neueKarte(String name,int id,int x,int y,String kartenArt,String feldArt,int refKartenID,String globusArt);
	String ladenKarte(String pfad);
	String speichernKarte(String pfad);
	String speichernUniversum(String daten); // Speichert ein ganzes Universum ab
	String ladenUniversum(int id); // Für dsa laden einers gesamten Universums mit rückgabe des gesamten universums
	String auswahlUniversum(); // Rückgabe der Möglichen Universen die geladen werden können 
	String getKarte();
	
	String getKartenArten();
	String getErlaubteFeldArten(String kartenArt);
	String getErlaubteRessourcenArten(String feldArt);
	String getKartenDaten();
	String getFeldDaten(int x,int y);
	
	String setFeldArt(int refKartenID, int x, int y, String feldArtNeu); // Karten ID Referenc dazugetragen
	String setFeldArt(int refKartenID, int x, int y, String feldArtNeu,int zielX,int zielY,int wurmlochIdBiDirektional); // Für Wurmlöcher
	String setFeldArt(int x, int y, String feldArtNeu);
	String setSpielerstart(int x,int y,int spielerstart);
	String getSpielerstart(int spielerstart);
	String setRessource(int x,int y,String ressorce);
	String delRessource(int x,int y);

	
	
}
