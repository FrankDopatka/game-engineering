package schach.backend.server;

import java.util.ArrayList;
import java.util.HashSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.glassfish.jersey.server.ResourceConfig;

import schach.backend.Figur;
import schach.backend.Spiel;
import schach.backend.Zug;
import schach.daten.D;
import schach.daten.D_ZugHistorie;
import schach.daten.Xml;
import schach.interfaces.iBackendSpiel;

@Path("schach/spiel")
public class BackendSpiel extends ResourceConfig implements iBackendSpiel{
	private static Spiel spiel;

	public BackendSpiel(){
	}
	
	public static Spiel getSpiel(){
		return BackendSpiel.spiel;
	}
	
	public static void setSpiel(Spiel spiel){
		BackendSpiel.spiel=spiel;
	}

	@GET
	@Path("getSpielDaten/")
	@Produces("application/xml")
	@Override
	public String getSpielDaten() {
		try{
			String xml=spiel.getDaten().toXml();
			return Xml.verpacken(xml);
		} catch (Exception e) {
			return Xml.verpackeFehler(e);
		}
	}
	
	@GET
	@Path("getAktuelleBelegung/")
	@Produces("application/xml")
	@Override
	public String getAktuelleBelegung() {
		try{
			String xml=spiel.getAktuelleBelegung().toXml();
			return Xml.verpacken(xml);
		} catch (Exception e) {
			return Xml.verpackeFehler(e);
		}
	}

	@GET
	@Path("getBelegung/{nummer}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getBelegung(
			@PathParam("nummer")int nummer) {
		try{
			String xml=spiel.getBelegung(nummer).toXml();
			return Xml.verpacken(xml);
		} catch (Exception e) {
			return Xml.verpackeFehler(e);
		}
	}
	
	@GET
	@Path("getAlleErlaubtenZuege/")
	@Produces("application/xml")
	@Override
	public String getAlleErlaubtenZuege() {
		try{
			HashSet<Zug> zuege=spiel.getAlleErlaubteZuege();
			ArrayList<D> zuegeDaten=new ArrayList<D>();
			if (zuege!=null){
				for(Zug zug:zuege){
					zuegeDaten.add(zug.getDaten());
				}
			}

			return Xml.verpacken(Xml.fromArray(zuegeDaten));
		} catch (Exception e) {
			return Xml.verpackeFehler(e);
		}
	}

	@GET
	@Path("getFigur/{position}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getFigur(
			@PathParam("position")String position) {
		try{
			Figur figur=spiel.getAktuelleBelegung().getFigur(position);
			if (figur==null) throw new RuntimeException("Keine Figur auf dem Feld "+position+" vorhanden!");
			return Xml.verpacken(figur.toXml());
		} catch (Exception e) {
			return Xml.verpackeFehler(e);
		}
	}

	@GET
	@Path("getErlaubteZuege/{position}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getErlaubteZuege(
			@PathParam("position")String position) {
		try{
			HashSet<Zug> zuege=spiel.getAktuelleBelegung().getErlaubteZuege(position);
			ArrayList<D> zuegeListe=new ArrayList<D>();
			if (zuege!=null){
				for(Zug z:zuege){
					zuegeListe.add(z.getDaten());
				}
			}
			return Xml.verpacken(Xml.fromArray(zuegeListe));
		} catch (Exception e) {
			return Xml.verpackeFehler(e);
		}
	}

	@GET
	@Path("ziehe/{von}/{nach}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String ziehe(
			@PathParam("von")String von,
			@PathParam("nach")String nach) {
			try{
				spiel.ziehe(von,nach);
				return Xml.verpackeOK("Zug erfolgreich durchgefuehrt.");
			} catch (Exception e) {
				return Xml.verpackeFehler(e);
			}
	}

	@GET
	@Path("getZugHistorie/")
	@Produces("application/xml")
	@Override
	public String getZugHistorie() {
		try{
			ArrayList<String> zugListe=spiel.getZugHistorie();
			ArrayList<D> zugHistorie=new ArrayList<D>();
			if(zugListe!=null){
				for(String zug:zugListe){
					D_ZugHistorie d=new D_ZugHistorie();
					d.setString("zug",zug);
					zugHistorie.add(d);
				}				
			}
			return Xml.verpacken(Xml.fromArray(zugHistorie));
		} catch (Exception e) {
			return Xml.verpackeFehler(e);
		}
	}
}
