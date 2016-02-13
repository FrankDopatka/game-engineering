package ru.backend.karte.felder.planeten;

import ru.backend.karte.Feld;

public class Kueste extends Feld {
	
	public Kueste(){
		init();
	}	
	public Kueste(int idKarte,int x,int y) {
		super(idKarte,x,y,"Kueste");
		init();
	}
	
	public void init(){
		getDaten().setBool("istWasserfeld",true);
		setBewegungspunkte(50);
	}

	@Override
	public void setErlaubteRessourcenArt(){
		addErlaubteRessourcenArt("Fisch");
		addErlaubteRessourcenArt("Oel");
	}
}
