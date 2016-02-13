package ru.backend.karte.felder.universum;

import ru.backend.karte.Feld;

public abstract class Planet extends Feld {
	
	public Planet(){
	}	
	public Planet(int idKarte,int x,int y,String planetArt) {
		super(idKarte,x,y,"Planet");
		getDaten().addString("globusArt",planetArt);
	}

	@Override
	public void setErlaubteRessourcenArt(){
	}

}
