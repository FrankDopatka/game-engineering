package schach.interfaces;

public interface iBackendSpiel {
	String getSpielDaten();
	String getAktuelleBelegung();
	String getBelegung(int nummer);
	String getAlleErlaubtenZuege();
	String getFigur(String position);
	String getErlaubteZuege(String position);
	String ziehe(String von,String nach);
	String bauernUmwandlung(String zuFigur);
	String getZugHistorie();
}