package server;
import ru.backend.server.BackendSpielAdmin;
import ru.daten.D_OK;
import ru.daten.Xml;

public class Initialisierung {
	private static String ladeSpiel="/home/dopatka02/Repo-GE/Game-Engineering/ru.spiel/spiel01.wom";
	
	public static void ausfuehren(){
		BackendSpielAdmin administration=new BackendSpielAdmin();
		try{
			System.out.println("  Lade das Spiel auf dem Server von "+ladeSpiel+"...");
			String antwort=administration.ladenSpiel(ladeSpiel);
			if (Xml.toD(antwort) instanceof D_OK) System.out.println("  OK");
		}
		catch (Exception e){
			System.out.println("  SpielAdmin ausfuehren:"+e.getMessage());
		}
	}
}
