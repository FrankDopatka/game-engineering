package ru.backend.karte.karten;

import ru.backend.karte.Karte;

public class Tiefsee extends Karte {

	public Tiefsee(){
		setArt("Tiefsee");
	}
	
	public void setIdZielkarte(int id){
		getDaten().addInt("idZielkarte",id);
	}

	@Override
	public void setErlaubteFeldArten() {
		addErlaubteFeldArt("Tiefsee");
	}
}
