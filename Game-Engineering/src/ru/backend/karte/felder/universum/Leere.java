package ru.backend.karte.felder.universum;

import ru.backend.karte.Feld;

public class Leere extends Feld {
	
	public Leere(){
	}	
	public Leere(int idKarte,int x,int y) {
		super(idKarte,x,y,"Leere");
	}

	@Override
	public void setErlaubteRessourcenArt(){
	}
}
