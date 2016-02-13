package ru.daten;

public class D_Feld extends D {

	public D_Feld(){
		addInt("id",0);
		addInt("idKarte",0);
		addInt("refKarte",0); //ID zur Unterkarte wenn retKarte > 0
		addInt("x",0);
		addInt("y",0);
		addString("feldArt",""); // Art des Feldes, wie Wald,Wiese,Berg,...
		addString("globusArt",""); // Art des Feldes, wie Wald,Wiese,Berg,...
		
		addInt("spielerstart",0); // 0: von hier startet kein Spieler, >0: von hier startet der Spieler mit dieser ID
		addString("ressource",""); // Ressource auf diesem Feld
		
		addBool("istWasserfeld",false);
		addInt("bewegungspunkte",100);
		
		addInt("zielX",0);
		addInt("zielY",0);
		addInt("biDirektional",0);
	}
}