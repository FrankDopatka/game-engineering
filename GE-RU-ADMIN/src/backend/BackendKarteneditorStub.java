package backend;

import java.net.URLEncoder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import ru.interfaces.iBackendKarteneditor;


public class BackendKarteneditorStub implements iBackendKarteneditor{
	private String url;
	private Client client=ClientBuilder.newClient();
	 
	public BackendKarteneditorStub(String url){
		if (url.endsWith("/"))
			this.url=url+"ru/karteneditor/";
		else
			this.url=url+"/ru/karteneditor/";
	}
	
	private String getXmlvonRest(String pfad){
		String s=client.target(url+pfad).request().accept("application/xml").get(String.class);
		return s;
	}
	
	private String postXmlvonRest(String methode,String post){
		Form form = new Form();
		form.param("daten", post);
		
		String s= client.target(url+methode).request().accept("application/x-www-form-urlencoded")
    .post(Entity.entity(form,"application/x-www-form-urlencoded"),
        String.class);
		return s;
	}

	@Override
	public String neueKarte(String name,int id,int x,int y,String kartenArt,String feldArt, int refKartenID,String globusArt){
		return getXmlvonRest("neueKarte"+"/"+name+"/"+id+"/"+x+"/"+y+"/"+kartenArt+"/"+feldArt+"/"+refKartenID+"/"+globusArt);
	}
	
	@Override
	public String speichernKarte(String pfad){
		try {
			URLEncoder.encode(""+pfad,"ISO-8859-1");
			return getXmlvonRest("speichernKarte"+"/"+URLEncoder.encode(""+pfad,"ISO-8859-1"));
		} catch (Exception e) {
			return "";
		}
	}
	
	@Override
	public String ladenKarte(String pfad){
		try {
			return getXmlvonRest("ladenKarte"+"/"+URLEncoder.encode(""+pfad,"ISO-8859-1"));
		} catch (Exception e) {
			return "";
		}
	}
	
	@Override
	public String getKarte() {
		return getXmlvonRest("getKarte");
	}
	
	@Override
	public String getKartenArten() {
		return getXmlvonRest("getKartenArten");
	}

	@Override
	public String getErlaubteFeldArten(String kartenArt) {
		return getXmlvonRest("getErlaubteFeldArten"+"/"+kartenArt);
	}

	@Override
	public String getErlaubteRessourcenArten(String feldArt) {
		return getXmlvonRest("getErlaubteRessourcenArten"+"/"+feldArt);
	}
	
	@Override
	public String getFeldDaten(int x,int y) {
		return getXmlvonRest("getFeldDaten"+"/"+x+"/"+y);
	}

	@Override
	public String getKartenDaten() {
		return getXmlvonRest("getKartenDaten");
	}

	//refKartenID eingefügt zum Verweisen auf eine andere Karte
	@Override
	public String setFeldArt(int refKartenID, int x,int y,String feldArtNeu) {
		return getXmlvonRest("setFeldArt"+"/"+refKartenID+"/"+x+"/"+y+"/"+feldArtNeu);
	}
	
	//refKartenID für die WurmlochID und wurmlochIdBiDirektional für verweiß auf ein anderes wurmloch
	@Override
	public String setFeldArt(int refKartenID, int x,int y,String feldArtNeu,int zielX,int zielY, int wurmlochIdBiDirektional) {
		return getXmlvonRest("setFeldArt"+"/"+refKartenID+"/"+x+"/"+y+"/"+feldArtNeu+"/"+zielX+"/"+zielY+"/"+wurmlochIdBiDirektional);
	}
	
	@Override
	public String setFeldArt(int x, int y, String feldArtNeu) {
		return getXmlvonRest("setFeldArt"+"/"+x+"/"+y+"/"+feldArtNeu);
	}

	@Override
	public String setSpielerstart(int x,int y,int spielerstart) {
		return getXmlvonRest("setSpielerstart"+"/"+x+"/"+y+"/"+spielerstart);
	}

	@Override
	public String getSpielerstart(int spielerstart) {
		return getXmlvonRest("getSpielerstart"+"/"+spielerstart);
	}

	@Override
	public String setRessource(int x,int y,String ressorce) {
		return getXmlvonRest("setRessource"+"/"+x+"/"+y+"/"+ressorce);
	}

	@Override
	public String delRessource(int x,int y) {
		return getXmlvonRest("delRessource"+"/"+x+"/"+y);
	}

	//Speichert das gesamte Universum durch eine Post übergabe aller Karten im Universum in XML format
	@Override
	public String speichernUniversum(String daten){
		try {
			return postXmlvonRest("speichernUniversum",daten);
	} catch (Exception e) {
		return "";
	}
	}
	
	@Override
	public String ladenUniversum(int id){
		try {
			return getXmlvonRest("ladenUniversum"+"/"+id);
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public String auswahlUniversum() {
		return getXmlvonRest("auswahlUniversum");
	}

}
