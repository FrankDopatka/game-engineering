package ru.backend.karte.felder.universum;

import ru.backend.karte.Feld;

public class Wurmloch extends Feld {
	
	public Wurmloch(){
	}	
	public Wurmloch(int idFeld,int x,int y,int wurmlochIdBiDirektional) {
		super(idFeld,x,y,"Wurmloch");
		getDaten().addInt("xZiel",0); // X-Koordinate des Zielfeldes im Weltraum
		getDaten().addInt("yZiel",0); // y-Koordinate des Zielfeldes im Weltraum
		getDaten().addInt("biDirektional", wurmlochIdBiDirektional); // wenn es ein anderes Wurmloch gibt
	}

	@Override
	public void setErlaubteRessourcenArt(){
	}
}
