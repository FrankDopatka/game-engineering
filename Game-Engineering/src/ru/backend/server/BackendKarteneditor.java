package ru.backend.server;

import ru.backend.Parameter;
import ru.backend.karte.Feld;
import ru.backend.karte.Karte;
import ru.daten.*;
import ru.interfaces.iBackendKarteneditor;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("ru/karteneditor")
public class BackendKarteneditor implements iBackendKarteneditor {
	private static Karte karte;

	public BackendKarteneditor() {
	}

	@GET
	@Path("neueKarte/{name}/{id}/{x}/{y}/{kartenArt}/{feldArt}/{refKartenID}/{globusArt}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String neueKarte(@PathParam("name") String name, @PathParam("id") int id, @PathParam("x") int x,
			@PathParam("y") int y, @PathParam("kartenArt") String kartenArt, @PathParam("feldArt") String feldArt,
			@PathParam("refKartenID") int refKartenID, @PathParam("globusArt") String globusArt) {
		try {
			@SuppressWarnings("unchecked")
			Class<Karte> c = (Class<Karte>) Class.forName(Parameter.pfadKlassenKarten + kartenArt);
			Karte karte = (Karte) c.newInstance();
			karte.setId(id);
			karte.setArt(kartenArt);
			if(globusArt.length()>2){
				karte.setGlobus(globusArt);
			}
			karte.setGroesse(new int[] { x, y });
			karte.setAlleFelder(feldArt);
			karte.setName(name);
			karte.setIdZielkarte(refKartenID);
			BackendKarteneditor.karte = karte;
			return getKarte();
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("speichernKarte/{pfad}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String speichernKarte(@PathParam("pfad") String pfad) {
		PrintWriter pw = null;
		try {
			if (!pfad.endsWith(".map"))
				pfad = pfad + ".map";
			if (karte == null)
				throw new RuntimeException("BackendKarteneditor speichernKarte: Es existiert noch keine Karte!");
			pw = new PrintWriter(new FileWriter(pfad));
			pw.println(Xml.verpacken(karte.toXml()));
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		} finally {
			try {
				pw.close();
			} catch (Exception e) {
			}
		}
	}

	@GET
	@Path("ladenKarte/{pfad}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String ladenKarte(@PathParam("pfad") String pfad) {
		try {
			String karteXML = Karte.karteLaden(pfad);
			karte = Karte.karteVonXml(karteXML);
			return karteXML;
		} catch (Exception e) {
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}
	

	@GET
	@Path("getKarte")
	@Produces("application/xml")
	@Override
	public String getKarte() {
		try {
			return Xml.verpacken(karte.toXml());
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("getKartenArten")
	@Produces("application/xml")
	@Override
	public String getKartenArten() {
		try {
			return Xml.verpacken(Xml.fromD(new D_KartenArt()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("getErlaubteFeldArten/{kartenArt}")
	@Produces("application/xml")
	@Override
	public String getErlaubteFeldArten(@PathParam("kartenArt") String kartenArt) {
		try {
			@SuppressWarnings("unchecked")
			Class<Karte> c = (Class<Karte>) Class.forName(Parameter.pfadKlassenKarten + kartenArt);
			Karte karte = (Karte) c.newInstance();
			return Xml.verpacken(Xml.fromD(karte.getErlaubteFeldArten()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("getErlaubteRessourcenArten/{feldArt}")
	@Produces("application/xml")
	@Override
	public String getErlaubteRessourcenArten(@PathParam("feldArt") String feldArt) {
		try {

			@SuppressWarnings("unchecked")
			Class<Feld> c;
			if (karte.getArt().equals("Weltraum")) {
				c = (Class<Feld>) Class.forName(Parameter.pfadKlassenFelderUniversum + feldArt);
			} else {
				c = (Class<Feld>) Class.forName(Parameter.pfadKlassenFelderPlaneten + feldArt);
			}
			Feld feld = (Feld) c.newInstance();
			return Xml.verpacken(Xml.fromD(feld.getErlaubteRessourcenArten()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("getKartenDaten")
	@Produces("application/xml")
	@Override
	public String getKartenDaten() {
		try {
			if (karte == null)
				throw new RuntimeException("BackendKarteneditor getKartenDaten: Es existiert noch keine Karte!");
			return Xml.verpacken(Xml.fromD(karte.getDaten()));
		} catch (Exception e) {
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("getFeldDaten/{x}/{y}")
	@Produces("application/xml")
	@Override
	public String getFeldDaten(@PathParam("x") int x, @PathParam("y") int y) {
		try {
			return Xml.verpacken(Xml.fromD(karte.getFeld(new int[] { x, y }).getDaten()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	// Nur für felder die ein Planet sind
	@GET
	@Path("setFeldArt/{id}/{x}/{y}/{feldArtNeu}") // ID eintrag für die ID
																								// verwaltung
	@Produces("application/xml")
	@Override
	public String setFeldArt(@PathParam("id") int refKartenID, @PathParam("x") int x, @PathParam("y") int y,
			@PathParam("feldArtNeu") String feldArtNeu) {
		try {
			String ressource = karte.getFeld(new int[] { x, y }).getDaten().getString("ressource");
			if (!Xml.toD(getErlaubteRessourcenArten(feldArtNeu)).existValue(ressource))
				ressource = "";
			int spielerstart = karte.getFeld(new int[] { x, y }).getDaten().getInt("spielerstart");
			if (spielerstart > 0) {
				if (feldArtNeu.equals("Kueste") || feldArtNeu.equals("Meer"))
					spielerstart = 0;
			}
			karte.setFeld(karte.getId(), new int[] { x, y }, feldArtNeu, ressource, spielerstart,refKartenID);
			// ID eintrag für die ID verwaltung
			
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("setFeldArt/{id}/{x}/{y}/{feldArtNeu}/{zielX}/{zielY}/{wurmlochIdBiDirektional}") // ID eintrag für die ID verwaltung
	@Produces("application/xml")
	@Override
	public String setFeldArt(@PathParam("id") int refKartenID, @PathParam("x") int x, @PathParam("y") int y,
			@PathParam("feldArtNeu") String feldArtNeu, @PathParam("zielX") int zielX, @PathParam("zielY") int zielY,
			@PathParam("wurmlochIdBiDirektional") int idBiDirektional) {
		try {

			karte.setFeldWurmloch(karte.getId(), x, y, feldArtNeu, zielX, zielY, idBiDirektional);
			karte.getFeld(new int[] { x, y }).setRefIDKarte(refKartenID); // ID eintrag für die ID verwaltung

			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("setFeldArt/{x}/{y}/{feldArtNeu}") // ID eintrag für die ID verwaltung
	@Produces("application/xml")
	@Override
	public String setFeldArt(@PathParam("x") int x, @PathParam("y") int y, @PathParam("feldArtNeu") String feldArtNeu) {
		try {
			String ressource = karte.getFeld(new int[] { x, y }).getDaten().getString("ressource");
			if (!Xml.toD(getErlaubteRessourcenArten(feldArtNeu)).existValue(ressource))
				ressource = "";
			int spielerstart = karte.getFeld(new int[] { x, y }).getDaten().getInt("spielerstart");
			if (spielerstart > 0) {
				if (feldArtNeu.equals("Kueste") || feldArtNeu.equals("Meer"))
					spielerstart = 0;
			}
			karte.setFeld(karte.getId(), new int[] { x, y }, feldArtNeu, ressource, spielerstart);

			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("setSpielerstart/{x}/{y}/{spielerstart}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String setSpielerstart(@PathParam("x") int x, @PathParam("y") int y,
			@PathParam("spielerstart") int spielerstart) {
		try {
			karte.setSpielerstart(new int[] { x, y }, spielerstart);
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("getSpielerstart/{spielerstart}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String getSpielerstart(@PathParam("spielerstart") int spielerstart) {
		try {
			int[] pos = karte.getSpielerstart(spielerstart);
			D_Position datenPos = new D_Position();
			if (pos == null) {
				datenPos.setInt("x", 0);
				datenPos.setInt("y", 0);
			} else {
				datenPos.setInt("x", pos[0]);
				datenPos.setInt("y", pos[1]);
			}
			return Xml.verpacken(Xml.fromD(datenPos));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("setRessource/{x}/{y}/{ressorce}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String setRessource(@PathParam("x") int x, @PathParam("y") int y, @PathParam("ressorce") String ressorce) {
		try {
			karte.getFeld(new int[] { x, y }).setRessource(ressorce);
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	@GET
	@Path("delRessource/{x}/{y}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String delRessource(@PathParam("x") int x, @PathParam("y") int y) {
		try {
			karte.getFeld(new int[] { x, y }).setRessource(null);
			return Xml.verpacken(Xml.fromD(new D_OK()));
		} catch (Exception e) {
			e.printStackTrace();
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

	// Speichert ein gesamtes Universum Ab
	@POST
	@Path("speichernUniversum")
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/x-www-form-urlencoded")
	@Override
	public String speichernUniversum(@FormParam("daten") String daten) {
		PrintWriter pw = null;
		String pfad = "../Game-Engineering/ru.spiel/universum/";
		try {
			String dirName = "";
			File dir = null;
			
			String planetenNamen = null;
			ArrayList<ArrayList <D>> kartenListe = Xml.toDArray(daten);
			String[] xmlKarte = Xml.splitString(daten);
			for (int i = 0; i < kartenListe.size();i++) {
				karte = Karte.karteVonArray(kartenListe.get(i));
			
				if (karte.getArt().equals("Weltraum")) {
					if(karte.getId()<10){
						dirName = "00" + karte.getId();
					}else if(karte.getId()<100){
						dirName = "0" + karte.getId();
					}else{
						dirName = "" + karte.getId();
					}
					dir = new File(pfad + dirName);
					if (dir.exists()) {
						dir.delete();
					}
					
					dir.mkdir();
				}
					planetenNamen = karte.getArt() + karte.getId() + ".map";
				if (karte == null)
					throw new RuntimeException("BackendKarteneditor speichernKarte: Es existiert noch keine Karte!");
				pw = new PrintWriter(new FileWriter(dir.getPath() + "/" + planetenNamen));

				pw.println(xmlKarte[i+1]);

				pw.close();
			}
			return Xml.fromD(new D_OK());
		} catch (Exception e) {
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		} finally {
			pw.close();
		}
	}
	
	@GET
	@Path("auswahlUniversum")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String auswahlUniversum() {
		try {
			String pfad = "../Game-Engineering/ru.spiel/universum";
			File dir = null;
			D_AuswahlUniversum[] universum = null;
			String verpackt = "";
			
			dir = new File(pfad);
			if (dir.exists()) {
				File[] files = dir.listFiles();
				universum = new D_AuswahlUniversum[files.length];
				Karte k = null;
				
				int i = 0;
				for(File f : files){
					if(f.isDirectory()){
						int id = Integer.valueOf(f.getName());
						k = Karte.karteVonXml(Karte.karteLaden(f.getPath()+"/Weltraum"+id+".map"));
						universum[i] = new D_AuswahlUniversum(k.getName(), k.getId());
						verpackt = verpackt + Xml.fromD(universum[i]);
						i++;
					}
				}
				
				
				return Xml.verpacken(verpackt);
			}else{
				return Xml.verpacken(Xml.fromD(new D_Fehler("ID exsestiert nicht bitte vorhandenne ID eingeben")));
			}
		} catch (Exception e) {
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}
	
	@GET
	@Path("ladenUniversum/{id}")
	@Consumes("text/plain")
	@Produces("application/xml")
	@Override
	public String ladenUniversum(@PathParam("id") int id) {
		try {
			String pfad = "../Game-Engineering/ru.spiel/universum/";
			String dirName = "";
			File dir = null;
			String universum = "";
			
			if(id<10){
				dirName = "00" + id;
			}else if(id<100){
				dirName = "0" + id;
			}else{
				dirName = "" + id;
			}
			dir = new File(pfad + dirName);
			if (dir.exists()) {
				File[] files = dir.listFiles();
				for(File f : files){
					if(f.isFile()){
						universum = universum+Karte.karteLaden(f.getPath());
					}
				}
				karte = Karte.karteVonXml(Karte.karteLaden(pfad + dirName + "/" + "Weltraum"+id+".map"));
				return universum;
			}else{
				return Xml.verpacken(Xml.fromD(new D_Fehler("ID exsestiert nicht bitte vorhandenne ID eingeben")));
			}
		} catch (Exception e) {
			return Xml.verpacken(Xml.fromD(new D_Fehler(e.getMessage())));
		}
	}

}
