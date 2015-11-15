package server;

public class Initialisierung {
	private static String ladeSpielRU="/home/dopatka02/Repo-GE/Game-Engineering/ru.spiel/spiel01.wom";
	private static String ladeSpielSchach="/home/dopatka02/Repo-GE/Game-Engineering/schach.spiel/spiel01.wom";
	
	public static void ausfuehren(){
		starteRisingUniverse();
		starteSchach();
	}

	public static void starteRisingUniverse(){
		try{
			ru.backend.server.BackendSpielAdmin administration=new ru.backend.server.BackendSpielAdmin();
			System.out.println("  Lade Rising Universe auf dem Server von "+ladeSpielRU+"...");
			String antwort=administration.ladenSpiel(ladeSpielRU);
			if (ru.daten.Xml.toD(antwort) instanceof ru.daten.D_OK) System.out.println("  OK");
		}
		catch (Exception e){
			System.out.println("  FEHLER Rising Universe:"+e.getMessage());
		}
	}
	
	
	private static void starteSchach() {
		try{
			schach.backend.server.BackendSpielAdmin administration=new schach.backend.server.BackendSpielAdmin();
			System.out.println("  Lade Schach auf dem Server von "+ladeSpielSchach+"...");
			String antwort=administration.ladenSpiel(ladeSpielSchach);
			if (schach.daten.Xml.toD(antwort) instanceof schach.daten.D_OK) System.out.println("  OK");
		}
		catch (Exception e){
			System.out.println("  FEHLER Schach:"+e.getMessage());
		}
	}
}
