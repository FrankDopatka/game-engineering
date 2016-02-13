package ru.backend.karte.felder.universum;

import ru.backend.karte.Feld;

public class Mond extends Feld {
	
	public Mond(){
	}	
	public Mond(int idKarte,int x,int y,String mondArt) {
		super(idKarte,x,y,"Mond");
		getDaten().addString("globusArt",mondArt);
	}

	@Override
	public void setErlaubteRessourcenArt(){
	}
}
